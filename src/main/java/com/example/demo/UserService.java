package com.example.demo;

public class UserService {

    public User findByNickAndPassword(String nick, String password) {
        // Usuario normal de prueba
        if(nick.equals("user123") && password.equals("1234")) {
            return new User("Juan", "Pérez", "user123", "juan@mail.com", 25, "1234");
        }
        // Usuario administrador (Sesión 2, punto 6)
        if(nick.equals("admin") && password.equals("admin123")) {
            User admin = new User("Admin", "Root", "admin", "admin@tienda.com", 99, "admin123");
            return admin; 
        }
        return null;
    }

}
