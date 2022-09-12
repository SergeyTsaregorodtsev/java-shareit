package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Page<Item> findByOwnerIdOrderById(int ownerId, Pageable page);

    Page<Item> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailable(
            String name, String description, boolean available, Pageable page);

    List<Item> findAllByRequestIdOrderById(int requestId);
}