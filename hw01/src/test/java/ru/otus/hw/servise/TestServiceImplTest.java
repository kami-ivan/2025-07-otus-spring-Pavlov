package ru.otus.hw.servise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.mockito.Mockito.*;

class TestServiceImplTest {
    private IOService ioService;
    private QuestionDao questionDao;
    private TestService testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void shouldPrintAllQuestionsAndAnswersCorrectly() {
        List<Question> questions = List.of(
                new Question("Is there life on Mars?", List.of(
                        new Answer("Science doesn't know this yet", true),
                        new Answer("Certainly. The red UFO is from Mars", false)
                )),
                new Question("How to load resources from jar?", List.of(
                        new Answer("ClassLoader#getResourceAsStream", true),
                        new Answer("Wingardium Leviosa", false)
                ))
        );

        when(questionDao.findAll()).thenReturn(questions);

        testService.executeTest();

        InOrder inOrder = inOrder(ioService);

        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printFormattedLine("Please answer the questions below%n");

        inOrder.verify(ioService).printFormattedLine("Question 1: Is there life on Mars?");
        inOrder.verify(ioService).printFormattedLine("  1. Science doesn't know this yet");
        inOrder.verify(ioService).printFormattedLine("  2. Certainly. The red UFO is from Mars");
        inOrder.verify(ioService).printLine("");

        inOrder.verify(ioService).printFormattedLine("Question 2: How to load resources from jar?");
        inOrder.verify(ioService).printFormattedLine("  1. ClassLoader#getResourceAsStream");
        inOrder.verify(ioService).printFormattedLine("  2. Wingardium Leviosa");
        inOrder.verify(ioService).printLine("");

        verify(questionDao, times(1)).findAll();
    }

    @Test
    void shouldHandleQuestionWithoutAnswers() {
        List<Question> questions = List.of(
                new Question("Empty question?", null)
        );

        when(questionDao.findAll()).thenReturn(questions);

        testService.executeTest();

        InOrder inOrder = inOrder(ioService);
        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printFormattedLine("Please answer the questions below%n");

        inOrder.verify(ioService).printFormattedLine("Question 1: Empty question?");
        inOrder.verify(ioService).printLine("  No answers available");
        inOrder.verify(ioService).printLine("");

        verify(questionDao, times(1)).findAll();
    }

    @Test
    void shouldPrintNothingWhenNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());

        testService.executeTest();

        InOrder inOrder = inOrder(ioService);
        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printFormattedLine("Please answer the questions below%n");

        verifyNoMoreInteractions(ioService);
        verify(questionDao, times(1)).findAll();
    }
}