package com.flairstech.workshop;

import com.flairstech.workshop.controllers.CountryController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ErrorHandlingTest {
    @Autowired
    CountryController countryController;

    @Test
    public void getCountry_shouldReturnErrorMessage_whenNoCountryIsFound() {
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
