package de.pincservices.gtd.service;

import de.pincservices.gtd.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DueBasedTaskFinderTest {

    @Test
    void findTasks_with_concrete_expression() {
        TaskRepository taskRepository = mock(TaskRepository.class);
        DueBasedTaskFinder finder = new DueBasedTaskFinder(taskRepository);

        finder.findTasks("BEFORE:2020-10-01");

        verify(taskRepository).findAllByDue(eq(-1), eq("2020-10-01"), any(Sort.class));
    }

    @Test
    void findTasks_with_date_only() {
        TaskRepository taskRepository = mock(TaskRepository.class);
        DueBasedTaskFinder finder = new DueBasedTaskFinder(taskRepository);

        finder.findTasks("2020-10-01");

        verify(taskRepository).findAllByDue(eq(null), eq("2020-10-01"), any(Sort.class));
    }

}
