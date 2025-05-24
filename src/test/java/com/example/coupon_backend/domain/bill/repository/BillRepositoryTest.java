package com.example.coupon_backend.domain.bill.repository;

import com.example.coupon_backend.domain.bill.entity.Bill;
import com.example.coupon_backend.domain.bill.enums.BillType;
import com.example.coupon_backend.domain.brand.entity.Brand;
import com.example.coupon_backend.domain.brand.repository.BrandRepository;
import com.example.coupon_backend.domain.delivery.entity.Address;
import com.example.coupon_backend.domain.delivery.entity.Delivery;
import com.example.coupon_backend.domain.goods.entity.Goods;
import com.example.coupon_backend.domain.goods.repository.GoodsRepository;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.repository.MemberRepository;
import com.example.coupon_backend.domain.order.entity.Order;
import com.example.coupon_backend.domain.order.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class BillRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void test_buy() throws Exception {
        // given
        Member member = createMember();
        Brand brand = createBrand();
        Goods americano = createGoods(brand, "아메리카노", 4500, 4000);
        Goods latte = createGoods(brand, "카페라떼", 5000, 4500);

        // when
        Order order1 = Order.create(americano, americano.getGoodsPrice(), 3);
        Order order2 = Order.create(latte, latte.getGoodsPrice(), 2);
        Delivery delivery = Delivery.create(Address.create("city", "street", "zipCode"));

        Bill bill = Bill.create(member, BillType.BUY, delivery, order1, order2);
        Bill savedBill = billRepository.save(bill);

        // then
        Assertions.assertThat(savedBill).isEqualTo(bill);
        Assertions.assertThat(savedBill.getId()).isEqualTo(bill.getId());

        List<Order> orders = orderRepository.findByBill(bill);
        Assertions.assertThat(orders).containsExactly(order1, order2);
    }

    @Test
    public void test_charge() throws Exception {
        // given
        Member member = createMember();
        Brand brand = createBrand();
        Goods americano = createGoods(brand, "아메리카노", 4500, 4000);
        Goods latte = createGoods(brand, "카페라떼", 5000, 4500);

        // when
        Order order1 = Order.create(americano, americano.getGoodsPrice(), 3);
        Order order2 = Order.create(latte, latte.getGoodsPrice(), 2);
        Delivery delivery = Delivery.create(Address.create("city", "street", "zipCode"));

        Bill bill = Bill.create(member, BillType.CHARGE, delivery, order1, order2);
        Bill savedBill = billRepository.save(bill);

        // then
        Assertions.assertThat(savedBill).isEqualTo(bill);
        Assertions.assertThat(savedBill.getId()).isEqualTo(bill.getId());

        List<Order> orders = orderRepository.findByBill(bill);
        Assertions.assertThat(orders).isEmpty();
    }

    private Goods createGoods(Brand brand, String goodsName, int goodsPrice, int salePrice) {
        Goods americano = Goods.create(goodsName, goodsPrice, salePrice, brand);
        goodsRepository.save(americano);
        return americano;
    }

    private Brand createBrand() {
        Brand brand = Brand.create("스타벅스");
        brandRepository.save(brand);
        return brand;
    }

    private Member createMember() {
        Member member = Member.join("userId", "kim");
        memberRepository.save(member);
        return member;
    }
}