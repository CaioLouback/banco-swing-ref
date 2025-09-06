package utils;

import java.text.ParseException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 * Classe responsável por validação e formatação de CPF.
 * <p>
 * Funcionalidades incluem:
 * <ul>
 *   <li>Validação de CPF (dígitos verificadores);</li>
 *   <li>Formatação com ou sem máscara;</li>
 *   <li>Fornecimento de máscara pronta para JTextField.</li>
 * </ul>
 * 
 * <p><b>Observação:</b> Esta classe foi retirada de um repositório no GitHub,
 * porém o link original não pôde ser recuperado.</p>
 */

public class Formatacao {
    private String cpf;
    private static final String Formato = "###.###.###-##";
    
    /**
     * Construtor que recebe o CPF como String e o armazena limpo (sem máscara).
     * 
     * @param C CPF a ser formatado e armazenado
     */
    
    public Formatacao(String C) {
        this.cpf = this.Format(C,false);
    }
    
    /**
     * Verifica se o CPF armazenado é válido de acordo com os dígitos verificadores.
     * 
     * @return true se o CPF for válido; false caso contrário
     */
  
    public boolean isCPF(){
        
        if (this.cpf.equals("00000000000") || 
            this.cpf.equals("11111111111") || 
            this.cpf.equals("22222222222") || 
            this.cpf.equals("33333333333") ||
            this.cpf.equals("44444444444") ||
            this.cpf.equals("55555555555") ||
            this.cpf.equals("66666666666") ||
            this.cpf.equals("77777777777") ||
            this.cpf.equals("88888888888") ||
            this.cpf.equals("99999999999") ||
            this.cpf.length() != 11)
            return(false);
        
        char dig10, dig11; 
        int sm, i, r, num, peso; 

        try { 
            // Calculo do primeiro Digito Verificador 
            sm = 0; 
            peso = 10; 
            for (i=0; i<9; i++) { 
                num = (int)(this.cpf.charAt(i) - 48); 
                sm = sm + (num * peso); 
                peso = peso - 1;
            } 
            r = 11 - (sm % 11); 
            if ((r == 10) || (r == 11)) 
                dig10 = '0'; 
            else 
                dig10 = (char)(r + 48); 

            // Calculo do segundo Digito Verificador 
            sm = 0; 
            peso = 11; 
            for(i=0; i<10; i++) { 
                num = (int)(this.cpf.charAt(i) - 48);
                sm = sm + (num * peso); 
                peso = peso - 1;
            } 
            r = 11 - (sm % 11); 
            if ((r == 10) || (r == 11)) 
                dig11 = '0'; 
            else 
                dig11 = (char)(r + 48); 

            if ((dig10 == this.cpf.charAt(9)) && (dig11 == this.cpf.charAt(10))) 
                return(true); 
            else return(false);
        } catch(Exception e) { 
            return(false); 
        } 
    }
    
    /**
     * Retorna o CPF com ou sem máscara.
     * 
     * @param Mascara true para retorno com máscara; false para sem máscara
     * @return CPF formatado
     */
    
    public String getCPF(boolean Mascara) {
        return Format(this.cpf,Mascara);
    }
    
    /**
     * Formata o CPF para máscara ou limpa caracteres especiais.
     * 
     * @param C CPF a ser formatado
     * @param Mascara true para adicionar máscara; false para limpar
     * @return CPF formatado ou limpo
     */
    
    
    private String Format(String C, boolean Mascara){
        if(Mascara){
            return(C.substring(0, 3) + "." + C.substring(3, 6) + "." +
            C.substring(6, 9) + "-" + C.substring(9, 11));
        }else{
            C = C.replace(".","");
            C = C.replace("-","");
            return C;
        }
    }
    
    /**
     * Retorna um DefaultFormatterFactory com a máscara de CPF pronta para uso em JTextField.
     * 
     * @return DefaultFormatterFactory para campo de CPF, ou null em caso de erro
     */
    
    public static DefaultFormatterFactory getFormat(){
        try {
            return new DefaultFormatterFactory(new MaskFormatter(Formato));
        } catch (ParseException e) {
            return null;
        }
    }
   
}
