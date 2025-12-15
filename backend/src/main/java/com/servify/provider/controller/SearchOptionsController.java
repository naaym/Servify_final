package com.servify.provider.controller;

import com.servify.provider.service.SearchOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/providers/search/options")
public class SearchOptionsController {

    private final SearchOptionsService searchOptionsService;

    @GetMapping("/services")
    public ResponseEntity<List<String>> getServices() {
        return ResponseEntity.ok(searchOptionsService.getAvailableServiceCategories());
    }

    @GetMapping("/governorates")
    public ResponseEntity<List<String>> getGovernorates(@RequestParam(required = false) String serviceCategory) {
        return ResponseEntity.ok(searchOptionsService.getAvailableGovernorates(serviceCategory));
    }
}
