package com.alvaro.taskmanager.kafka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Controlador de pruebas para el flujo de Kafka.
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final UsuarioProducer producer;

    public KafkaController(UsuarioProducer producer) {
        this.producer = producer;
    }

    /**
     * Endpoint de prueba.
     */
    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    /**
     * Publica un evento JSON de ejemplo en el topic.
     */
    @PostMapping
    public String enviar() {

        // Evento de ejemplo para probar el flujo de Kafka.
        TaskEvent evento = new TaskEvent(
                "TAREA_CREADA",
                0L,
                "Hola Kafka",
                "Evento de prueba",
                false,
                LocalDateTime.now()
        );

        producer.enviarEvento(evento);

        return "Evento enviado a Kafka";
    }
}
