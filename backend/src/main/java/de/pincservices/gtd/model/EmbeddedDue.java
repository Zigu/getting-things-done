package de.pincservices.gtd.model;

import lombok.Value;

import java.time.LocalDate;

@Value
public class EmbeddedDue {

    private LocalDate date;
    private Due.Type type;
}
