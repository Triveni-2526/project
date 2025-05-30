package com.correspondence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.correspondence.binding.CoResponse;
import com.correspondence.binding.CoTriggerBinding;
import com.correspondence.service.CoTriggerService;

@RestController
public class CoTriggerController {

    @Autowired
    private CoTriggerService triggerService;

    @PostMapping("/api/triggers")
    public ResponseEntity<?> saveTrigger(@RequestBody CoTriggerBinding triggerBinding) {
        CoTriggerBinding savedTrigger = triggerService.saveTrigger(triggerBinding);

        if (savedTrigger != null) {
            return ResponseEntity.ok(savedTrigger);
        } else {
            return ResponseEntity
                .badRequest()
                .body("Trigger with this case number already exists.");
        }
    }
    @GetMapping("/process")
    public CoResponse processPendingTriggers() {
        return triggerService.processPendingTriggers();
    }
}
