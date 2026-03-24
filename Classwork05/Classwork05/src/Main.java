import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

class FileTranslator {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // read OpenAIToken environment variable
        String apiToken = System.getenv("OpenAIToken");
        if (apiToken == null || apiToken.isBlank()) {
            System.out.println("Error: 'OpenAIToken' environment variable not defined.");
            return;
        }
        System.out.println("OpenAIToken found.");

        // ask file path
        System.out.print("file path .txt to Translate: ");
        String inputPath = scanner.nextLine().trim();

        // read the content of file if not error
        String content = Files.readString(Path.of(inputPath));
        if (content.isBlank()) {
            System.out.println("empty file");
            return;
        }

        // request the language to translate it
        System.out.print("language to translate? ( Spanish, French, Japanese): ");
        String targetLang = scanner.nextLine().trim();

        // build JSON body for Groq API
        String jsonBody = """ 
                {
                  "model": "llama-3.3-70b-versatile",
                  "messages": [
                    {
                      "role": "system",
                      "content": "You are a professional translator. Translate the user's text to %s. Return ONLY the translated text, no explanations."
                    },
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(targetLang, content.replace("\"", "\\\"").replace("\n", "\\n"));

        // write JSON to temp file
        File tempJson = File.createTempFile("groq_body", ".json");
        Files.writeString(tempJson.toPath(), jsonBody);
        tempJson.deleteOnExit();

        // Run curl with ProcessBuilder
        ProcessBuilder pb = new ProcessBuilder(
                "curl", "-s",
                "-X", "POST",
                "https://api.groq.com/openai/v1/chat/completions",
                "-H", "Content-Type: application/json",
                "-H", "Authorization: Bearer " + apiToken,
                "-d", "@" + tempJson.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String response = new String(process.getInputStream().readAllBytes());
        process.waitFor();

        // Translate of JSON
        String translated = extractTranslation(response);
        if (translated == null) {
            System.out.println("Error Translate");
            System.out.println(response);
            return;
        }

        // Save the file translate in .txt
        String outputPath = inputPath.replace(".txt", "_" + targetLang.toLowerCase() + ".txt");
        Files.writeString(Path.of(outputPath), translated);

        System.out.println("finish: " + outputPath);
    }

    private static String extractTranslation(String json) {
        String marker = "\"content\":\"";
        int idx = json.lastIndexOf(marker);
        if (idx == -1) return null;
        int start = idx + marker.length();
        StringBuilder sb = new StringBuilder();
        int i = start;
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);
                switch (next) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    default -> sb.append(next);
                }
                i += 2;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString().isBlank() ? null : sb.toString();
    }
}