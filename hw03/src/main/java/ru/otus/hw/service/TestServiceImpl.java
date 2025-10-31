package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
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
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        int questionCount = questions.size();

        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printFormattedLineLocalized("TestService.test.info", questionCount);
        ioService.printLine("");

        ioAllQuestion(testResult, questions, questionCount);
        return testResult;
    }

    private void ioAllQuestion(TestResult testResult, List<Question> questions, int questionCount) {
        for (int questionNumber = 0; questionNumber < questionCount; questionNumber++) {
            var isAnswerValid = false;
            ioService.printFormattedLineLocalized("TestService.output.question", questionNumber + 1,
                    questions.get(questionNumber).text());


            List<Answer> answers = questions.get(questionNumber).answers();
            for (int answerNumber = 0; answerNumber < answers.size(); answerNumber++) {
                Answer answer = answers.get(answerNumber);
                ioService.printFormattedLineLocalized("TestService.output.answers", answerNumber + 1, answer.text());
            }

            int selectedAnswer = ioService.readIntForRangeWithPromptLocalized(1, answers.size(),
                    "TestService.input.number.answer",
                    "TestService.error.incorrect.number.answer");

            isAnswerValid = answers.get(selectedAnswer - 1).isCorrect();

            testResult.applyAnswer(questions.get(questionNumber), isAnswerValid);
        }
    }

}
