package com.safiap.techchallengeoficinamecanica;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TechChallengeOficinaMecanicaApplicationTests {

    @Test
    @DisplayName("teste carrega o contexto da aplicação com sucesso")
    void contextLoads() {
    }

}
