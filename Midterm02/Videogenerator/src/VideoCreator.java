import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class VideoCreator {

    private static final int VIDEO_W = 1080;
    private static final int VIDEO_H = 1920;
    private static final int SILENCE_SECS = 3;

    private final String workDir;

    public VideoCreator(String workDir) {
        this.workDir = workDir;
        new File(workDir).mkdirs();
    }


    public boolean createSlide(String mediaPath, String audioPath, String outputPath) {
        try {
            String resolvedAudio = audioPath;

            if (audioPath == null || !new File(audioPath).exists()) {
                resolvedAudio = outputPath + "_silence.mp3";
                if (!generateSilence(resolvedAudio, SILENCE_SECS)) {
                    System.out.println("[VideoCreator] Could not generate silence");
                    return false;
                }
            }

            boolean isImage = type.isImage(mediaPath);

            String filter = "scale=" + VIDEO_W + ":" + VIDEO_H +
                    ":force_original_aspect_ratio=increase,crop=" +
                    VIDEO_W + ":" + VIDEO_H;

            List<String> cmd = new ArrayList<>();
            cmd.add("ffmpeg");
            cmd.add("-y");

            if (isImage) {
                cmd.add("-loop");
                cmd.add("1");
            }

            cmd.add("-i");
            cmd.add(mediaPath);

            cmd.add("-i");
            cmd.add(resolvedAudio);

            cmd.add("-shortest");
            cmd.add("-c:v");
            cmd.add("libx264");
            cmd.add("-vf");
            cmd.add(filter);
            cmd.add("-c:a");
            cmd.add("aac");
            cmd.add("-b:a");
            cmd.add("192k");
            cmd.add("-pix_fmt");
            cmd.add("yuv420p");
            cmd.add("-movflags");
            cmd.add("+faststart");
            cmd.add(outputPath);

            return runProcess(cmd);

        } catch (Exception e) {
            System.out.println("[VideoCreator] createSlide error: " + e.getMessage());
            return false;
        }
    }


    public boolean generateSilence(String outputPath, int seconds) {
        try {
            List<String> cmd = List.of(
                    "ffmpeg", "-y",
                    "-f", "lavfi",
                    "-i", "anullsrc=r=44100:cl=mono",
                    "-t", String.valueOf(seconds),
                    "-c:a", "libmp3lame",
                    "-b:a", "128k",
                    outputPath
            );
            return runProcess(cmd);
        } catch (Exception e) {
            System.out.println("[VideoCreator] generateSilence error: " + e.getMessage());
            return false;
        }
    }


    public boolean concatenate(List<String> parts, String outputPath) {
        try {
            String listFile = workDir + "concat.txt";

            try (PrintWriter pw = new PrintWriter(listFile)) {
                for (String p : parts) {
                    pw.println("file '" + new File(p).getAbsolutePath() + "'");
                }
            }

            List<String> cmd = new ArrayList<>();
            cmd.add("ffmpeg");
            cmd.add("-y");
            cmd.add("-f");
            cmd.add("concat");
            cmd.add("-safe");
            cmd.add("0");
            cmd.add("-i");
            cmd.add(listFile);
            cmd.add("-c");
            cmd.add("copy");
            cmd.add(outputPath);

            return runProcess(cmd);

        } catch (Exception e) {
            System.out.println("[VideoCreator] concatenate error: " + e.getMessage());
            return false;
        }
    }

   //run video
    private boolean runProcess(List<String> command) {
        try {
            System.out.println("[VideoCreator] Running FFmpeg...");

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);

            Process p = pb.start();

            //  Read FFmpeg output
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {

                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }
            }

            // TIMEOUT
            boolean finished = p.waitFor(5, java.util.concurrent.TimeUnit.MINUTES);

            if (!finished) {
                p.destroy();
                System.out.println("[VideoCreator] TIMEOUT - process killed");
                return false;
            }

            int exit = p.exitValue();

            if (exit != 0) {
                System.out.println("[VideoCreator] FFmpeg FAILED exit=" + exit);
                return false;
            }

            System.out.println("[VideoCreator] SUCCESS");
            return true;

        } catch (Exception e) {
            System.out.println("[VideoCreator] runProcess error: " + e.getMessage());
            return false;
        }
    }
}