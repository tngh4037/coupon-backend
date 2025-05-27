package com.example.coupon_backend.domain.goods.entity;

import com.example.coupon_backend.domain.brand.entity.Brand;
import com.example.coupon_backend.domain.goods.enums.GoodsType;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goods extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_no")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_no")
    private Brand brand;

    private String goodsName;

    @Enumerated(EnumType.STRING)
    private GoodsType goodsType;

    private int goodsPrice;
    private int salePrice;
    private String displayYn;

    public static Goods create(String goodsName, GoodsType goodsType, int goodsPrice, int salePrice, Brand brand) {
        Assert.notNull(brand, "brand cannot be null.");
        Assert.hasText(goodsName, "goodsName cannot be empty.");
        Assert.notNull(goodsType, "goodsType cannot be null.");

        Goods goods = new Goods();
        goods.goodsName = goodsName;
        goods.goodsType = goodsType;
        goods.goodsPrice = goodsPrice;
        goods.salePrice = salePrice;
        goods.displayYn = "Y";
        goods.associateWith(brand);
        return goods;
    }

    private void associateWith(Brand brand) {
        this.brand = brand;
        brand.getGoodsList().add(this);
    }

}
