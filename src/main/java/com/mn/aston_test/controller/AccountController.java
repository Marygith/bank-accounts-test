package com.mn.aston_test.controller;

import com.mn.aston_test.exceptions.*;
import com.mn.aston_test.mapper.AccountDtoMapper;
import com.mn.aston_test.model.dto.*;
import com.mn.aston_test.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.mn.aston_test.mapper.AccountDtoMapper.*;

import java.util.List;

@RestController
@RequestMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AccountOutputDto> getAllAccounts() {
        return
                convertToAccountDto(accountService.getAllAccounts());
    }

    @PostMapping(path = "/addAccount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public AccountOutputDto createAccount(@RequestBody AccountInputDto accountInputDto) {
        return accountToDto(
                accountService.addAccount(inputToAccount(accountInputDto)));
    }

    @PostMapping("/deposit")
    @ResponseBody
    public AccountOutputDto deposit(@RequestBody DepositDto depositDto) {
        return accountToDto(
                accountService.deposit(AccountDtoMapper.convertDtoToDeposit(depositDto)));
    }

    @PostMapping("/withdraw")
    @ResponseBody
    public AccountOutputDto withdraw(@RequestBody WithdrawalDto withdrawalDto) {
        return accountToDto(
                accountService.withdraw(AccountDtoMapper.convertDtoToWithdrawal(withdrawalDto)));
    }

    @PostMapping("/transfer")
    @ResponseBody
    public AccountOutputDto transfer(@RequestBody TransferDto transferDto) {
        return accountToDto(
                accountService.transfer(AccountDtoMapper.convertDtoToTransfer(transferDto)));
    }


    @ExceptionHandler({
            AccountNotFoundException.class,
            InsufficientFundsException.class,
            PinIsNotCorrectException.class,
            BadPinException.class,
            AccountAlreadyExistsException.class})
    public ResponseEntity<String> handleAccountException(
            ResponseStatusException exception
    ) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<String> handleAccountException(
            HttpMessageConversionException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BadPinException().getMessage());

    }

}
