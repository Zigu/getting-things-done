package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import org.springframework.data.domain.Sort;

public class DefaultTaskFinder extends TaskFinder {

    public DefaultTaskFinder(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        return taskRepository.findAllSortedBy(Sort.by("n.due_date").ascending()
                .and(Sort.by("n.due_type").ascending()));
    }
}
