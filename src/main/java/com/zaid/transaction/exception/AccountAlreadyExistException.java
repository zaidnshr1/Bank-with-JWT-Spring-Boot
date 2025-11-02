package com.zaid.transaction.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String accountNumber) {
        super("Nomor Akun " + accountNumber + " Telah Tersedia. Gunakan Nomor Akun Yang Lain.");
    }
}
