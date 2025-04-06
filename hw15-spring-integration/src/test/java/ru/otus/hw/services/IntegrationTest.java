package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.gateway.CheckGateway;
import ru.otus.hw.models.Item;
import ru.otus.hw.models.Check;
import ru.otus.hw.models.FinalCheck;
import ru.otus.hw.models.ProductCategory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private CheckGateway gateway;

    @Test
    @DisplayName("Должен применить к предварительному чеку скидку и вернуть то же количество товаров в чеке")
    public void testCheckProcessing() {
        Check check = new Check(1L, Arrays.asList(
                new Item("Bread", 100, ProductCategory.FOOD),
                new Item("Cheese", 800, ProductCategory.FOOD),
                new Item("Shampoo", 350, ProductCategory.HYGIENE),
                new Item("Snow shovel", 1700, ProductCategory.TOOL)
        ));

        FinalCheck finalCheck = gateway.process(check);
        assertEquals(finalCheck.getItems().size(), check.getItems().size());
    }
}