package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.BookService;

@Component
@RequiredArgsConstructor
public class BookCountIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        try {
            return Health.up()
                    .withDetail("message", "book count = %d"
                            .formatted(bookService.count()))
                    .build();
        } catch (Exception ex) {
            return Health.down()
                    .withDetail("error", "База данных недоступна")
                    .build();
        }
    }
}