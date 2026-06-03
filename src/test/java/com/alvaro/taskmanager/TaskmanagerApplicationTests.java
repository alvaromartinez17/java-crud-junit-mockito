package com.alvaro.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Clase de pruebas unitarias para la aplicación TaskManager.
 * 
 * Utiliza JUnit 5 y Spring Boot Testing para verificar que
 * el contexto de la aplicación se carga correctamente.
 */
@SpringBootTest
class TaskmanagerApplicationTests {

    /**
     * Prueba que verifica si el contexto de Spring Boot se carga sin errores.
     * 
     * Si esta prueba pasa, significa que todas las anotaciones y configuraciones
     * de Spring están correctamente configuradas y no hay conflictos de dependencias.
     */
    @Test
    void contextLoads() {
        // El contexto se ha cargado correctamente si no se lanza excepción
    }

}
