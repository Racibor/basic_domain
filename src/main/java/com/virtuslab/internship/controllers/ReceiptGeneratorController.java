package com.virtuslab.internship.controllers;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller()
public class ReceiptGeneratorController {

    ReceiptGenerator receiptGenerator;

    ProductDb productDb;

    ReceiptGeneratorController(ReceiptGenerator receiptGenerator, ProductDb productDb) {
        this.receiptGenerator = receiptGenerator;
        this.productDb = productDb;
    }

    @ResponseBody
    @GetMapping("getReceipt")
    public ResponseEntity<Receipt> getReceipt(@RequestBody Basket basket) {
        List<Product> malformedProducts = basket.getProducts().stream()
                .filter(product -> !product.equals(productDb.getProduct(product.name()))).collect(Collectors.toList());

        if(malformedProducts.size() > 0) {
            return ResponseEntity.status(400).body(null);
        }

        Receipt receipt = receiptGenerator.generate(basket);
        return ResponseEntity.ok(receipt);
    }

}
