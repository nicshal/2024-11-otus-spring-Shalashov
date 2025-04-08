package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalCheck {

    private Long checkId;

    private List<Item> items;

    private String identificationNumber;

    @Override
    public String toString() {
        return "FinalCheck{" +
                "checkId=" + checkId +
                ", items=" + items +
                '}';
    }
}