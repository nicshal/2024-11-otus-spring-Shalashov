package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CsvQuestionDao.class})
public class CsvQuestionDaoTest {

    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    @DisplayName("Проверяем корректность чтения файла, формирование списка вопросов-ответов")
    void testFindAll() {
        String testFileName = "test-questions.csv";
        when(fileNameProvider.getTestFileName()).thenReturn(testFileName);

        List<Question> questions = csvQuestionDao.findAll();

        assertNotNull(questions);
        assertEquals(2, questions.size());

        Question question1 = questions.get(0);
        assertEquals("Question 1", question1.text());
        assertEquals(List.of(new Answer("Answer 1", false), new Answer("Answer 2", true)), question1.answers());

        Question question2 = questions.get(1);
        assertEquals("Question 2", question2.text());
        assertEquals(List.of(new Answer("Answer 3", false), new Answer("Answer 4", true), new Answer("Answer 5", false)), question2.answers());
    }

    @Test
    @DisplayName("Проверяем на исключение QuestionReadException при отсутствии файла")
    void testFindAllWithException() {
        String testFileName = "test-no-file.csv";
        when(fileNameProvider.getTestFileName()).thenReturn(testFileName);

        Assertions.assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());
    }

}
