package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.Discount;
import com.virtuslab.internship.product.Product;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReceiptGenerator {
    List<Discount> discountList;

    ReceiptGenerator(List<Discount> discounts) {
        discountList = discounts;
    }

    public Receipt generate(Basket basket) {
        HashMap<Product, Integer> merger = new HashMap<>();
        basket.getProducts().forEach(product -> {
            merger.merge(product, 1, Integer::sum);
        });

        List<ReceiptEntry> entries = new ArrayList<>();
        merger.forEach((product, quantity) -> {
            entries.add(new ReceiptEntry(product, quantity));
        });

        Receipt receipt = new Receipt(entries);
        return applyDiscounts(receipt);
    }

    private Receipt applyDiscounts(Receipt receipt) {
        for(Discount discount : discountList) {
            receipt = discount.apply(receipt);
        }
        return receipt;
    }
}
