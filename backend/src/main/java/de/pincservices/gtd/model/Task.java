package de.pincservices.gtd.model;

import de.pincservices.gtd.model.converters.DueToMapConverter;
import de.pincservices.gtd.model.converters.ResolutionToMapConverter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
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

    @Relationship(type = "BELONGS_TO", direction = OUTGOING)
    private Project project;

    @Relationship(type = "IS_NEXT_OF", direction = INCOMING)
    private Collection<Task> previousTasks;

    /**
     * Custom delimiter required because Spring does not allow multiple dots in order fragment of query.
     * See {@link de.pincservices.gtd.repository.TaskRepository#findAllSortedBy(Sort)}.
     */
    @CompositeProperty(converter = DueToMapConverter.class, delimiter = "_")
    private EmbeddedDue due;

    @CompositeProperty(converter = ResolutionToMapConverter.class,  delimiter = "_")
    private EmbeddedResolution resolution;

}
