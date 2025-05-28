package com.example.coupon_backend.domain.goods.repository;

import com.example.coupon_backend.IntegrationTestSupport;
import com.example.coupon_backend.domain.brand.entity.Brand;
import com.example.coupon_backend.domain.brand.repository.BrandRepository;
import com.example.coupon_backend.domain.goods.entity.Goods;
import com.example.coupon_backend.domain.goods.enums.GoodsType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class GoodsRepositoryTest extends IntegrationTestSupport {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    BrandRepository brandRepository;

    @DisplayName("연동 브랜드의 상품 정보를 등록할 수 있다.")
    @Test
    public void save() throws Exception {
        // given
        Brand brand = createBrand();
        Goods goods = Goods.create("아메리카노", GoodsType.B2B, 4500, 4000, brand);

        // when
        Goods savedGoods = goodsRepository.save(goods);

        // then
        assertThat(savedGoods).isEqualTo(goods);
        assertThat(savedGoods.getGoodsName()).isEqualTo("아메리카노");
        assertThat(savedGoods.getGoodsType()).isEqualTo(GoodsType.B2B);
        assertThat(savedGoods.getGoodsPrice()).isEqualTo(4500);
        assertThat(savedGoods.getSalePrice()).isEqualTo(4000);
        assertThat(savedGoods.getBrand()).isEqualTo(brand);
        assertThat(brand.getGoodsList()).hasSize(1);
    }

    private Brand createBrand() {
        Brand brand = Brand.create("스타벅스");
        brandRepository.save(brand);
        return brand;
    }

}