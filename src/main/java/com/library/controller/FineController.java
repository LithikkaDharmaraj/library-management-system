package com.library.controller;

import com.library.entity.Fine;
import com.library.service.FineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public List<Fine> getAllFines() {
        return fineService.getAllFines();
    }

    @GetMapping("/{issuedId}")
    public Fine getFineByIssuedId(@PathVariable Long issuedId) {
        return fineService.getFineByIssuedId(issuedId);
    }
}
