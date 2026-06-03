package com.alvaro.taskmanager.service;

import com.alvaro.taskmanager.model.Task;
import com.alvaro.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de gestión de tareas.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para administrar tareas en la aplicación.
 */
@Service
public class TaskService {

    // Inyección de dependencia del repositorio para acceder a la base de datos
    @Autowired
    private TaskRepository repository;

    /**
     * Obtiene todas las tareas de la base de datos.
     *
     * @return Lista de todas las tareas registradas
     */
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    /**
     * Busca una tarea específica por su identificador.
     *
     * @param id Identificador único de la tarea
     * @return Tarea encontrada o null si no existe
     */
    public Task getTaskById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Crea y guarda una nueva tarea en la base de datos.
     *
     * @param task Objeto tarea con los datos a guardar
     * @return Tarea guardada con su ID asignado
     */
    public Task createTask(Task task) {
        return repository.save(task);
    }

    /**
     * Actualiza una tarea existente con nuevos datos.
     *
     * @param id Identificador de la tarea a actualizar
     * @param task Objeto con los nuevos datos
     * @return Tarea actualizada o null si no existe
     */
    public Task updateTask(Long id, Task task) {
        // Busca la tarea existente
        Task existingTask = repository.findById(id).orElse(null);
        if (existingTask != null) {
            // Actualiza los campos de la tarea
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setCompleted(task.isCompleted());
            // Guarda los cambios en la base de datos
            return repository.save(existingTask);
        }
        return null;
    }

    /**
     * Elimina una tarea de la base de datos.
     *
     * @param id Identificador de la tarea a eliminar
     */
    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
