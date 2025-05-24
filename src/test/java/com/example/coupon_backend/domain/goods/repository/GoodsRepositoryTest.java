package com.example.coupon_backend.domain.goods.repository;

import com.example.coupon_backend.domain.brand.entity.Brand;
import com.example.coupon_backend.domain.brand.repository.BrandRepository;
import com.example.coupon_backend.domain.goods.entity.Goods;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GoodsRepositoryTest {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    BrandRepository brandRepository;

    @Test
    public void save() throws Exception {
        // given
        Brand brand = Brand.create("스타벅스");
        brandRepository.save(brand);
        Goods goods = Goods.create("아메리카노", 4500, 4000, brand);

        // when
        Goods savedGoods = goodsRepository.save(goods);

        // then
        Assertions.assertThat(savedGoods).isEqualTo(goods);
        Assertions.assertThat(savedGoods.getGoodsName()).isEqualTo("아메리카노");
        Assertions.assertThat(savedGoods.getGoodsPrice()).isEqualTo(4500);
        Assertions.assertThat(savedGoods.getSalePrice()).isEqualTo(4000);
        Assertions.assertThat(savedGoods.getBrand()).isEqualTo(brand);
        Assertions.assertThat(brand.getGoodsList().size()).isEqualTo(1);
    }

}