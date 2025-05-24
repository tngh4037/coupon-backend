package com.example.coupon_backend.domain.brand.repository;

import com.example.coupon_backend.domain.brand.entity.Brand;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BrandRepositoryTest {

    @Autowired
    BrandRepository brandRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void save() throws Exception {
        // given
        Brand brand = Brand.create("스타벅스");

        // when
        Brand newBrand = brandRepository.save(brand);
        Brand findBrand = brandRepository.findById(brand.getId()).orElse(null);

        // then
        assertThat(newBrand).isEqualTo(findBrand);
        assertThat(findBrand.getBrandName()).isEqualTo("스타벅스");
        assertThat(findBrand.getDisplayYn()).isEqualTo("Y");
    }

    @Test
    public void modifyDisplayYn() throws Exception {
        // given
        Brand brand = Brand.create("스타벅스");

        // when
        Brand newBrand = brandRepository.save(brand);
        newBrand.changeDisplayYn("N");
        em.flush();

        // then
        assertThat(newBrand.getDisplayYn()).isEqualTo("N");
    }

}