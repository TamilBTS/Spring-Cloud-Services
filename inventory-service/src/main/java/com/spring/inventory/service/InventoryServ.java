package com.spring.inventory.service;

import com.spring.inventory.repo.InventoryRepo;
import com.spring.inventory.vo.InventoryResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryServ {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Transactional
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepo.findBySkuCodeIn(skuCode)
                .stream()
                .map(inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0).build())
                .collect(Collectors.toList());
    }
}
