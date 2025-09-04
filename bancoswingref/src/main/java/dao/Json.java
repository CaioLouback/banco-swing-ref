package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import user.Usuarios;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import user.Caixa;
import user.Cliente;
import user.Gerente;



public class Json {
    private static final String PASTA_BANCO = System.getProperty("user.home") + "/bancoswingref";
    private static final String CAMINHO_ARQUIVO = PASTA_BANCO + "/usuarios.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private Json(){};
    
    public static void salvarUsuarios(Usuarios user){
        List<Usuarios> listaUsuarios = lerUsuarios();
        listaUsuarios.add(user);
        
        File pasta = new File(PASTA_BANCO);
        if (!pasta.exists()) {
            pasta.mkdirs(); 
        }
        
        try(FileWriter escrever = new FileWriter(CAMINHO_ARQUIVO)){
            gson.toJson(listaUsuarios, escrever);
            System.out.println("Usuarios salvos com sucesso!");
        } catch (IOException e){
            System.err.println("Erro ao salvar usuario: " + e.getMessage()); 
        }   
    }
    
    public static List<Usuarios> lerUsuarios(){
        File arq = new File(CAMINHO_ARQUIVO);
        
        if(!arq.exists())
            return new ArrayList<>();
        
        List<Usuarios> listaUsuarios = new ArrayList<>();

        try (FileReader ler = new FileReader(CAMINHO_ARQUIVO)) {
            JsonArray jsonArray = JsonParser.parseReader(ler).getAsJsonArray();

            for (JsonElement elem : jsonArray) {
                JsonObject obj = elem.getAsJsonObject();
                String tipo = obj.get("tipo").getAsString();

                Usuarios usuario = null;
                switch (tipo) {
                    case "Cliente":
                        usuario = gson.fromJson(obj, Cliente.class);
                        break;
                    case "Gerente":
                        usuario = gson.fromJson(obj, Gerente.class);
                        break;
                    case "Caixa":
                        usuario = gson.fromJson(obj, Caixa.class);
                        break;
                    default:
                        System.err.println("Tipo desconhecido: " + tipo);
                }

                if (usuario != null) {
                    listaUsuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR ao ler o arquivo!");
        }

        return listaUsuarios;

    }
}
