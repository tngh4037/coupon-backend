package com.example.coupon_backend.domain.goods.entity;

import com.example.coupon_backend.domain.brand.entity.Brand;
import com.example.coupon_backend.domain.goods.enums.GoodsType;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
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

        Goods goods = Goods.builder()
                .goodsName(goodsName)
                .goodsType(goodsType)
                .goodsPrice(goodsPrice)
                .salePrice(salePrice)
                .displayYn("Y")
                .build();

        goods.associateWith(brand);

        return goods;
    }

    private void associateWith(Brand brand) {
        this.brand = brand;
        brand.getGoodsList().add(this);
    }

}
