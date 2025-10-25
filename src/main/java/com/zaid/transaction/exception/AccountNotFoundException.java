package com.zaid.transaction.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String accountNummber) {
        super("Akun dengan nomor " + accountNummber + " tidak ditemukan.");
    }
}
