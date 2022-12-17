package net.bookdevcontainer.todolist.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public record Task (int id, String status, String title, LocalDate dueDate,
    String memo, ZonedDateTime createdOn, ZonedDateTime updatedOn) {

}
