package lk.epic.chatApp.handler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private String message = "";
    private final Socket localSocket;
    private final ArrayList<ClientHandler> clientHandlersList;
    private long clientThreadNumber;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String fullMessage;

    public ClientHandler(Socket localSocket, ArrayList<ClientHandler> clientHandlersList) {
        this.localSocket = localSocket;
        this.clientHandlersList = clientHandlersList;
    }

    @Override
    public void run() {
        try {
            this.dataInputStream = new DataInputStream(localSocket.getInputStream());
            this.dataOutputStream = new DataOutputStream(localSocket.getOutputStream());

            while (true) {
                this.clientThreadNumber = Thread.currentThread().getId();

                message = dataInputStream.readUTF().toString();
                if (message.equals("quit")) {
                    for (ClientHandler client : clientHandlersList) {
                        if (client.localSocket == this.localSocket) {
                            /* ---- To notified to the server which client was disconnected ---- */
                            clientHandlersList.remove(client);
                            break;
                        }
                    }

                    sendMessagesToServer(this.dataOutputStream, clientThreadNumber, this.message);
                    break;
                }

                sendMessagesToServer(this.dataOutputStream, clientThreadNumber, this.message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessagesToServer(DataOutputStream dataOutputStream, long clientThreadNumber, String clientMessage) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Client Thread Number-"+clientThreadNumber+" Says : "+clientMessage);
        System.out.print("Enter Message to Client Thread Number-"+clientThreadNumber+" : ");
        dataOutputStream.writeUTF("to Client Thread Number-"+clientThreadNumber+" : "+bufferedReader.readLine());
    }
}
