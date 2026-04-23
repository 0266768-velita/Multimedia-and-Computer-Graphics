import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


 // Extracts GPS coordinates from images and videos using external tools.
 // Images: exiftool
 //Videos: ffprobe

public class GPSima {
//It receives the file, analyzes its type, and then proceeds to collect the data.

    public static double[] getGPS(File file) {
        if (type.isImage(file.getName())) {
            return fromImage(file);
        } else if (type.isVideo(file.getName())) {
            return fromVideo(file);
        }
        return null;
    }

   //for Image use Exiftool-You need download this...is for get coordinates line by line
    private static double[] fromImage(File file) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "exiftool", "-n",
                    "-GPSLatitude", "-GPSLongitude",
                    file.getAbsolutePath()
            );

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            Double lat = null;
            Double lng = null;
            String line;

            while ((line = reader.readLine()) != null) {
                String lower = line.toLowerCase();
                if (lower.contains("gps latitude")) {
                    lat = parseAfterColon(line);
                } else if (lower.contains("gps longitude")) {
                    lng = parseAfterColon(line);
                }
            }

            process.waitFor();

            if (lat != null && lng != null) return new double[]{lat, lng};
//if dont have GPS image. Send a error
        } catch (Exception e) {
            System.out.println("[GPSima] Image error: " + e.getMessage());
        }
        return null;
    }


    private static double[] fromVideo(File file) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe", "-v", "quiet",
                    "-show_entries", "format_tags=location",
                    "-of", "default=nw=1",
                    file.getAbsolutePath()
            );

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("location=")) {
                    String raw = line.substring(line.indexOf('=') + 1)
                            .replace("/", "").trim();
                    String[] parts = raw.split("(?=[+-])");
                    if (parts.length >= 3) {
                        double lat = Double.parseDouble(parts[1]);
                        double lng = Double.parseDouble(parts[2]);
                        return new double[]{lat, lng};
                    }
                }
            }

            process.waitFor();

        } catch (Exception e) {
            System.out.println("[GPSima] Video error: " + e.getMessage());
        }
        return null;
    }

    private static Double parseAfterColon(String line) {
        try {
            return Double.parseDouble(line.split(":")[1].trim());
        } catch (Exception e) {
            return null;
        }
    }
}