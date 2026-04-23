import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;import java.util.List;

public class Prompt {

    // FALLBACKS for errors
    private static final String FALLBACK_IMAGE =
            "A cinematic moment captured in warm light, full of emotion.";

    private static final String FALLBACK_PHRASE =
            "Every memory becomes a story worth remembering.";

    private static final String FALLBACK_INTRO =
            "cinematic travel memories golden aesthetic montage";


    // IMAGE DESCRIPTION

    public static String describeImage(String imagePath) {
        try {
            String result = callGeminiImage(imagePath);

            if (result == null || result.isBlank()) {
                return FALLBACK_IMAGE;
            }

            return result;

        } catch (Exception e) {
            System.out.println("[Prompt] fallback image used");
            return FALLBACK_IMAGE;
        }
    }

   //phrase with call API
    public static String generatePhrase() {
        try {
            String result = callGeminiText();

            if (result == null || result.isBlank()) {
                return FALLBACK_PHRASE;
            }

            return result;

        } catch (Exception e) {
            return FALLBACK_PHRASE;
        }
    }


    // INTRO PROMPT

    public static String generateIntroPrompt(List<MediaItem> mediaItems) {
        try {
            if (mediaItems == null || mediaItems.isEmpty()) {
                return FALLBACK_INTRO;
            }

            String result = callGeminiIntro(mediaItems.size());

            return (result == null || result.isBlank())
                    ? FALLBACK_INTRO
                    : result;

        } catch (Exception e) {
            return FALLBACK_INTRO;
        }
    }


    private static String callGeminiImage(String imagePath) {
        return null; // fallback safe
    }

    private static String callGeminiText() {
        return null;
    }

    private static String callGeminiIntro(int size) {
        return null;
    }
}