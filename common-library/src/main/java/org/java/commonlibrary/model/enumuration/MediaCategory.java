package org.java.commonlibrary.model.enumuration;

import lombok.Getter;

@Getter
public enum MediaCategory {
    EVENT("EVENT", "event"),
    VENUE("VENUE", "venue"),
    TALENT("TALENT", "talent");

    private final String value;
    private final String urlName;

    MediaCategory(String value, String urlName) {
        this.value = value;
        this.urlName = urlName;
    }
}
