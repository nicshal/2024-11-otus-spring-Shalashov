package ru.otus.hw.actuators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.services.BookService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookCountIndicator.class,
        properties = {"spring.sql.init.mode=never"})
@EnableAutoConfiguration
@AutoConfigureMockMvc
class BookCountIndicatorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Должен вернуть индикатор bookCountIndicator в успешном статусе с корректным количеством книг")
    void shouldReturnCorrectBookCountIndicator() throws Exception {
        when(bookService.count()).thenReturn(777888999L);

        mockMvc.perform(get("/monitor/health"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("book count = 777888999")));
    }
}