package com.alvaro.taskmanager.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consume eventos de tareas del topic de Kafka.
 *
 * <p>La anotación {@code @KafkaListener} registra este servicio
 * como oyente del topic: cada vez que llega un mensaje, Spring
 * Kafka lo deserializa de JSON a {@link TaskEvent} e invoca al
 * método {@link #escuchar(TaskEvent)}. El topic y el grupo se
 * leen de {@code application.properties} para que coincidan
 * con los del productor.</p>
 */
@Service
public class UsuarioConsumer {

    private static final Logger log = LoggerFactory.getLogger(UsuarioConsumer.class);

    /**
     * Recibe cada evento publicado en el topic.
     *
     * @param evento evento de tarea deserializado del JSON
     */
    @KafkaListener(
            topics = "${app.kafka.topic.usuarios}",
            groupId = "${app.kafka.consumer-group}"
    )
    public void escuchar(TaskEvent evento) {
        log.info(
                "Evento recibido: tipo={}, tareaId={}, titulo={}, completada={}",
                evento.getTipo(), evento.getTareaId(),
                evento.getTitulo(), evento.isCompletada()
        );
    }
}
