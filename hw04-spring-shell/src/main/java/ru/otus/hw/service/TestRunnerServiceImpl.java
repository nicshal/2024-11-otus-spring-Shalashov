package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;

    private Student student;

    @Override
    public void register() {
        this.student = studentService.determineCurrentStudent();
    }

    @Override
    public void run() {
        if (student != null) {
            try {
                var testResult = testService.executeTestFor(student);
                resultService.showResult(testResult);
            } catch (QuestionReadException e) {
                ioService.printLineLocalized("TestRunnerService.error.questions");
            } catch (Exception e) {
                ioService.printLineLocalized("TestRunnerService.unexpected.application.error");
            }
        } else {
            ioService.printLineLocalized("TestRunnerService.student.not.registered");
        }
    }
}