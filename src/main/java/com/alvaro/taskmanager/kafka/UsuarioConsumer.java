package com.alvaro.taskmanager.kafka;

import org.springframework.kafka.annotation.KafkaListener;

//@Service
public class UsuarioConsumer {

    @KafkaListener(
            topics = "usuarios-topic",
            groupId = "crud-group"
    )
    public void escuchar(String mensaje) {

        System.out.println(
                "Mensaje recibido: " + mensaje
        );
    }
}