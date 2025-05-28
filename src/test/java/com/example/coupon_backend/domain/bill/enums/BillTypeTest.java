package com.example.coupon_backend.domain.bill.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BillTypeTest {

    // Csv: 콤마(,) 구분자로 이루어진 형식
    @DisplayName("결제유형이 상품 결제 관련 타입인지를 체크한다.")
    @CsvSource({"BUY,true", "EVENT,true", "CHARGE,false"})
    @ParameterizedTest
    void isPurchaseType_csv(BillType billType, boolean expected) { // 콤마(,)로 구분된 값들이 파라미터로 들어온다.

        // when
        boolean result = billType.isPurchaseType();

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> provideBillTypesForCheckingPurchase() {
        return Stream.of(
                Arguments.of(BillType.BUY, true),
                Arguments.of(BillType.EVENT, true),
                Arguments.of(BillType.CHARGE, false)
        );
    }

    // 메서드 이름으로 호출해서, 반환되는 것을 파라미터에 대입
    @DisplayName("결제유형이 상품 결제 관련 타입인지를 체크한다.")
    @MethodSource("provideBillTypesForCheckingPurchase") // 메서드 이름을 넣어준다.
    @ParameterizedTest
    void isPurchaseType_method(BillType billType, boolean expected) {

        // when
        boolean result = billType.isPurchaseType();

        // then
        assertThat(result).isEqualTo(expected);
    }

}
