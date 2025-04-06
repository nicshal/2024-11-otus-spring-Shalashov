package ru.otus.hw.services;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Item;
import ru.otus.hw.models.FinalCheck;
import ru.otus.hw.models.ProductCategory;

import java.util.List;
import java.util.UUID;

@Service
public class CheckProcessingServiceImpl implements CheckProcessingService {

    public Item processItem(Item item) {
        var discount = item.getCategory() == ProductCategory.FOOD ? 0.5 : 1;
        item.setPrice(item.getPrice() * discount);
        return item;
    }

    public FinalCheck processCheck(Message<List<Item>> message) {
        List<Item> items = message.getPayload();
        Long id = (Long) message.getHeaders().get("checkId");
        return new FinalCheck(id, items, UUID.randomUUID().toString());
    }
}