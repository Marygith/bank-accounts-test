package com.mn.aston_test.mapper;

import com.mn.aston_test.model.Account;
import com.mn.aston_test.model.Deposit;
import com.mn.aston_test.model.Transfer;
import com.mn.aston_test.model.Withdrawal;
import com.mn.aston_test.model.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountDtoMapper {

    public static List<AccountOutputDto> convertToAccountDto(List<Account> accounts) {
        return accounts.stream().map(AccountDtoMapper::accountToDto).collect(Collectors.toList());
    }

    public static AccountOutputDto accountToDto(Account account) {
        return new AccountOutputDto(account.getName(), account.getBalance());
    }

    public static Account inputToAccount(AccountInputDto accountInputDto) {
        return new Account(accountInputDto.name(), accountInputDto.pin());
    }

    public static Deposit convertDtoToDeposit(DepositDto dto) {
        return new Deposit(dto.name(), dto.amount());

    }

    public static Withdrawal convertDtoToWithdrawal(WithdrawalDto dto) {
        return new Withdrawal(dto.amount(), dto.source(), dto.pin());

    }

    public static Transfer convertDtoToTransfer(TransferDto dto) {
        return new Transfer(dto.sourceAccount(), dto.targetAccount(), dto.amount(), dto.sourcePin());
    }
}
