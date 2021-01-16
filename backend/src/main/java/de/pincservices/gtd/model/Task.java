package de.pincservices.gtd.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Collection;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Persistent
@Data
public class Task {

    @Id
    private String id;
    @Version
    private Long version;
    private String summary;
    private String notes;
    private Set<String> tags;

    @Relationship(type = "HAS_PARENT", direction = OUTGOING)
    private Collection<Task> parents;

    @Relationship(type = "HAS_DUE", direction = OUTGOING)
    private Due due;

    @Relationship(type = "HAS_RESOLUTION", direction = OUTGOING)
    private Resolution resolution;
}
