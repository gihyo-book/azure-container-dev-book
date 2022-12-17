package net.bookdevcontainer.todolist.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

record FormTaskUpdate(@NotNull Integer id, @NotEmpty @Size(max = 64) String title, @Size(max = 64) String memo,
        @NotEmpty String status, @NotEmpty String dueDate) {
}
