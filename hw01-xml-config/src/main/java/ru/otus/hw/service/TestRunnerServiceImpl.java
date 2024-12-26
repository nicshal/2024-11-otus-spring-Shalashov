package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final IOService ioService;
    private final TestService testService;

    @Override
    public void run() {
        try {
            testService.executeTest();
        } catch (QuestionReadException e) {
            ioService.printFormattedLine("Error while generating list of questions. Check if file with questions is present and correct in application resources");
        } catch (Exception e) {
            ioService.printFormattedLine("An unexpected application error occurred. Please contact technical support.");
        }
    }
}
