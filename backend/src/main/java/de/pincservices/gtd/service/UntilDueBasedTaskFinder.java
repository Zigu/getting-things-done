package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;

import java.util.Collections;

public class UntilDueBasedTaskFinder extends TaskFinder {
    public UntilDueBasedTaskFinder(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        if (searchExpression == null) {
            return Collections.emptyList();
        }
        return taskRepository.findAllByDueBefore(searchExpression, defaultSort);
    }
}
