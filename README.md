UDP Multi-Threaded Server and Client
1. Overview

This project implements a multi-threaded UDP server and client in Java.
The server listens for datagram packets, processes each message in a separate thread, and keeps track of the total number of messages 
received since startup. Each client can send messages to the server, receive replies indicating the total message count, and 
gracefully exit by typing "exit".


2. Features

Handles multiple client datagram messages simultaneously using threads.

Uses an AtomicInteger for safe, synchronized message counting across threads.

Displays each client’s IP address and port number upon receiving a message.

Sends a reply to each client with the current total number of messages received.

Graceful client shutdown when "exit" is entered.

Includes proper exception handling using try-catch blocks for reliability.

3. How to Run

1. Compile the code

Open a terminal or command prompt in the project directory and compile the Java files:

javac -d . UDPServer.java UDPClient.java

2. Start the server

Run the UDP server:

java com.mycompany.udp_example.UDPServer

3. Start one or more clients 

Run the UDP client:

java com.mycompany.udp_example.UDPClient

Example Interaction

Client 1:

UDP Client started. Type messages (type 'exit' to quit):
> Hello
FROM SERVER: Server has received 1 messages in total.
> exit
Exiting in 2 seconds...


Client 2:

UDP Client started. Type messages (type 'exit' to quit):
> Hi
FROM SERVER: Server has received 2 messages in total.
> exit
Exiting in 2 seconds...


Server Output:

UDP Server started. Listening on port 9876...
Received from 127.0.0.1:60234 → Hello
Total messages so far: 1
Received from 127.0.0.1:60235 → Hi
Total messages so far: 2
Client 127.0.0.1:60234 sent exit.
Client 127.0.0.1:60235 sent exit.

4. Files Included
File	Description
UDPClient.java	UDP client implementation that sends messages and receives replies from the server.
UDPServer.java	Multi-threaded UDP server that handles each incoming message in a separate thread and tracks total message count.
README.md	Instructions and overview for running and testing the project.
5. Notes

If you see “Address already in use”, change the port number in both files (default: 9876).

The server must be running before starting any clients.

Unlike TCP, UDP is connectionless, so there’s no persistent session between client and server.

Make sure the client and server are using the same host and port number.

Each datagram message is handled independently — even multiple clients can send simultaneously.
