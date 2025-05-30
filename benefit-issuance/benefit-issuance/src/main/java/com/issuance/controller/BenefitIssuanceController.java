package com.issuance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.issuance.service.BenefitIssuanceService;

@RestController
public class BenefitIssuanceController {

    @Autowired
    private BenefitIssuanceService benefitIssuanceService;

    /**
     * Endpoint to trigger benefit issuance and email sending
     */
    @GetMapping("/issue")
    public String issueBenefits() {
        try {
            benefitIssuanceService.sendMail();
            return "Benefits issued and email sent successfully.";
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging in production
            return "Failed to issue benefits: " + e.getMessage();
        }
    }
}
