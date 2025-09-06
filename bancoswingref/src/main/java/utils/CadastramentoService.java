package utils;

import dao.Json;
import static dao.Json.lerUsuarios;
import java.util.Arrays;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import user.Caixa;
import user.Cliente;
import user.Gerente;
import user.Usuarios;

/**
 * Classe utilitária que fornece serviços de cadastramento de usuários no sistema bancário.
 * 
 * <p>Responsabilidades incluem:</p>
 * <ul>
 *   <li>Cadastrar novos usuários (Cliente, Caixa ou Gerente);</li>
 *   <li>Verificar campos vazios em formulários de cadastro;</li>
 *   <li>Validar confirmação de senha;</li>
 *   <li>Verificar se um CPF já está cadastrado;</li>
 *   <li>Exibir ou ocultar senhas nos formulários.</li>
 * </ul>
 * 
 * <p>Todos os métodos são <b>estáticos</b> e a classe não pode ser instanciada diretamente fora do pacote.</p>
 * 
 * @author Caio
 * @version 1.0
 * @since 2025-09-06
 */

public class CadastramentoService {
    protected CadastramentoService(){}; /** Construtor protegido para impedir instanciação externa */
    
    /**
     * Cadastra um usuário no sistema de acordo com o tipo informado.
     * 
     * @param nome Nome do usuário
     * @param cpf CPF do usuário
     * @param senha Senha do usuário
     * @param tipo Tipo do usuário ("Cliente", "Caixa" ou "Gerente")
     */
    
    protected static void cadastrar(String nome, String cpf, String senha, String tipo){
        if(null != tipo){
            switch (tipo) {
            case "Cliente":
                Cliente cliente = new Cliente(nome, senha, cpf, 0);
                Json.salvarUsuarios(cliente);
                break;
            case "Caixa":
                Caixa caixa = new Caixa(nome, senha, cpf);
                Json.salvarUsuarios(caixa);
                break;
            case "Gerente":
                Gerente gerente = new Gerente(nome, senha, cpf);
                Json.salvarUsuarios(gerente);
                break;
            default:
                break;
            }  
        } 
    }
    
     /**
     * Verifica se a senha e a confirmação de senha são iguais.
     * 
     * @param senha Campo de senha
     * @param confSenha Campo de confirmação de senha
     * @return true se as senhas forem iguais, false caso contrário
     */
    
    protected static boolean confirmacaoSenha(JPasswordField senha,JPasswordField confSenha){
        if(Arrays.equals(senha.getPassword(), confSenha.getPassword()))
            return true;
        return false;
    }
    
    /**
     * Verifica se algum campo do formulário de cadastro está vazio.
     * 
     * @param nome Nome do usuário
     * @param cpf CPF do usuário
     * @param senha Campo de senha
     * @param confSenha Campo de confirmação de senha
     * @return Código indicando o campo vazio:
     *         <ul>
     *           <li>0 - Todos os campos preenchidos</li>
     *           <li>1 - Nome vazio</li>
     *           <li>2 - CPF vazio</li>
     *           <li>3 - Senha vazia</li>
     *           <li>4 - Confirmação de senha vazia</li>
     *         </ul>
     */
    
    protected static int verificarVazio(String nome, String cpf,JPasswordField senha, JPasswordField confSenha){
        if(nome.trim().isEmpty())
            return 1;
        if(cpf.trim().isEmpty())
            return 2;
        if(senha.getPassword().length == 0)  
            return 3;
        if(confSenha.getPassword().length == 0)
            return 4;
        return 0;
    }
    
    /**
     * Mostra ou oculta a senha e confirmação de senha em um formulário de cadastro.
     * 
     * @param check Caixa de seleção para mostrar a senha
     * @param senha Campo de senha
     * @param confSenha Campo de confirmação de senha
     */
    
    public static void mostrarSenha(JCheckBox check, JPasswordField senha, JPasswordField confSenha){
        if(check.isSelected()){
           senha.setEchoChar((char)0);
           confSenha.setEchoChar((char)0);
        }else {
           senha.setEchoChar('*');
           confSenha.setEchoChar('*');
        }    
    }
    
    /**
     * Verifica se um CPF já está cadastrado no sistema.
     * 
     * @param cpf CPF a ser verificado
     * @return true se o CPF já estiver cadastrado, false caso contrário
     */
    
    protected static boolean cpfCadastrado(String cpf){
        List<Usuarios> listaUsuarios = lerUsuarios();
        for(Usuarios var: listaUsuarios){
            if(var.getCPF().equals(cpf))
                return true;
        }
        return false;
    }
    
    
    
}
