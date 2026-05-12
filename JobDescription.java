import java.util.*;

public class JobDescription {
    String title;
    String content;

    public JobDescription(String title, String content) {
        this.title = title;
        this.content = content.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
    }

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
