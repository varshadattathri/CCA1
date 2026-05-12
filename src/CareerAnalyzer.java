import java.util.*;

public class CareerAnalyzer {

    // Find missing skills
    public static List<String> findMissingSkills(List<String> jd, List<String> resume) {
        List<String> missing = new ArrayList<>();
        for (String skill : jd) {
            if (!resume.contains(skill)) {
                missing.add(skill);
            }
        }
        return missing;
    }

    // Generate interview questions
    public static List<String> generateQuestions(List<String> jd, List<String> resume) {
        List<String> questions = new ArrayList<>();

        for (String skill : jd) {
            if (resume.contains(skill)) {
                questions.add("Explain your experience in " + skill);
            } else {
                questions.add("What do you know about " + skill + "?");
            }
        }

        return questions;
    }
}
