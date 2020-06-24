package com.flairstech.workshop;

import com.flairstech.workshop.config.AppConfig;
import com.flairstech.workshop.config.AppInitializer;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
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
    public static void setTest() throws InterruptedException{
        /**
         * now getting org.postgresql.util.PSQLException
         */
        AppInitializer.test = true;
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
        while (!AppInitializer.isReady) {}  //wait for context to set up
        Thread.sleep(5000);     //wait for database to set up

        //given
        String validUrl = "/POL";
        int INTERNAL_SERVER_ERROR_STATUS = 500;
        String INTERNAL_ERROR_MSG = "INTERNAL_ERROR";

        //when, then
        mockMvc.perform(get(validUrl)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(INTERNAL_SERVER_ERROR_STATUS))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['error message']")
                        .value(INTERNAL_ERROR_MSG));
    }
}
