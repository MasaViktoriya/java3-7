package com.geekbrains.server;

import com.geekbrains.CommonConstants;
import com.geekbrains.server.authorization.AuthService;
import com.geekbrains.server.authorization.InMemoryAuthServiceImpl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private final AuthService authService;
    private List<ClientHandler> connectedUsers;
    private final ExecutorService executorService;

    public Server() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        authService = new InMemoryAuthServiceImpl();
        try (ServerSocket server = new ServerSocket(CommonConstants.SERVER_PORT)) {
            authService.start();
            connectedUsers = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(executorService, this, socket);
            }
        }
        catch (IOException exception){
            exception.printStackTrace();
            System.out.println("Ошибка в работе сервера");
        }
        finally {
            if (authService != null) {
                authService.end();
            }
        }
    }


    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickNameBusy(String nickName) {
        for (ClientHandler handler: connectedUsers){
            if (handler.getNickname().equals(nickName)){
                return  true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler handler: connectedUsers){
            handler.sendMessage(message);
        }
    }

    public synchronized void addConnectedUser(ClientHandler handler) {
        connectedUsers.add(handler);
    }

    public synchronized void disconnectUser(ClientHandler handler) {
        connectedUsers.remove(handler);
    }

    public void sendPersonalMessage(String senderNickName, String recipientNickName, String personalMessage) {
        for (ClientHandler handler: connectedUsers){
            if (handler.getNickname().equals(recipientNickName) || handler.getNickname().equals(senderNickName)) {
                handler.sendMessage("PM "+ senderNickName + " to "+ recipientNickName + ": " + personalMessage);
            }
        }
    }

    public String getClients(){
        StringBuilder builder = new StringBuilder("/clients\n");
        for (ClientHandler user: connectedUsers){
            builder.append(user.getNickname()).append("\n");
        }
        return builder.toString();
    }
}
