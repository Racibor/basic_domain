package com.virtuslab.internship.controllers;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ReceiptGeneratorControllerTest {
    @Autowired
    ReceiptGeneratorController receiptGeneratorController;

    @Autowired
    ProductDb productDb;

    @Test
    public void shouldReturnStatus400OnMalformedData() {
        // Given
        Product product = productDb.getProduct("Cheese");

        Basket cart = new Basket();
        cart.addProduct(new Product("Cheese", Product.Type.DAIRY, BigDecimal.valueOf(0)));

        // When
        ResponseEntity<Receipt> receiptResponseEntity = receiptGeneratorController.getReceipt(cart);

        // Then
        assertEquals(400, receiptResponseEntity.getStatusCodeValue());
        assertNull(receiptResponseEntity.getBody());
    }

    @Test
    public void shouldReturnStatus200OnGoodData() {
        // Given
        Product product = productDb.getProduct("Cheese");

        Basket cart = new Basket();
        cart.addProduct(product);

        // When
        ResponseEntity<Receipt> receiptResponseEntity = receiptGeneratorController.getReceipt(cart);

        // Then
        assertEquals(200, receiptResponseEntity.getStatusCodeValue());
        assertNotNull(receiptResponseEntity.getBody());
    }

    @Test
    public void shouldApplyDiscounts() {
        // Given

        Basket cart = new Basket();

        var milk = productDb.getProduct("Milk");
        var bread = productDb.getProduct("Bread");
        var apple = productDb.getProduct("Apple");
        var cheese = productDb.getProduct("Cheese");
        var expectedTotalPrice = milk.price().multiply(BigDecimal.valueOf(3)).add(bread.price().multiply(BigDecimal.valueOf(3))).add(apple.price()).add(cheese.price().multiply(BigDecimal.valueOf(5))).multiply(BigDecimal.valueOf(0.85)).multiply(BigDecimal.valueOf(0.9));

        cart.addProduct(milk);
        cart.addProduct(milk);
        cart.addProduct(milk);
        cart.addProduct(bread);
        cart.addProduct(bread);
        cart.addProduct(bread);
        cart.addProduct(apple);
        cart.addProduct(cheese);
        cart.addProduct(cheese);
        cart.addProduct(cheese);
        cart.addProduct(cheese);
        cart.addProduct(cheese);

        // When
        ResponseEntity<Receipt> receiptResponseEntity = receiptGeneratorController.getReceipt(cart);
        Receipt receipt = receiptResponseEntity.getBody();

        // Then
        assertNotNull(receipt);
        assertEquals(4, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(2, receipt.discounts().size());
    }
}
