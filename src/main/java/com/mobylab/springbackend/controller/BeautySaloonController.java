package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.BeautySaloonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BeautySaloonController implements SecuredRestController {
    private final BeautySaloonService beautySaloonService;

    public BeautySaloonController(BeautySaloonService beautySaloonService) {
        this.beautySaloonService = beautySaloonService;
    }

    public ResponseEntity<List<>>
}
