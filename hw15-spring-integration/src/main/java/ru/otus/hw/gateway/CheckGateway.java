package ru.otus.hw.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.models.Check;
import ru.otus.hw.models.FinalCheck;

@MessagingGateway
public interface CheckGateway {
    @Gateway(requestChannel = "inputChannel", replyChannel = "outputChannel")
    FinalCheck process(Check check);
}