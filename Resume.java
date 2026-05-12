import java.util.*;

public class Resume {
    String name;
    String content;
    double score;

    public Resume(String name, String content) {
        this.name = name;
        this.content = content.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
        this.score = 0.0;
    }

    // Tokenize and remove stopwords
    public List<String> getTokens(Set<String> stopwords) {
        String[] tokens = content.split("\\s+");
        List<String> words = new ArrayList<>();
        for (String token : tokens) {
            if (!stopwords.contains(token)) {
                words.add(token);
            }
        }
        return words;
    }
}
