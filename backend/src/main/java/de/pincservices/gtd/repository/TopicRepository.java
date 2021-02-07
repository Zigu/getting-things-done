package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.Topic;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface TopicRepository extends Neo4jRepository<Topic, String> {
    Optional<Topic> findByName(String topicName);
}
