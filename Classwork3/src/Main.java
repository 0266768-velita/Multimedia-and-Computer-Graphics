import java.io.FileWriter;

public class Main {
    public static void main(String[] args) throws Exception {

        String svg =
                "<svg width=\"400\" height=\"300\" xmlns=\"http://www.w3.org/2000/svg\">\n" +

                        "  <path d=\"M 400 0 L 0 0 L 400 300 Z\" fill=\"red\"/>\n" +

                        "  <path d=\"M 0 0 L 0 300 L 400 300 Z\" fill=\"blue\"/>\n" +

                        "</svg>";

        FileWriter writer = new FileWriter("triangles.svg");
        writer.write(svg);
        writer.close();
    }
}
