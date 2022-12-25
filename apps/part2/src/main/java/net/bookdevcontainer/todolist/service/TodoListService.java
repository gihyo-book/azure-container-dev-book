package net.bookdevcontainer.todolist.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class TodoListService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final String SQL_QUERY_TASKS_BY_USER = "SELECT id, `status`, title, dueDate, memo, createdOn, updatedOn FROM task WHERE `user`=?";

  private static final String SQL_CREATE_TASK = "INSERT INTO task (`user`, `status`, title, dueDate, memo, createdOn, updatedOn) VALUES (?, ?, ?, ?, ?, ?, ?)";

  private static final String SQL_UPDATE_TASK = "UPDATE task SET title=?, dueDate=?, memo=?, `status`=?, updatedOn=? WHERE id=? and `user`=?";

  private static final String SQL_DELETE_TASK = "DELETE FROM task WHERE id=? and `user`=?";

  public List<Task> queryTasksByUser(String user) {
    sometimesRaiseException();

    return jdbcTemplate.query(SQL_QUERY_TASKS_BY_USER,
        new PreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement pst) throws SQLException {
            pst.setString(1, user);
          }
        },
        new RowMapper<Task>() {
          @Override
          public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Task(rs.getInt("id"), rs.getString("status"),
                rs.getString("title"),
                rs.getDate("dueDate").toLocalDate(),
                rs.getString("memo"),
                ZonedDateTime.of(
                    rs.getTimestamp("createdOn").toLocalDateTime(), ZoneId.systemDefault()),
                ZonedDateTime.of(
                    rs.getTimestamp("updatedOn").toLocalDateTime(), ZoneId.systemDefault()));
          }
        });
  }

  public int createTask(String user, String title, String memo, LocalDate dueDate) {
    sometimesRaiseException();

    var now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());

    return jdbcTemplate.update(SQL_CREATE_TASK, user,
        TodoStatus.CREATED.toString(), title, Date.valueOf(dueDate), memo, now, now);
  }

  public int updateTaskById(String user, int id, String title, String memo, TodoStatus status, LocalDate dueDate) {
    sometimesRaiseException();

    var now = Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime());

    return jdbcTemplate.update(SQL_UPDATE_TASK, title, dueDate, memo, status.toString(), now, id, user);
  }

  public int deleteTaskById(String user, int id) {
    sometimesRaiseException();

    return jdbcTemplate.update(SQL_DELETE_TASK, id, user);
  }

  // 5回に1回例外を発生させる
  private void sometimesRaiseException() {
    if (ThreadLocalRandom.current().nextInt() % 5 == 0) {
      throw new TransientDataAccessResourceException("transient error happened.");
    }
  }
}