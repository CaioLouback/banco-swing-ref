package controller;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import utils.CadastramentoService;
import utils.Formatacao;


public class CadastramentoController extends CadastramentoService {
    
    
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
