package org.java.inventoryservice.service;

import org.java.inventoryservice.model.dto.BulkSeatCreateRequest;
import org.java.inventoryservice.model.dto.SeatDetailDto;
import org.java.inventoryservice.model.dto.SeatResponseDto;
import org.java.inventoryservice.model.enumration.SeatStatus;

import java.util.List;

public interface SeatService {
    void createSeatsInBulk(BulkSeatCreateRequest request);
    void syncSeatsInBulk(BulkSeatCreateRequest request);
    List<SeatResponseDto> getSeatsByEventId(Long eventId);
    void updateSeatStatus(List<String> seatGuids, SeatStatus newStatus);
    List<SeatDetailDto> getSeatsByGuids(List<String> guids);
}