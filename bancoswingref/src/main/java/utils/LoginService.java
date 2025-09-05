package utils;

import static controller.CaixaController.instanciaTelaCaixa;
import static controller.ClienteController.instaciaTelaCliente;
import static dao.Json.lerUsuarios;
import java.util.List;
import user.Usuarios;
import view.TelaCaixa;
import view.TelaCliente;

public class LoginService {
    
    protected LoginService(){}; // Evitar instaciar 
    
    protected static boolean autenticadorLogin(String cpfDigitado, String senhaDigitada){ 
        boolean loginValido = false;
        List<Usuarios> listaUsuarios = lerUsuarios();
       
        for(Usuarios var : listaUsuarios){ // Valiando o login e senha
            System.out.println("Usuários no JSON: " + var.getNome() + " " + var.getCPF()  + " Tipo: " + var.getTipo());
            if(var.getCPF().equals(cpfDigitado) && var.verificarSenha(senhaDigitada)){
                loginValido = true;
                System.out.println("Logou!");
                return loginValido;
            }
        } 
        return loginValido;      
    }
    
    protected static void logar(String cpf){
        List<Usuarios> listaUsuarios = lerUsuarios();
        
        for(Usuarios var: listaUsuarios){
            if (var.getTipo().trim().equalsIgnoreCase("Cliente") && var.getCPF().equals(cpf)) {
                System.out.println("Entrou como Cliente!");
                telaCliente(cpf);
            } else if (var.getTipo().trim().equalsIgnoreCase("Caixa") && var.getCPF().equals(cpf)) {
                System.out.println("Entrou como Caixa!");
                telaCaixa(cpf);
            } else if(var.getTipo().trim().equalsIgnoreCase("Gerente") && var.getCPF().equals(cpf)) {
                System.out.println("Entrou como Gerente!");
                //telaGerente(cpf);
            }
        }
    }
    
    private static void telaCaixa(String cpf){
        TelaCaixa tela = new TelaCaixa(cpf);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
        instanciaTelaCaixa(tela);
    }
    
    
    private static void telaCliente(String cpf){
        TelaCliente tela = new TelaCliente(cpf);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
        instaciaTelaCliente(tela);
    }
    
    
    public static boolean verificarVazio(String cpfDigitado, String senhaDigitada){
        if(cpfDigitado.trim().isEmpty() || senhaDigitada.length() == 0){
            return true;
        }
        return false;
    }

}
