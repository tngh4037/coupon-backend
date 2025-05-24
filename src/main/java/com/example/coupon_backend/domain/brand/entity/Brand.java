package com.example.coupon_backend.domain.brand.entity;

import com.example.coupon_backend.domain.goods.entity.Goods;
import com.example.coupon_backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_no")
    private Long id;

    private String brandName;
    private String displayYn;

    @OneToMany(mappedBy = "brand")
    private List<Goods> goodsList = new ArrayList<>();

    public static Brand create(String brandName) {
        Assert.hasText(brandName, "brandName cannot be empty.");

        Brand brand = new Brand();
        brand.brandName = brandName;
        brand.displayYn = "Y";
        return brand;
    }

    public void changeDisplayYn(String displayYn) {
        Assert.isTrue(displayYn.equals("Y") || displayYn.equals("N"), "displayYn must be 'Y' or 'N'.");

        this.displayYn = displayYn;
    }

}
