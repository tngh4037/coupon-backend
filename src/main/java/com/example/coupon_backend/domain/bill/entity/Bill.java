package com.example.coupon_backend.domain.bill.entity;

import com.example.coupon_backend.domain.bill.enums.BillStatus;
import com.example.coupon_backend.domain.bill.enums.BillType;
import com.example.coupon_backend.domain.delivery.entity.Delivery;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.order.entity.Order;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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

    @OneToOne(mappedBy = "bill", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;

    @Builder.Default
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public static Bill create(Member member, BillType billType, Delivery delivery, Order... orders) {
        Assert.notNull(member, "member cannot be null.");
        Assert.notNull(billType, "billType cannot be null.");

        Bill bill = Bill.builder()
                .member(member)
                .status(BillStatus.WAIT)
                .type(billType)
                .build();

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

    private int calculateTotalAmount() {
        return orders.stream()
                .mapToInt(Order::getOrderPrice)
                .sum();
    }

}
