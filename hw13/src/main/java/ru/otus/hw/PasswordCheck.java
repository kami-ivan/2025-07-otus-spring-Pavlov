package ru.otus.hw;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCheck {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String userHash = "$2a$10$RQ2BRK5oIsobxSUa/PVMyOqiK2kcFczj8vE/2VilcBaled2fclIq.";
        String adminHash = "$2a$10$EXvc3B0tSzxmFTJF98FbFut9SZchbWdJDxORXh7MGPfLwrFGPjTvq";

        System.out.println("user hash matches 'userPass': " + encoder.matches("userPass", userHash));
        System.out.println("user hash matches 'password': " + encoder.matches("password", userHash));

        System.out.println("admin hash matches 'adminPass': " + encoder.matches("adminPass", adminHash));
        System.out.println("admin hash matches 'admin': " + encoder.matches("admin", adminHash));
    }
}