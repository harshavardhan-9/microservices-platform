package com.proejct.inventory_service.service;

import com.proejct.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
            .map(inventory -> inventory.getQuantity() > 0)
            .orElse(false);
    }
}
