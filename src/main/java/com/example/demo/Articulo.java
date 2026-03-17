package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "articulos")
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double price;
    private String description;
    private String url_img;


    public Articulo() {
        this.id = 0;
        this.name = "";
        this.price = 0.0;
        this.description = "";
        this.url_img = "";
    }


    public Articulo(int id, String name, double price, String description, String url_img) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.url_img = url_img;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getUrl_img() {
        return url_img;
    }


    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    

    


}
