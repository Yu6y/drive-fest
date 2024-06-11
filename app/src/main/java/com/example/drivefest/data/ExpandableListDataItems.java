package com.example.drivefest.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<String>> getData(){
        HashMap<String, List<String>> expandableList = new HashMap<>();
        List<String> voivodeships = new ArrayList<>();

        voivodeships.add("Dolnośląskie");
        voivodeships.add("Kujawsko-Pomorskie");
        voivodeships.add("Lubelskie");
        voivodeships.add("Lubuskie");
        voivodeships.add("Łódzkie");
        voivodeships.add("Małopolskie");
        voivodeships.add("Mazowieckie");
        voivodeships.add("Opolskie");
        voivodeships.add("Podkarpackie");
        voivodeships.add("Podlaskie");
        voivodeships.add("Pomorskie");
        voivodeships.add("Śląskie");
        voivodeships.add("Świętokrzyskie");
        voivodeships.add("Warmińsko-Mazurskie");
        voivodeships.add("Wielkopolskie");
        voivodeships.add("Zachodniopomorskie");

        List<String> tags = new ArrayList<>();

        tags.add("drift");
        tags.add("moto");
        tags.add("zlot");
        tags.add("luxury");
        tags.add("klasyk");
        tags.add("detailing");
        tags.add("tuning");
        tags.add("vag");
        tags.add("japan");
        tags.add("usa");

        expandableList.put("Województwo", voivodeships);
        expandableList.put("Tagi", tags);

        return expandableList;
    }
}
