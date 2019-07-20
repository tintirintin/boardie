package com.hsbc.boardie.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardieController {

    @GetMapping("/wall")
    public String getCurrentWall() {
        return "Siemanko!";
    }
}
