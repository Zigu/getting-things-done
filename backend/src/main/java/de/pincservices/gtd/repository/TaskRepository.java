package de.pincservices.gtd.repository;

import de.pincservices.gtd.model.Resolution;
import de.pincservices.gtd.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, String> {

    Iterable<Task> findAllByResolution_State(Resolution.State state);
}
