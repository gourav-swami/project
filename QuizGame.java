import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class QuizGame {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        // OpenTriviaDB API
        URL url = new URL("https://opentdb.com/api.php?amount=1&type=multiple");
        URLConnection connection = url.openConnection();

        // Use Scanner directly on InputStream
        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder json = new StringBuilder();

        while (scanner.hasNext()) {
            json.append(scanner.nextLine());
        }
        scanner.close();

        String data = json.toString();

        // Extract question & answers
        String question = extract(data, "\"question\":\"", "\"");
        String correctAnswer = extract(data, "\"correct_answer\":\"", "\"");
        List<String> choices = extractList(data, "\"incorrect_answers\":[", "]");

        choices.add(correctAnswer);
        Collections.shuffle(choices);

        System.out.println("Question:");
        System.out.println(question);
        System.out.println();

        for (int i = 0; i < choices.size(); i++) {
            System.out.println((i + 1) + ". " + choices.get(i));
        }

        System.out.print("\nYour choice (1-4): ");
        int ans = input.nextInt();

        if (choices.get(ans - 1).equals(correctAnswer)) {
            System.out.println("✅ Correct!");
        } else {
            System.out.println("❌ Wrong! Correct answer: " + correctAnswer);
        }

        input.close();
    }

    // Extract single value
    public static String extract(String json, String startKey, String endChar) {
        int start = json.indexOf(startKey) + startKey.length();
        int end = json.indexOf(endChar, start);
        return json.substring(start, end);
    }

    // Extract list of incorrect answers
    public static List<String> extractList(String json, String startKey, String endChar) {
        int start = json.indexOf(startKey) + startKey.length();
        int end = json.indexOf(endChar, start);
        String array = json.substring(start, end);

        String[] items = array.split(",");
        List<String> list = new ArrayList<>();
        for (String item : items) {
            list.add(item.trim().replace("\"", ""));
        }
        return list;
    }
}