package com.zaid.transaction.exception;

public class InvalidPinException extends RuntimeException{
    public InvalidPinException() {
        super("PIN Rransaksi salah. Otorisasi Gagal.");
    }
}
