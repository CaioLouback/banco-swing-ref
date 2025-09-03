package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import user.Usuarios;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class Json {
    private static final String PASTA_BANCO = System.getProperty("user.home") + "/bancoswingref";
    private static final String CAMINHO_ARQUIVO = PASTA_BANCO + "/usuarios.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    
    public static void salvarUsuarios(Usuarios user){
        List<Usuarios> listaUsuarios = lerUsuarios();
        listaUsuarios.add(user);
        
        try(FileWriter escrever = new FileWriter(CAMINHO_ARQUIVO)){
            gson.toJson(listaUsuarios, escrever);
            System.out.println("Usuarios salvos com sucesso!");
        } catch (IOException e){
            System.err.println("Erro ao salvar usuário: " + e.getMessage()); 
        }   
    }
    
    public static List<Usuarios> lerUsuarios(){
        File arq = new File(CAMINHO_ARQUIVO);
        
        if(!arq.exists())
            return new ArrayList<>();
        
        try(FileReader ler = new FileReader(CAMINHO_ARQUIVO)){
            Type listType = new TypeToken<List<Usuarios>>() {}.getType();
            return gson.fromJson(ler, listType);
        }catch (IOException e){
            return new ArrayList<>();
        }
        
    } 
}
