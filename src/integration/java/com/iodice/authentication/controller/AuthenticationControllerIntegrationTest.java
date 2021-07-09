package com.iodice.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iodice.authentication.configuration.SecurityConfiguration;
import com.iodice.authentication.model.dto.AccountDto;
import com.iodice.authentication.model.dto.AuthenticationRequestDto;
import com.iodice.authentication.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthenticationController.class)
@ContextConfiguration(classes = SecurityConfiguration.class)
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void register_thenReturn201AndCorrectResponse() throws Exception {
        // Prepare
        AccountDto mockServiceResponseDto = new AccountDto("dummyUsername", "dummyToken");
        String mockServiceResponseString =  objectMapper.writeValueAsString(mockServiceResponseDto);
        when(authenticationService.register(any())).thenReturn(mockServiceResponseDto);

        // Perform
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto("dummyUsername", "dummyPassword");
        String requestString = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/authentication/register").contentType(MediaType.APPLICATION_JSON).content(requestString))
                .andDo(print())
                .andExpect(status().isCreated()) // Assert
                .andExpect(content().string(containsString(mockServiceResponseString)));
    }

    @Test
    void login() {
    }
}