package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TestServiceImpl.class})
public class TestServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao csvQuestionDao;

    @Autowired
    private TestServiceImpl testService;

    private final List<Question> questionList = List.of(
            new Question("Question 1",
                    List.of(
                            new Answer("Answer 1", true),
                            new Answer("Answer 2", false)
                    )
            ),
            new Question("Question 2",
                    List.of(
                            new Answer("Answer 1", false),
                            new Answer("Answer 2", true)
                    )
            ),
            new Question("Question 3",
                    List.of(
                            new Answer("Answer 1", false),
                            new Answer("Answer 2", true)
                    )
            )
    );

    @Test
    @DisplayName("Проверяем баланс вопросов/правильных ответов")
    public void testExecuteTest() {
        Student student = new Student("FIRST_NAME", "LAST_NAME");
        when(csvQuestionDao.findAll()).thenReturn(questionList);
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1, 1, 1);

        TestResult result = testService.executeTestFor(student);

        verify(ioService, times(1)).printLineLocalized("TestService.answer.the.questions");
        verify(ioService, times(3)).readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString());

        assertThat(result.getAnsweredQuestions().size()).isEqualTo(3);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);
    }

}