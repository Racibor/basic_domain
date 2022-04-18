package com.virtuslab.internship.discount;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DiscountsConfiguration {

    @Bean
    public List<Discount> getDiscountList() {
        List<Discount> discountList = List.of(
          new FifteenPercentDiscount(),
          new TenPercentDiscount()
        );
        return discountList;
    }
}
