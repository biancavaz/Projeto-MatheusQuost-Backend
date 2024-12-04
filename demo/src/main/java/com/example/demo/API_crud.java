package com.example.demo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        try(Connection con = banco.getConnection()){
            PreparedStatement ps =con.prepareStatement("""
                    SELECT * FROM usuarios WHERE email = ?
                    """);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String senha1 = rs.getString("senha");
                if(senha1.equals(senha)){
                    Map<String, Object> response = new HashMap<>();
                    response.put("ok", true);
                    response.put("message", "CONECTADO");

                    return new ResponseEntity(response, HttpStatus.ACCEPTED); // Status 202
                }
                else{
                    Map<String, Object> response = new HashMap<>();
                    response.put("ok", false);

                    response.put("message", "SENHA INCORRETA");

                    return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else{
                Map<String, Object> response = new HashMap<>();
                response.put("ok", false);

                response.put("message", "EMAIL INEXISTENTE");

                return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
                int id = rs.getInt("id");
                String nome =rs.getString("nome");
                String email = rs.getString("email");
                lista.add(new User(id, nome, email));

            }
        }catch (SQLException e){
            return e.toString();
        }
        String json = new Gson().toJson(lista);
        return json;
    }

    //CRUD SELECT
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){

        try(Connection con = banco.getConnection()){
            PreparedStatement ps =con.prepareStatement("""
                    DELETE FROM usuarios WHERE id = ?
                    """);
            ps.setLong(1, id);

            if(ps.executeUpdate()>0){
                Map<String, Object> response = new HashMap<>();
                response.put("message", "DELETADO");
                return new ResponseEntity(response, HttpStatus.ACCEPTED);

            }

        }catch (SQLException e){
            throw new RuntimeException();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "NÃO DELETADO");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }


}