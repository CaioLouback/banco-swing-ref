package utils;

import static dao.Json.atualizarSaldo;
import static dao.Json.lerUsuarios;
import java.util.List;
import user.Cliente;
import user.Usuarios;


public class CaixaService{
    
    protected static void transferir(Cliente cliente_origem, Cliente cliente_destino, double valor){
        double saldoOrigem = cliente_origem.getSaldo();
        saldoOrigem -= valor;
        cliente_origem.setSaldo(saldoOrigem);
        double saldoDestino = cliente_destino.getSaldo();
        saldoDestino += valor;
        cliente_destino.setSaldo(saldoDestino);
        atualizarSaldo(cliente_origem);
        atualizarSaldo(cliente_destino);   
    }
    
    protected static void sacar(Cliente cliente, double valor){
        double saldo = cliente.getSaldo() - valor;
        cliente.setSaldo(saldo);
        atualizarSaldo(cliente);
    }
    
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
    
    protected static boolean deposito(Cliente cliente, double valor){
        double saldo = cliente.getSaldo();
        saldo += valor;
        cliente.setSaldo(saldo);
        atualizarSaldo(cliente);
        return true;
    }
   
}
