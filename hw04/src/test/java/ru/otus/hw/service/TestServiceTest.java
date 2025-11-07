package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<Integer> intCaptor;

    private Student student;
    private List<Question> questions;

    @BeforeEach
    void setUp() {
        student = new Student("Test", "Student");
        questions = List.of(
                new Question("Question 1", List.of(
                        new Answer("Answer 1-1", true),
                        new Answer("Answer 1-2", false)
                )),
                new Question("Question 2", List.of(
                        new Answer("Answer 2-1", false),
                        new Answer("Answer 2-2", true),
                        new Answer("Answer 2-3", false)
                ))
        );
    }

    @Test
    @DisplayName("Должен корректно выполнить тест и вернуть результат")
    void shouldExecuteTestAndReturnResult() {
        when(questionDao.findAll()).thenReturn(questions);
        when(ioService.readIntForRangeWithPromptLocalized(
                anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1, 3);


        TestResult result = testService.executeTestFor(student);

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);
        assertThat(result.getAnsweredQuestions()).hasSize(2);

        verify(questionDao).findAll();
        verify(ioService, times(2)).printLine("");
        verify(ioService).printLineLocalized("TestService.answer.the.questions");
        verify(ioService).printFormattedLineLocalized("TestService.test.info", 2);
        verify(ioService, times(2)).printFormattedLineLocalized(eq("TestService.output.question"),
                intCaptor.capture(), stringCaptor.capture());

        assertThat(intCaptor.getAllValues()).containsExactly(1, 2);
        assertThat(stringCaptor.getAllValues()).containsExactly("Question 1", "Question 2");
    }

    @Test
    @DisplayName("Должен обработать все вопросы")
    void shouldProcessAllQuestions() {
        when(questionDao.findAll()).thenReturn(questions);
        when(ioService.readIntForRangeWithPromptLocalized(
                anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1, 2);

        TestResult result = testService.executeTestFor(student);

        verify(ioService, times(5)).printFormattedLineLocalized(
                eq("TestService.output.answers"),
                intCaptor.capture(),
                stringCaptor.capture());

        assertThat(stringCaptor.getAllValues())
                .containsExactly("Answer 1-1", "Answer 1-2", "Answer 2-1", "Answer 2-2", "Answer 2-3");
    }

    @Test
    @DisplayName("Должен ничего не выводить когда вопросов нет")
    void shouldPrintNothingWhenNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());

        testService.executeTestFor(student);

        verify(ioService, times(2)).printLine("");
        verify(ioService).printLineLocalized("TestService.answer.the.questions");
        verify(ioService).printFormattedLineLocalized("TestService.test.info", 0);


        verifyNoMoreInteractions(ioService);
        verify(questionDao, times(1)).findAll();
    }
}