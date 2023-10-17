package com.mn.aston_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.aston_test.exceptions.AccountNotFoundException;
import com.mn.aston_test.exceptions.BadPinException;
import com.mn.aston_test.exceptions.InsufficientFundsException;
import com.mn.aston_test.exceptions.PinIsNotCorrectException;
import com.mn.aston_test.model.dto.AccountInputDto;
import com.mn.aston_test.model.dto.DepositDto;
import com.mn.aston_test.model.dto.TransferDto;
import com.mn.aston_test.model.dto.WithdrawalDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
@Sql(scripts = {"classpath:/clean-account-table.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"classpath:/create-accounts.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AstonTestApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() {
    }

    @Test
    public void getAllAccounts() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/accounts/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("[0].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("[1].name").value("Jane"));
    }

    @Test
    public void addAccountWithBadPinTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/accounts/addAccount")
                        .content("{\"name\":Eve,\"pin\":\"-\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new BadPinException().getMessage()));
    }

    @Test
    public void addAccountWithShortPinTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/accounts/addAccount")
                        .content(asJsonString(new AccountInputDto(123, "Eve")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new BadPinException().getMessage()));
    }

    @Test
    public void transferFromNonexistentAccount() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/accounts/transfer")
                        .content(asJsonString(new TransferDto("Eve", "John", 1234, 100.0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new AccountNotFoundException().getMessage()));
    }

    @Test
    public void transferWithIncorrectPassword() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/accounts/transfer")
                        .content(asJsonString(new TransferDto("John", "Jane", 1111, 100.0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new PinIsNotCorrectException().getMessage()));
    }

    @Test
    public void withdrawFromAccountWithInsufficientFunds() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                        .post("/accounts/withdraw")
                        .content(asJsonString(new WithdrawalDto("John", 1234, 1000.0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(new InsufficientFundsException().getMessage()));
    }

    @Test
    public void deposit() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/accounts/deposit")
                        .content(asJsonString(new DepositDto("John", 20.0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(120.0));
    }

    @Test
    public void transfer() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/accounts/transfer")
                        .content(asJsonString(new TransferDto("John", "Jane", 1234, 20.0)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                        .get("/accounts/getAll")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("[0].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("[1].name").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("[0].balance").value(80.0))
                .andExpect(MockMvcResultMatchers.jsonPath("[1].balance").value(220.0));

    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
