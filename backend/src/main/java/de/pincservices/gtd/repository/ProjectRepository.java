package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.Project;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface ProjectRepository extends Neo4jRepository<Project, String> {
    Optional<Project> findByName(String projectName);
}
