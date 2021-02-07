package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;

import java.util.Collections;

public class SolvableDueBasedTaskFinder extends TaskFinder {
    public SolvableDueBasedTaskFinder(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        if (searchExpression == null) {
            return Collections.emptyList();
        }
        return taskRepository.findAllByDueSolvableAt(searchExpression, defaultSort);
    }
}
