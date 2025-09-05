package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import user.Usuarios;
import java.io.*;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import user.Caixa;
import user.Cliente;
import user.Gerente;



public class Json {
    private static final String PASTA_BANCO = System.getProperty("user.home") + "/bancoswingref";
    private static final String CAMINHO_ARQUIVO = PASTA_BANCO + "/usuarios.json";
    private static final String CAMINHO_CREDITO = PASTA_BANCO + "/credito.json";
    private static final String CAMINHO_EXTRATO = PASTA_BANCO + "/extrato.json";
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
    
    public static boolean credito(Cliente cliente, double valor) {
        File arq = new File(CAMINHO_CREDITO);
        
        List<Map<String, Object>> listaCreditos = new ArrayList<>();

        // Se já existir, carrega o JSON atual
        if (arq.exists()) {
            try (FileReader reader = new FileReader(arq)) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                listaCreditos = gson.fromJson(reader, listType);
                if (listaCreditos == null) {
                    listaCreditos = new ArrayList<>();
                }
            } catch (IOException e) {
                return false;
            }
        }

        // Criar o registro de crédito
        Map<String, Object> registro = new HashMap<>();
        registro.put("nome", cliente.getNome());
        registro.put("cpf", cliente.getCPF());
        registro.put("valor", valor);

        listaCreditos.add(registro);

        // Gravar no arquivo
        try (FileWriter writer = new FileWriter(arq)) {
            gson.toJson(listaCreditos, writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
            
    public static List<Map<String, Object>> lerExtrato(String cpf) {
        File file = new File(CAMINHO_EXTRATO);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(CAMINHO_EXTRATO)) {
            Type type = new TypeToken<Map<String, List<Map<String, Object>>>>() {
            }.getType();
            Map<String, List<Map<String, Object>>> extratos = gson.fromJson(reader, type);
            return extratos.getOrDefault(cpf, new ArrayList<>());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    
    public static String obterExtrato(String cpf) {
        List<Map<String, Object>> extrato = lerExtrato(cpf);
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); // Formato monetário BR

        StringBuilder sb = new StringBuilder();
        sb.append("Extrato Bancário:\n\n");

        if (extrato.isEmpty()) {
            sb.append("Nenhuma movimentação encontrada.");
        } else {
            for (Map<String, Object> movimentacao : extrato) {
                String tipo = (String) movimentacao.get("tipo");
                double valor = (double) movimentacao.get("valor");
                sb.append(String.format("%s: %s\n", tipo, formatoMoeda.format(valor)));
            }
        }

        return sb.toString();
    }
    
    
    
    
    
    
    
    
    
    
}
