package com.example.coupon_backend.global.config;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest
class JasyptConfigTest {

    private StandardPBEStringEncryptor pbeEnc;

    @BeforeEach
    void beforeEach() {
        pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(getKey());
    }

    @Test
    void jasypt() {
        String secret = "암호화 할 정보";
        String encryptedText = jasyptEncoding(secret);
        log.info("encryptedText :: {} -> {}", secret, encryptedText);
        String decryptedText = jasyptDecoding(encryptedText);
        log.info("decryptedText :: {} -> {}", encryptedText, decryptedText);

        Assertions.assertThat(decryptedText).isEqualTo(secret);
    }

    private String jasyptEncoding(String value) {
        return pbeEnc.encrypt(value);
    }

    private String jasyptDecoding(String value) {
        return pbeEnc.decrypt(value);
    }

    private String getKey() {
        try {
            return new String(Files.readAllBytes(Paths.get("/CouponSecretKey/key.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}