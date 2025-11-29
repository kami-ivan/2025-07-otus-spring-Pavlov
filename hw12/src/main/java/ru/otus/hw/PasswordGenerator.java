package ru.otus.hw;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("user password: " + encoder.encode("userPass"));
        System.out.println("admin password: " + encoder.encode("adminPass"));
    }
}
