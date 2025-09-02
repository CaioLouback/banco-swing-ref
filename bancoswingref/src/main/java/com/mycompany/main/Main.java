package com.mycompany.main;

import user.Cliente;

public class Main {

    public static void main(String[] args) {
        Cliente user = new Cliente("Caio", "senha123", "123456789", 100.00);
        
        System.out.println("Nome: " + user.getNome());
        System.out.println("cpf:  " + user.getCPF());
        System.out.println("Saldo: " + user.getSaldo());
        user.setSaldo(250.00);
        System.out.println("Saldo: " + user.getSaldo());
        
        
    }
}
