package org.java.inventoryservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java.inventoryservice.model.dto.BulkSeatCreateRequest;
import org.java.inventoryservice.model.dto.SeatDetailDto;
import org.java.inventoryservice.model.dto.SeatResponseDto;
import org.java.inventoryservice.model.entity.Seat;
import org.java.inventoryservice.model.enumration.SeatStatus;
import org.java.inventoryservice.repository.SeatRepository;
import org.java.inventoryservice.service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public void createSeatsInBulk(BulkSeatCreateRequest request) {
        log.info("Creating {} seats for Event ID: {}", request.seats().size(), request.eventId());

        List<Seat> seatsToSave = request.seats().stream()
                .map(dto -> Seat.builder()
                        .eventId(request.eventId())
                        .priceCategoryGuid(dto.priceCategoryGuid())
                        .seatName(dto.seatIdentifier())
                        .rowLabel(dto.rowLabel())
                        .seatNumber(dto.seatNumber())
                        .status(SeatStatus.AVAILABLE)
                        .guid(UUID.randomUUID().toString())
                        .build())
                .collect(Collectors.toList());

        seatRepository.saveAll(seatsToSave);
        log.info("Successfully saved bulk seats to inventory.");
    }

    @Override
    @Transactional
    public void syncSeatsInBulk(BulkSeatCreateRequest request) {
        log.info("Synchronizing seats for Event ID: {}", request.eventId());
        List<Seat> existingSeats = seatRepository.findByEventId(request.eventId());
        Map<String, Seat> existingSeatMap = existingSeats.stream()
                .collect(Collectors.toMap(Seat::getSeatName, seat -> seat));

        List<Seat> seatsToSave = new ArrayList<>();
        for (BulkSeatCreateRequest.SeatDto dto : request.seats()) {
            if (existingSeatMap.containsKey(dto.seatIdentifier())) {
                Seat seatToUpdate = existingSeatMap.get(dto.seatIdentifier());
                seatToUpdate.setPriceCategoryGuid(dto.priceCategoryGuid());
                seatToUpdate.setRowLabel(dto.rowLabel());
                seatToUpdate.setSeatNumber(dto.seatNumber());

                seatsToSave.add(seatToUpdate);
                existingSeatMap.remove(dto.seatIdentifier());
            } else {
                seatsToSave.add(Seat.builder()
                        .eventId(request.eventId())
                        .priceCategoryGuid(dto.priceCategoryGuid())
                        .seatName(dto.seatIdentifier())
                        .rowLabel(dto.rowLabel())
                        .seatNumber(dto.seatNumber())
                        .status(SeatStatus.AVAILABLE)
                        .guid(UUID.randomUUID().toString())
                        .build());
            }
        }
        if (!existingSeatMap.isEmpty()) {
            log.info("Removing {} seats that were unassigned from the event map.", existingSeatMap.size());
            seatRepository.deleteAll(existingSeatMap.values());
        }
        seatRepository.saveAll(seatsToSave);
        log.info("Seat synchronization complete.");
    }

    @Override
    public List<SeatResponseDto> getSeatsByEventId(Long eventId) {
        return seatRepository.findByEventId(eventId).stream()
                .map(seat -> new SeatResponseDto(
                        seat.getSeatName(),
                        seat.getGuid(),
                        seat.getPriceCategoryGuid(),
                        seat.getRowLabel(),
                        seat.getSeatNumber(),
                        seat.getStatus().name()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void updateSeatStatus(List<String> seatGuids, SeatStatus newStatus) {
        try{
            log.info("Updating seat status for {} seats.", seatGuids.size());
            List<Seat> seats = seatRepository.findByGuidIn(seatGuids);

            if (seats.size() != seatGuids.size()) {
                throw new RuntimeException("One or more seats were not found in inventory.");
            }

            seats.forEach(seat -> {
                if (seat.getStatus() == SeatStatus.SOLD) {
                    throw new RuntimeException("Seat " + seat.getSeatName() + " is already sold!");
                }
                seat.setStatus(newStatus);
            });

            seatRepository.saveAll(seats);
            log.info("Updated {} seats to status: {}", seats.size(), newStatus);
        } catch (Exception e){
            log.error("Failed to update seat status", e);
            throw new RuntimeException("Failed to update seat status");
        }
    }

    @Override
    public List<SeatDetailDto> getSeatsByGuids(List<String> guids) {
        List<Seat> seats = seatRepository.findByGuidIn(guids);

        return seats.stream().map(seat -> new SeatDetailDto(
                seat.getGuid(),
                seat.getSeatName(),
                seat.getRowLabel(),
                seat.getSeatNumber()
        )).toList();
    }}