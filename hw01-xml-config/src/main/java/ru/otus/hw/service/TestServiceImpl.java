package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        printQuestions();
    }

    private void printQuestions() {
        for (Question question : questionDao.findAll()) {
            printQuestion(question);
        }
        ioService.printLine("");
    }

    private void printQuestion(Question question) {
        ioService.printFormattedLine("Question: " + question.text());
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine((question.answers().indexOf(answer) + 1) + ". " + answer.text());
        }
        ioService.printLine("");
    }

}
