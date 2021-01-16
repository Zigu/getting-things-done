package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface TaskRepository extends Neo4jRepository<Task, String> {
    /**
     * Spring data neo4j only allows ordering of direct properties, neither on relation properties nor on composite properties.
     * Therefore, I needed to add a custom findAll method. Neo4J supports ordering on related and embedded properties natively.
     */
    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " RETURN n{.*, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`]->()-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllSortedBy(Sort sort);
}
