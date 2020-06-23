package com.flairstech.workshop.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class DatabaseErrorController implements ErrorController {

    public static final ResponseEntity<Map<String, Object>>
            INTERNAL_ERROR_JSON = new ResponseEntity<>(
                    new LinkedHashMap<String, Object>() {{
                        put("error message", "INTERNAL_ERROR");
                    }}, HttpStatus.INTERNAL_SERVER_ERROR);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError() {
        return INTERNAL_ERROR_JSON;
    }
}

