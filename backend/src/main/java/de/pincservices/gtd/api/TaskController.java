package de.pincservices.gtd.api;

import de.pincservices.gtd.model.EmbeddedResolution;
import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import de.pincservices.gtd.service.TaskSearchService;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

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
    public void fixTasks() {
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
}
