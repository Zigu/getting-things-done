package de.pincservices.gtd.model.converters;

import de.pincservices.gtd.model.EmbeddedResolution;
import de.pincservices.gtd.model.Resolution;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.DateValue;
import org.neo4j.driver.internal.value.StringValue;
import org.springframework.data.neo4j.core.convert.Neo4jConversionService;
import org.springframework.data.neo4j.core.convert.Neo4jPersistentPropertyToMapConverter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ResolutionToMapConverter implements Neo4jPersistentPropertyToMapConverter<String, EmbeddedResolution> {

    @Override
    public Map<String, Value> decompose(EmbeddedResolution property, Neo4jConversionService neo4jConversionService) {
        if (property == null) {
            return null;
        }
        Map<String, Value> decomposed = new HashMap<>();
        decomposed.put("date", new DateValue(property.getDate()));
        decomposed.put("state", new StringValue(property.getState().name()));
        if (property.getComment() != null) {
            decomposed.put("comment", new StringValue(property.getComment()));
        }
        return decomposed;
    }

    @Override
    public EmbeddedResolution compose(Map<String, Value> source, Neo4jConversionService neo4jConversionService) {

        if (source == null || source.isEmpty()) {
            return null;
        }
        LocalDate date = source.get("date").asLocalDate();
        Resolution.State state = Resolution.State.valueOf(source.get("state").asString());

        String comment = source.get("comment") != null ? source.get("comment").asString() : null;
        return new EmbeddedResolution(date, state, comment);
    }
}
