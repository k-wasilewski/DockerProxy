package com.flairstech.workshop.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DatabaseErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {
        return new ResponseEntity<>("INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

