package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.models.Item;
import ru.otus.hw.models.Check;
import ru.otus.hw.services.CheckProcessingService;

import java.util.Objects;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    //Обрабатывается чек с несколькими товарами.
    //К товарам определенной категории применяется скидка
    //Формируется окончательный чек, чеку присваивается идентификационный номер
    @Bean
    public IntegrationFlow checkFlow(CheckProcessingService checkProcessingService) {
        return IntegrationFlow.from("inputChannel")
                .enrichHeaders(header -> header
                        .headerFunction("checkId", m -> ((Check) m.getPayload()).getId())
                )
                .log()
                .split(Check.class, Check::getItems)
                .transform(Item.class, checkProcessingService::processItem)
                .log()
                .aggregate(aggregator -> aggregator
                        .correlationStrategy(m -> m.getHeaders().get("checkId"))
                        .releaseStrategy(group -> Objects.equals(group.size(), group.getSequenceSize()))
                        .expireGroupsUponCompletion(true)
                )
                .handle(checkProcessingService, "processCheck")
                .log()
                .channel("outputChannel")
                .get();
    }
}