package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;

public class TagBasedTaskFinder extends TaskFinder {
    public TagBasedTaskFinder(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        return taskRepository.findByTagsContains(searchExpression, defaultSort);
    }
}
