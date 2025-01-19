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

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
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
        ioService.printFormattedLineLocalized("TestService.question", question.text());
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine((question.answers().indexOf(answer) + 1) + ". " + answer.text());
        }
        checkingAnswer(question, testResult);
        ioService.printLine("");
    }

    private void checkingAnswer(Question question, TestResult testResult) {
        var answerIndex = ioService.readIntForRangeWithPromptLocalized(
                1,
                question.answers().size() + 1,
                "TestService.choose.answer",
                "TestService.invalid.answer.number");
        testResult.applyAnswer(question, question.answers().get(answerIndex - 1).isCorrect());
    }
}
