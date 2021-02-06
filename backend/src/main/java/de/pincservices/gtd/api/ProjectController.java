package de.pincservices.gtd.api;

import de.pincservices.gtd.model.Project;
import de.pincservices.gtd.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@CrossOrigin
@RestController
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostConstruct
    public void fixProjects() {
        // For node migrations during development
        //projectRepository.deleteAll();
    }

    @PostMapping("/projects")
    public void create(@RequestBody Project project) {
        projectRepository.save(project);
    }

    @GetMapping("/projects")
    public Iterable<Project> getAll() {
        return projectRepository.findAll();
    }
}
