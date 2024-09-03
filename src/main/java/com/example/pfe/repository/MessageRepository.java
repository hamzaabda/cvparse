package com.example.pfe.repository;


import com.example.pfe.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
