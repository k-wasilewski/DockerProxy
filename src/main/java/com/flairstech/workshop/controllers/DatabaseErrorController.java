package com.flairstech.workshop.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@RestController
public class DatabaseErrorController implements ErrorController {

    private static final ResponseEntity<Object> INTERNAL_ERROR_JSON =
            new ResponseEntity<>(
                    new LinkedHashMap<String, Object>() {{
                        put("error message", "INTERNAL_ERROR");
                    }},
                    HttpStatus.INTERNAL_SERVER_ERROR);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError() {
        return INTERNAL_ERROR_JSON;
    }
}

