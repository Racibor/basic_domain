package com.virtuslab.internship.product;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductDbTest {

    @Test
    public void shouldReturnNullOnNotFound() {
        ProductDb productDb = new ProductDb();
        assertNull(productDb.getProduct("thisProductDoesntExist"));
    }

    @Test
    public void shouldReturnProductOnGoodMatch() {
        ProductDb productDb = new ProductDb();
        assertNotNull(productDb.getProduct("Cheese"));
    }
}
