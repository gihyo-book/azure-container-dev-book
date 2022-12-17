package net.bookdevcontainer.todolist.service;

public enum TodoStatus {
  CREATED {
    @Override
    public String toString() {
      return "Created";
    }
  },
  STARTED {
    @Override
    public String toString() {
      return "Started";
    }
  },
  DONE {
    @Override
    public String toString() {
      return "Done";
    }
  }
}
