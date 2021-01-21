package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;

public class RegexBasedTaskFinder extends TaskFinder {
    public RegexBasedTaskFinder(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        return taskRepository.findAllBySummaryOrNotesMatches(searchExpression, defaultSort);
    }
}
