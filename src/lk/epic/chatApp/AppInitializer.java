package lk.epic.chatApp;

import lk.epic.chatApp.clientSide.Client;

public class AppInitializer {
    public static void main(String[] args) {
        new Thread(new Client()).start();
    }
}
