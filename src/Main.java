import java.util.*;

public class Main {

    // Simple stopwords list
    public static Set<String> stopwords = new HashSet<>(Arrays.asList(
            "the","is","and","a","an","of","to","in","for","with","on","by"
    ));

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Step 1: Input Job Description
        System.out.println("Enter Job Description:");
        String jdContent = sc.nextLine();
        JobDescription jd = new JobDescription("JD", jdContent);

        // Step 2: Input Resumes
        List<Resume> resumes = new ArrayList<>();
        System.out.println("How many resumes do you want to enter?");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < n; i++) {
            System.out.println("Enter Resume Name:");
            String name = sc.nextLine();
            System.out.println("Enter Resume Content:");
            String content = sc.nextLine();
            resumes.add(new Resume(name, content));
        }

        // Step 3: Compute similarity
        List<String> jdTokens = jd.getTokens(stopwords);
        for (Resume r : resumes) {
            List<String> resTokens = r.getTokens(stopwords);
            r.score = cosineSimilarity(jdTokens, resTokens);

            List<String> missing = CareerAnalyzer.findMissingSkills(jdTokens, resTokens);
    List<String> questions = CareerAnalyzer.generateQuestions(jdTokens, resTokens);

    System.out.println("\nAnalysis for " + r.name);

    System.out.println("Missing Skills: " + missing);

    System.out.println("Interview Questions:");
    for (String q : questions) {
        System.out.println("- " + q);
    }
        }

        // Step 4: Sort resumes by score
        resumes.sort((r1, r2) -> Double.compare(r2.score, r1.score));

        // Step 5: Display results
        System.out.println("\nRanked Resumes:");
        for (Resume r : resumes) {
            System.out.printf("%s → %.2f%% match\n", r.name, r.score * 100);
        }
        CloudSimulation.simulate(resumes.size());
    }

    // Compute cosine similarity using Bag-of-Words
    public static double cosineSimilarity(List<String> tokens1, List<String> tokens2) {
        Set<String> allWords = new HashSet<>();
        allWords.addAll(tokens1);
        allWords.addAll(tokens2);

        Map<String, Integer> freq1 = new HashMap<>();
        Map<String, Integer> freq2 = new HashMap<>();

        for (String w : allWords) {
            freq1.put(w, 0);
            freq2.put(w, 0);
        }
        for (String w : tokens1) freq1.put(w, freq1.get(w) + 1);
        for (String w : tokens2) freq2.put(w, freq2.get(w) + 1);

        // Dot product and norms
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (String w : allWords) {
            int f1 = freq1.get(w);
            int f2 = freq2.get(w);
            dot += f1 * f2;
            normA += f1 * f1;
            normB += f2 * f2;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
