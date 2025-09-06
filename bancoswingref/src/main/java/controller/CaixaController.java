package controller;

import static dao.Json.lerUsuarios;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import user.Caixa;
import user.Cliente;
import user.Usuarios;
import utils.CaixaService;
import view.Confirmacao;
import view.Deposito;
import view.Saque;
import view.TelaCaixa;
import view.TelaTransferencia;


public class CaixaController extends CaixaService{
    private static Caixa caixa;
    private static TelaCaixa tela;
    private static Cliente cliente_origem;
    private static Cliente cliente_destino;
    
    public static void nomeCaixa(JLabel nome, String cpf){
        List<Usuarios> listaUsuarios = lerUsuarios();
        
        for(Usuarios var : listaUsuarios){
            if(cpf.equals(var.getCPF())){
                if(var instanceof Caixa){
                    caixa = (Caixa) var;
                    nome.setText(caixa.getNome());
                }  
            }      
        }  
    }
    
    public static void instanciaTelaCaixa(TelaCaixa tela1){
        tela = tela1;
    }
    
    public static void abrirTelaTransferencia(){
        TelaTransferencia telaT = new TelaTransferencia(tela ,true);
        telaT.setLocationRelativeTo(tela);
        telaT.setVisible(true);
    }
    
    public static void abrirTelaSaque(){
        Saque saque = new Saque(tela, true);
        saque.setLocationRelativeTo(tela);
        saque.setVisible(true);
    }
    
    
    public static void controladorTransferencia(JFormattedTextField origem,JFormattedTextField destino, JFormattedTextField v){
        double valor = formatarValor(v.getText());

        if(origem.getText() == null || destino.getText() == null){
            JOptionPane.showMessageDialog(null, "Favor preencher corretamente todos os campos!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else if (valor == 0){
            JOptionPane.showMessageDialog(null, "Insira um valor que não seja 0.", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else if (valor < 0){
            JOptionPane.showMessageDialog(null, "Favor preencher corretamente o campo valor!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else {
            if(origem.getText() != null || destino.getText() != null){
                boolean cadastrado = verificarCadastro(origem.getText(),destino.getText());
                if(!cadastrado)
                    JOptionPane.showMessageDialog(null, "CPF inválido! Favor inserir um CPF que tenha conta no banco.", "Atenção!", JOptionPane.WARNING_MESSAGE);
                else {
                    String cpf_origem = origem.getText();
                    String cpf_destino = destino.getText();
                    Cliente cliente_origem = retornaCliente(cpf_origem);
                    Cliente cliente_destino = retornaCliente(cpf_destino);

                    if (cliente_origem.getSaldo() < valor) {
                        JOptionPane.showMessageDialog(null, "Saldo insuficiente!", "Atenção!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        telaConfirmacao(tela, cpf_origem, valor, cpf_destino);
                        //JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }   
        }  
    }       
           
    public static void abrirTelaDeposito(){
        Deposito dep = new Deposito(tela, true);
        dep.setLocationRelativeTo(tela);
        dep.setVisible(true);
    }
   
    protected static void telaConfirmacao(TelaCaixa tela, String cpf_origem, double valor, String cpfD){
        Confirmacao conf = new Confirmacao(tela, true, cpf_origem, valor, cpfD);
        conf.setLocationRelativeTo(tela);
        conf.setVisible(true);
        
    }
            
    public static void confirmar(JFormattedTextField cpf, JPasswordField senha, double valor, String cpfD){
        boolean verifica = false;
        if (cliente_origem == null){
            JOptionPane.showMessageDialog(null,"Login ou senha estão incorretos!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else {
            cliente_origem = retornaCliente(cpf.getText());
            cliente_destino = retornaCliente(cpfD);
            verifica = cliente_origem.verificarSenha(String.valueOf(senha.getPassword()));
        }
        if(senha.getPassword().length == 0){
            JOptionPane.showMessageDialog(null,"Insira a sua senha!", "Atenção!", JOptionPane.WARNING_MESSAGE);   
        }else if(!cpf.getText().equals(cliente_origem.getCPF()) || !verifica){
            JOptionPane.showMessageDialog(null,"Login ou senha estão incorretos!", "Atenção!", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (cpf.getText().equals(cliente_origem.getCPF()) && verifica == true){
            transferir(cliente_origem, cliente_destino, valor);
            JOptionPane.showMessageDialog(null,"Confirmação realizada com sucesso! Sua trasferência foi bem sucedida! ","Sucesso!", JOptionPane.INFORMATION_MESSAGE);
        } 
    }

    protected static void campoMonetario(JFormattedTextField valor) {
       NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
       formato.setMaximumFractionDigits(2); // Permite duas casas decimais
       formato.setMinimumFractionDigits(2); // Sempre exibe duas casas decimais

       NumberFormatter formatador = new NumberFormatter(formato);
       formatador.setAllowsInvalid(false);
       formatador.setOverwriteMode(true);
       formatador.setCommitsOnValidEdit(true); // Atualiza automaticamente

       valor.setFormatterFactory(new DefaultFormatterFactory(formatador));
       valor.setValue(0.00); 
    }
    
    protected static double formatarValor(String valorTexto) {
        if (valorTexto == null || valorTexto.isEmpty()) {
            return 0.0;
        }
        try {
            String valorLimpo = valorTexto.replace("R$", "").replace(".", "").replace(",", ".").trim();
            return Double.parseDouble(valorLimpo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido! Digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return 0.0;
        }
    }
    
    public static void controladorDeposito(JFormattedTextField txtCPF, JFormattedTextField  txtValor){
        double valor = formatarValor(txtValor.getText());
        Cliente cliente = retornaCliente(txtCPF.getText());
        if(txtCPF.getText() == null || valor == 0){
            JOptionPane.showMessageDialog(null, "Favor preencher corretamente todos os campos!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else if (cliente == null){
            JOptionPane.showMessageDialog(null, "CPF não é cadastrado no bancoo.", "Erro", JOptionPane.ERROR_MESSAGE);
        }else if (valor <0){
            JOptionPane.showMessageDialog(null, "VALOR NEGATIVO NÃO SÃO ACEITOS!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }else{
            deposito(cliente, valor);
            JOptionPane.showMessageDialog(null, "Depósito registrado com sucesso!", "Depósito bem sucedido!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    public static void controladorSaque(JFormattedTextField txtCPF, JPasswordField txtSenha , JFormattedTextField  txtValor){
        if(txtCPF.getText() == null || txtSenha.getPassword().length == 0 || txtValor.getText() == null){
            JOptionPane.showMessageDialog(null, "Favor preencher corretamente todos os campos!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else{
            String cpf = txtCPF.getText();
            String senha = new String(txtSenha.getPassword());
            double valor = formatarValor(txtValor.getText());
            if(valor < 0){
                JOptionPane.showMessageDialog(null, "VALOR NEGATIVO NÃO SÃO ACEITOS!", "ERROR!", JOptionPane.ERROR_MESSAGE);
            } else {
                Cliente cliente = retornaCliente(cpf);
                if(cliente != null){
                    if(cliente.getSaldo() < valor){
                        JOptionPane.showMessageDialog(null, "Saldo insuficiente!", "Atenção!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        boolean s = cliente.verificarSenha(senha);
                        if(cliente.getCPF().equals(cpf) && s == true){
                            sacar(cliente, valor);
                            JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!", "Saque bem sucedido!", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,"Login ou senha estão incorretos!", "Atenção!", JOptionPane.WARNING_MESSAGE);
                        }  
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "CPF não é cadastrado no bancoo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }  
        }
    }
    
    public static void consultarSaldo(JLabel lblSaldo,JFormattedTextField txtCPF){
        if(txtCPF.getText() == null ){
            JOptionPane.showMessageDialog(null, "Favor preencher corretamente o campo referente ao CPF!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        }
        String cpf = txtCPF.getText();
        Cliente cliente = retornaCliente(cpf);
        if(cliente == null){
            JOptionPane.showMessageDialog(null, "CPF não é cadastrado no bancoo.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else{
            NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            lblSaldo.setText(formato.format(cliente.getSaldo()));
        } 
    }
    
    
    
}
