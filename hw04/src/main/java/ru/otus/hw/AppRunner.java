package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.shell.ApplicationCommands;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(ApplicationCommands.class)
public class AppRunner implements ApplicationRunner {

    private final TestRunnerService testRunnerService;

    @Override
    public void run(ApplicationArguments args) {
        testRunnerService.run();
    }
}
