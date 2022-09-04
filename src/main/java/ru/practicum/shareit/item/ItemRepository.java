package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwnerIdOrderById(int ownerId);

    List<Item> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailable(
            String name, String description, boolean available);

}
