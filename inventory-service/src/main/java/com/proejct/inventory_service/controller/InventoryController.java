package com.proejct.inventory_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import com.proejct.inventory_service.dto.InventoryResponse;
import com.proejct.inventory_service.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> checkInventory(@RequestParam("skuCode") List<String> skuCodes) {
        return inventoryService.isInStock(skuCodes);
    }

}
