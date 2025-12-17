package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymptomMatcher {
    private final Map<String, String> keywordMap = new HashMap<>();

    public SymptomMatcher() {
        keywordMap.put("chest", "Cardiology");
        keywordMap.put("heart", "Cardiology");
        keywordMap.put("fever", "Internal");
        keywordMap.put("cough", "Internal");
        keywordMap.put("asthma", "Pulmonology");
        keywordMap.put("breath", "Pulmonology");
        keywordMap.put("fracture", "Orthopedics");
        keywordMap.put("broken", "Orthopedics");
        keywordMap.put("headache", "Neurology");
        keywordMap.put("dizzy", "Neurology");
        keywordMap.put("skin", "Dermatology");
        keywordMap.put("eczema", "Dermatology");
        keywordMap.put("stomach", "Gastroenterology");
        keywordMap.put("abdominal", "Gastroenterology");
    }
    public String matchSpecialization(List<String> symptoms) {
        Map<String, Integer> counts = new HashMap<>();
        for (String symptom : symptoms) {
            String s = symptom.toLowerCase();
            for (Map.Entry<String, String> e : keywordMap.entrySet()) {
                if (s.contains(e.getKey())) { //Checks if symptom contains the keyword
                    counts.put(e.getValue(), counts.getOrDefault(e.getValue(), 0) + 1);
                }   //Increases the specialization count
            }
        }
        //Chooses the specialization with the highest count
        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("General");
    }
}
