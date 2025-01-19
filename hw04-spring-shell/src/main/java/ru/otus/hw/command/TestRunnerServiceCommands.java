package ru.otus.hw.command;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestRunnerServiceCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Registration command", key = {"r", "reg"})
    public void register() {
        testRunnerService.register();
    }

    @ShellMethod(value = "Command to start the testing process", key = {"start", "s"})
    public void start() {
        testRunnerService.run();
    }
}
