package com.flairstech.workshop;

import com.flairstech.workshop.config.AppConfig;
import com.flairstech.workshop.config.DockerProxy;
import com.flairstech.workshop.controllers.DatabaseErrorController;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import org.testcontainers.containers.PostgreSQLContainer;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {InternalErrorTest.Initializer.class})
@AutoConfigureMockMvc
public class InternalErrorTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeClass
    public static void setTest() {
        DockerProxy.setTesting(true);
        postgreSQLContainer.start();
    }

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer("postgres:11.1")
                    .withDatabaseName("world-db")
                    .withUsername("world")
                    .withPassword("world123");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            AppConfig.testUrl = postgreSQLContainer.getJdbcUrl();
        }
    }

    @Test
    public void shouldReturnINTERNAL_ERRORmsg_whenDatabaseIsDown() throws Exception {
        //given
        String validUrl = "/POL";
        int INTERNAL_SERVER_ERROR_STATUS = 500;

        //when, then
        mockMvc.perform(get(validUrl)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(INTERNAL_SERVER_ERROR_STATUS))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['error message']")
                        .value(DatabaseErrorController.ERROR_MESSAGE));
    }
}
