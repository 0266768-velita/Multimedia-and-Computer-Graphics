import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
//Create a new image with AI
public class imageAi {

    private static final String HF_API_KEY = System.getenv("HUGGINGFACE_API_KEY");
    private static final String HF_URL =
            "https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-xl-base-1.0";

  //Principal API HUGGIN and generate the image with prommpt
    public static boolean generateImage(String prompt, String outputPath) {
        if (HF_API_KEY != null && !HF_API_KEY.isBlank()) {
            if (tryHuggingFace(prompt, outputPath)) return true;
        }
        return tryPollinations(prompt, outputPath);
    }

    private static boolean tryHuggingFace(String prompt, String outputPath) {
        try {
            String json = "{\"inputs\":\"" + prompt.replace("\"", "'") + "\"}";

            URL url = new URL(HF_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + HF_API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));

            if (conn.getResponseCode() != 200) return false;

            byte[] data = conn.getInputStream().readAllBytes();
            if (!isImageBytes(data)) return false;

            saveBytes(data, outputPath);
            return true;

        } catch (Exception e) {
            System.out.println("[imageAi] HuggingFace error: " + e.getMessage());
            return false;
        }
    }
//If Huggin fail I use pollinations AI
    private static boolean tryPollinations(String prompt, String outputPath) {
        try {
            String encoded = URLEncoder.encode(prompt, StandardCharsets.UTF_8);
            URL url = new URL("https://image.pollinations.ai/prompt/" + encoded);

            try (InputStream is = url.openStream()) {
                byte[] data = is.readAllBytes();
                if (!isImageBytes(data)) return false;
                saveBytes(data, outputPath);
                return true;
            }

        } catch (Exception e) {
            System.out.println("[imageAi] Pollinations error: " + e.getMessage());
            return false;
        }
    }

   //Verify that the bytes are a real image

    private static boolean isImageBytes(byte[] data) {
        if (data == null || data.length < 4) return false;
        boolean isPng  = (data[0] == (byte) 0x89 && data[1] == 0x50);
        boolean isJpeg = (data[0] == (byte) 0xFF && data[1] == (byte) 0xD8);
        return isPng || isJpeg;
    }
//save image
    private static void saveBytes(byte[] data, String path) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(data);
        }
    }
}