package org.java.eventservice.service.event;

import org.java.eventservice.model.dto.event.*;

import java.util.List;
import java.util.Map;

public interface EventService {
    void createEvent(CreateEventRequest request);
    void updateEvent(Long eventId, CreateEventRequest request);
    List<EventListDto> getAllEvents();
    EventDetailDto getEventById(Long eventId);
    EventFrontDetailDto getEventForFront(Long eventId);
    BasicEventDto getBasicEventById(Long eventId);
    Map<String, List<HomeEventSummaryDto>> getHomePageEvents();
}
