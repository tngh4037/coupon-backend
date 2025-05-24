package com.example.coupon_backend.domain.goods.repository;

import com.example.coupon_backend.domain.goods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
