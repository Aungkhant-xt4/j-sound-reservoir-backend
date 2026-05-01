package org.java.inventoryservice.controller;

import org.java.commonlibrary.model.dto.ApiResponse;
import org.java.inventoryservice.model.dto.BulkSeatCreateRequest;
import org.java.inventoryservice.model.dto.SeatDetailDto;
import org.java.inventoryservice.model.dto.SeatResponseDto;
import org.java.inventoryservice.model.dto.SeatStatusUpdateRequest;
import org.java.inventoryservice.model.enumration.SeatStatus;
import org.java.inventoryservice.service.SeatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final SeatService seatService;

    @PostMapping("/seats/bulk")
    public ResponseEntity<ApiResponse<Void>> createSeatsInBulk(@RequestBody BulkSeatCreateRequest request) {
        seatService.createSeatsInBulk(request);
        return ResponseEntity.ok(ApiResponse.success("Bulk seats created successfully."));
    }

    @PutMapping("/seats/bulk/sync")
    public ResponseEntity<ApiResponse<Void>> updateSeatsInBulk(@RequestBody BulkSeatCreateRequest request) {
        seatService.syncSeatsInBulk(request);
        return ResponseEntity.ok(ApiResponse.success("Bulk seats synchronized successfully."));
    }

    @GetMapping("/seats/event/{eventId}")
    public ResponseEntity<ApiResponse<List<SeatResponseDto>>> getSeatsByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(ApiResponse.success(seatService.getSeatsByEventId(eventId)));
    }

    @PutMapping("/seats/status")
    public ResponseEntity<ApiResponse<Void>> updateSeatStatus(@RequestBody SeatStatusUpdateRequest request) {
        seatService.updateSeatStatus(request.seatGuids(), SeatStatus.valueOf(request.status()));
        return ResponseEntity.ok(ApiResponse.success("Seat statuses updated successfully."));
    }

    @PostMapping("/seats/batch-details")
    public ResponseEntity<ApiResponse<List<SeatDetailDto>>> getSeatsByGuids(@RequestBody List<String> guids) {
        return ResponseEntity.ok(ApiResponse.success(seatService.getSeatsByGuids(guids)));
    }
}