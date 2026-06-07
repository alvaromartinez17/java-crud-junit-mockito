package com.alvaro.taskmanager;


import com.alvaro.taskmanager.model.Task;
import com.alvaro.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private com.alvaro.taskmanager.service.TaskService service;

    @Test
    void shouldReturnTask_whenIdExists() {

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Tarea prueba");
        task.setDescription("Descripcion");
        task.setCompleted(false);

        when(repository.findById(1L)).thenReturn(Optional.of(task));

        Task result = service.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Tarea prueba", result.getTitle());

        verify(repository, times(1)).findById(1L);
    }
}