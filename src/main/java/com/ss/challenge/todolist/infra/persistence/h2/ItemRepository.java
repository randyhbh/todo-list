package com.ss.challenge.todolist.infra.persistence.h2;

import com.ss.challenge.todolist.domain.items.Item;
import com.ss.challenge.todolist.domain.items.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Stream;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Stream<Item> streamByStatusIn(@NonNull Collection<ItemStatus> statuses);

    @Modifying(clearAutomatically = true)
    @Query("update Item i set i.status = com.ss.challenge.todolist.domain.items.ItemStatus.PAST_DUE where i.doneAt is null and i.status = com.ss.challenge.todolist.domain.items.ItemStatus.NOT_DONE and i.dueAt <= now()")
    int setPastDueOnExpiredItems();
}