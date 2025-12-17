package util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TipGenerator {
    private final List<String> tips = Arrays.asList(
            "Stay hydrated and rest.",
            "If symptoms worsen, seek immediate care.",
            "Avoid heavy physical activity for the next 48 hours.",
            "Follow up with your primary care physician if symptoms persist.",
            "Take prescribed medications as directed.",
            "Keep a symptom diary for your next visit."
    );
    private final Random random = new Random();

    public String randomTip() {
        return tips.get(random.nextInt(tips.size()));
    }

}