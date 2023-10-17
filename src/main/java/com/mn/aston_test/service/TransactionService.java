package com.mn.aston_test.service;

import com.mn.aston_test.exceptions.InsufficientFundsException;
import com.mn.aston_test.model.Account;
import com.mn.aston_test.repo.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final AccountRepository repository;

    public TransactionService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Account withdraw(Account account, Double amount) {
        checkAvailableFunds(account, amount);
        account.setBalance(account.getBalance() - amount);
        return repository.save(account);
    }

    @Transactional
    public Account deposit(Account account, Double amount) {
        account.setBalance(account.getBalance() + amount);
        return repository.save(account);
    }

    @Transactional
    public Account transfer(Account sourceAccount, Account targetAccount, double amount) {
        checkAvailableFunds(sourceAccount, amount);
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
        repository.save(targetAccount);
        return repository.save(sourceAccount);
    }

    private void checkAvailableFunds(Account account, Double amount) {
        if (!(amount <= account.getBalance()))
            throw new InsufficientFundsException();
    }
}
