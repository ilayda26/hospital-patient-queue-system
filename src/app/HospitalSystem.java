package app;

import model.Doctor;
import model.EmergencyPatient;
import model.NormalPatient;
import model.Patient;
import model.enums.Priority;
import service.QueueManager;
import service.StoryGenerator;
import service.StressCalculator;
import service.SymptomMatcher;
import util.BillingCalculator;
import util.TipGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HospitalSystem {
    private final Scanner scanner = new Scanner(System.in);
    private final QueueManager queueManager = new QueueManager();
    private final SymptomMatcher symptomMatcher = new SymptomMatcher();
    private final TipGenerator tipGenerator = new TipGenerator();
    private final List<Doctor> doctors = new ArrayList<>();

    public HospitalSystem() {
        // seed doctors
        doctors.add(new Doctor("D-01", "Smith", "Cardiology"));
        doctors.add(new Doctor("D-02", "Lee", "Internal"));
        doctors.add(new Doctor("D-03", "John", "Orthopedics"));
        doctors.add(new Doctor("D-04", "Maria", "General"));
        doctors.add(new Doctor("D-05", "Julie", "Pulmonology"));
        doctors.add(new Doctor("D-06", "Garcia", "Dermatology"));
        doctors.add(new Doctor("D-07", "Richard", "Gastroenterology"));
        doctors.add(new Doctor("D-08", "Emma", "Neurology"));
    }
    public void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addPatientFlow(); break;
                case "2": viewWaitingList(); break;
                case "3": assignDoctorFlow(); break;
                case "4": viewDoctors(); break;
                case "5": generateDailyReportAndSave(); break;
                case "6": running = false; break;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
        System.out.println("Exiting. Goodbye!");
    }

    private void printMainMenu() {
        System.out.println("\n=== Hospital Simulation Main Menu ===");
        System.out.println("1. Add New Patient");
        System.out.println("2. View Waiting List");
        System.out.println("3. Assign Doctor to Next Patient");
        System.out.println("4. View Doctors");
        System.out.println("5. Generate Daily Treatment Report (and save)");
        System.out.println("6. Exit");
        System.out.print("Please select: ");
    }

    private void addPatientFlow() {
        System.out.println("\nAdd patient type: 1) Emergency  2) Normal");
        String t = scanner.nextLine().trim();

        System.out.print("Full Name: "); String name = scanner.nextLine().trim();
        System.out.print("Age: "); int age = safeIntInput();

        System.out.print("Gender (1-Male, 2-Female, 3-Other, anything else for Unknown): ");
        String g = scanner.nextLine().trim();
        String gender;
        switch (g) {
            case "1": gender = "Male"; break;
            case "2": gender = "Female"; break;
            case "3": gender = "Other"; break;
            default:  gender = "Unknown";
        }

        System.out.print("Phone number: "); String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) phone = null;
        else if (!isValidPhone(phone)) {
            System.out.println("Warning: phone number looks invalid; it will be stored as entered.");
        }

        System.out.print("National ID: "); String nationalId = scanner.nextLine().trim();
        if (nationalId.isEmpty()) nationalId = "N/A";

        System.out.print("Enter symptoms (comma separated): ");
        String symLine = scanner.nextLine().trim();
        List<String> symptoms = Arrays.stream(symLine.split(","))
                .map(String::trim).filter(sy -> !sy.isEmpty()).collect(Collectors.toList());

        System.out.print("Allergies (comma separated, or 'none'): ");
        String allergyLine = scanner.nextLine().trim();
        List<String> allergies;
        if (allergyLine.equalsIgnoreCase("none") || allergyLine.isEmpty()) {
            allergies = new ArrayList<>();
        } else {
            allergies = Arrays.stream(allergyLine.split(","))
                    .map(String::trim).filter(a -> !a.isEmpty()).collect(Collectors.toList());
        }

        Patient p;
        if ("1".equals(t)) {
            System.out.print("Please enter short condition description: ");
            String cond = scanner.nextLine().trim();
            p = new EmergencyPatient(name, age, gender, phone, nationalId, symptoms, allergies, cond);
        } else {
            p = new NormalPatient(name, age, gender, phone, nationalId, symptoms, allergies);
        }

        // pain/anxiety -> stress -> priority
        System.out.print("Pain level (1-10): "); int pain = safeIntInputRange(1, 10);
        System.out.print("Anxiety level (1-10): "); int anxiety = safeIntInputRange(1, 10);
        double stress = StressCalculator.computeStressScore(pain, anxiety);
        p.setStressScore(stress);
        Priority derived = StressCalculator.derivePriorityFromStress(stress);

        // Symptom -> specialization suggestion
        String specialization = symptomMatcher.matchSpecialization(symptoms);
        System.out.printf("Suggested specialization based on symptoms: %s%n", specialization);

        // vulnerability index: age bonus
        if (p.getAge() > 65 || p.getAge() < 10) {
            if (derived == Priority.LOW) derived = Priority.MEDIUM;
            else if (derived == Priority.MEDIUM) derived = Priority.HIGH;
            System.out.println("Vulnerability index: age-based priority adjustment applied.");
        }

        p.setPriority(derived);
        queueManager.addPatient(p);

        System.out.printf("Patient added: %s | Priority: %s | Stress: %.2f%n",
                p.getFullName(), p.getPriority(), p.getStressScore());
    }

    private void viewWaitingList() {
        List<Patient> list = queueManager.listWaiting();
        if (list.isEmpty()) {
            System.out.println("The waiting list is empty.");
            return;
        }
        System.out.println("\n--- Waiting List ---");
        for (Patient p : list) {
            System.out.println(p);
        }
    }

    private void assignDoctorFlow() {
        Patient p = queueManager.getNextPatientAndEnqueue();
        if (p == null) {
            System.out.println("No patients in queue.");
            return;
        }
        System.out.println("Next patient selected: " + p.getFullName() + " | " + p.primarySymptoms());
        String specialization = symptomMatcher.matchSpecialization(p.getSymptoms());

        // find available doctor with matching specialization first
        Optional<Doctor> docOpt = doctors.stream()
                .filter(Doctor::isAvailable)
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .findFirst();

        if (!docOpt.isPresent()) {
            // fallback: any available doctor
            docOpt = doctors.stream().filter(Doctor::isAvailable).findFirst();
        }

        if (!docOpt.isPresent()) {
            System.out.println("No doctors available at the moment. Re-adding patient to queue.");
            queueManager.addPatient(p);
            return;
        }

        Doctor doc = docOpt.get();
        System.out.println("Assigned to Dr. " + doc.getName() + " (" + doc.getSpecialization() + ")");
        doc.treat(p);

        double bill = BillingCalculator.estimateBill(p);
        String tip = tipGenerator.randomTip();
        String story = StoryGenerator.generate(p, doc, bill, tip);
        System.out.println(story);
    }

    private void viewDoctors() {
        System.out.println("\n--- Doctors ---");
        for (Doctor d : doctors) {
            System.out.println(d);
        }
    }

    private void generateDailyReportAndSave() {
        List<Patient> treated = queueManager.listTreatedPatients();
        StringBuilder sb = new StringBuilder();

        sb.append("===== Daily Treatment Report =====\n");
        sb.append("Date: ").append(LocalDate.now()).append("\n");
        sb.append("Total patients treated: ").append(treated.size()).append("\n");

        long high = treated.stream().filter(pt -> pt.getPriority() == Priority.HIGH).count();
        long med = treated.stream().filter(pt -> pt.getPriority() == Priority.MEDIUM).count();
        long low = treated.stream().filter(pt -> pt.getPriority() == Priority.LOW).count();

        sb.append(String.format("High: %d | Medium: %d | Low: %d%n", high, med, low));
        sb.append("\n-- Patient Summaries --\n");

        for (Patient p : treated) {
            sb.append(String.format(
                    "%s | %s | age:%d | gender:%s | ID:%s | priority:%s | stress:%.2f | allergies:%s%n",
                    p.getID(), p.getFullName(), p.getAge(), p.getGender(),
                    p.maskedNationalId(), p.getPriority(),
                    p.getStressScore(), p.allergiesAsString()
            ));
        }

        sb.append("\n-- Doctor Summary --\n");
        for (Doctor d : doctors) {
            sb.append(String.format("Dr. %s (%s) treated: %d%n",
                    d.getName(), d.getSpecialization(), d.getPatientsTreatedCount()));
        }

        String filename = "daily_report_" + LocalDate.now() + ".txt";

        try {
            Files.writeString(Paths.get(filename), sb.toString(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Daily report saved to " + filename);

        } catch (IOException e) {
            System.out.println("Error saving report: " + e.getMessage());
        }
    }


    private int safeIntInput() {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException nfe) {
                System.out.print("Invalid number, try again: ");
            }
        }
    }

    private int safeIntInputRange(int min, int max) {
        while (true) {
            int v = safeIntInput();
            if (v >= min && v <= max) return v;
            System.out.printf("Please enter a number between %d and %d: ", min, max);
        }
    }

    private boolean isValidPhone(String phone) {
        String digits = phone.replaceAll("[^0-9]", "");
        return digits.length() >= 7 && digits.length() <= 15;
    }

    public static void main(String[] args) {
        HospitalSystem hs = new HospitalSystem();
        hs.run();
    }
}

