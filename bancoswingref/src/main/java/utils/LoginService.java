package utils;

import static controller.CaixaController.instanciaTelaCaixa;
import static controller.ClienteController.instaciaTelaCliente;
import static dao.Json.lerUsuarios;
import java.util.List;
import user.Usuarios;
import view.TelaCaixa;
import view.TelaCliente;

/**
 * Serviço de login para autenticação e abertura de telas de usuários.
 * <p>
 * Esta classe fornece métodos para validar login, verificar campos vazios
 * e abrir a tela correspondente ao tipo do usuário (Cliente ou Caixa).
 * Métodos são estáticos e a classe não pode ser instanciada.
 * </p>
 * 
 * @author Caio
 * @version 1.0
 */

public class LoginService {
    
    /**
     * Construtor protegido para evitar instanciação direta da classe.
     */
    protected LoginService(){}; 
    
     /**
     * Valida as credenciais de login comparando CPF e senha.
     *
     * @param cpfDigitado O CPF digitado pelo usuário.
     * @param senhaDigitada A senha digitada pelo usuário.
     * @return true se o login for válido, false caso contrário.
     */
    
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
    
    /**
     * Realiza o login de um usuário, abrindo a tela correspondente ao tipo.
     *
     * @param cpf CPF do usuário que realizou login.
     */
    
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
    
    /**
     * Abre a tela do Caixa.
     *
     * @param cpf CPF do usuário do tipo Caixa.
     */
    
    private static void telaCaixa(String cpf){
        TelaCaixa tela = new TelaCaixa(cpf);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
        instanciaTelaCaixa(tela);
    }
    
    /**
     * Abre a tela do Cliente.
     *
     * @param cpf CPF do usuário do tipo Cliente.
     */
    
    private static void telaCliente(String cpf){
        TelaCliente tela = new TelaCliente(cpf);
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
        instaciaTelaCliente(tela);
    }
    
    /**
     * Verifica se o CPF ou senha estão vazios.
     *
     * @param cpfDigitado CPF digitado.
     * @param senhaDigitada Senha digitada.
     * @return true se algum campo estiver vazio, false caso contrário.
     */
    
    public static boolean verificarVazio(String cpfDigitado, String senhaDigitada){
        if(cpfDigitado.trim().isEmpty() || senhaDigitada.length() == 0){
            return true;
        }
        return false;
    }

}
