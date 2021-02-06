package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import org.springframework.data.domain.Sort;

public abstract class TaskFinder {

    protected final TaskRepository taskRepository;

    protected final Sort defaultSort;

    public TaskFinder(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.defaultSort = Sort.by("n.due_date").ascending()
                .and(Sort.by("n.due_type").ascending());
    }

    abstract Iterable<Task> findTasks(String searchExpression);

}
