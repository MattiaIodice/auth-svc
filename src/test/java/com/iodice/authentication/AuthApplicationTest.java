package com.iodice.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthApplicationTest {

    @Test
    public void loadContext() { }

    @Test
    public void testMain() {
        AuthApplication.main(new String[]{});
    }
}