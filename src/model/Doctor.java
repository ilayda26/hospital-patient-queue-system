package model;

import model.enums.Status;

public class Doctor {
    private final String id;
    private String name;
    private String specialization;
    private int patientsTreatedCount;
    private boolean available;

    public Doctor(String id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.patientsTreatedCount = 0;
        this.available = true;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public int getPatientsTreatedCount() { return patientsTreatedCount; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    // Simulate treatment (instant). Updates patient status and doctor's counters.
    public void treat(Patient p) {
        this.available = false;
        p.setStatus(Status.IN_TREATMENT);
        // treatment simulated instantly
        p.setStatus(Status.COMPLETED);
        this.patientsTreatedCount++;
        this.available = true;
    }

    @Override
    public String toString() {
        return String.format("%s | Dr. %s | %s | treated:%d | available:%s",
                id, name, specialization, patientsTreatedCount, available);
    }
}

