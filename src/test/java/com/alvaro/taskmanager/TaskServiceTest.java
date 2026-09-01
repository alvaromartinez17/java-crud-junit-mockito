package com.alvaro.taskmanager;

import com.alvaro.taskmanager.model.Task;
import com.alvaro.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para TaskService.
 *
 * Un test unitario prueba UNA sola pieza de lógica de forma aislada,
 * sin depender de base de datos, red, ni otros servicios externos.
 * Para lograrlo usamos dos herramientas:
 *
 *   - JUnit 5:  el framework que ejecuta los tests y verifica resultados
 *   - Mockito:  crea objetos "falsos" (mocks) que simulan dependencias reales
 */
@ExtendWith(MockitoExtension.class) // Le dice a JUnit 5 que active el soporte de Mockito en esta clase
public class TaskServiceTest {

    /**
     * @Mock crea un repositorio FALSO.
     * No conecta a ninguna base de datos real — simplemente simula su comportamiento.
     * Así el test no falla por problemas de BD y se ejecuta muy rápido.
     */
    @Mock
    private TaskRepository repository;

    /**
     * @InjectMocks crea una instancia REAL de TaskService,
     * pero inyectando automáticamente el repositorio falso (@Mock) que definimos arriba.
     * Es decir: probamos lógica real, con dependencias simuladas.
     */
    @InjectMocks
    private com.alvaro.taskmanager.service.TaskService service;

    /**
     * Nombre del test: shouldReturnTask_whenIdExists
     * Convención: "deberíaHacerX_cuandoCondiciónY"
     * Así al leer el nombre ya sabemos qué caso estamos probando.
     *
     * Este test verifica que getTaskById() devuelve la tarea correcta
     * cuando el ID existe en el repositorio.
     */
    @Test
    void shouldReturnTask_whenIdExists() {

        // ── GIVEN (preparación) ──────────────────────────────────────────
        // Creamos una tarea de ejemplo con datos conocidos.
        // Usamos datos fijos para que el test sea predecible y repetible.
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Tarea prueba");
        task.setDescription("Descripcion");
        task.setCompleted(false);

        // Le decimos al mock: "cuando alguien llame findById(1L),
        // devuelve esta tarea envuelta en Optional".
        // Optional es el tipo que devuelve JPA cuando busca por ID —
        // puede contener un valor o estar vacío si no existe.
        when(repository.findById(1L)).thenReturn(Optional.of(task));

        // ── WHEN (ejecución) ─────────────────────────────────────────────
        // Llamamos al método REAL del servicio que queremos probar.
        // Internamente llamará a repository.findById(), que devolverá
        // nuestra tarea falsa definida arriba.
        Task result = service.getTaskById(1L);

        // ── THEN (verificación) ──────────────────────────────────────────
        // assertNotNull: comprueba que el resultado NO es null,
        // es decir, que el servicio encontró y devolvió algo.
        assertNotNull(result);

        // assertEquals: comprueba que el título es exactamente el esperado.
        // Si no coincide, JUnit marca el test como FALLIDO.
        assertEquals("Tarea prueba", result.getTitle());

        // verify: comprueba que findById(1L) fue llamado exactamente 1 vez.
        // Esto garantiza que el servicio realmente usó el repositorio
        // y no devolvió un resultado hardcodeado o de algún otro lado.
        verify(repository, times(1)).findById(1L);
    }
}