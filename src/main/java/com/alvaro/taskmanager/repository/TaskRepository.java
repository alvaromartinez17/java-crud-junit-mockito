package com.alvaro.taskmanager.repository;

import com.alvaro.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad Task.
 * Proporciona métodos heredados de JpaRepository para operaciones CRUD
 * sin necesidad de implementar lógica SQL manualmente.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository proporciona métodos como:
    // findAll(), findById(), save(), update(), delete(), deleteById(), etc.
}
