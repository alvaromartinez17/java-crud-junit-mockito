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

    // Inyección de dependencia del servicio de tareas
    @Autowired
    private TaskService service;

    /**
     * Obtiene todas las tareas.
     * GET /tasks
     *
     * @return Lista de tareas
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAllTasks();
    }

    /**
     * Obtiene una tarea por su ID.
     * GET /tasks/{id}
     *
     * @param id ID de la tarea
     * @return Tarea encontrada o null
     */
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    /**
     * Crea una nueva tarea.
     * POST /tasks
     *
     * @param task Datos de la tarea
     * @return Tarea creada
     */
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return service.createTask(task);
    }

    /**
     * Actualiza una tarea existente.
     * PUT /tasks/{id}
     *
     * @param id ID de la tarea
     * @param task Nuevos datos
     * @return Tarea actualizada o null
     */
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return service.updateTask(id, task);
    }

    /**
     * Elimina una tarea.
     * DELETE /tasks/{id}
     *
     * @param id ID de la tarea
     */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
