package com.example.coupon_backend.domain.order.entity;

import com.example.coupon_backend.domain.bill.entity.Bill;
import com.example.coupon_backend.domain.goods.entity.Goods;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Table(name = "orders")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_no")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_no")
    private Bill bill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_no")
    private Goods goods;

    private int goodsPrice;

    private int orderCnt;

    public static Order create(Goods goods, int goodsPrice, int orderCnt) {
        Assert.notNull(goods, "goods cannot be null.");

        Order order = new Order();
        order.goods = goods;
        order.goodsPrice = goodsPrice;
        order.orderCnt = orderCnt;
        return order;
    }

    public void associateWith(Bill bill) {
        this.bill = bill;
    }

    public int getOrderPrice() {
        return this.goodsPrice * orderCnt;
    }

}
