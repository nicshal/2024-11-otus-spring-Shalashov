package ru.otus.hw.config;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    private int rightAnswersCountToPass;

    private String testFileName;

    public AppProperties(@Value("${test.fileName}") String testFileName,
                         @Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass
    ) {
        this.testFileName = testFileName;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
    }
}
