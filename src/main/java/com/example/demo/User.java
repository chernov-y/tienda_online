package com.example.demo;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User {


    @Id
    private String name;
    private String surname;
    private String nick;
    private String email;
    private int age;
    private String password;

    public User() {
        this.name = "";
        this.surname = "";
        this.nick = "";
        this.email = "";
        this.age = -1;
        this.password = "";
    }
    public User(String name, String surname, String nick, String email, int age, String password) {
        this.name = name;
        this.surname = surname;
        this.nick = nick;
        this.email = email;
        this.age = age;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getNick() {
        return nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
    

}
