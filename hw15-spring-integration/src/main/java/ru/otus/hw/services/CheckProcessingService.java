package ru.otus.hw.services;

import org.springframework.messaging.Message;
import ru.otus.hw.models.Item;
import ru.otus.hw.models.FinalCheck;

import java.util.List;

public interface CheckProcessingService {

    Item processItem(Item item);

    FinalCheck processCheck(Message<List<Item>> message);
}