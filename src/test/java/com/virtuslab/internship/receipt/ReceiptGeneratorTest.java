package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.ProductDb;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
class ReceiptGeneratorTest {

    @Autowired
    ReceiptGenerator receiptGenerator;

    @Test
    void shouldGenerateReceiptForGivenBasket() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var milk = productDb.getProduct("Milk");
        var bread = productDb.getProduct("Bread");
        var apple = productDb.getProduct("Apple");
        var expectedTotalPrice = milk.price().multiply(BigDecimal.valueOf(2)).add(bread.price()).add(apple.price());

        cart.addProduct(milk);
        cart.addProduct(milk);
        cart.addProduct(bread);
        cart.addProduct(apple);


        // When
        var receipt = receiptGenerator.generate(cart);

        // Then
        assertNotNull(receipt);
        assertEquals(3, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(0, receipt.discounts().size());
    }

    @Test
    void shouldApply15PercentDiscountBefore10PercentDiscount() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
        var milk = productDb.getProduct("Milk");
        var bread = productDb.getProduct("Bread");
        var apple = productDb.getProduct("Apple");
        var expectedTotalPrice = milk.price().multiply(BigDecimal.valueOf(1)).add(bread.price().multiply(BigDecimal.valueOf(3))).add(apple.price()).multiply(BigDecimal.valueOf(0.85));

        cart.addProduct(milk);
        cart.addProduct(bread);
        cart.addProduct(bread);
        cart.addProduct(bread);
        cart.addProduct(apple);


        // When
        var receipt = receiptGenerator.generate(cart);

        // Then
        assertNotNull(receipt);
        assertEquals(3, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(1, receipt.discounts().size());
    }

    @Test
    void shouldApply10PercentDiscountAfter15PercentDiscount() {
        // Given
        var productDb = new ProductDb();
        var cart = new Basket();
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
        var receipt = receiptGenerator.generate(cart);

        // Then
        assertNotNull(receipt);
        assertEquals(4, receipt.entries().size());
        assertEquals(expectedTotalPrice, receipt.totalPrice());
        assertEquals(2, receipt.discounts().size());
    }

}
