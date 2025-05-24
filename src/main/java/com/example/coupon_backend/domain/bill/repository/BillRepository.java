package com.example.coupon_backend.domain.bill.repository;

import com.example.coupon_backend.domain.bill.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
