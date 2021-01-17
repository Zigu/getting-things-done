package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private static Neo4j embeddedDatabaseServer;

    @BeforeAll
    static void initializeNeo4j() {

        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }

    @AfterAll
    static void stopNeo4j() {

        embeddedDatabaseServer.close();
    }

    @Test
    void save() {

        Task task = new Task();
        task.setId("junitActionId");
        task.setSummary("junitAction");
        task.setTags(Collections.singleton("junitTag"));
        task.setDue(new EmbeddedDue(LocalDate.now(), Due.Type.AT));
        task.setResolution(new EmbeddedResolution(LocalDate.now(), Resolution.State.SOLVED, null));
        taskRepository.save(task);

        assertThat(taskRepository.count()).isEqualTo(1L);
        assertThat(taskRepository.findById("junitActionId")).isPresent()
                .get()
                .extracting(Task::getDue, Task::getResolution)
                .filteredOn(Objects::nonNull)
                .hasSize(2);
    }

    @Test
    void save_with_parent() {

        Task parentTask = new Task();
        parentTask.setId("junitParentActionId");
        parentTask.setSummary("junitParentAction");
        parentTask.setDue(new EmbeddedDue(LocalDate.now(), Due.Type.AT));
        taskRepository.save(parentTask);


        Task task = new Task();
        task.setPreviousTasks(Collections.singleton(parentTask));
        task.setId("junitActionId");
        task.setSummary("junitAction");
        task.setTags(Collections.singleton("junitTag"));
        task.setDue(new EmbeddedDue(LocalDate.now(), Due.Type.AT));
        task.setResolution(new EmbeddedResolution(LocalDate.now(), Resolution.State.SOLVED, null));
        taskRepository.save(task);

        assertThat(taskRepository.count()).isEqualTo(2L);
        assertThat(taskRepository.findById("junitActionId")).isPresent()
                .get()
                .extracting(Task::getDue, Task::getResolution)
                .filteredOn(Objects::nonNull)
                .hasSize(2);
    }

}
