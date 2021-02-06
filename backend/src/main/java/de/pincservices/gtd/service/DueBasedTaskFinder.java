package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Due;
import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;

import java.util.Collections;

public class DueBasedTaskFinder extends TaskFinder {
    public DueBasedTaskFinder(TaskRepository taskRepository) {
        super(taskRepository);
    }

    @Override
    Iterable<Task> findTasks(String searchExpression) {
        if (searchExpression == null) {
            return Collections.emptyList();
        }
        String[] parts = searchExpression.split(":", 2);

        Integer type = null;
        String date = null;

        if (parts.length == 2) {

            Due.Type dueType = Due.Type.valueOf(parts[0]);

            type = dueType.getOrdering();
            date = parts[1];
        } else {
            date = parts[0];
        }

        return taskRepository.findAllByDue(type, date, defaultSort);
    }
}
