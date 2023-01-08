// package net.bookdevcontainer.todolist.api.repository;

// import java.time.LocalDate;
// import java.time.ZonedDateTime;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;


// @Entity // This tells Hibernate to make a table out of this class
// public class Task {
//   @Id
//   @GeneratedValue(strategy=GenerationType.AUTO)
//   private Integer id;

//   private String user;

//   private String status;

//   private String title;

//   private LocalDate dueDate;

//   private String memo;

//   private ZonedDateTime createdOn;

//   private ZonedDateTime updatedOn;

//   public Integer getId() {
//     return id;
//   }

//   public void setId(Integer id) {
//     this.id = id;
//   }

//   public String getUser() {
//     return user;
//   }

//   public void setUser(String user) {
//     this.user = user;
//   }

//   public String getStatus() {
//     return status;
//   }

//   public void setStatus(String status) {
//     this.status = status;
//   }

//   public String getTitle() {
//     return title;
//   }

//   public void setTitle(String title) {
//     this.title = title;
//   }

//   public LocalDate getDueDate() {
//     return dueDate;
//   }

//   public void setDueDate(LocalDate dueDate) {
//     this.dueDate = dueDate;
//   }

//   public String getMemo() {
//     return memo;
//   }

//   public void setMemo(String memo) {
//     this.memo = memo;
//   }

//   public ZonedDateTime getCreatedOn() {
//     return createdOn;
//   }

//   public void setCreatedOn(ZonedDateTime createdOn) {
//     this.createdOn = createdOn;
//   }

//   public ZonedDateTime getUpdatedOn() {
//     return updatedOn;
//   }

//   public void setUpdatedOn(ZonedDateTime updatedOn) {
//     this.updatedOn = updatedOn;
//   }
// }