package com.example.pharmacy15.repository.iface;

import com.example.pharmacy15.domain.Purchase;

import java.util.List;

public interface PurchaseRepository {
    List<Purchase> findAvailableByDrugId(int drugId);
    void updateRemainingQuantity(int purchaseId, int remaining);
}
