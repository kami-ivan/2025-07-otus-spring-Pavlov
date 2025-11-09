package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService /*, CommandLineRunner */ {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run() {
        Student student = studentService.determineCurrentStudent();
        TestResult testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }

/*
    @Override
    public void run(String... args) {
        run();
    }
*/
}
