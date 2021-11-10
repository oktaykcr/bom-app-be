package com.oktaykcr.bomappbe.repository.inventory;

import com.oktaykcr.bomappbe.model.inventory.Inventory;
import com.oktaykcr.bomappbe.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends BaseRepository<Inventory> {
    Optional<Inventory> getByUserUsername(String username);
}
