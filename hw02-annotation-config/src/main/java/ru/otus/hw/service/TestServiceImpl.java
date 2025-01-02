package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var testResult = new TestResult(student);
        printQuestions(testResult);
        return testResult;
    }

    private void printQuestions(TestResult testResult) {
        for (Question question : questionDao.findAll()) {
            printQuestion(question, testResult);
        }
        ioService.printLine("");
    }

    private void printQuestion(Question question, TestResult testResult) {
        ioService.printFormattedLine("Question: " + question.text());
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine((question.answers().indexOf(answer) + 1) + ". " + answer.text());
        }
        checkingAnswer(question, testResult);
        ioService.printLine("");
    }

    private void checkingAnswer(Question question, TestResult testResult) {
        var answerIndex = ioService.readIntForRangeWithPrompt(
                1,
                question.answers().size() + 1,
                "Please choose correct answer: ",
                "Possible answers between 1 and %d".formatted(question.answers().size()));
        testResult.applyAnswer(question, question.answers().get(answerIndex - 1).isCorrect());
    }
}
