package de.pincservices.gtd.model.converters;

import de.pincservices.gtd.model.Due;
import de.pincservices.gtd.model.EmbeddedDue;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.DateValue;
import org.neo4j.driver.internal.value.IntegerValue;
import org.springframework.data.neo4j.core.convert.Neo4jConversionService;
import org.springframework.data.neo4j.core.convert.Neo4jPersistentPropertyToMapConverter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DueToMapConverter implements Neo4jPersistentPropertyToMapConverter<String, EmbeddedDue> {

    @Override
    public Map<String, Value> decompose(EmbeddedDue property, Neo4jConversionService neo4jConversionService) {
        if (property == null) {
            return null;
        }
        Map<String, Value> decomposed = new HashMap<>();
        decomposed.put("date", new DateValue(property.getDate()));
        decomposed.put("type", new IntegerValue(property.getType().getOrdering()));
        return decomposed;
    }

    @Override
    public EmbeddedDue compose(Map<String, Value> source, Neo4jConversionService neo4jConversionService) {

        if (source == null || source.isEmpty()) {
            return null;
        }
        LocalDate date = source.get("date").asLocalDate();

        Due.Type type;
        try {
            String typeValue = source.get("type").asString();
            type = Due.Type.valueOf(typeValue);
        } catch (Exception e) {
            type = Due.Type.forOrdering(source.get("type").asInt());

        }
        return new EmbeddedDue(date, type);
    }
}
