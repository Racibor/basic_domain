package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TenPercentDiscount implements Discount {

    public static String NAME = "TenPercentDiscount";

    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(0.9));
            var discounts = receipt.discounts();
            var appendedDiscounts = Stream.concat(discounts.stream(), Stream.of(NAME)).collect(Collectors.toList());
            return new Receipt(receipt.entries(), appendedDiscounts, totalPrice);
        }
        return receipt;
    }

    private boolean shouldApply(Receipt receipt) {
        return receipt.totalPrice().compareTo(BigDecimal.valueOf(50)) > 0;
    }
}
