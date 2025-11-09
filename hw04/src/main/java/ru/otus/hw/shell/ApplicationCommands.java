package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.shell.interactive.enabled", havingValue = "true")
public class ApplicationCommands {

    private final LocalizedIOService ioService;

    private final TestRunnerService testRunnerService;

    @ShellMethod(key = "test", value = "Run student testing")
    @ShellMethodAvailability(value = "isLaunchCommandAvailable")
    public void runTesting() {
        testRunnerService.run();
        ioService.printLineLocalized("ShellCommandHandler.test.run.completed");
    }

    @ShellMethod(key = "info", value = "Show application information")
    public void showInfo() {
        ioService.printLineLocalized("ShellCommandHandler.test.info");
    }
}