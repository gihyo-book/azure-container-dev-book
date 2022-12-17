package net.bookdevcontainer.todolist;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import net.bookdevcontainer.todolist.service.Task;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TodolistApplicationTests {

  private URI targetEndpoint;

  public TodolistApplicationTests(@LocalServerPort int port) {
    try {
      this.targetEndpoint = new URI("http://localhost:" + port + "/api/todo");
    } catch (URISyntaxException e) {
      e.printStackTrace();
      this.targetEndpoint = null;
    }
  }

  private static final TestRestTemplate restTemplate = new TestRestTemplate(HttpClientOption.ENABLE_COOKIES);

  @Test
  @Order(1)
  void postTask() {
    Map<String, String> map = Map.of("title", "foobar-title", "memo", "foobar-memo", "dueDate", "2022-12-31");

    ResponseEntity<String> result = restTemplate.postForEntity(targetEndpoint, map, String.class);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertTrue(result.getBody().contains("Succeeded"));
  }

  @Test
  @Order(2)
  void getTask() throws JsonMappingException, JsonProcessingException {
    ResponseEntity<String> result = restTemplate.getForEntity(targetEndpoint, String.class);
    assertEquals(HttpStatus.OK, result.getStatusCode());

    List<Task> tasks = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(result.getBody(),
        new TypeReference<List<Task>>() {
        });

    assertEquals(1, tasks.size());
    assertEquals("foobar-title", tasks.get(0).title());
    assertEquals("foobar-memo", tasks.get(0).memo());
    assertEquals("Created", tasks.get(0).status());
    assertEquals("2022-12-31", tasks.get(0).dueDate().toString());
  }

  @Test
  @Order(3)
  void updateTask() throws JsonMappingException, JsonProcessingException {
    // 前回作ったタスクを修正する
    Map<String, Object> map = Map.of("id", 1, "title", "foobar-title2", "memo", "foobar-memo2", "status", "Done",
        "dueDate", "2023-12-31");

    ResponseEntity<String> result = restTemplate.exchange(targetEndpoint, HttpMethod.PATCH, new HttpEntity<>(map),
        String.class);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertTrue(result.getBody().contains("Succeeded"));

    // 修正した内容でタスクが API から取得できるのか検証
    result = restTemplate.getForEntity(targetEndpoint, String.class);
    assertEquals(HttpStatus.OK, result.getStatusCode());

    List<Task> tasks = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(result.getBody(),
        new TypeReference<List<Task>>() {
        });

    assertEquals(1, tasks.size());
    assertEquals("foobar-title2", tasks.get(0).title());
    assertEquals("foobar-memo2", tasks.get(0).memo());
    assertEquals("Done", tasks.get(0).status());
    assertEquals("2023-12-31", tasks.get(0).dueDate().toString());
  }

  @Test
  @Order(4)
  void deleteTask() {
    // 前回作ったタスクを削除する
    Map<String, Object> map = Map.of("id", 1);
    ResponseEntity<String> result = restTemplate.exchange(targetEndpoint, HttpMethod.DELETE, new HttpEntity<>(map),
        String.class);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertTrue(result.getBody().contains("Succeeded"));

    // 削除によって、API からタスクが返却されないことを確認
    result = restTemplate.getForEntity(targetEndpoint, String.class);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("[]", result.getBody());
  }
}
