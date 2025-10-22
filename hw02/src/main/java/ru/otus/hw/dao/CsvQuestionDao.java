package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;


    @Override
    public List<Question> findAll() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
             Reader reader = new InputStreamReader(Objects.requireNonNull(is))) {

            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Question> questions = csvToBean.parse().stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
            if (isFullFilledQuestions(questions)) {
                return questions;
            } else {
                throw new QuestionReadException("There are no filled questions");
            }

        } catch (Exception e) {
            throw new QuestionReadException("Error reading questions file", e);
        }
    }

    private boolean isFullFilledQuestions(List<Question> questions) {
        for (Question question : questions) {
            List<Answer> answers = question.answers();
            if (CollectionUtils.isEmpty(answers)) {
                return false;
            }
        }
        return true;
    }
}
