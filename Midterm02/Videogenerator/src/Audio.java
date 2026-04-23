import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
//generate audio with prommpt
public class Audio {
//Principal API elevenlabs
    //Convert AI-generated text into MP3 audio using APIs with fallback if something fails.
    private static final String API_KEY  = System.getenv("ELEVENLABS_API_KEY");
    private static final String VOICE_ID = "Nh2zY9kknu6z4pZy6FhD";
    private static final String API_URL  = "https://api.elevenlabs.io/v1/text-to-speech/" + VOICE_ID;
    private static final String SE_URL   = "https://api.streamelements.com/kappa/v2/speech?voice=Brian&text=";

    //I generate audio from text provided by an AI that analyzes the images or videos
// entered by my user to simplify the audio process.
    public static boolean generateFromPrompt(String prompt, String outputPath) {
        if (prompt == null || prompt.isBlank()) {
            return generateSilence(outputPath);
        }

        if (API_KEY != null && !API_KEY.isEmpty()) {
            if (tryElevenLabs(prompt, outputPath)) return true;
        }

        if (tryStreamElements(prompt, outputPath)) return true;

        System.out.println("[Audio] All TTS failed. Using silence.");
        return generateSilence(outputPath);
    }

    public static boolean generate(String text, String outputPath) {
        return generateFromPrompt(text, outputPath);
    }

    //API elevenlabs for audio
    private static boolean tryElevenLabs(String prompt, String outputPath) {
        try {
            String safeText = prompt.replace("\\", "").replace("\"", "'").trim();

            String json = "{"
                    + "\"text\":\"" + safeText + "\","
                    + "\"model_id\":\"eleven_multilingual_v2\","
                    + "\"voice_settings\":{"
                    +     "\"stability\":0.4,"
                    +     "\"similarity_boost\":0.8"
                    + "}"
                    + "}";

            HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("xi-api-key", API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() != 200) return false;

            return saveStream(conn.getInputStream(), outputPath);

        } catch (Exception e) {
            return false;
        }
    }

    //Generate audio using an external API, limit and clean the prompt
// to send it as a URL to the API page, and if successful,
// save and download the audio if eleven fail
    private static boolean tryStreamElements(String prompt, String outputPath) {
        try {
            String safe = prompt.length() > 300 ? prompt.substring(0, 297) + "..." : prompt;
            String encoded = URLEncoder.encode(safe, StandardCharsets.UTF_8);

            HttpURLConnection conn = (HttpURLConnection)
                    new URL(SE_URL + encoded).openConnection();

            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) return false;

            return saveStream(conn.getInputStream(), outputPath);

        } catch (Exception e) {
            return false;
        }
    }

    //It generates silence if something goes wrong.
    private static boolean generateSilence(String outputPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-y",
                    "-f", "lavfi",
                    "-i", "anullsrc=r=44100:cl=mono",
                    "-t", "3",
                    "-q:a", "9",
                    "-acodec", "libmp3lame",
                    outputPath
            );

            Process p = pb.start();
            return p.waitFor() == 0;

        } catch (Exception e) {
            return false;
        }
    }

    // Saves an audio data stream received from an HTTP connection to an MP3 file
// Converts the InputStream to bytes and writes them to disk
    private static boolean saveStream(InputStream is, String outputPath) {
        try {
            byte[] audio = is.readAllBytes();
            if (audio.length < 100) return false;

            File file = new File(outputPath);
            file.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(audio);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}