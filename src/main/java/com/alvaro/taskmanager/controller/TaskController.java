package com.alvaro.taskmanager.controller;

import com.alvaro.taskmanager.model.Task;
import com.alvaro.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de tareas.
 *
 * Proporciona endpoints para realizar operaciones CRUD sobre las tareas.
 * Todas las rutas están prefijadas con "/tasks".
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    /**
     * Endpoint de prueba.
     */
    @GetMapping("/test")
    public String testTask() {
        return "TASK OK";
    }

    /**
     * Obtiene todas las tareas.
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAllTasks();
    }

    /**
     * Obtiene una tarea por su ID.
     */
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    /**
     * Crea una nueva tarea.
     */
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return service.createTask(task);
    }

    /**
     * Actualiza una tarea existente.
     */
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return service.updateTask(id, task);
    }

    /**
     * Elimina una tarea.
     */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }
}