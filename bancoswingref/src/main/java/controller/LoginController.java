package controller;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import utils.LoginService;
import view.Cadastramento;

/**
 * Controlador responsável pelo gerenciamento do login de usuários.
 * 
 * <p>
 * Esta classe estende {@link LoginService} e fornece métodos estáticos para:
 * <ul>
 *     <li>Validar login de usuários;</li>
 *     <li>Mostrar ou ocultar senha em campos JPasswordField;</li>
 *     <li>Abrir a tela de cadastramento de novos usuários.</li>
 * </ul>
 * </p>
 * 
 * @author Caio
 * @version 1.0
 * @since 2025-09-06
 */

public class LoginController extends LoginService{
    
     /**
     * Controla o fluxo de login de um usuário.
     * 
     * <p>
     * O método realiza as seguintes operações:
     * <ul>
     *     <li>Verifica se os campos CPF e senha estão preenchidos;</li>
     *     <li>Valida o login chamando o {@link LoginService#autenticadorLogin(String, String)};</li>
     *     <li>Exibe mensagens de erro caso o login falhe;</li>
     *     <li>Realiza o login caso as credenciais estejam corretas.</li>
     * </ul>
     * </p>
     * 
     * @param cpf CPF do usuário
     * @param senha Senha do usuário
     */
    
    public static void controladorLogin(String cpf, String senha){
        boolean vazio = verificarVazio(cpf, senha);
        boolean loginValidado = false;
        
        if(vazio){
            JOptionPane.showMessageDialog(null,"Há informações em branco. Favor inserir um cadastro válido!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else {
            loginValidado = autenticadorLogin(cpf, senha);
            if(!loginValidado){
                JOptionPane.showMessageDialog(null, "CPF ou senha incorretos. Tente novamente!", "Atenção!", JOptionPane.WARNING_MESSAGE);
            } else {
                logar(cpf);
            }     
        }       
    }
    
    /**
     * Mostra ou oculta a senha no campo JPasswordField.
     * 
     * @param check JCheckBox indicando se a senha deve ser exibida
     * @param senha Campo JPasswordField da senha
     */
    
    public static void mostrarSenha(JCheckBox check, JPasswordField senha){
         if(check.isSelected())
            senha.setEchoChar((char)0);
        else
            senha.setEchoChar('*');
    }
    
    /**
     * Abre a tela de cadastramento de novos usuários.
     */
    
    public static void telaCadastramento(){
        Cadastramento cad = new Cadastramento();
        cad.setLocationRelativeTo(null);
        cad.setVisible(true);
    }
    
    
    
    
    
}
