package de.pincservices.gtd.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Version;

@Data
@Persistent
public class Topic {

    @Id
    private String id;
    @Version
    private Long version;
    private String name;

}
