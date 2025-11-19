package com.mycompany.udp_example;

import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UDPServer 
{
    // Thread-safe counter for tracking total number of messages received
    // AtomicInteger ensures correct incrementing even when multiple threads modify it at once
    private static AtomicInteger totalMessages = new AtomicInteger(0);

    public static void main(String[] args) 
    {
        int port = 9876; // Port number the server will listen on
        System.out.println("UDP Server started. Listening on port " + port + "...");

        // Try-with-resources automatically closes the socket when program ends
        try (DatagramSocket serverSocket = new DatagramSocket(port)) 
        {
            // Buffer to hold incoming data from clients
            byte[] receiveBuffer = new byte[1024];

            // Infinite loop keeps server running and ready to handle new messages
            while (true) 
            {
                // Create a DatagramPacket to store incoming data
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                // Wait for a packet from a client (blocking call)
                serverSocket.receive(receivePacket);

                // For each received packet, create a new thread to handle it
                // This allows multiple clients or messages to be processed concurrently
                Thread handler = new Thread(new MessageHandler(serverSocket, receivePacket));
                handler.start();
            }

        } 
        catch (Exception e) 
        {
            // Print any errors that occur in the server
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Inner class responsible for handling each message in its own thread
    private static class MessageHandler implements Runnable 
    {
        private DatagramSocket socket;   // Reference to the main server socket
        private DatagramPacket packet;   // The specific client message to process

        public MessageHandler(DatagramSocket socket, DatagramPacket packet) 
        {
            this.socket = socket;
            this.packet = packet;
        }

        @Override
        public void run() 
        {
            try 
            {
                // Convert raw byte data into a readable string
                String clientMessage = new String(packet.getData(), 0, packet.getLength());
                // Retrieve client’s IP address and port
                String clientAddress = packet.getAddress().getHostAddress();
                int clientPort = packet.getPort();

                // If client sent "exit", acknowledge and skip sending a reply
                if (clientMessage.equalsIgnoreCase("exit")) 
                {
                    System.out.println("Client " + clientAddress + ":" + clientPort + " sent exit.");
                    return;
                }

                // Safely increment the shared message counter
                int messageCount = totalMessages.incrementAndGet();

                // Log the received message and updated count to the server console
                System.out.println("Received from " + clientAddress + ":" + clientPort + " → " + clientMessage);
                System.out.println("Total messages so far: " + messageCount);

                // Prepare a response showing the total number of messages received
                String response = "Server has received " + messageCount + " messages in total.";
                byte[] sendData = response.getBytes();

                // Create a packet to send the response back to the client
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                socket.send(sendPacket); // Send response
            } 
            catch (Exception e) 
            {
                // Catch and report any errors that happen while handling the message
                System.err.println("Error handling message: " + e.getMessage());
            }
        }
    }
}