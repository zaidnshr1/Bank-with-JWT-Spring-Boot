package com.zaid.transaction.service;

import com.zaid.transaction.dto.TransferRequest;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Transaction;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@Transactional
@SpringBootTest
public class TransferServiceIntegrationTest {
    @Autowired
    private TransferService transferService;
    @Autowired
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;

    @Test @WithMockUser(username = "userAdmin", roles = {"ADMIN"})
    void transferMoney__Fails_WhenUserIsNotClient() {
        TransferRequest request = new TransferRequest(
                "12345678",
                "12344321",
                BigDecimal.ONE,
                "test"
        );
        assertThrows(AuthorizationDeniedException.class, () -> {
            transferService.transferMoney(request);
        });
    }

    @Test @WithMockUser(username = "userClient", roles = {"CLIENT"})
    void transferMoney_Rollback_OnFailuer() {
        Account sourceBefore = accountRepository.findByAccountNumber("12344321").get();
        BigDecimal initialBalance = sourceBefore.getBalance();

        BigDecimal transferAmount = new BigDecimal("10000");
        TransferRequest request = new TransferRequest(
                "12344321",
                "12341234",
                transferAmount, "uji coba rollback"
        );

        doThrow(new RuntimeException("Simyulasi Kegagalan I/O Data"))
                .when(transactionRepository).save(any(Transaction.class));

        assertThrows(RuntimeException.class, () -> {
            transferService.transferMoney(request);
        });

        Account sourceAfter = accountRepository.findByAccountNumber("12344321").get();
        assertEquals(initialBalance.compareTo(sourceAfter.getBalance()), 0,
                "Salod Setelah exception harus sama dengan di awal karena di rollback");
        assertTrue(transactionRepository.findAll().isEmpty(),
                "Tidak ada Transaksi Tersimpan");
    }

}
