package com.zaid.transaction.repository;

import com.zaid.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @EntityGraph(attributePaths = {"sourceAccount", "targetAccount"})
    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount.accountNumber = :accountNumber OR t.targetAccount.accountNumber = :accountNumber ORDER BY t.transactionDate DESC")
    Page<Transaction> findBySourceAccountOrTargetAccount(String accountNumber, Pageable pageable);

}
