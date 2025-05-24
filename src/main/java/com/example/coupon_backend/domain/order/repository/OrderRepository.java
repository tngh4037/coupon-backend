package com.example.coupon_backend.domain.order.repository;

import com.example.coupon_backend.domain.bill.entity.Bill;
import com.example.coupon_backend.domain.order.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBill(Bill bill);
}
