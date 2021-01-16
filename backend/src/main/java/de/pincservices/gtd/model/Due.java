package de.pincservices.gtd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import javax.annotation.Generated;
import java.util.Date;

@Persistent
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Due {

    public enum Type {
        BEFORE,
        AT
    }

    @Id
    private String id;
    @Version
    private Long version;
    private Date date;
    private Due.Type type;
}
