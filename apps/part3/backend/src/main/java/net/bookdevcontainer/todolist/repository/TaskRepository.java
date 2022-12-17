package net.bookdevcontainer.todolist.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import net.bookdevcontainer.todolist.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByTitleContaining(String title);
}