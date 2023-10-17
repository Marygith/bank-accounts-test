package com.mn.aston_test.service;

import com.mn.aston_test.exceptions.AccountAlreadyExistsException;
import com.mn.aston_test.exceptions.AccountNotFoundException;
import com.mn.aston_test.exceptions.BadPinException;
import com.mn.aston_test.exceptions.PinIsNotCorrectException;
import com.mn.aston_test.model.Account;
import com.mn.aston_test.model.Deposit;
import com.mn.aston_test.model.Transfer;
import com.mn.aston_test.model.Withdrawal;
import com.mn.aston_test.repo.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository repository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository repository, TransactionService transactionService) {
        this.repository = repository;
        this.transactionService = transactionService;
    }

    public Account addAccount(Account account) {
        checkAccountExistenceByName(account.getName());
        validatePinLength(account.getPin());
        return repository.save(account);
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public Account withdraw(Withdrawal withdrawal) {
        Account account = findAccountByName(withdrawal.sourceName());
        validateAccountPin(account, withdrawal.pin());
        return transactionService.withdraw(account, withdrawal.amount());
    }

    public Account deposit(Deposit deposit) {
        Account account = findAccountByName(deposit.targetName());
        return transactionService.deposit(account, deposit.amount());
    }

    public Account transfer(Transfer transfer) {
        Account sourceAccount = findAccountByName(transfer.sourceName());
        Account targetAccount = findAccountByName(transfer.targetName());
        validateAccountPin(sourceAccount, transfer.pin());
        return transactionService.transfer(sourceAccount, targetAccount, transfer.amount());
    }

    private Account findAccountByName(String name) {
        Optional<Account> account = repository.findAccountByName(name);
        return account.orElseThrow(AccountNotFoundException::new);
    }

    private void validateAccountPin(Account accFoundByName, Integer supposedPin) {
        if (!accFoundByName.getPin().equals(supposedPin)) throw new PinIsNotCorrectException();
    }

    private void validatePinLength(Integer pin) {
        if (String.valueOf(pin).length() != 4) throw new BadPinException();
    }

    private void checkAccountExistenceByName(String name) {
        if (repository.existsAccountByName(name)) throw new AccountAlreadyExistsException();
    }
}
