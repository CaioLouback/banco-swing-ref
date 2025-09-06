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

/**
 * Controlador responsável pelo gerenciamento das operações realizadas por um Caixa.
 * 
 * <p>
 * Esta classe estende {@link CaixaService} e fornece métodos estáticos para:
 * <ul>
 *     <li>Exibir o nome do caixa logado;</li>
 *     <li>Abrir telas de movimentação (transferência, saque, depósito);</li>
 *     <li>Controlar operações de transferência, depósito e saque;</li>
 *     <li>Consultar saldo de clientes;</li>
 *     <li>Formatar campos monetários.</li>
 * </ul>
 * </p>
 * 
 * @author Caio
 * @version 1.0
 * @since 2025-09-06
 */


public class CaixaController extends CaixaService{
    private static Caixa caixa; /** Objeto do caixa atualmente logado */
    private static TelaCaixa tela; /** Tela principal do caixa */
    private static Cliente cliente_origem; /** Cliente origem da operação (transferência ou saque) */
    private static Cliente cliente_destino; /** Cliente destino da transferência */
    
    /**
     * Atualiza o JLabel com o nome do caixa logado.
     * 
     * @param nome JLabel que exibirá o nome do caixa
     * @param cpf CPF do caixa logado
     */
    
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
    
     /**
     * Armazena a referência da tela principal do caixa.
     * 
     * @param tela1 Instância da {@link TelaCaixa} a ser armazenada
     */
    
    public static void instanciaTelaCaixa(TelaCaixa tela1){
        tela = tela1;
    }
    
    /**
     * Abre a tela de transferência centralizada em relação à tela principal do caixa.
     */
    
    public static void abrirTelaTransferencia(){
        TelaTransferencia telaT = new TelaTransferencia(tela ,true);
        telaT.setLocationRelativeTo(tela);
        telaT.setVisible(true);
    }
    
    /**
     * Abre a tela de saque centralizada em relação à tela principal do caixa.
     */
    
    public static void abrirTelaSaque(){
        Saque saque = new Saque(tela, true);
        saque.setLocationRelativeTo(tela);
        saque.setVisible(true);
    }
    
    /**
     * Controla o fluxo de transferência entre clientes.
     * 
     * @param origem Campo com CPF do cliente origem
     * @param destino Campo com CPF do cliente destino
     * @param v Campo com o valor a ser transferido
     */
    
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
                    }
                }
            }   
        }  
    }       
     
    /**
     * Abre a tela de depósito centralizada em relação à tela principal do caixa.
     */
    
    public static void abrirTelaDeposito(){
        Deposito dep = new Deposito(tela, true);
        dep.setLocationRelativeTo(tela);
        dep.setVisible(true);
    }
    
     /**
     * Exibe a tela de confirmação de transferência.
     * 
     * @param tela Tela do caixa
     * @param cpf_origem CPF do cliente origem
     * @param valor Valor a ser transferido
     * @param cpfD CPF do cliente destino
     */
    
    protected static void telaConfirmacao(TelaCaixa tela, String cpf_origem, double valor, String cpfD){
        Confirmacao conf = new Confirmacao(tela, true, cpf_origem, valor, cpfD);
        conf.setLocationRelativeTo(tela);
        conf.setVisible(true);
        
    }
    
    /**
     * Confirma a transferência verificando senha e saldo.
     * 
     * @param cpf Campo de CPF do cliente origem
     * @param senha Campo de senha do cliente origem
     * @param valor Valor a ser transferido
     * @param cpfD CPF do cliente destino
     */
    
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
    
    /**
     * Configura um campo JFormattedTextField para entrada monetária.
     * 
     * @param valor Campo a ser formatado
     */
    
    protected static void campoMonetario(JFormattedTextField valor) {
       NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
       formato.setMaximumFractionDigits(2); 
       formato.setMinimumFractionDigits(2); 

       NumberFormatter formatador = new NumberFormatter(formato);
       formatador.setAllowsInvalid(false);
       formatador.setOverwriteMode(true);
       formatador.setCommitsOnValidEdit(true); 

       valor.setFormatterFactory(new DefaultFormatterFactory(formatador));
       valor.setValue(0.00); 
    }
    
    /**
     * Converte texto monetário em double.
     * 
     * @param valorTexto Texto com o valor (ex: "R$ 1.234,56")
     * @return Valor numérico em double. Retorna 0.0 se inválido.
     */
    
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
    
    /**
     * Controla o fluxo de depósito para um cliente.
     * 
     * @param txtCPF Campo com CPF do cliente
     * @param txtValor Campo com o valor do depósito
     */
    
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
    
    /**
     * Controla o fluxo de saque de um cliente.
     * 
     * @param txtCPF Campo com CPF do cliente
     * @param txtSenha Campo com senha do cliente
     * @param txtValor Campo com valor a ser sacado
     */
    
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
                            if(valor > 1000000){
                                JOptionPane.showMessageDialog(null, "Solicitação Realizada! Basta aguardar o seu gerente liberar o saque!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                                
                            } else {
                                sacar(cliente, valor);
                                JOptionPane.showMessageDialog(null, "Saque realizado com sucesso!", "Saque bem sucedido!", JOptionPane.INFORMATION_MESSAGE);
                            } 
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
    
    /**
     * Consulta o saldo de um cliente e atualiza JLabel.
     * 
     * @param lblSaldo JLabel que exibirá o saldo
     * @param txtCPF Campo com CPF do cliente
     */
    
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
