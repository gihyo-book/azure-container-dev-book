package net.bookdevcontainer.todolist.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tasks")
@NoArgsConstructor
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "task")
	private String task;

	@Column(name = "status")
	private String status;
	
	@Column(name = "duedate")
	private String duedate;

	@Column(name = "updatedOn")
	private ZonedDateTime updatedOn;

	public Task(String title, String task, String status,  String duedate, ZonedDateTime updatedOn) {
		this.title = title;
		this.task = task;
		this.status = status;
		this.duedate = duedate;
		this.updatedOn = updatedOn;
	}

}
