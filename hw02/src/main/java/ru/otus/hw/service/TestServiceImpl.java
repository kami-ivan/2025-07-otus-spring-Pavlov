package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        int questionNumber = 1;

        ioService.printLine("13 questions with 4 answer options, but only one is correct. " +
                "After each question, enter the number of the correct answer.\n");

        for (var question : questions) {
            var isAnswerValid = false; // Задать вопрос, получить ответ
            ioService.printFormattedLine("Question %d: %s", questionNumber, question.text());

            List<Answer> answers = question.answers();
            if (!CollectionUtils.isEmpty(answers)) {
                for (int answerNumber = 0; answerNumber < answers.size(); answerNumber++) {
                    Answer answer = answers.get(answerNumber);
                    ioService.printFormattedLine("  %d. %s", answerNumber + 1, answer.text());
                }
            } else {
                ioService.printLine("  No answers available");
            }

            int selectedAnswer = ioService.readIntForRangeWithPrompt(1, 4,
                    "Enter the correct answer number",
                    "There is no such answer, so enter it again.");

            isAnswerValid = answers.get(selectedAnswer - 1).isCorrect();

            testResult.applyAnswer(question, isAnswerValid);
            questionNumber++;
        }
        return testResult;
    }
}
