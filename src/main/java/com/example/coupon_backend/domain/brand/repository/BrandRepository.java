package com.example.coupon_backend.domain.brand.repository;

import com.example.coupon_backend.domain.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
