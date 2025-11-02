package com.zaid.transaction.exception;

public class InvalidPinException extends RuntimeException{
    public InvalidPinException() {
        super("PIN Transaksi salah. Otorisasi Gagal.");
    }
}
