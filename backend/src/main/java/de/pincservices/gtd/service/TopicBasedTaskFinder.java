package de.pincservices.gtd.service;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.model.Topic;
import de.pincservices.gtd.repository.TaskRepository;
import de.pincservices.gtd.repository.TopicRepository;

import java.util.Collections;
import java.util.Optional;

public class TopicBasedTaskFinder extends TaskFinder {
    private final TopicRepository topicRepository;

    public TopicBasedTaskFinder(TaskRepository taskRepository, TopicRepository topicRepository) {
        super(taskRepository);
        this.topicRepository = topicRepository;
    }

    @Override
    Iterable<Task> findTasks(String topicName) {
        Optional<Topic> foundTopic = topicRepository.findByName(topicName);
        if (foundTopic.isEmpty()) {
            return Collections.emptyList();
        }
        return taskRepository.findAllByTopic(foundTopic.get().getId(), defaultSort);
    }
}
