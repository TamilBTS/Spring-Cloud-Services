package com.spring.inventory.repo;

import com.spring.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, String> {

    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
