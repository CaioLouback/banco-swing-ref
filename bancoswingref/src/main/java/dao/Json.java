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
import java.util.Iterator;
import java.util.List;
import user.Caixa;
import user.Cliente;
import user.Gerente;
import utils.Movimentacao;

/**
 * Classe responsável por todas as operações de leitura, escrita e atualização de dados
 * no formato JSON do sistema bancário.
 * 
 * <p>
 * As operações incluem:
 * <ul>
 *   <li>Cadastro, leitura e atualização de usuários;</li>
 *   <li>Registro e consulta de extratos, saques e créditos;</li>
 *   <li>Persistência dos dados em arquivos JSON dentro da pasta do usuário.</li>
 * </ul>
 * </p>
 * 
 * <p>Todos os métodos são estáticos e a classe não pode ser instanciada.</p>
 * 
 * @author Caio
 * @version 1.0
 * @since 2025-09-06
 */

public class Json {
    private static final String PASTA_BANCO = System.getProperty("user.home") + "/bancoswingref";  /** Pasta principal onde os arquivos JSON são armazenados */
    private static final String CAMINHO_ARQUIVO = PASTA_BANCO + "/usuarios.json"; /** Arquivo JSON que armazena os usuários */
    private static final String CAMINHO_CREDITO = PASTA_BANCO + "/credito.json"; /** Arquivo JSON que armazena solicitações de crédito */
    private static final String CAMINHO_EXTRATO = PASTA_BANCO + "/extrato.json"; /** Arquivo JSON que armazena solicitações de crédito */
    private static String CAMINHO_SAQUE = PASTA_BANCO + "/saque.json";  /** Arquivo JSON que armazena pedidos de saque */
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create(); /** Objeto Gson configurado para saída "pretty print" */
    
    private Json(){}; /** Construtor privado para impedir instanciação */
    
    /**
     * Salva um usuário no arquivo JSON de usuários.
     * 
     * @param user Usuário a ser salvo
     */
    
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
    
    /**
     * Lê todos os usuários cadastrados no JSON.
     * 
     * @return Lista de {@link Usuarios} cadastrados
     */
    
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
    
    /**
     * Atualiza o saldo de um {@link Cliente} no JSON de usuários.
     * 
     * @param clienteAtualizado Cliente com saldo atualizado
     */
    
    public static void atualizarSaldo(Cliente clienteAtualizado) {
        List<Usuarios> listaUsuarios = lerUsuarios();
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuarios u = listaUsuarios.get(i);
            if (u.getCPF().equals(clienteAtualizado.getCPF()) && u instanceof Cliente) {
                ((Cliente) u).setSaldo(clienteAtualizado.getSaldo()); 
                System.out.println("SALDO ATT!");
                break; 
            }
        }
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
    
    /**
     * Registra uma solicitação de crédito para um cliente.
     * 
     * @param cliente Cliente que solicitou o crédito
     * @param valor Valor do crédito
     * @return true se a operação foi bem-sucedida, false caso contrário
     */
    
    public static boolean credito(Cliente cliente, double valor) {
        File arq = new File(CAMINHO_CREDITO);
        if (!arq.exists()) {
            try {
                arq.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }

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
    
    /**
     * Registra uma movimentação no extrato de um cliente.
     * 
     * @param cpf CPF do cliente
     * @param tipo Tipo da movimentação (ex: "SAQUE", "DEPÓSITO")
     * @param valor Valor da movimentação
     */
    
    public static void registrarMovimentacoes(String cpf, String tipo, double valor) {
        String caminho = System.getProperty("user.home") + "/bancoswingref/extratos/" + cpf + "_extrato.json";
        File arq = new File(caminho);

        File pasta = arq.getParentFile();
        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }
        List<Movimentacao> movimentacoes;
        try {     
            if (arq.exists()) {
                try (FileReader reader = new FileReader(arq)) {
                    Type listType = new TypeToken<List<Movimentacao>>() {
                    }.getType();
                    movimentacoes = gson.fromJson(reader, listType);
                    if (movimentacoes == null) {
                        movimentacoes = new ArrayList<>();
                    }
                }
            } else {
                movimentacoes = new ArrayList<>();
            }
            movimentacoes.add(new Movimentacao(tipo, valor));

            try (FileWriter writer = new FileWriter(arq)) {
                gson.toJson(movimentacoes, writer);
            }
        } catch (IOException e) {
            System.err.println("Erro ao registrar movimentação: " + e.getMessage());
        }
    }
    
    /**
     * Lê todas as movimentações de um cliente.
     * 
     * @param cpf CPF do cliente
     * @return Lista de {@link Movimentacao} do cliente
     */
        
    public static List<Movimentacao> lerExtrato(String cpf) {
        String caminho = System.getProperty("user.home") + "/bancoswingref/extratos/" + cpf + "_extrato.json";
        File arq = new File(caminho);
        if (!arq.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(arq)) {
            Type listType = new TypeToken<List<Movimentacao>>() {
            }.getType();
            List<Movimentacao> lista = gson.fromJson(reader, listType);
            if (lista == null) {
                lista = new ArrayList<>();
            }
            return lista;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtém o extrato de um cliente em formato de texto.
     * 
     * @param cpf CPF do cliente
     * @return String formatada contendo o extrato
     */
    
    public static String obterExtrato(String cpf) {
        List<Movimentacao> lista = lerExtrato(cpf);
        if (lista.isEmpty()) {
            return "Nenhuma movimentação encontrada.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Extrato de Movimentações ===\n\n");
        for (Movimentacao mov : lista) {
            sb.append("Tipo: ").append(mov.getTipo()).append("\n");
            sb.append("Valor: R$ ").append(mov.getValor()).append("\n");
            sb.append("-----------------------------\n");
        }
        return sb.toString();
    }
    
    /**
     * Registra um pedido de saque de um cliente.
     * 
     * @param cpf CPF do cliente
     * @param valor Valor solicitado
     */

    public static void pedidoSaque(String cpf, double valor) {
        String caminho = System.getProperty("user.home") + "/bancoswingref/saques/" + cpf + "_saques.json";
        File arq = new File(caminho);
        // Garante que a pasta exista
        File pasta = arq.getParentFile();
        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }
        List<Movimentacao> pedidos;
        try {
            if (arq.exists()) {
                try (FileReader reader = new FileReader(arq)) {
                    Type listType = new TypeToken<List<Movimentacao>>() {
                    }.getType();
                    pedidos = gson.fromJson(reader, listType);
                    if (pedidos == null) {
                        pedidos = new ArrayList<>();
                    }
                }
            } else {
                pedidos = new ArrayList<>();
            } 
            pedidos.add(new Movimentacao("SAQUE", valor));
            try (FileWriter writer = new FileWriter(arq)) {
                gson.toJson(pedidos, writer);
            }
        } catch (IOException e) {
            System.err.println("Erro ao registrar pedido de saque: " + e.getMessage());
        }
    }
    
    /**
     * Lê os pedidos de saque de um cliente.
     * 
     * @param cpf CPF do cliente
     * @return Lista de {@link Movimentacao} representando pedidos de saque
     */
    
    public static List<Movimentacao> lerPedidoSaque(String cpf) {
        String caminho = System.getProperty("user.home") + "/bancoswingref/saques/" + cpf + "_saques.json";
        File arq = new File(caminho);
        if (!arq.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(arq)) {
            Type listType = new TypeToken<List<Movimentacao>>() {
            }.getType();
            List<Movimentacao> lista = gson.fromJson(reader, listType);
            if (lista == null) {
                lista = new ArrayList<>();
            }
            return lista;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtém os pedidos de saque de um cliente em formato de texto.
     * 
     * @param cpf CPF do cliente
     * @return String formatada contendo os pedidos de saque
     */
    
    public static String obterPedidoSaque(String cpf) {
        List<Movimentacao> lista = lerPedidoSaque(cpf);
        if (lista.isEmpty()) {
            return "Nenhum pedido de saque encontrado.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Pedidos de Saque ===\n\n");
        for (Movimentacao mov : lista) {
            sb.append("Tipo: ").append(mov.getTipo()).append("\n");
            sb.append("Valor: R$ ").append(mov.getValor()).append("\n");
            sb.append("-----------------------------\n");
        }
        return sb.toString();
    }
    
    /**
     * Remove um pedido de saque específico.
     * 
     * @param cpf CPF do cliente
     * @param valor Valor do pedido de saque a ser removido
     */
    
    public static void removerPedidoSaque(String cpf, double valor) {
        String caminho = System.getProperty("user.home") + "/bancoswingref/saques/" + cpf + "_saques.json";
        File arq = new File(caminho);
        if (!arq.exists()) {
            return;
        }
        try {
            Type listType = new TypeToken<List<Movimentacao>>() {
            }.getType();
            List<Movimentacao> pedidos;
            try (FileReader reader = new FileReader(arq)) {
                pedidos = gson.fromJson(reader, listType);
            }
            if (pedidos == null || pedidos.isEmpty()) {
                return;
            }
            Iterator<Movimentacao> it = pedidos.iterator();
            while (it.hasNext()) {
                Movimentacao mov = it.next();
                if ("SAQUE".equalsIgnoreCase(mov.getTipo()) && mov.getValor() == valor) {
                    it.remove(); 
                    break;       
                }
            }
            try (FileWriter writer = new FileWriter(arq)) {
                gson.toJson(pedidos, writer);
            }
        } catch (IOException e) {
            System.err.println("Erro ao remover pedido de saque: " + e.getMessage());
        }
    }
    
    /**
     * Registra uma solicitação de crédito para um cliente em arquivo separado.
     * 
     * @param cpf CPF do cliente
     * @param valor Valor solicitado
     */
    
    public static void solicitacaoCredito(String cpf, double valor) {
        String caminho = System.getProperty("user.home") + "/bancoswingref/creditos/" + cpf + "_creditos.json";
        File arq = new File(caminho);
        File pasta = arq.getParentFile();
        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }
        List<Movimentacao> solicitacoes;
        try {
            if (arq.exists()) {
                try (FileReader reader = new FileReader(arq)) {
                    Type listType = new TypeToken<List<Movimentacao>>() {
                    }.getType();
                    solicitacoes = gson.fromJson(reader, listType);
                    if (solicitacoes == null) {
                        solicitacoes = new ArrayList<>();
                    }
                }
            } else {
                solicitacoes = new ArrayList<>();
            }
            solicitacoes.add(new Movimentacao("CREDITO", valor));
            try (FileWriter writer = new FileWriter(arq)) {
                gson.toJson(solicitacoes, writer);
            }
        } catch (IOException e) {
            System.err.println("Erro ao registrar solicitação de crédito: " + e.getMessage());
        }
    }

    /**
     * Lê todas as solicitações de crédito de todos os clientes.
     * 
     * @return Lista de {@link Movimentacao} com todas as solicitações
     */
    
    public static List<Movimentacao> lerSolicitacaoCredito() {
        String pastaCreditos = System.getProperty("user.home") + "/bancoswingref/creditos/";
        File pasta = new File(pastaCreditos);
        
        List<Movimentacao> todasSolicitacoes = new ArrayList<>();
        if (!pasta.exists() || !pasta.isDirectory()) {
            return todasSolicitacoes;
        }
        File[] arquivos = pasta.listFiles((dir, nome) -> nome.endsWith("_creditos.json"));
        if (arquivos == null) {
            return todasSolicitacoes;
        }
        for (File arq : arquivos) {
            try (FileReader reader = new FileReader(arq)) {
                Type listType = new TypeToken<List<Movimentacao>>() {
                }.getType();
                List<Movimentacao> lista = gson.fromJson(reader, listType);

                if (lista != null) {
                    todasSolicitacoes.addAll(lista);
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler " + arq.getName() + ": " + e.getMessage());
            }
        }

        return todasSolicitacoes;
    }
    
    /**
     * Obtém todas as solicitações de crédito em formato de texto.
     * 
     * @param cpf CPF do cliente
     * @return String formatada com as solicitações de crédito
     */
    
    public static String obterSolicitacoesCredito(String cpf) {
        List<Movimentacao> lista = lerSolicitacaoCredito();
        if (lista.isEmpty()) {
            return "Nenhuma solicitação de crédito encontrada.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== Solicitações de Crédito ===\n\n");

        for (Movimentacao mov : lista) {
            sb.append("Tipo: ").append(mov.getTipo()).append("\n");
            sb.append("Valor: R$ ").append(mov.getValor()).append("\n");
            sb.append("-----------------------------\n");
        }
        return sb.toString();
    }
    
    /**
     * Remove uma solicitação de crédito específica.
     * 
     * @param cpf CPF do cliente
     * @param valor Valor da solicitação de crédito a ser removida
     */
    
    public static void removerSolicitacaoCredito(String cpf, double valor) {
        String caminho = System.getProperty("user.home") + "/bancoswingref/creditos/" + cpf + "_creditos.json";
        File arq = new File(caminho);
        if (!arq.exists()) {
            return;
        }
        try {
            Type listType = new TypeToken<List<Movimentacao>>() {
            }.getType();
            List<Movimentacao> solicitacoes;

            try (FileReader reader = new FileReader(arq)) {
                solicitacoes = gson.fromJson(reader, listType);
            }

            if (solicitacoes == null || solicitacoes.isEmpty()) {
                return;
            } 
            Iterator<Movimentacao> it = solicitacoes.iterator();
            while (it.hasNext()) {
                Movimentacao mov = it.next();
                if ("CREDITO".equalsIgnoreCase(mov.getTipo()) && mov.getValor() == valor) {
                    it.remove(); 
                    break;       
                }
            }
            try (FileWriter writer = new FileWriter(arq)) {
                gson.toJson(solicitacoes, writer);
            }

        } catch (IOException e) {
            System.err.println("Erro ao remover solicitação de crédito: " + e.getMessage());
        }
    }
    
    
}
