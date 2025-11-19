package com.mycompany.udp_example;

import java.net.*;
import java.io.*;

public class UDPClient 
{
    public static void main(String[] args) 
    {
        // Define the server's IP address and port number
        String serverIP = "localhost";
        int port = 9876;

        // Try-with-resources automatically closes the socket and input reader when done
        try (DatagramSocket clientSocket = new DatagramSocket();
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in))) 
        {

            System.out.println("UDP Client started. Type messages (type 'exit' to quit):");

            // Infinite loop to continuously send and receive messages until user exits
            while (true) 
            {
                System.out.print("> ");
                // Read a line of text input from the user
                String message = inFromUser.readLine();
                // Convert the string message to bytes for UDP transmission
                byte[] sendData = message.getBytes();

                // Create a UDP packet with the data, length, destination IP, and port
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverIP), port);
                // Send the packet through the UDP socket
                clientSocket.send(sendPacket);

                // If user types "exit", close connection after a short delay
                if (message.equalsIgnoreCase("exit")) 
                {
                    System.out.println("Exiting in 2 seconds...");
                    Thread.sleep(2000);
                    break;  // Exit the while loop
                }

                // Prepare a buffer to receive response data from the server
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                // Wait for server's reply (blocking call)
                clientSocket.receive(receivePacket);

                // Convert the received bytes back into a readable string
                String serverReply = new String(receivePacket.getData(), 0, receivePacket.getLength());
                // Display the server's reply
                System.out.println("FROM SERVER: " + serverReply);
            }

        } 
        catch (Exception e) 
        {
            // Print error message if any exception occurs
            System.err.println("Client error: " + e.getMessage());
        }
    }
}