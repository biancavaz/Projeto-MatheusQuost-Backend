package com.example.demo;

public class User {
    int id;
    String nome;
    String email;
    String senha;
    User(int id, String nome, String email){
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
    User(String nome, String email){
        this.nome = nome;
        this.email = email;
    }
}
