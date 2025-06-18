package com.example.pharmacy15.repository.iface;

public interface OrderDrugRepository {
    void save(int orderId, int drugId, int saleQuantity, int salePrice);
}
