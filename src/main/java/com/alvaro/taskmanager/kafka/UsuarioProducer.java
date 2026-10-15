package com.alvaro.taskmanager.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Publica eventos de tareas en el topic de Kafka.
 */
@Service
public class UsuarioProducer {

    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

    // Topic destino, definido en application.properties.
    @Value("${app.kafka.topic.usuarios}")
    private String topic;

    public UsuarioProducer(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Envía un evento de tarea al topic. El evento viaja
     * serializado en formato JSON.
     *
     * @param evento evento con los datos de la tarea afectada
     */
    public void enviarEvento(TaskEvent evento) {
        kafkaTemplate.send(topic, evento);
    }
}
