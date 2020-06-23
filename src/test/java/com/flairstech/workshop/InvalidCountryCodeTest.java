package com.flairstech.workshop;

import com.flairstech.workshop.config.AppInitializer;
import com.flairstech.workshop.controllers.CountryController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvalidCountryCodeTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnINVALID_COUNTRY_CODEmsg_whenNoCountryIsFound()
            throws Exception {
        while (!AppInitializer.isReady) {}  //wait for context to set up
        Thread.sleep(5000);     //wait for database to set up

        //given
        String invalidUrl = "/ABC";
        int INTERNAL_SERVER_ERROR_STATUS = 500;
        String INVALID_COUNTRY_CODE_MSG = "INVALID_COUNTRY_CODE";

        //when, then
        mockMvc.perform(get(invalidUrl)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(INTERNAL_SERVER_ERROR_STATUS))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("['error message']")
                        .value(INVALID_COUNTRY_CODE_MSG));
    }
}
