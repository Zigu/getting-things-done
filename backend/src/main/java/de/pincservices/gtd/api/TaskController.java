package de.pincservices.gtd.api;

import de.pincservices.gtd.model.EmbeddedResolution;
import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@CrossOrigin
@RestController
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
    public Iterable<Task> getTasks() {
        return taskRepository.findAllSortedBy(Sort.by("n.due_date").ascending()
                                                .and(Sort.by("n.due_type").ascending()));
    }
}
