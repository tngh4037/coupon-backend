package com.example.coupon_backend.domain.delivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;
    private String street;

    @Column(name = "zipcode")
    private String zipCode;

    public static Address create(String city, String street, String zipCode) {
        Address address = new Address();
        address.city = city;
        address.street = street;
        address.zipCode = zipCode;
        return address;
    }
}
