package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (InputStream file = getInputStream()) {
            return readQuestionsFromCsv(file);
        } catch (IOException e) {
            throw new QuestionReadException("Resource file reading error", e);
        }
    }

    private List<Question> readQuestionsFromCsv(InputStream inputStream) {
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {
            List<QuestionDto> questionList = buildCsvToBean(bufferedReader).parse();
            if (questionList.isEmpty()) {
                throw new QuestionReadException("Resource file is empty error");
            }
            return questionList.stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuestionReadException("CSV file parsing error", e);
        }
    }

    private CsvToBean<QuestionDto> buildCsvToBean(BufferedReader reader) {
        return new CsvToBeanBuilder<QuestionDto>(reader)
                .withSkipLines(1)
                .withSeparator(';')
                .withType(QuestionDto.class)
                .build();
    }

    private InputStream getInputStream() {

        System.out.println(fileNameProvider.getTestFileName());

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName());
        if (inputStream == null) {
            throw new QuestionReadException(String.format("File not found error: %s",
                    fileNameProvider.getTestFileName()));
        } else {
            return inputStream;
        }
    }
}
