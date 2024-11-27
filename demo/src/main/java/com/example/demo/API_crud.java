package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//This code creates and URL "/getDados" that returns a string
//Access http://127.0.0.1:8081/getDados to test the code
@RestController
public class API_crud {
    static DB banco = new DB();

    //API LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> TentativaLogin(@RequestBody String json){
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String email = jsonObject.get("email").toString();
        email = email.replace("\"", "");

        String senha = jsonObject.get("senha").toString();
        senha = senha.replace("\"", "");

        System.out.println(email);
        System.out.println(senha);
        try(Connection con = banco.getConnection()){
            PreparedStatement ps =con.prepareStatement("""
                    SELECT * FROM usuarios WHERE email = ?
                    """);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String senha1 = rs.getString("senha");
                System.out.println(senha1);
                System.out.println(senha);
                if(senha1.equals(senha)){
                    throw new ResponseStatusException(HttpStatus.ACCEPTED, "CONECTADO");
                }
                else{
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Senha incorreta");
                }
            }
            else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Conta inexistente");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    //CRUD SELECT
    @GetMapping("/select")
    public String getUsers(){
        ArrayList<User> lista = new ArrayList<>();
        try(Connection con = banco.getConnection()){
            PreparedStatement ps =con.prepareStatement("""
                    SELECT * FROM usuarios
                    """);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String nome =rs.getString("nome");
                String email = rs.getString("email");
                lista.add(new User(nome, email));

            }
        }catch (SQLException e){
            return e.toString();
        }
        String json = new Gson().toJson(lista);
        return json;
    }
}