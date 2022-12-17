package net.bookdevcontainer.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import net.bookdevcontainer.todolist.service.ScheduleService;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {

    @Autowired
    @Qualifier(value = "retryService")
    private ScheduleService retryService;

    @GetMapping(value = "/schedule")
    public ResponseEntity<String> scheduleForRetry() {
        return retryService.schedule();
    }

}
