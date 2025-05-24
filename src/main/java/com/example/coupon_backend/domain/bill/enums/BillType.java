package com.example.coupon_backend.domain.bill.enums;

public enum BillType {
    BUY, EVENT, CHARGE;

    public boolean isPurchaseType() {
        return this == BUY || this == EVENT;
    }
}
