package com.example.coupon_backend.domain.delivery.entity;

import com.example.coupon_backend.domain.bill.entity.Bill;
import com.example.coupon_backend.domain.delivery.enums.DeliveryStatus;
import com.example.coupon_backend.domain.member.entity.Member;
import com.example.coupon_backend.domain.member.enums.MemberStatus;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_no")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_no")
    private Bill bill;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Embedded
    private Address address;

    public static Delivery create(Address address) {
        Assert.notNull(address, "address cannot be null.");

        return Delivery.builder()
                .status(DeliveryStatus.WAIT)
                .address(address)
                .build();
    }

    public void associateWith(Bill bill) {
        this.bill = bill;
    }

}
