package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;

public class TextBasedTaskFinder extends TaskFinder {
    public TextBasedTaskFinder(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        return taskRepository.findAllBySummaryOrNotesContains(searchExpression, defaultSort);
    }
}
