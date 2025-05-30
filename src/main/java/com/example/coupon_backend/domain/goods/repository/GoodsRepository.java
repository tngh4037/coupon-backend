package com.example.coupon_backend.domain.goods.repository;

import com.example.coupon_backend.domain.goods.entity.Goods;
import com.example.coupon_backend.domain.goods.enums.GoodsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    List<Goods> findByBrandIdIn(List<Long> brandNos);

    List<Goods> findByBrandIdInAndDisplayYn(List<Long> brandIds, String displayYn);

    List<Goods> findByGoodsType(GoodsType goodsType);

}
