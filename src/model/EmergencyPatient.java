package model;

import java.util.List;

public class EmergencyPatient extends Patient {
    private String conditionDescription;

    public EmergencyPatient(String fullName, int age, String gender,
                            String phoneNumber, String nationalId,
                            List<String> symptoms, List<String> allergies,
                            String conditionDescription) {
        super(fullName, age, gender, phoneNumber, nationalId, symptoms, allergies);
        this.conditionDescription = conditionDescription;
    }
    public String getConditionDescription() {return conditionDescription;}
    public void setConditionDescription(String conditionDescription) {this.conditionDescription = conditionDescription;}

}
