package service;

import model.Patient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QueueManager {
    private final List<Patient> waitingList = new ArrayList<>();
    private final List<Patient> treatedPatients = new ArrayList<>();

    public void addPatient(Patient patient) {
        waitingList.add(patient);
    }
    //Returns a copy of the waiting list for display.
    public List<Patient> listWaiting() {
        return new ArrayList<>(waitingList);
    }
    // Selects the next patient based on:
    // 1) Priority (HIGH → MEDIUM → LOW)
    // 2) Stress score (higher first)
    // 3) Arrival time (earlier first)
    //Removes the patient from the waiting list and moves them to treatedPatients.
    public Patient getNextPatientAndEnqueue() {
        if (waitingList.isEmpty()) {
            return null;
        }
        // Sort by priority ordinal (lower ordinal = higher priority)
        waitingList.sort(Comparator
                .comparingInt((Patient p) -> p.getPriority().ordinal())
                .thenComparing(Comparator.comparingDouble(Patient::getStressScore).reversed())
                .thenComparing(Patient::getArrivalTime));
        Patient next = waitingList.remove(0);
        treatedPatients.add(next);
        return next;
    }
    public List<Patient> listTreatedPatients() {
        return new ArrayList<>(treatedPatients);
    }


}
