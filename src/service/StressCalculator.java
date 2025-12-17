package service;
import model.enums.Priority;


public class StressCalculator {
    public static double computeStressScore(int pain, int anxiety) {
        if (pain < 0) pain = 0;
        if (anxiety < 0) anxiety = 0;
        if (pain > 10) pain = 10;
        if (anxiety > 10) anxiety = 10;

        return (pain * 0.7) + (anxiety * 0.3);
    }

    public static Priority derivePriorityFromStress(double score) {
        if (score >= 7.5) return Priority.HIGH;
        if (score >= 4.5) return Priority.MEDIUM;
        return Priority.LOW;
    }
}
