package de.pincservices.gtd.api;

import de.pincservices.gtd.model.Task;
import de.pincservices.gtd.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskController taskController;

    @Test
    void delete_in_chain() {
        Task task1 = new Task();
        task1.setId("junitTask1");

        Task task2 = new Task();
        task2.setId("junitTask2");
        task2.setPreviousTasks(new ArrayList<>());
        task2.getPreviousTasks().add(task1);

        Task task3 = new Task();
        task3.setId("junitTask3");
        task3.setPreviousTasks(new ArrayList<>());
        task3.getPreviousTasks().add(task2);

        when(taskRepository.findById("junitTask2")).thenReturn(Optional.of(task2));
        when(taskRepository.findAllByPreviousTasksContains("junitTask2")).thenReturn(Collections.singleton(task3));
        when(taskRepository.findAllById(Collections.singleton("junitTask1"))).thenReturn(Collections.singletonList(task1));

        taskController.deleteTask("junitTask2");

        verify(taskRepository).findById("junitTask2");

        ArgumentCaptor<Task> argumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getId()).isEqualTo("junitTask3");
        assertThat(argumentCaptor.getValue().getPreviousTasks()).containsExactly(task1);

        verify(taskRepository).deleteById("junitTask2");
    }
}
