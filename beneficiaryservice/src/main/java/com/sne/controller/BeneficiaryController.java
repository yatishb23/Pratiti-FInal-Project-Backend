package com.sne.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sne.dto.BeneficiaryDto;
import com.sne.dto.response.Response;
import com.sne.service.BeneficiaryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService service;

    @PostMapping("/add")
    public ResponseEntity<Response> add(@Valid @RequestBody BeneficiaryDto beneficiaryDto) {
        return ResponseEntity.ok(service.addBeneficiary(beneficiaryDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BeneficiaryDto>> getByCustomer(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getBeneficiariesByCustomer(userId));
    }

    @GetMapping("/{beneficiaryAccountNo}")
    public ResponseEntity<BeneficiaryDto> getById(@PathVariable Long beneficiaryAccountNo) {
        return ResponseEntity.ok(service.getBeneficiaryById(beneficiaryAccountNo));
    }

    @PutMapping("/update/{beneficiaryAccountNo}")
    public ResponseEntity<Response> update(@PathVariable Long beneficiaryAccountNo,
                                           @RequestBody BeneficiaryDto request) {
        return ResponseEntity.ok(service.updateBeneficiary(beneficiaryAccountNo, request));
    }

    @DeleteMapping("/delete/{beneficiaryAccountNo}")
    public ResponseEntity<Response> delete(@PathVariable Long beneficiaryAccountNo) {
        return ResponseEntity.ok(service.deleteBeneficiary(beneficiaryAccountNo));
    }
}