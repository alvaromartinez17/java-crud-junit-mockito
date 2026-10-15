package com.alvaro.taskmanager.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

// Serializadores JSON de spring-kafka (usan Jackson por debajo).
// Desde Spring Kafka 4.0, las clases JsonSerializer/JsonDeserializer
// están deprecadas; los equivalentes sin deprecar son JacksonJson*.
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import com.alvaro.taskmanager.kafka.TaskEvent;

/**
 * Configuración de Kafka: topic, productor y consumidor.
 *
 * <p>Toda la conexión al broker y los nombres de topic/grupo
 * se leen de {@code application.properties}, así no hay valores
 * escritos a mano en el código. Los valores de los mensajes
 * viajan como JSON de {@link TaskEvent}.</p>
 */
@Configuration
public class KafkaConfig {

    // Servidor de arranque del broker.
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Nombre del topic de usuarios.
    @Value("${app.kafka.topic.usuarios}")
    private String usuariosTopic;

    // Grupo de consumidores.
    @Value("${app.kafka.consumer-group}")
    private String consumerGroup;

    /**
     * Crea el topic de usuarios al arrancar la aplicación si
     * no existe todavía.
     */
    @Bean
    public NewTopic usuariosTopic() {
        // 1 partición y 1 réplica, suficiente para desarrollo local.
        return new NewTopic(usuariosTopic, 1, (short) 1);
    }

    /**
     * Configura cómo se conecta el productor a Kafka y cómo
     * serializa las claves (texto) y los valores (eventos JSON).
     */
    @Bean
    public ProducerFactory<String, TaskEvent> producerFactory() {

        Map<String, Object> config = new HashMap<>();

        // Servidores de arranque del clúster.
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Las claves se envían como texto.
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Los valores (TaskEvent) se envían como JSON.
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * Plantilla para enviar eventos JSON al topic desde el productor.
     */
    @Bean
    public KafkaTemplate<String, TaskEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Configura cómo se conecta el consumidor a Kafka y cómo
     * deserializa las claves (texto) y los valores (eventos JSON).
     */
    @Bean
    public ConsumerFactory<String, TaskEvent> consumerFactory() {

        Map<String, Object> config = new HashMap<>();

        // Servidores de arranque del clúster.
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Grupo de consumidores: varias instancias con el mismo
        // grupo se reparten las particiones del topic.
        config.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);

        // Si el grupo es nuevo (no tiene offset guardado), se empieza
        // a leer desde el principio del topic.
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Las claves se reciben como texto.
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Los valores se reciben como JSON y se convierten a TaskEvent.
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);

        // Permite deserializar eventos de los paquetes de la aplicación.
        // El comodín .* incluye subpaquetes (TaskEvent vive en
        // com.alvaro.taskmanager.kafka). Sin esto, el deserializador
        // rechaza por seguridad cualquier clase fuera de java.*.
        config.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "com.alvaro.taskmanager.*");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    /**
     * Fábrica de contenedores que permite escuchar mensajes
     * mediante la anotación {@code @KafkaListener}.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskEvent> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, TaskEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        // Asocia la configuración del consumidor definida arriba.
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
