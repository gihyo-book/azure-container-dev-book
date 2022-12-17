package net.bookdevcontainer.todolist.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

record FormTaskCreation(@NotEmpty @Size(max = 64) String title, @Size(max = 64) String memo, @NotEmpty String dueDate) {
}
