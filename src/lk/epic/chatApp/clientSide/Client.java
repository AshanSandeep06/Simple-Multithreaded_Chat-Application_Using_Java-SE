package lk.epic.chatApp.clientSide;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    private static final int PORT = 5000;
    public static Socket remoteSocket;
    public static String message;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    public static Scanner scanner;
    private static String clientMessage = "";
    private static int instanceCounter = 0;
    private int instanceId;

    public static void main(String[] args) {
        Thread clientThread = new Thread(new Client());
//        clientThread.setName("Client-" + clientThread.getId());
//        System.out.println(clientThread.getId());
//        System.out.println(clientThread.getName());
        clientThread.start();
    }

    public Client() {
        synchronized (Client.class) {
            instanceId = ++instanceCounter;
        }
    }

    @Override
    public void run() {

        try {
//            instanceId = ++instanceCounter;
            remoteSocket = new Socket("localhost", PORT);

            dataInputStream = new DataInputStream(remoteSocket.getInputStream());
            dataOutputStream = new DataOutputStream(remoteSocket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            // Should catch Thread Number
            /*Long clientThreadNumber;
            clientThreadNumber = Thread.currentThread().getId();
            System.out.println(clientThreadNumber);*/

            // System.out.println("Thread ID for instance " + instanceId + ": " + Thread.currentThread().getId() + " is running");
            // Long clientThreadNumber =  Thread.currentThread().getId();

            while (!clientMessage.equals("quit")) {
                System.out.print("Enter Your Message : ");
                clientMessage = bufferedReader.readLine();
                dataOutputStream.writeUTF(clientMessage);

                message = dataInputStream.readUTF();
                System.out.println("Server Says "+ message);
            }

            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
