package utils;

import static dao.Json.atualizarSaldo;
import static dao.Json.lerUsuarios;
import static dao.Json.registrarMovimentacoes;
import java.util.List;
import user.Cliente;
import user.Usuarios;

/**
 * Classe utilitária que fornece serviços de operações bancárias realizadas por caixas.
 * 
 * <p>Responsabilidades incluem:</p>
 * <ul>
 *   <li>Transferência entre contas de clientes;</li>
 *   <li>Saque de valores;</li>
 *   <li>Depósito em contas;</li>
 *   <li>Retorno de objetos Cliente a partir de CPF;</li>
 *   <li>Verificação de cadastro de dois CPFs;</li>
 *   <li>Formatação de valores monetários.</li>
 * </ul>
 * 
 * <p>Todos os métodos são <b>estáticos</b> e protegidos, sendo utilizados apenas internamente pelos controladores.</p>
 * 
 * @author Caio
 * @version 1.0
 * @since 2025-09-06
 */

public class CaixaService{
    
    /**
     * Realiza a transferência de um valor de uma conta de cliente para outra.
     * 
     * @param cliente_origem Cliente que irá transferir o valor
     * @param cliente_destino Cliente que irá receber o valor
     * @param valor Valor a ser transferido
     */
    
    protected static void transferir(Cliente cliente_origem, Cliente cliente_destino, double valor){
        double saldoOrigem = cliente_origem.getSaldo();
        saldoOrigem -= valor;
        cliente_origem.setSaldo(saldoOrigem);
        double saldoDestino = cliente_destino.getSaldo();
        saldoDestino += valor;
        cliente_destino.setSaldo(saldoDestino);
        atualizarSaldo(cliente_origem);
        atualizarSaldo(cliente_destino);
        registrarMovimentacoes(cliente_origem.getCPF(), "Transferência (-)", valor);
        registrarMovimentacoes(cliente_destino.getCPF(), "Transferência (+)", valor);
    }
    
    /**
     * Realiza o saque de um valor da conta do cliente.
     * 
     * @param cliente Cliente que realizará o saque
     * @param valor Valor a ser sacado
     */
    
    protected static void sacar(Cliente cliente, double valor){
        double saldo = cliente.getSaldo() - valor;
        cliente.setSaldo(saldo);
        atualizarSaldo(cliente);
        registrarMovimentacoes(cliente.getCPF(), "Saque (-)", valor);
    }
    
     /**
     * Retorna o objeto Cliente correspondente ao CPF informado.
     * 
     * @param cpf CPF do cliente a ser buscado
     * @return Objeto Cliente caso encontrado; null se não existir
     */
    
    protected static Cliente retornaCliente(String cpf) {
        List<Usuarios> listaUsuarios = lerUsuarios();
        Cliente cliente = null;
        for (Usuarios var : listaUsuarios) {
            if (cpf.equals(var.getCPF())) {
                if (var instanceof Cliente) {
                    cliente = (Cliente) var;
                    break;
                }
            }
        }
        return cliente;
    }
    /**
     * Verifica se dois CPFs informados estão cadastrados no sistema.
     * 
     * @param cpf CPF do primeiro cliente
     * @param cpf2 CPF do segundo cliente
     * @return true se ambos estiverem cadastrados, false caso contrário
     */
    
    protected static boolean verificarCadastro(String cpf, String cpf2){
        boolean cliente1 = false;
        boolean cliente2 = false;
        List<Usuarios> lista = lerUsuarios();
        for (Usuarios var : lista) {
            if(var.getCPF().equals(cpf))
                cliente1 = true;
            else if (var.getCPF().equals(cpf2))
                cliente2 = true;
        }
        
        if(cliente1 && cliente2)
            return true;
        else
            return false;
    }
       
    /**
     * Converte uma string de valor monetário para double.
     * Aceita strings no formato "R$ 1.234,56".
     * 
     * @param valorTexto Valor em formato String
     * @return Valor convertido em double; 0.0 se vazio; -1 se inválido
     */
    
    protected static double formatarValor(String valorTexto) {
        if (valorTexto == null || valorTexto.isEmpty()) {
            return 0.0;
        }
        try {
            String valorLimpo = valorTexto.replace("R$", "").replace(".", "").replace(",", ".").trim();
            return Double.parseDouble(valorLimpo);
        } catch (NumberFormatException e) {      
            return -1;
        }
    }
    
    /**
     * Realiza um depósito na conta do cliente.
     * 
     * @param cliente Cliente que irá receber o depósito
     * @param valor Valor a ser depositado
     * @return true se o depósito foi registrado com sucesso
     */
    
    protected static boolean deposito(Cliente cliente, double valor){
        double saldo = cliente.getSaldo();
        saldo += valor;
        cliente.setSaldo(saldo);
        atualizarSaldo(cliente);
        registrarMovimentacoes(cliente.getCPF(), "Depósito (+)", valor);
        return true;
    }
   
}
