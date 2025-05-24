package com.example.coupon_backend.domain.bill.entity;

import com.example.coupon_backend.domain.bill.enums.BillStatus;
import com.example.coupon_backend.domain.bill.enums.BillType;
import com.example.coupon_backend.domain.delivery.entity.Delivery;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.order.entity.Order;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bill extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_no")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column(name = "bill_status")
    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @Column(name = "bill_type")
    @Enumerated(EnumType.STRING)
    private BillType type;

    private int totalAmount;

    @OneToOne(mappedBy = "bill", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Delivery delivery;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.PERSIST)
    private List<Order> orders = new ArrayList<>();

    public static Bill create(Member member, BillType billType, Delivery delivery, Order... orders) {
        Assert.notNull(member, "member cannot be null.");
        Assert.notNull(billType, "billType cannot be null.");

        Bill bill = new Bill();
        bill.member = member;
        bill.status = BillStatus.WAIT;
        bill.type = billType;
        if (billType.isPurchaseType()) {
            bill.associateWith(delivery);
            for (Order order : orders) {
                bill.associateWith(order);
            }
        }
        bill.totalAmount = bill.calculateTotalAmount();
        return bill;
    }

    private void associateWith(Delivery delivery) {
        this.delivery = delivery;
        delivery.associateWith(this);
    }

    private void associateWith(Order order) {
        this.getOrders().add(order);
        order.associateWith(this);
    }

    public int calculateTotalAmount() {
        int totalPrice = 0;
        for (Order order : orders) {
            totalPrice += order.getOrderPrice();
        }
        return totalPrice;
    }

}
