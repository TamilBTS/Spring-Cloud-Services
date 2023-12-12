package com.spring.inventory.controller;

import com.spring.inventory.service.InventoryServ;
import com.spring.inventory.vo.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
public class InventoryController {


    @Autowired
    InventoryServ inventoryServ;

    @Transactional(readOnly = true)
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("sku-code") List<String> skuCode){
        log.info("Is In Stock Method being called");
        return inventoryServ.isInStock(skuCode);
    }

}
