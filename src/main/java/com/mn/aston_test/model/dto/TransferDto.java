package com.mn.aston_test.model.dto;

public record TransferDto(String sourceAccount, String targetAccount, Integer sourcePin, Double amount) {
}
