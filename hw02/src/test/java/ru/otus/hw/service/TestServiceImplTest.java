package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.Mockito.*;

class TestServiceImplTest {
    private IOService ioService;
    private QuestionDao questionDao;
    private TestService testService;
    Student student = new Student("Ivan", "Ivanov");

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
        when(ioService.readIntForRangeWithPrompt(1, 2,
                "Enter the correct answer number",
                "There is no such answer, so enter it again.")).thenReturn(1);

        testService.executeTestFor(student);

        InOrder inOrder = inOrder(ioService);

        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printFormattedLine("Please answer the questions below%n");
        inOrder.verify(ioService).printLine(2 + " questions with one correct answer. " +
                "After each question, enter the number of the correct answer.\n");

        inOrder.verify(ioService).printFormattedLine("Question %d: %s", 1, questions.get(0).text());
        inOrder.verify(ioService).printFormattedLine("  %d. %s", 1, "Science doesn't know this yet");
        inOrder.verify(ioService).printFormattedLine("  %d. %s", 2, "Certainly. The red UFO is from Mars");
        inOrder.verify(ioService).readIntForRangeWithPrompt(1, 2,
                "Enter the correct answer number",
                "There is no such answer, so enter it again.");


        inOrder.verify(ioService).printFormattedLine("Question %d: %s", 2, questions.get(1).text());
        inOrder.verify(ioService).printFormattedLine("  %d. %s", 1, "ClassLoader#getResourceAsStream");
        inOrder.verify(ioService).printFormattedLine("  %d. %s", 2, "Wingardium Leviosa");
        inOrder.verify(ioService).readIntForRangeWithPrompt(1, 2,
                "Enter the correct answer number",
                "There is no such answer, so enter it again.");

        verify(questionDao, times(1)).findAll();
    }

    @Test
    void shouldHandleQuestionWithoutAnswers() {
        List<Question> questions = List.of(
                new Question("Empty question?", null)
        );

        when(questionDao.findAll()).thenReturn(questions);

        testService.executeTestFor(student);

        InOrder inOrder = inOrder(ioService);

        inOrder.verify(ioService).printLine("Error");
        inOrder.verify(ioService).printLine("  Question 1: No answers available.");

        verify(questionDao, times(1)).findAll();
    }

    @Test
    void shouldPrintNothingWhenNoQuestions() {
        when(questionDao.findAll()).thenReturn(List.of());

        testService.executeTestFor(student);

        InOrder inOrder = inOrder(ioService);
        inOrder.verify(ioService).printLine("");
        inOrder.verify(ioService).printFormattedLine("Please answer the questions below%n");
        inOrder.verify(ioService).printLine("0 questions with one correct answer. " +
                "After each question, enter the number of the correct answer.\n");

        verifyNoMoreInteractions(ioService);
        verify(questionDao, times(1)).findAll();
    }
}