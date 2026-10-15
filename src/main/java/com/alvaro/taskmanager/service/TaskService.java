package com.alvaro.taskmanager.service;

import com.alvaro.taskmanager.kafka.TaskEvent;
import com.alvaro.taskmanager.kafka.UsuarioProducer;
import com.alvaro.taskmanager.model.Task;
import com.alvaro.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de gestión de tareas.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para administrar tareas en la aplicación.
 */
@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    // Inyección de dependencia del repositorio para acceder a la base de datos
    @Autowired
    private TaskRepository repository;

    // Inyección del productor de Kafka para publicar eventos
    @Autowired
    private UsuarioProducer producer;

    /**
     * Obtiene todas las tareas de la base de datos.
     *
     * @return Lista de todas las registradas
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
     * Crea y guarda una nueva tarea en la base de datos y
     * publica un evento JSON en Kafka informando de la creación.
     *
     * @param task Objeto tarea con los datos a guardar
     * @return Tarea guardada con su ID asignado
     */
    public Task createTask(Task task) {

        // Guarda la tarea y obtiene el ID asignado por la base de datos.
        Task savedTask = repository.save(task);

        // Construye el evento JSON con los datos de la tarea creada.
        TaskEvent evento = new TaskEvent(
                "TAREA_CREADA",
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.isCompleted(),
                LocalDateTime.now()
        );

        log.info("EVENTO KAFKA -> tipo={}, tareaId={}",
                evento.getTipo(), evento.getTareaId());

        // Publica el evento en el topic (viaja como JSON).
        producer.enviarEvento(evento);

        return savedTask;
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
