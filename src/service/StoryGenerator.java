package service;

import model.Doctor;
import model.Patient;

public class StoryGenerator {
    public static String generate(Patient p, Doctor d, double bill, String tip) {
        String sb = "----- Patient Treatment Summary -----\n" +
                "Patient: " + p.getFullName() +
                " | Age: " + p.getAge() +
                " | Gender: " + p.getGender() + "\n" +
                "Contact: " + (p.getPhoneNumber() == null ? "N/A" : p.getPhoneNumber()) +
                " | ID: " + p.maskedNationalId() + "\n" +
                "Primary symptoms: " + p.primarySymptoms() + "\n" +
                "Allergies: " + p.allergiesAsString() + "\n" +
                "Assigned Doctor: Dr. " + d.getName() +
                " (" + d.getSpecialization() + ")\n" +
                String.format("Stress Score: %.2f (%s)%n", p.getStressScore(), p.getPriority()) +
                String.format("Estimated Bill: %.2f €%n", bill) +
                "Medical Tip: " + tip + "\n" +
                "-------------------------------------\n";
        return sb;
    }
}
