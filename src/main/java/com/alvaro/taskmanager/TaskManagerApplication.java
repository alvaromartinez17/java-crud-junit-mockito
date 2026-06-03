package com.alvaro.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que inicia la aplicación Spring Boot de Gestión de Tareas.
 * 
 * Esta aplicación permite crear, leer, actualizar y eliminar tareas.
 * Utiliza Spring Data JPA para la persistencia de datos y Spring Boot
 * para la configuración automática de la aplicación.
 */
@SpringBootApplication
public class TaskManagerApplication {

    /**
     * Punto de entrada de la aplicación.
     * Inicia el contexto de Spring Boot y pone en marcha la aplicación.
     * 
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {

        SpringApplication.run(TaskManagerApplication.class, args);
    }

}
