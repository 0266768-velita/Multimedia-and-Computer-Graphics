import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

class ImageCompressor {

    // Compress Image
    public static void compressImage(String inputPath, String outputPath) {
        try {
            BufferedImage image = ImageIO.read(new File(inputPath));

            // Save image
            File tempFile = new File("temp_compressed.jpg");
            ImageIO.write(image, "jpg", tempFile);

            // bytes
            byte[] inputBytes = readFileToByteArray(tempFile);

            // Compress bytes
            Deflater deflater = new Deflater();
            deflater.setInput(inputBytes);
            deflater.finish();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(inputBytes.length);
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            deflater.end();

            byte[] compressedBytes = outputStream.toByteArray();

            // save compress
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {

                if (compressedBytes.length > inputBytes.length) {
                    fos.write(inputBytes);
                } else {
                    fos.write(compressedBytes);
                }
            }

            tempFile.delete();
            System.out.println("Compression complete: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Descompress
    public static void decompressImage(String inputPath, String outputPath) {
        try {
            byte[] compressedBytes = readFileToByteArray(new File(inputPath));

            Inflater inflater = new Inflater();
            inflater.setInput(compressedBytes);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedBytes.length);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            inflater.end();

            byte[] decompressedBytes = outputStream.toByteArray();

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(decompressedBytes);
            }

            System.out.println("Decompression complete: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    public static void main(String[] args) {
        String original = "example.png";        // Image orinial
        String compressed = "compressed.img";   // image compress
        String decompressed = "decompressed.jpg"; // final image

        compressImage(original, compressed);
        decompressImage(compressed, decompressed);
    }
}




