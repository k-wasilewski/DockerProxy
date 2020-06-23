package com.flairstech.workshop;

import com.flairstech.workshop.config.AppInitializer;
import com.flairstech.workshop.controllers.CountryController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorHandlingTest {
    @Autowired
    CountryController countryController;

    @Test
    public void getCountry_shouldReturnErrorMessage_whenNoCountryIsFound()
            throws InterruptedException {
        while (!AppInitializer.isReady) {}  //wait for context to set up
        Thread.sleep(5000);     //wait for database to set up

        //given
        String invalidCountryCode = "ABC";

        //when
        ResponseEntity<Map<String, Object>> response =
                countryController.getCountry(invalidCountryCode);

        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(CountryController.INVALID_COUNTRY_CODE_JSON, response);
    }
}
