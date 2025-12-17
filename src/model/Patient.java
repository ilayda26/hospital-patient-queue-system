package model;

import model.enums.Priority;
import model.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class Patient {
    private String fullName;
    private final String ID;
    private int age;
    private String gender;
    private String phoneNumber;
    private String nationalId;
    private List<String> allergies;
    private List<String> symptoms;
    private Priority priority;
    private Status status;
    private double stressScore;
    private LocalDateTime arrivalTime;

    public Patient(String name, int age, String gender,
                   String phoneNumber, String nationalId,
                   List<String> symptoms, List<String> allergies) {
        this.ID = UUID.randomUUID().toString().substring(0, 8);
        this.fullName = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.nationalId = nationalId;
        this.allergies = allergies;
        this.symptoms = symptoms;
        this.priority = Priority.LOW;
        this.status = Status.WAITING;
        this.stressScore = 0.0;
        this.arrivalTime = LocalDateTime.now();
    }
    public String getID() {return ID;}
    public String getFullName() {return fullName;}
    public int getAge() {return age;}
    public String getGender() {return gender;}
    public String getPhoneNumber() {return phoneNumber;}
    public List<String> getAllergies() {return allergies;}
    public List<String> getSymptoms() {return symptoms;}
    public Priority getPriority() {return priority;}
    public void setPriority(Priority priority) {this.priority = priority;}
    public Status getStatus() {return status;}
    public void setStatus(Status status) {this.status = status;}
    public double getStressScore() {return stressScore;}
    public void setStressScore(double stressScore) {this.stressScore = stressScore;}
    public LocalDateTime getArrivalTime() {return arrivalTime;}

    public String primarySymptoms() {
        return String.join(", ", symptoms);
    }
    public String allergiesAsString() {
        return String.join(", ", allergies);
    }
    //MasK national id for display
    public String maskedNationalId() {
        if (nationalId == null) return "N/A";
        int len = nationalId.length();
        if (len <= 4)  return nationalId;
        return nationalId.substring(0, len-4);
    }
    public String toString() {
        return String.format("%s | %s | age:%d | gender:%s | phone:%s | ID:%s | priority:%s | status:%s | stress:%.2f | allergies:%s",
        ID, fullName, age, gender, phoneNumber == null ? "N/A" : phoneNumber,
                maskedNationalId(),priority, status, stressScore, allergiesAsString());
    }

}


