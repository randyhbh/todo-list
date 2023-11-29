package com.ss.challenge.todolist.infra.persistence.h2;

import com.ss.challenge.todolist.domain.expirations.Expiration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpirationRepository extends JpaRepository<Expiration, UUID> {
}