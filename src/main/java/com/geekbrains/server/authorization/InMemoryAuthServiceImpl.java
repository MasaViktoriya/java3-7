package com.geekbrains.server.authorization;

import com.geekbrains.SQLConnection;
import java.sql.*;

public class InMemoryAuthServiceImpl implements  AuthService {

    public InMemoryAuthServiceImpl() {
    }


    @Override
    public void start() {
        System.out.println("Сервис аутентификации инициализирован");

    }

    @Override
    public synchronized String getNicknameByLoginAndPassword(String login, String password) {
        try {
            SQLConnection.connect();
            try (ResultSet user = SQLConnection.statement.executeQuery(String.format("SELECT * FROM userList WHERE login = '%s'", login))) {
                while (user.next()) {
                    if (user.getString("password").equals(password)) {
                        return user.getString("nickname");
                    }
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLConnection.disconnect();
        }
        return null;
    }

    @Override
    public void end() {
        System.out.println("Сервис аутентификации отключен");

    }
}
