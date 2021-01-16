package de.pincservices.gtd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.Date;

@Persistent
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Due {

    public enum Type {
        BEFORE(-1),
        AT(0),
        AFTER(1);

        private final int ordering;

        Type(int ordering) {
            this.ordering = ordering;
        }

        public int getOrdering() {
            return ordering;
        }

        public static Type forOrdering(int ordering) {
            for (Type value : Type.values()) {
                if (value.ordering == ordering) {
                    return value;
                }
            }
            return null;
        }
    }

    @Id
    private String id;
    @Version
    private Long version;
    private Date date;
    private Due.Type type;
}
