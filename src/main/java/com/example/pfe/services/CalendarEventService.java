package com.example.pfe.services;



import com.example.pfe.models.CalendarEvent;
import com.example.pfe.repository.CalendarEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarEventService {

    @Autowired
    private CalendarEventRepository repository;

    public List<CalendarEvent> getAllEvents() {
        return repository.findAll();
    }

    public CalendarEvent createEvent(CalendarEvent event) {
        return repository.save(event);
    }
}
