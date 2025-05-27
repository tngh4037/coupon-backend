package com.example.coupon_backend.domain.order.entity;

import com.example.coupon_backend.domain.brand.entity.Brand;
import com.example.coupon_backend.domain.goods.entity.Goods;
import com.example.coupon_backend.domain.goods.enums.GoodsType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 상품 금액의 합계를 계산할 수 있다.")
    @Test
    public void getOrderPrice() throws Exception {
        // given
        Brand brand = Brand.create("스타벅스");
        Goods americano = Goods.create("아메리카노", GoodsType.B2B, 4500, 4500, brand);

        // when
        Order order = Order.create(americano, americano.getGoodsPrice(), 3);

        // then
        Assertions.assertThat(order.getOrderPrice()).isEqualTo(13500);
    }

}