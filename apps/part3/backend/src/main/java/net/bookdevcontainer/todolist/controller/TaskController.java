package net.bookdevcontainer.todolist.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import net.bookdevcontainer.todolist.model.Task;
import net.bookdevcontainer.todolist.repository.TaskRepository;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;


@CrossOrigin
@RestController
@Tag(name = "todo", description = "TodoリストAPI")
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

	@Autowired
	private TaskRepository repository;

	@Operation(summary = "Todoリスト全件取得", description = "登録されているTodoリストを取得します")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class)))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@GetMapping("/todos")
	public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) String title) {

		try {
			List<Task> tasks = new ArrayList<>();
			if (title == null)
			  tasks.addAll(repository.findAll());
			else
			  tasks.addAll(repository.findByTitleContaining(title));
			if (tasks.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tasks, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Todoリスト取得", description = "指定したIDのTodoリストを1件取得します")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = Task.class))),
			@ApiResponse(responseCode = "404", description = "Todo not found") })
	@GetMapping("/todos/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
		Optional<Task> taskData = repository.findById(id);
		return taskData.map(task -> new ResponseEntity<>(task, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@Operation(summary = "Todoリスト登録", description = "新しいTodoリストを1件登録します")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Todo created", content = @Content(schema = @Schema(implementation = Task.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@PostMapping("/todos")
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		try {

			Task _task = repository
					.save(new Task(task.getTitle(), task.getTask(), task.getStatus(), task.getDuedate(), ZonedDateTime.now()));
			return new ResponseEntity<>(_task, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Todoリスト更新", description = "指定したIDのTodoリストを1件更新します")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Todo updated"),
			@ApiResponse(responseCode = "404", description = "Todo not found")
	})
	@PutMapping("/todos/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task task) {
		Optional<Task> taskData = repository.findById(id);
		if (taskData.isPresent()) {
			Task _task = taskData.get();
			_task.setTitle(task.getTitle());
			_task.setTask(task.getTask());
			_task.setStatus(task.getStatus());
			_task.setDuedate(task.getDuedate());
			_task.setUpdatedOn(ZonedDateTime.now());
			return new ResponseEntity<>(repository.save(_task), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Todoリスト削除", description = "指定したIDのTodoリストを1件削除します")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Todo deleted"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@DeleteMapping("/todos/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable("id") long id) {
		try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Todoリスト全削除", description = "Todoリストを全件削除します")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Todo all deleted"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	@DeleteMapping("/todos")
	public ResponseEntity<Void> deleteAllTasks() {
		try {
			repository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}