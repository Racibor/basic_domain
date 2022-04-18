package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class FifteenPercentDiscount implements Discount {

    public static String NAME = "FifteenPercentDiscount";

    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(0.85));
            var discounts = receipt.discounts();
            var appendedDiscounts = Stream.concat(discounts.stream(), Stream.of(NAME)).collect(Collectors.toList());
            return new Receipt(receipt.entries(), appendedDiscounts, totalPrice);
        }
        return receipt;
    }

    private boolean shouldApply(Receipt receipt) {
        Optional<Integer> grainQuantity = receipt.entries().stream()
                .filter(entry -> entry.product().type().equals(Product.Type.GRAINS))
                .map(entry -> entry.quantity())
                .reduce((a, b) -> a + b);
        if(grainQuantity.isPresent()) {
            return grainQuantity.get() >= 3;
        }
        return false;
    }
}
