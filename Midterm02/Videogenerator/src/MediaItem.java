import java.io.File;
import java.util.Date;

//Save information of project AUDIO-IMAGE-GPS,etc.
public class MediaItem {

    private final File file;
    private final String mediaType;
    private final Date date;

    private double latitude;
    private double longitude;
    private String description = "";
    private String audioPath = "";

    public MediaItem(File file) {
        this.file = file;

        // Safe media type detection (avoids wrong classification)
        String name = file.getName().toLowerCase();

        this.mediaType = type.isImage(name)
                ? "image"
                : (name.endsWith(".mp4") ? "video" : "unknown");

        this.date = new Date(file.lastModified());
    }

    // Getters
    public File getFile() {
        return file;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Date getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public String getAudioPath() {
        return audioPath;
    }

    // Setters
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setAudioPath(String path) {
        this.audioPath = path;
    }


    public boolean hasGPS() {
        return latitude != 0.0 && longitude != 0.0;
    }
}