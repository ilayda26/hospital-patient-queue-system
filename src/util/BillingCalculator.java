package util;

import model.Patient;
import model.enums.Priority;

public class BillingCalculator {
    private static final double CONSULTATION_FEE = 20.0;
    private static final double EMERGENCY_FEE = 25.0;

    public static double estimateBill(Patient p) {
        double bill = CONSULTATION_FEE;
        Priority pr = p.getPriority();
        if (pr == Priority.MEDIUM) bill += 10.0;
        if (pr == Priority.HIGH) bill += 15.0 + EMERGENCY_FEE;
        if (p.getAge() > 65) bill += 5.0; // small senior care add-on
        // Minor processing fee for allergies
        if (p.getAllergies() != null && !p.getAllergies().isEmpty()) bill += 3.0;
        return bill;
    }
}
