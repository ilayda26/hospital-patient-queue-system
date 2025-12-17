package model;

import java.util.List;

public class NormalPatient extends Patient {
    public NormalPatient(String fullName, int age, String gender,
                         String phoneNumber, String nationalId,
                         List<String> symptoms, List<String> allergies) {
        super(fullName, age, gender, phoneNumber, nationalId, symptoms, allergies);

    }

}
