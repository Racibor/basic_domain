package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FifteenPercentDiscountTest {

    @Test
    void shouldApply15PercentDiscountWhenMoreThan3Grains() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var bread = productDb.getProduct("Bread");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));
        receiptEntries.add(new ReceiptEntry(bread, 3));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var expectedTotalPrice = cheese.price().multiply(BigDecimal.valueOf(2)).add(bread.price().multiply(BigDecimal.valueOf(3))).multiply(BigDecimal.valueOf(0.85));

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(1, receiptAfterDiscount.discounts().size());
    }

    @Test
    void shouldNotApply15PercentDiscountWhenLessThan3Grains() {
        // Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var bread = productDb.getProduct("Bread");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));
        receiptEntries.add(new ReceiptEntry(bread, 2));

        var receipt = new Receipt(receiptEntries);
        var discount = new FifteenPercentDiscount();
        var expectedTotalPrice = cheese.price().multiply(BigDecimal.valueOf(2)).add(bread.price().multiply(BigDecimal.valueOf(2)));

        // When
        var receiptAfterDiscount = discount.apply(receipt);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterDiscount.totalPrice());
        assertEquals(0, receiptAfterDiscount.discounts().size());
    }

}
