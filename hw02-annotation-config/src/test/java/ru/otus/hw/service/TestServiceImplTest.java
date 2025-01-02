package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao csvQuestionDao;

    @InjectMocks
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

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Проверяем печать вопросов")
    public void testExecuteTest() {
        Student student = new Student("FIRST_NAME", "LAST_NAME");
        when(csvQuestionDao.findAll()).thenReturn(questionList);
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1, 1, 1);

        TestResult result = testService.executeTestFor(student);

        verify(ioService, times(5)).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService).printFormattedLine("Question: Question 1");
        verify(ioService).printFormattedLine("Question: Question 2");
        verify(ioService).printFormattedLine("Question: Question 3");

        assertThat(result.getRightAnswersCount()).isEqualTo(1);
    }

}