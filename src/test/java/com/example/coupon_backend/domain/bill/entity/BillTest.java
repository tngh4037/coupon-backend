package com.example.coupon_backend.domain.bill.entity;

import com.example.coupon_backend.domain.bill.enums.BillType;
import com.example.coupon_backend.domain.brand.entity.Brand;
import com.example.coupon_backend.domain.delivery.entity.Address;
import com.example.coupon_backend.domain.delivery.entity.Delivery;
import com.example.coupon_backend.domain.goods.entity.Goods;
import com.example.coupon_backend.domain.goods.enums.GoodsType;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.order.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BillTest {

    @DisplayName("결제시 상품의 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotalAmount() {

        // given
        Member member = Member.join("userId", "kim");
        Brand brand = Brand.create("스타벅스");
        Goods americano = Goods.create("아메리카노", GoodsType.B2B, 4500, 4500, brand);
        Goods latte = Goods.create("카페라떼", GoodsType.B2B, 5000, 5000, brand);
        Order americano_order = Order.create(americano, 4500, 2); // 9000
        Order latte_order = Order.create(latte, 5000, 3); // 15000
        Delivery delivery = Delivery.create(Address.create("city", "street", "zipCode"));

        // when
        Bill bill = Bill.create(member, BillType.BUY, delivery, americano_order, latte_order);

        // then
        assertThat(bill.getTotalAmount()).isEqualTo(24000);
        assertThat(bill.calculateTotalAmount()).isEqualTo(24000);
    }

}