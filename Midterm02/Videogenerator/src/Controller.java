import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Controller {

    private static final String WORK_DIR = "video_output/";

    private final List<MediaItem> mediaItems = new ArrayList<>();
    private final VideoCreator videoCreator;
    private JProgressBar progressBar;

    public Controller(List<File> files) {
        new File(WORK_DIR).mkdirs();
        videoCreator = new VideoCreator(WORK_DIR);

        for (File f : files) {
            String name = f.getName().toLowerCase();
            if (type.isImage(name) || type.isVideo(name)) {
                mediaItems.add(new MediaItem(f));
            }
        }
    }

    public void setProgressBar(JProgressBar bar) {
        this.progressBar = bar;
    }

    public void run(JFrame frame, JTextArea logArea) {

        log("Starting pipeline with " + mediaItems.size() + " file(s)...", logArea);

        int totalSteps = mediaItems.size() * 3 + 4;
        int[] step = {0};

        // 1. GPS
        for (MediaItem item : mediaItems) {
            double[] gps = GPSima.getGPS(item.getFile());

            if (gps != null) {
                item.setLatitude(gps[0]);
                item.setLongitude(gps[1]);
            }

            log("GPS processed: " + item.getFile().getName(), logArea);
            updateProgress(++step[0], totalSteps);
        }

        // 2. Sort dates of videos and images
        mediaItems.sort(Comparator.comparing(MediaItem::getDate));
        log("Sorted media by date.", logArea);

        // 3. Descriptions
        for (MediaItem item : mediaItems) {
            String desc = Prompt.describeImage(item.getFile().getAbsolutePath());
            item.setDescription(desc);

            log("Description ready: " + item.getFile().getName(), logArea);
            updateProgress(++step[0], totalSteps);
        }

        // 4. Audio
        for (int i = 0; i < mediaItems.size(); i++) {
            MediaItem item = mediaItems.get(i);

            String audioPath = WORK_DIR + "audio_" + i + ".mp3";
            boolean ok = Audio.generateFromPrompt(item.getDescription(), audioPath);

            if (ok) {
                item.setAudioPath(audioPath);
            }

            log("Audio processed: " + i, logArea);
            updateProgress(++step[0], totalSteps);
        }

        // 5. Intro
        log("Generating intro...", logArea);
        generateIntro(logArea);
        updateProgress(++step[0], totalSteps);

        // 6. Map
        log("Generating map...", logArea);
        generateMapSlide(logArea);
        updateProgress(++step[0], totalSteps);

        // 7. Assemble
        log("Assembling video...", logArea);

        try {
            assembleVideo(logArea);
        } catch (Exception e) {
            log("ERROR: " + e.getMessage(), logArea);
        }

        updateProgress(totalSteps, totalSteps);
        log("DONE! Video ready.", logArea);
    }

    //  INTRO

    private void generateIntro(JTextArea log) {
        String imagePath = WORK_DIR + "intro.png";
        String audioPath = WORK_DIR + "intro.mp3";

        String prompt = Prompt.generateIntroPrompt(mediaItems);

        imageAi.generateImage(prompt, imagePath);
        Audio.generateFromPrompt("Welcome to your video.", audioPath);

        log("Intro created.", log);
    }

    //  MAP

    private void generateMapSlide(JTextArea log) {
        MediaItem first = getFirstWithGPS();
        MediaItem last = getLastWithGPS();

        if (first == null || last == null) {
            log("No GPS data, skipping map.", log);
            return;
        }

        String phrase = Prompt.generatePhrase();

        String mapPath = WORK_DIR + "map.png";
        String audioPath = WORK_DIR + "map.mp3";

        generateMap.render(
                first.getLatitude(), first.getLongitude(),
                last.getLatitude(), last.getLongitude(),
                phrase,
                mapPath
        );

        Audio.generateFromPrompt(phrase, audioPath);

        log("Map ready.", log);
    }

    //  VIDEO finish

    private void assembleVideo(JTextArea log) throws Exception {

        List<String> parts = new ArrayList<>();

        // Intro
        String intro = WORK_DIR + "intro.png";
        if (new File(intro).exists()) {
            String part = WORK_DIR + "part_intro.mp4";
            videoCreator.createSlide(intro, WORK_DIR + "intro.mp3", part);
            parts.add(part);
        }

        // Media
        for (int i = 0; i < mediaItems.size(); i++) {
            MediaItem item = mediaItems.get(i);

            String part = WORK_DIR + "part_" + i + ".mp4";

            videoCreator.createSlide(
                    item.getFile().getAbsolutePath(),
                    item.getAudioPath(),
                    part
            );

            parts.add(part);
        }

        // Map
        String map = WORK_DIR + "map.png";
        if (new File(map).exists()) {
            String part = WORK_DIR + "part_map.mp4";
            videoCreator.createSlide(map, WORK_DIR + "map.mp3", part);
            parts.add(part);
        }

        // final video
        String finalVideo = WORK_DIR + "AIVIDEOFINISH.mp4";

        boolean ok = videoCreator.concatenate(parts, finalVideo);

        if (ok) {
            log("Video saved as: " + finalVideo, log);
        } else {
            log("ERROR: video failed.", log);
        }
    }

  //get

    private MediaItem getFirstWithGPS() {
        for (MediaItem m : mediaItems) {
            if (m.hasGPS()) return m;
        }
        return null;
    }

    private MediaItem getLastWithGPS() {
        for (int i = mediaItems.size() - 1; i >= 0; i--) {
            if (mediaItems.get(i).hasGPS()) return mediaItems.get(i);
        }
        return null;
    }

    private void log(String msg, JTextArea area) {
        SwingUtilities.invokeLater(() -> {
            area.append(msg + "\n");
        });
    }

    private void updateProgress(int current, int total) {
        if (progressBar == null) return;

        int percent = (int) ((current / (double) total) * 100);

        SwingUtilities.invokeLater(() ->
                progressBar.setValue(percent)
        );
    }
}