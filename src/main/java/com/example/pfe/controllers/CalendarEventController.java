package com.example.pfe.controllers;


import com.example.pfe.models.CalendarEvent;
import com.example.pfe.services.CalendarEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/events")
public class CalendarEventController {

    @Autowired
    private CalendarEventService service;

    @GetMapping
    public List<CalendarEvent> getAllEvents() {
        return service.getAllEvents();
    }

    @PostMapping
    public CalendarEvent createEvent(@RequestBody CalendarEvent event) {
        return service.createEvent(event);
    }
}
