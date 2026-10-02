package com.sanjay;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlienResource {

    private final AlienRepository repo;

    public AlienResource(AlienRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/aliens")
    public List<Alien> getAlien() {
        return (List<Alien>) repo.findAll();
    }
}