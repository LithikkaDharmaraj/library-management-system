package com.library.controller;

import com.library.dto.IssueRequest;
import com.library.entity.Issued;
import com.library.service.IssuedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issued")
public class IssuedController {

    private final IssuedService issuedService;

    public IssuedController(IssuedService issuedService) {
        this.issuedService = issuedService;
    }

    @GetMapping
    public List<Issued> getAllIssues() {
        return issuedService.getAllIssues();
    }

    @GetMapping("/{id}")
    public Issued getIssueById(@PathVariable Long id) {
        return issuedService.getIssueById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Issued> getIssuesByUser(@PathVariable Long userId) {
        return issuedService.getIssuesByUser(userId);
    }

    @PostMapping("/issue")
    public ResponseEntity<Issued> issueBook(@RequestBody IssueRequest request) {
        Issued issued = issuedService.issueBook(request.getBookId(), request.getUserId(), request.getAllowedDays());
        return ResponseEntity.status(HttpStatus.CREATED).body(issued);
    }

    @PutMapping("/return/{issuedId}")
    public Issued returnBook(@PathVariable Long issuedId) {
        return issuedService.returnBook(issuedId);
    }
}
