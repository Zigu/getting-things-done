package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Project;
import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.ProjectRepository;
import de.pincservices.gtd.repository.TaskRepository;

import java.util.Collections;
import java.util.Optional;

public class ProjectBasedTaskFinder extends TaskFinder {
    private final ProjectRepository projectRepository;

    public ProjectBasedTaskFinder(TaskRepository taskRepository, ProjectRepository projectRepository) {
        super(taskRepository);
        this.projectRepository = projectRepository;
    }

    @Override
    Iterable<Task> findTasks(String projectName) {
        Optional<Project> foundProject = projectRepository.findByName(projectName);
        if (foundProject.isEmpty()) {
            return Collections.emptyList();
        }
        return taskRepository.findAllByProject(foundProject.get().getId(), defaultSort);
    }
}
