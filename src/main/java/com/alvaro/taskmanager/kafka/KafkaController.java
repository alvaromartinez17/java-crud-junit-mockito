package com.alvaro.taskmanager.kafka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final UsuarioProducer producer;

    public KafkaController(UsuarioProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @PostMapping
    public String enviar() {

        producer.enviarMensaje("Hola Kafka");

        return "Mensaje enviado a Kafka";
    }
}
