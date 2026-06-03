package com.alvaro.taskmanager.model;

import jakarta.persistence.*;

/**
 * Entidad que representa una tarea en el sistema.
 * 
 * Esta clase es mapeada como una tabla en la base de datos mediante JPA.
 * Cada instancia de Task representa un registro de tarea con su información
 * (título, descripción y estado de completado).
 */
@Entity
@Table(name = "tasks")
public class Task {

    // Identificador único y clave primaria, autoincrementado por la BD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Título descriptivo de la tarea
    private String title;

    // Descripción detallada de qué se debe realizar
    private String description;

    // Indica si la tarea ha sido completada
    private boolean completed;

    /**
     * Constructor vacío requerido por JPA para la instanciación
     * automática de objetos desde la base de datos.
     */
    public Task() {
    }

    /**
     * Constructor con parámetros para inicializar una tarea con datos específicos.
     * 
     * @param title       Título de la tarea
     * @param description Descripción detallada de la tarea
     * @param completed   Estado de completado de la tarea
     */
    public Task(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // =====================
    // Getters y Setters
    // =====================

    /**
     * Obtiene el identificador único de la tarea.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la tarea.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la tarea.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título de la tarea.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción de la tarea.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la tarea.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el estado de completado de la tarea.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Establece el estado de completado de la tarea.
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
