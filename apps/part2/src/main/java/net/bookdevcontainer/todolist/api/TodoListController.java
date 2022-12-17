package net.bookdevcontainer.todolist.api;

import java.util.List;
import java.util.Map;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.bookdevcontainer.todolist.service.Task;
import net.bookdevcontainer.todolist.service.TodoListService;
import net.bookdevcontainer.todolist.service.TodoStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
@RequestMapping(path = "/api/todo", produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoListController {

  private static final Logger logger = LoggerFactory.getLogger(TodoListController.class);

  // ToDoリストの操作はサービスクラスで実装
  @Autowired
  private TodoListService service;

  // ビジネスロジックのリトライ設定
  private final Retry RetryTodoListService = Retry.of("TodoListService", RetryConfig.custom()
      .maxAttempts(5) // リトライ回数は５回
      .intervalFunction(IntervalFunction.ofExponentialBackoff(Duration.ofSeconds(2))) // ExponentialBackoff を実施
      .retryOnException(e -> e instanceof TransientDataAccessException) // 一過性のデータベースの例外はリトライ
      .build());

  // (第6章で使用)認証したユーザーのユーザーIDの取得
  private String GetUserId(HttpServletRequest req){
    String userId = req.getHeader("X-MS-CLIENT-PRINCIPAL-ID");
    if(userId == null || userId.isEmpty()){
      userId = req.getSession().getId(); // ユーザーIDが空の場合でもサンプルの動作させるため、セッションIDを代入 (ExceptionにしてもOK)
    }
    return userId;
  }

  public TodoListController() {
    // リトライが発生した原因をログに出す
    RetryTodoListService.getEventPublisher().onRetry(event -> {
      logger.warn(event.toString(), event.getLastThrowable());
    });
  }

  @GetMapping
  public List<Task> get(HttpServletRequest req, HttpServletResponse res) {
    try {
      return RetryTodoListService.executeCallable(() -> {
        //return service.queryTasksByUser(req.getSession().getId());
        return service.queryTasksByUser(GetUserId(req)); //第6章で使用 
      });
    } catch (Throwable ex) {
      logger.error("Failed to query Task", ex);
    }

    res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    return List.of();
  }

  @RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
  public Map<String, String> put(@Valid @RequestBody FormTaskCreation form, HttpServletRequest req,
      HttpServletResponse res) {
    try {
      RetryTodoListService.executeCallable(() -> {
        // return service.createTask(req.getSession().getId(), HtmlUtils.htmlEscape(form.title()),
        return service.createTask(GetUserId(req), HtmlUtils.htmlEscape(form.title()), //第6章で使用 
             HtmlUtils.htmlEscape(form.memo()), LocalDate.parse(form.dueDate()));
      });
    } catch (DateTimeParseException ex) {
      logger.warn(ex.getMessage());

      res.setStatus(HttpStatus.BAD_REQUEST.value());
      return Map.of("status", "Failed", "message", HttpStatus.BAD_REQUEST.toString());
    } catch (Throwable ex) {
      logger.error("Failed to create Task", ex);

      res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      return Map.of("status", "Failed", "message", HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    return Map.of("status", "Succeeded");
  }

  @PatchMapping
  public Map<String, String> patch(@Valid @RequestBody FormTaskUpdate form, HttpServletRequest req,
      HttpServletResponse res) {
    int numRow = 0;
    try {
      numRow = RetryTodoListService.executeCallable(() -> {
        // return service.updateTaskById(req.getSession().getId(), form.id(), HtmlUtils.htmlEscape(form.title()),
        return service.updateTaskById(GetUserId(req), form.id(), HtmlUtils.htmlEscape(form.title()), //第6章で使用
            HtmlUtils.htmlEscape(form.memo()), TodoStatus.valueOf(form.status().toUpperCase()),
            LocalDate.parse(form.dueDate()));
      });
    } catch (DateTimeParseException | IllegalArgumentException ex) {
      logger.warn(ex.getMessage());

      res.setStatus(HttpStatus.BAD_REQUEST.value());
      return Map.of("status", "Failed", "message", HttpStatus.BAD_REQUEST.toString());
    } catch (Throwable ex) {
      logger.error("Failed to update Task", ex);

      res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      return Map.of("status", "Failed", "message", HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    if (numRow == 0) {
      res.setStatus(HttpStatus.NOT_FOUND.value());
      return Map.of("status", "Failed", "message", HttpStatus.NOT_FOUND.toString());
    }

    return Map.of("status", "Succeeded");
  }

  @DeleteMapping
  public Map<String, String> delete(@Valid @RequestBody FormTaskDelete form, HttpServletRequest req,
      HttpServletResponse res) {
    int numRow = 0;
    try {
      numRow = RetryTodoListService.executeCallable(() -> {
        // return service.deleteTaskById(req.getSession().getId(), form.id());
        return service.deleteTaskById(GetUserId(req), form.id()); //第6章で使用
      });
    } catch (Throwable ex) {
      logger.error("Failed to delete Task", ex);

      res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      return Map.of("status", "Failed", "message", HttpStatus.INTERNAL_SERVER_ERROR.toString());
    }

    if (numRow == 0) {
      res.setStatus(HttpStatus.NOT_FOUND.value());
      return Map.of("status", "Failed", "message", HttpStatus.NOT_FOUND.toString());
    }

    return Map.of("status", "Succeeded");
  }
}