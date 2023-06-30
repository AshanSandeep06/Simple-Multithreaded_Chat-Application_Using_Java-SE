package lk.epic.chatApp.serverSide;

import lk.epic.chatApp.handler.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ServerSocket serverSocket;
    private static final int PORT = 5000;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    private static String clientMessage;
    private static ArrayList<ClientHandler> clientHandlersList = new ArrayList<>();

    public static void main(String[] args) {
        Thread serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                System.out.println("Server has Started in Port : "+PORT);

                while (true) {
                    Socket localSocket = serverSocket.accept();

                    System.out.println("Connected Client Port Number : "+localSocket.getPort());
                    System.out.println("Connected Client IP : "+localSocket.getInetAddress());

                    ClientHandler client = new ClientHandler(localSocket, clientHandlersList);

                    dataInputStream = new DataInputStream(localSocket.getInputStream());
                    dataOutputStream = new DataOutputStream(localSocket.getOutputStream());

                    // First, Wait until the client send a message
                    /*clientMessage = dataInputStream.readUTF();
                    System.out.println("Client Says : "+clientThreadNumber+" - "+clientMessage);
                    dataOutputStream.writeUTF("Hi Client - "+clientThreadNumber);*/

                    // Add Client for handle all clients using seperated Threads.
                    clientHandlersList.add(client);

                    // Should Start Client Thread
                    client.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        serverThread.start();
    }
}
