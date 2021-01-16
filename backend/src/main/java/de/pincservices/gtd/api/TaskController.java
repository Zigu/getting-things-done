package de.pincservices.gtd.api;

import de.pincservices.gtd.model.Resolution;
import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@CrossOrigin
@RestController
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void fixTasks() {
        Collection<Task> toDelete = new ArrayList<>();
        taskRepository.findAll().forEach(task -> {
            if (task.getVersion() == null || task.getDue() == null) {
                toDelete.add(task);
            }
            if (task.getResolution() == null) {
                task.setResolution(new Resolution(UUID.randomUUID().toString(), 0l, new Date(), Resolution.State.UNSOLVED, null));
                taskRepository.save(task);
            }
        });
        taskRepository.deleteAll(toDelete);
    }

    @PostMapping("/tasks")
    public void storeTask(@RequestBody Task task) {
        taskRepository.save(task);
    }

    @PostMapping("/tasks/{id}/resolution")
    public void updateTask(@PathVariable("id") String id, @RequestBody Resolution resolution) {
        Task foundTask = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown Id " + id));
        foundTask.setResolution(resolution);
        taskRepository.save(foundTask);
    }

    @GetMapping("/tasks")
    public Iterable<Task> getTasks() {

        return taskRepository.findAll();
    }
}
