package org.java.eventservice.controller;

import lombok.RequiredArgsConstructor;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.java.eventservice.model.dto.city.CityDto;
import org.java.eventservice.service.city.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CityDto>>> getAllCities() {
        List<CityDto> cities = cityService.getAllCities();
        return ResponseEntity.ok(ApiResponse.success(cities));
    }
}
