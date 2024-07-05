package com.example.drivefest.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    private static List<String> voivodeships = new ArrayList<>();
    public static HashMap<String, List<String>> getEventData(){
        HashMap<String, List<String>> expandableList = new HashMap<>();

        setVoivodeships();

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
    private static void setVoivodeships(){
        voivodeships.clear();
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
    }

    public static HashMap<String, List<String>> getWorkshopData(){
        HashMap<String, List<String>> expandableList = new HashMap<>();

        setVoivodeships();

        List<String> tags = new ArrayList<>();

        tags.add("diagnostyka");
        tags.add("elektryka");
        tags.add("detailing");
        tags.add("classic");
        tags.add("tuning mechaniczny");
        tags.add("tuning wizualny");
        tags.add("chip tuning");
        tags.add("blacharka");
        tags.add("lakiernia");
        tags.add("myjnia");

        expandableList.put("Województwo", voivodeships);
        expandableList.put("Tagi", tags);

        return expandableList;
    }
}
