package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE $tag IN n.tags"
            + " RETURN n{.*, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`]->()-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findByTagsContains(@Param("tag") String tag, Sort sort);

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE n.summary =~ $searchExpression OR n.notes =~ $searchExpression"
            + " RETURN n{.*, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`]->()-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllBySummaryOrNotesMatches(@Param("searchExpression") String searchExpression, Sort sort);

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE n.summary CONTAINS $searchExpression OR n.notes CONTAINS $searchExpression"
            + " RETURN n{.*, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`]->()-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllBySummaryOrNotesContains(@Param("searchExpression") String searchExpression, Sort defaultSort);

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE n.due_date = date($date) AND ($type = null OR $type = n.due_type)"
            + " RETURN n{.*, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`]->()-[:`HAS_DUE`|`HAS_RESOLUTION`|`HAS_PARENT`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllByDue(@Param("type") Integer type, @Param("date") String date, Sort defaultSort);
}
