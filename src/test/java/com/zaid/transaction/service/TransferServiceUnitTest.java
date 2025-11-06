package com.zaid.transaction.service;

import com.zaid.transaction.dto.TransferRequest;
import com.zaid.transaction.dto.TransferResponse;
import com.zaid.transaction.exception.InvalidTransactionException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.model.Transaction;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.TransactionRepository;
import com.zaid.transaction.security.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private TransferService transferService;

    private final String SOURCE_ACCOUNT_NUMBER = "12345678";
    private final String TARGET_ACCOUNT_NUMBER = "12344321";
    private final String LOGGED_IN_USERNAME = "Sapi Moo";
    private final BigDecimal INITIAL_BALANCE = new BigDecimal(100000);

    @BeforeEach
    void setUp() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(LOGGED_IN_USERNAME);
    }

    private Account createMockAccount(String accNumber, BigDecimal balance, String username) {
        User user = new User();
        user.setUsername(username);
        Profile profile = new Profile();
        profile.setUser(user);

        Account account = new Account();
        account.setBalance(balance);
        account.setProfile(profile);
        return account;
    }

    private Transaction createMockTransaction(Account source, Account target, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSourceAccount(source);
        transaction.setTargetAccount(target);
        transaction.setAmount(amount);
        return transaction;
    }

    @Test
    void transferMoney_Success() {
        BigDecimal transferAmount = new BigDecimal("30000");
        TransferRequest request = new TransferRequest(
                SOURCE_ACCOUNT_NUMBER,
                TARGET_ACCOUNT_NUMBER,
                transferAmount,
                "Biaya Bulanan."
        );

        Account sourceAccount = createMockAccount(SOURCE_ACCOUNT_NUMBER, INITIAL_BALANCE, LOGGED_IN_USERNAME);
        Account targetAccount = createMockAccount(TARGET_ACCOUNT_NUMBER, BigDecimal.ZERO, "ohterUser");

        when(accountRepository.findByAccountNumber(SOURCE_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(TARGET_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(targetAccount));

        Transaction mockSavedTransaction = createMockTransaction(sourceAccount, targetAccount, transferAmount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockSavedTransaction);

        TransferResponse response = transferService.transferMoney(request);
        assertEquals("SUCCESS", response.status());
        assertEquals("Transfer Uang Berhasil", response.message());
        assertEquals(SOURCE_ACCOUNT_NUMBER, response.sourceAccount());
        assertEquals(TARGET_ACCOUNT_NUMBER, response.targetAccount());

        assertEquals(new BigDecimal("70000"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("30000"), targetAccount.getBalance());

        verify(accountRepository, times(1)).save(sourceAccount);
        verify(accountRepository, times(1)).save(targetAccount);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void transferMoney_ThrowsUnauthorizedAccessException() {
        TransferRequest request = new TransferRequest(
                SOURCE_ACCOUNT_NUMBER, TARGET_ACCOUNT_NUMBER, new BigDecimal("10000"), "test"
        );

        Account sourceAccount = createMockAccount(SOURCE_ACCOUNT_NUMBER, INITIAL_BALANCE, "Kambing Not Verify");
        when(accountRepository.findByAccountNumber(SOURCE_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(sourceAccount));
        assertThrows(UnauthorizedAccessException.class, () -> {
            transferService.transferMoney(request);
        });

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_ThrowsInvalidTransactionException_SameAccount() {
        TransferRequest request = new TransferRequest(
                SOURCE_ACCOUNT_NUMBER, SOURCE_ACCOUNT_NUMBER, new BigDecimal("10000"), "test"
        );

        Account sourceAccount = createMockAccount(SOURCE_ACCOUNT_NUMBER, INITIAL_BALANCE, LOGGED_IN_USERNAME);
        when(accountRepository.findByAccountNumber(SOURCE_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(sourceAccount));

        assertThrows(InvalidTransactionException.class, () -> {
            transferService.transferMoney(request);
        }, "Seharusnya melempar InvalidTransactionException karena akun sama");

        verify(accountRepository, never()).findByAccountNumber(TARGET_ACCOUNT_NUMBER);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_ThrowsInvalidTransactionException_InsufficientBalance() {
        BigDecimal transferAmount = new BigDecimal("150000");
        TransferRequest request = new TransferRequest(
                SOURCE_ACCOUNT_NUMBER, TARGET_ACCOUNT_NUMBER, transferAmount, "test"
        );

        Account sourceAccount = createMockAccount(SOURCE_ACCOUNT_NUMBER, INITIAL_BALANCE, LOGGED_IN_USERNAME);
        Account targetAccount = createMockAccount(TARGET_ACCOUNT_NUMBER, BigDecimal.ZERO, "otherUser");

        when(accountRepository.findByAccountNumber(SOURCE_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(TARGET_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(targetAccount));

        assertThrows(InvalidTransactionException.class, () -> {
            transferService.transferMoney(request);
        }, "Seharusnya melempar InvalidTransactionException karena saldo tidak cukup");

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_ThrowsInvalidTransactionException_BelowMinBalance() {
        BigDecimal transferAmount = new BigDecimal("86000");
        TransferRequest request = new TransferRequest(
                SOURCE_ACCOUNT_NUMBER, TARGET_ACCOUNT_NUMBER, transferAmount, "test"
        );

        Account sourceAccount = createMockAccount(SOURCE_ACCOUNT_NUMBER, INITIAL_BALANCE, LOGGED_IN_USERNAME);
        Account targetAccount = createMockAccount(TARGET_ACCOUNT_NUMBER, BigDecimal.ZERO, "otherUser");

        when(accountRepository.findByAccountNumber(SOURCE_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(TARGET_ACCOUNT_NUMBER))
                .thenReturn(Optional.of(targetAccount));

        assertThrows(InvalidTransactionException.class, () -> {
            transferService.transferMoney(request);
        }, "Seharusnya melempar InvalidTransactionException karena sisa saldo < 15000");

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

}
