package com.example.coupon_backend.domain.delivery.repository;

import com.example.coupon_backend.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
