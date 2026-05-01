package org.java.eventservice.model.dto.venue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.java.eventservice.model.dto.event.EventFrontDetailDto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueBlueprint {
    public Layout layout;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Layout {
        public EventFrontDetailDto.StagePosDto stagePos;
        public Integer canvasWidth;
        public Integer canvasHeight;
        public List<PhysicalSeat> seats;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhysicalSeat {
        public String id;
        public Integer x;
        public Integer y;
    }
}
