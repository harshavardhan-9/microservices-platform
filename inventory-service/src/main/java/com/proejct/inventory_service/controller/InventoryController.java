package com.proejct.inventory_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import com.proejct.inventory_service.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public String checkInventory(@PathVariable("skuCode") String skuCode) {
        return inventoryService.isInStock(skuCode) ? "In Stock" : "Out of Stock";
    }
    
}
