package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.Due;
import de.pincservices.gtd.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface TaskRepository extends Neo4jRepository<Task, String> {
    /**
     * Spring data neo4j only allows ordering of direct properties, neither on relation properties nor on composite properties.
     * Therefore, I needed to add a custom findAll method. Neo4J supports ordering on related and embedded properties natively.
     */
    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " RETURN n{.*, __allProperties__: n{.*}, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`IS_NEXT_OF`|`BELONGS_TO`]-()-[:`IS_NEXT_OF`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllSortedBy(Sort sort);

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE $tag IN n.tags"
            + " RETURN n{.*, __allProperties__: n{.*}, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`IS_NEXT_OF`|`BELONGS_TO`]-()-[:`IS_NEXT_OF`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findByTagsContains(@Param("tag") String tag, Sort sort);

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE n.summary =~ $searchExpression OR n.notes =~ $searchExpression"
            + " RETURN n{.*, __allProperties__: n{.*}, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`IS_NEXT_OF`|`BELONGS_TO`]-()-[:`IS_NEXT_OF`|`BELONGS_TO`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllBySummaryOrNotesMatches(@Param("searchExpression") String searchExpression, Sort sort);

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE n.summary CONTAINS $searchExpression OR n.notes CONTAINS $searchExpression"
            + " RETURN n{.*, __allProperties__: n{.*}, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`IS_NEXT_OF`|`BELONGS_TO`]-()-[:`IS_NEXT_OF`|`BELONGS_TO`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllBySummaryOrNotesContains(@Param("searchExpression") String searchExpression, Sort defaultSort);

    /**
     * Attention: Produces a warning when the type parameter null is, although the query can handle it.
     * @param type numerical representation of the due type. {@link Due.Type#getOrdering()}
     * @param date format 'YYYY-MM-DD'
     * @param sort sorting of the result set
     * @return tasks
     */
    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE n.due_date = date($date) AND ($type IS null OR $type = n.due_type)"
            + " RETURN n{.*, __allProperties__: n{.*}, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`IS_NEXT_OF`|`BELONGS_TO`]-()-[:`IS_NEXT_OF`|`BELONGS_TO`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllByDue(@Nullable @Param("type") Integer type, @Param("date") String date, Sort sort);

    // MATCH ()-[r:IS_NEXT_OF]->(n:Task) RETURN n
    @Query("MATCH (p:Task)-[:IS_NEXT_OF]->(n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__"
            + " WHERE p.id = $id"
            + " RETURN n{.*, __allProperties__: n{.*}, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`IS_NEXT_OF`|`BELONGS_TO`]-()-[:`IS_NEXT_OF`|`BELONGS_TO`*0..]-() | p]}"
    )
    Iterable<Task> findAllByPreviousTasksContains(@Param("id") String id);

    @Query("MATCH (n:Task)"
            + " WITH n, id(n) AS __internalNeo4jId__, n.due as due"
            + " WHERE n.due_date <= date($date)"
            + " RETURN n{.*, __allProperties__: n{.*}, __nodeLabels__: labels(n), __internalNeo4jId__: id(n), __paths__: [p = (n)-[:`IS_NEXT_OF`|`BELONGS_TO`]-()-[:`IS_NEXT_OF`|`BELONGS_TO`*0..]-() | p]}"
            + " :#{orderBy(#sort)}"
    )
    Iterable<Task> findAllByDueBefore(@Param("date") String date, Sort sort);
}
