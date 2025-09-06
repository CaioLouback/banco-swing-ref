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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public static void atualizarSaldo(Cliente clienteAtualizado) {
        List<Usuarios> listaUsuarios = lerUsuarios();

        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuarios u = listaUsuarios.get(i);

            // Identifica o usuário pelo CPF
            if (u.getCPF().equals(clienteAtualizado.getCPF()) && u instanceof Cliente) {
                ((Cliente) u).setSaldo(clienteAtualizado.getSaldo()); // atualiza apenas o saldo
                System.out.println("SALDO ATT!");
                break; // sai do loop assim que encontrar
            }
        }

        // Salva a lista atualizada no JSON
        File pasta = new File(PASTA_BANCO);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        try (FileWriter escrever = new FileWriter(CAMINHO_ARQUIVO)) {
            gson.toJson(listaUsuarios, escrever);
            System.out.println("Saldo atualizado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao atualizar saldo: " + e.getMessage());
        }
    }
    
        public static boolean credito(Cliente cliente, double valor) {
        File arq = new File(CAMINHO_CREDITO);

        // se não existir, cria um arquivo vazio
        if (!arq.exists()) {
            try {
                arq.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

        // cliente recebe o valor do crédito
        cliente.setValorCredito(valor);

        try (FileReader reader = new FileReader(arq)) {
            Type listType = new TypeToken<List<Cliente>>() {
            }.getType();
            List<Cliente> lista = gson.fromJson(reader, listType);

            if (lista == null) {
                lista = new ArrayList<>();
            }

            lista.add(cliente);

            try (FileWriter writer = new FileWriter(arq)) {
                gson.toJson(lista, writer);
            }
            return true;

        } catch (IOException e) {
            return false;
        }
    }
         
    public static void registrarMovimentacoes(String cpf, String tipo, double valor) {
        File arq = new File(CAMINHO_EXTRATO);
        Map<String, List<Map<String, Object>>> extrato;

        try {
            
            if (arq.exists()) {
                try (FileReader reader = new FileReader(arq)) {
                    Type mapType = new TypeToken<Map<String, List<Map<String, Object>>>>() {
                    }.getType();
                    extrato = gson.fromJson(reader, mapType);

                    if (extrato == null) {
                        extrato = new HashMap<>();
                    }
                }
            } else {
                extrato = new HashMap<>();
            }

            
            List<Map<String, Object>> movimentacoes = extrato.getOrDefault(cpf, new ArrayList<>());

            
            Map<String, Object> mov = new HashMap<>();
            mov.put("tipo", tipo);
            mov.put("valor", valor);

            movimentacoes.add(mov);

            
            extrato.put(cpf, movimentacoes);

            
            try (FileWriter writer = new FileWriter(arq)) {
                gson.toJson(extrato, writer);
            }

        } catch (IOException e) {
            System.err.println("Erro ao registrar movimentação: " + e.getMessage());
        }
    }
        
        
    public static List<Cliente> lerExtrato(String cpf) {
        File arq = new File(CAMINHO_CREDITO);

        if (!arq.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(arq)) {
            Type listType = new TypeToken<List<Cliente>>() {
            }.getType();
            List<Cliente> lista = gson.fromJson(reader, listType);

            if (lista == null) {
                lista = new ArrayList<>();
            }
            return lista;

        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    
    public static String obterExtrato(String cpf) {
        File arq = new File(CAMINHO_EXTRATO);
        if (!arq.exists()) {
            return "Nenhuma movimentação encontrada.";
        }

        try (FileReader reader = new FileReader(arq)) {
            Type mapType = new TypeToken<Map<String, List<Map<String, Object>>>>() {
            }.getType();
            Map<String, List<Map<String, Object>>> extrato = gson.fromJson(reader, mapType);

            if (extrato == null || !extrato.containsKey(cpf) || extrato.get(cpf).isEmpty()) {
                return "Nenhuma movimentação encontrada.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== Extrato de Movimentações ===\n\n");

            List<Map<String, Object>> movimentacoes = extrato.get(cpf);
            for (Map<String, Object> mov : movimentacoes) {
                sb.append("Tipo: ").append(mov.get("tipo")).append("\n");
                sb.append("Valor: R$ ").append(mov.get("valor")).append("\n");
                sb.append("-----------------------------\n");
            }

            return sb.toString();

        } catch (IOException e) {
            return "Erro ao ler o extrato: " + e.getMessage();
        }
    }

 
    
}
