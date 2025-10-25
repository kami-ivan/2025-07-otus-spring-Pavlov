package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        int questionCount = questions.size();


        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        ioService.printLine(questionCount + " questions with one correct answer. " +
                "After each question, enter the number of the correct answer.\n");

        for (int questionNumber = 0; questionNumber < questionCount; questionNumber++) {
            var isAnswerValid = false;
            ioService.printFormattedLine("Question %d: %s", questionNumber + 1,
                    questions.get(questionNumber).text());

            List<Answer> answers = questions.get(questionNumber).answers();
            for (int answerNumber = 0; answerNumber < answers.size(); answerNumber++) {
                Answer answer = answers.get(answerNumber);
                ioService.printFormattedLine("  %d. %s", answerNumber + 1, answer.text());
            }

            int selectedAnswer = ioService.readIntForRangeWithPrompt(1, answers.size(),
                    "Enter the correct answer number",
                    "There is no such answer, so enter it again.");

            isAnswerValid = answers.get(selectedAnswer - 1).isCorrect();

            testResult.applyAnswer(questions.get(questionNumber), isAnswerValid);
        }
        return testResult;
    }

}
