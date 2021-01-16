package de.pincservices.gtd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Date;

@Persistent
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resolution {

    public enum State {
        SOLVED,
        DISCARDED,
        UNSOLVED
    }

    @Id
    private String id;
    @Version
    private Long version;
    private Date date;
    private State state;
    private String comment;
}
