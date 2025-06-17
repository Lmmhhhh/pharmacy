package com.example.pharmacy15.repository;

public interface OrderDrugRepository {
    void save(int orderId, int drugId, int saleQuantity, int salePrice);
}
