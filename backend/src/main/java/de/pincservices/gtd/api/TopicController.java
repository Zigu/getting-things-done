package de.pincservices.gtd.api;

import de.pincservices.gtd.model.Topic;
import de.pincservices.gtd.repository.TopicRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class TopicController {

    private final TopicRepository topicRepository;

    public TopicController(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @PostConstruct
    public void migrate() {
        // For node migrations during development
    }

    @PostMapping("/topics")
    public void create(@RequestBody Topic topic) {
        topicRepository.save(topic);
    }

    @GetMapping("/topics")
    public Iterable<Topic> getAll() {
        return topicRepository.findAll();
    }
}
