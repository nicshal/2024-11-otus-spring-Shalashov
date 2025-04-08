package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.gateway.CheckGateway;
import ru.otus.hw.models.Item;
import ru.otus.hw.models.Check;
import ru.otus.hw.models.FinalCheck;
import ru.otus.hw.models.ProductCategory;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class IntegrationFlowRunner implements CommandLineRunner {

    private final CheckGateway gateway;

    @Override
    public void run(String... args) {
        Check check = new Check(1L, Arrays.asList(
                new Item("Bread", 100, ProductCategory.FOOD),
                new Item("Cheese", 800, ProductCategory.FOOD),
                new Item("Shampoo", 350, ProductCategory.HYGIENE),
                new Item("Snow shovel", 1700, ProductCategory.TOOL)
        ));
        FinalCheck finalCheck = gateway.process(check);
        System.out.println("Final check: " + finalCheck);
    }
}