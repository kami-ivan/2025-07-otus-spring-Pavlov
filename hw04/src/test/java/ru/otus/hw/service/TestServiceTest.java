package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TestServiceTest {

    @MockitoBean
    private QuestionDao questionDao;

    @MockitoBean
    private LocalizedIOService ioService;

    @Autowired
    private TestService testService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;


    private List<Question> questions;

    private Student student;

    private TestResult expextedTestResult;

    @BeforeEach
    void setUp() {
        questions = List.of(
                new Question("Question 1", List.of(
                        new Answer("Answer 1-1", false),
                        new Answer("Answer 1-2", true))),
                new Question("Question 2", List.of(
                        new Answer("Answer 2-1", false),
                        new Answer("Answer 2-2", true),
                        new Answer("Answer 2-3", false))));
        student = new Student("Ivan", "Ivanov");
        expextedTestResult = new TestResult(student);
        expextedTestResult.setRightAnswersCount(2);
    }

    @Test
    @DisplayName("Должен корректно выполнить тест и вернуть результат")
    void shouldExecuteTestAndReturnResult() {
        given(questionDao.findAll()).willReturn(questions);
        given(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString())).willReturn(2);

        willDoNothing().given(ioService).printLine(stringArgumentCaptor.capture());

        TestResult actualTestResult = testService.executeTestFor(student);

        assertEquals(questionDao.findAll().size(), actualTestResult.getAnsweredQuestions().size());
        assertEquals(expextedTestResult.getRightAnswersCount(), actualTestResult.getRightAnswersCount());

        verify(questionDao, times(2)).findAll();
        verify(ioService, times(2)).printLine(any(String.class));
        verify(ioService, times(2)).readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString());

        assertThat(actualTestResult.getAnsweredQuestions()).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(questions);

        assertThat(actualTestResult).isNotNull().usingRecursiveComparison().ignoringFields("answeredQuestions").isEqualTo(expextedTestResult);
    }

    @Test
    @DisplayName("Должен ничего не выводить когда вопросов нет")
    void shouldPrintNothingWhenNoQuestions() {
        given(questionDao.findAll()).willReturn(List.of());

        testService.executeTestFor(student);

        verify(ioService, times(6)).printLine("");
        verify(ioService, times(2)).printLineLocalized("TestService.answer.the.questions");
        verify(ioService, times(2)).printFormattedLineLocalized("TestService.test.info", 0);

        verify(questionDao, times(2)).findAll();
    }
}
