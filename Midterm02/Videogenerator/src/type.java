//Class for analize correct type of files to avoid mistakes
public class type {

    public static boolean isImage(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
    }

    public static boolean isVideo(String fileName) {
        return fileName.toLowerCase().endsWith(".mp4");
    }
}