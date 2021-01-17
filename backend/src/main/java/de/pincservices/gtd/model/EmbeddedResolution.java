package de.pincservices.gtd.model;

import lombok.Value;

import java.time.LocalDate;

@Value
public class EmbeddedResolution {

    private LocalDate date;
    private Resolution.State state;
    private String comment;
}
