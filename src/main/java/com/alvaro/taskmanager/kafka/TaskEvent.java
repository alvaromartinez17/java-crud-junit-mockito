package com.alvaro.taskmanager.kafka;

import java.time.LocalDateTime;

/**
 * Evento JSON que se publica en Kafka cuando cambia una tarea.
 *
 * <p>El productor lo serializa a JSON (con Jackson) y el consumidor
 * lo deserializa de vuelta a esta clase. Así los servicios se
 * comunican con estructuras de datos tipadas en lugar de texto
 * plano, y cualquier otro sistema puede leer el topic.</p>
 */
public class TaskEvent {

    // Tipo de evento, por ejemplo "TAREA_CREADA".
    private String tipo;

    // Identificador de la tarea afectada.
    private Long tareaId;

    // Título de la tarea.
    private String titulo;

    // Descripción de la tarea.
    private String descripcion;

    // Indica si la tarea está completada.
    private boolean completada;

    // Momento en que se produjo el evento.
    private LocalDateTime fecha;

    /**
     * Constructor vacío requerido por Jackson para
     * deserializar el JSON en un objeto de esta clase.
     */
    public TaskEvent() {
    }

    /**
     * Constructor con todos los campos del evento.
     *
     * @param tipo        tipo de evento (ej. "TAREA_CREADA")
     * @param tareaId     identificador de la tarea afectada
     * @param titulo      título de la tarea
     * @param descripcion descripción de la tarea
     * @param completada  estado de completado de la tarea
     * @param fecha       momento en que se produjo el evento
     */
    public TaskEvent(String tipo, Long tareaId, String titulo,
                     String descripcion, boolean completada,
                     LocalDateTime fecha) {
        this.tipo = tipo;
        this.tareaId = tareaId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = completada;
        this.fecha = fecha;
    }

    // =====================
    // Getters y Setters
    // =====================

    /**
     * Obtiene el tipo de evento.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de evento.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el identificador de la tarea afectada.
     */
    public Long getTareaId() {
        return tareaId;
    }

    /**
     * Establece el identificador de la tarea afectada.
     */
    public void setTareaId(Long tareaId) {
        this.tareaId = tareaId;
    }

    /**
     * Obtiene el título de la tarea.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título de la tarea.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la descripción de la tarea.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la tarea.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Indica si la tarea está completada.
     */
    public boolean isCompletada() {
        return completada;
    }

    /**
     * Establece el estado de completado de la tarea.
     */
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    /**
     * Obtiene el momento en que se produjo el evento.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Establece el momento en que se produjo el evento.
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
