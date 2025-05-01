package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

@DisplayName("Тестирование контроллера книг - ограничение доступа")
@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @DisplayName("Должен вернуть ожидаемые статусы")
    @ParameterizedTest(name = "{0} {1} для пользователя {2} должен вернуть {4} статус")
    @MethodSource("getTestData")
    void shouldReturnExpectedStatus(String method, String url, String userName, String[] roles,
                                    int status, boolean checkLOginRedirection) throws Exception {
        var request = method2RequestBuilder(method, url);
        if (nonNull(userName)) {
            request = request.with(user(userName).roles(roles));
        }
        ResultActions resultActions = mvc.perform(request)
                .andExpect(status().is(status));
        if (checkLOginRedirection) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodMap =
                Map.of("get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post
                );
        return methodMap.get(method).apply(url);
    }

    public static Stream<Arguments> getTestData() {
        var userRole = new String[]{"USER"};
        var editorRole = new String[]{"EDITOR"};
        var adminRole = new String[]{"ADMIN"};
        return Stream.of(
                Arguments.of("get", "/books", null, null, 302, true),
                Arguments.of("get", "/books", "user", userRole, 200, false),
                Arguments.of("post", "/books/delete", null, null, 302, true),
                Arguments.of("post", "/books/delete", "user", userRole, 403, false),
                Arguments.of("post", "/books/delete", "user", editorRole, 403, false),
                Arguments.of("get", "/books/edit/1", null, null, 302, true),
                Arguments.of("get", "/books/edit/1", "user", userRole, 403, false),
                Arguments.of("get", "/books/edit/1", "user", editorRole, 200, false),
                Arguments.of("get", "/books/edit/1", "user", adminRole, 200, false),
                Arguments.of("get", "/books/insert", null, null, 302, true),
                Arguments.of("get", "/books/insert", "user", userRole, 403, false),
                Arguments.of("get", "/books/insert", "user", editorRole, 200, false),
                Arguments.of("get", "/books/insert", "user", adminRole, 200, false)
        );
    }

}