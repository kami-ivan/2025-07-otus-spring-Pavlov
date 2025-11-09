/*
package ru.otus.hw.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.script.enabled=false"
})
class ApplicationCommandsTest {

    @Autowired
    private Shell shell;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private TestService testService;

    @MockitoBean
    private ResultService resultService;

    @Test
    @DisplayName("Должен выполнить команду test")
    void shouldExecuteTestCommand() {
        Student student = new Student("Ivan", "Ivanov");
        TestResult testResult = new TestResult(student);

        when(studentService.determineCurrentStudent()).thenReturn(student);
        when(testService.executeTestFor(student)).thenReturn(testResult);

        Object result = shell.evaluate(() -> "test");

        assertThat(result).isEqualTo("Testing completed for student: Ivan Ivanov");
        verify(studentService).determineCurrentStudent();
        verify(testService).executeTestFor(student);
        verify(resultService).showResult(testResult);
    }

    @Test
    @DisplayName("Должен выполнить команду test с параметрами")
    void shouldExecuteTestCommandWithParameters() {
        Student student = new Student("Petr", "Petrov");
        TestResult testResult = new TestResult(student);

        when(testService.executeTestFor(student)).thenReturn(testResult);

        Object result = shell.evaluate(() -> "test --firstname Petr --lastname Petrov");

        assertThat(result).isEqualTo("Testing completed for student: Petr Petrov");
        verify(testService).executeTestFor(student);
        verify(resultService).showResult(testResult);
    }

    @Test
    @DisplayName("Должен выполнить команду info")
    void shouldExecuteInfoCommand() {
        Object result = shell.evaluate(() -> "info");

        assertThat(result).isEqualTo("Student Testing Application. Use 'test' command to start testing.");
    }
}*/
