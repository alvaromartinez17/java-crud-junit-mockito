package com.alvaro.taskmanager.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UsuarioProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UsuarioProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarMensaje(String mensaje) {

        kafkaTemplate.send(
                "usuarios-topic",
                mensaje
        );
    }
}