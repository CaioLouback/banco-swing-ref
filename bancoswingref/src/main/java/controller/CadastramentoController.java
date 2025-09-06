package controller;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import utils.CadastramentoService;
import utils.Formatacao;

/**
 * Controlador responsável pelo gerenciamento do processo de cadastramento de usuários.
 * 
 * <p>
 * Esta classe estende {@link CadastramentoService} e fornece métodos estáticos para validar
 * campos, confirmar senhas, verificar CPF e cadastrar usuários no sistema.
 * </p>
 * 
 * @author Caio
 * @version 1.0
 * @since 2025-09-06
 */

public class CadastramentoController extends CadastramentoService {
    
    /**
     * Controla todo o fluxo de cadastramento de um usuário.
     * 
     * <p>
     * O método realiza as seguintes operações:
     * <ul>
     *     <li>Verifica se os campos obrigatórios estão preenchidos;</li>
     *     <li>Confirma se a senha e a confirmação de senha são iguais;</li>
     *     <li>Valida o CPF informado;</li>
     *     <li>Verifica se o CPF já está cadastrado;</li>
     *     <li>Cadastra o usuário caso todas as validações sejam aprovadas;</li>
     *     <li>Exibe mensagens de alerta ou erro através de {@link JOptionPane}.</li>
     * </ul>
     * </p>
     *
     * @param nome Nome completo do usuário a ser cadastrado.
     * @param cpf CPF do usuário a ser cadastrado, apenas números.
     * @param senha Campo de senha do usuário (JPasswordField).
     * @param confSenha Campo de confirmação de senha (JPasswordField).
     * @param tipo Tipo de usuário (ex.: "Cliente", "Gerente", etc.).
     */
    
    public static void controladorCadastramento(String nome, String cpf, JPasswordField senha, JPasswordField confSenha, String tipo) {
        int vazio = verificarVazio(nome, cpf, senha, confSenha);

        if(vazio == 0){
            boolean senhaConfirmada = confirmacaoSenha(senha, confSenha);
            if (senhaConfirmada) {
                Formatacao numCpf = new Formatacao(cpf);
                if (!numCpf.isCPF())
                    JOptionPane.showMessageDialog(null, "CPF inválido! Favor inserir um CPF válido!", "ERROR!",JOptionPane.ERROR_MESSAGE);
                else{
                    boolean cpfJaCadastrado = cpfCadastrado(cpf);
                    if(!cpfJaCadastrado){
                        String pass = new String(senha.getPassword());
                        cadastrar(nome, cpf, pass, tipo);
                        JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(null, "CPF já está cadastrado!", "Atenção!", JOptionPane.WARNING_MESSAGE);
                    } 
                }
            } else {
                JOptionPane.showMessageDialog(null, "Sua confirmação de senha está incorreta!", "Atenção!", JOptionPane.WARNING_MESSAGE);
            }
        } else if (vazio == 1) {
            JOptionPane.showMessageDialog(null, "Favor inserir um nome!");
        } else if (vazio == 2) {
            JOptionPane.showMessageDialog(null, "Favor inserir um CPF!");
        } else if (vazio == 3) {
            JOptionPane.showMessageDialog(null, "Favor inserir uma senha!");
        } else if (vazio == 4) {
            JOptionPane.showMessageDialog(null, "Favor confirmar a sua senha!");
        }
    }
    
    
    
    
    
}
