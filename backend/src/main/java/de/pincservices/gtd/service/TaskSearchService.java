package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TaskSearchService {

    private final Map<String, TaskFinder> finders = new HashMap<>();

    public TaskSearchService(TaskRepository taskRepository) {
        finders.put("default", new DefaultTaskFinder(taskRepository));
        finders.put("tag", new TagBasedTaskFinder(taskRepository));
        finders.put("regex", new RegexBasedTaskFinder(taskRepository));
        finders.put("text", new TextBasedTaskFinder(taskRepository));
        finders.put("due", new DueBasedTaskFinder(taskRepository));
        finders.put("until_due", new UntilDueBasedTaskFinder(taskRepository));
    }

    public Iterable<Task> findTasks(String searchCriterion, String searchExpression) {

        TaskFinder finder = finders.getOrDefault(searchCriterion, new NoopTaskFinder());

        return finder.findTasks(searchExpression);
    }
}
