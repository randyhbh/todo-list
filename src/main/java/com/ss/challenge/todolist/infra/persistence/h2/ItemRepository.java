package com.ss.challenge.todolist.infra.persistence.h2;

import com.ss.challenge.todolist.domain.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}