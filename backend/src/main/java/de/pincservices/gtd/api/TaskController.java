package de.pincservices.gtd.api;

import de.pincservices.gtd.model.EmbeddedResolution;
import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import de.pincservices.gtd.service.TaskSearchService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskSearchService taskSearchService;

    public TaskController(TaskRepository taskRepository, TaskSearchService taskSearchService) {
        this.taskRepository = taskRepository;
        this.taskSearchService = taskSearchService;
    }

    @PostConstruct
    public void migrate() {
        // For node migrations during development
    }

    @PostMapping("/tasks")
    public void storeTask(@RequestBody Task task) {
        taskRepository.save(task);
    }

    @PostMapping("/tasks/{id}/resolution")
    public void updateTask(@PathVariable("id") String id, @RequestBody EmbeddedResolution resolution) {
        Task foundTask = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown Id " + id));
        foundTask.setResolution(resolution);
        taskRepository.save(foundTask);
    }

    @GetMapping("/tasks")
    public Iterable<Task> getTasks(@RequestParam(value = "searchCriterion", required = false) String searchCriterion,
                                   @RequestParam(value = "searchExpression", required = false) String searchExpression) {

        return StringUtils.hasText(searchCriterion) ?
                taskSearchService.findTasks(searchCriterion, searchExpression) :
                taskSearchService.findTasks("default", null);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable("id") String taskId) {
        Optional<Task> foundTask = taskRepository.findById(taskId);
        // connect previous tasks with next tasks to keep chain alive
        if (foundTask.isPresent()) {
            Task taskToDelete = foundTask.get();
            Collection<String> previousTaskIds = taskToDelete.getPreviousTasks().stream().map(Task::getId).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(previousTaskIds)) {
                Iterable<Task> nextTasks = taskRepository.findAllByPreviousTasksContains(taskToDelete.getId());
                if (nextTasks != null) {
                    nextTasks.forEach(nextTask -> {
                        // Required because neo4j updates the versions of related entities
                        List<Task> previousTasks = taskRepository.findAllById(previousTaskIds);
                        nextTask.getPreviousTasks().remove(taskToDelete);
                        nextTask.getPreviousTasks().addAll(previousTasks);
                        taskRepository.save(nextTask);
                    });
                }
            }
            taskRepository.deleteById(taskId);
        }

    }
}
