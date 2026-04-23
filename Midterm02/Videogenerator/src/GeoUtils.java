import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

//convert coordinates to city , Country
public class GeoUtils {

    //Use Database with places Nominatim for search coordinates
    public static String getCityCountry(double lat, double lng) {
        try {
            // zoom=10 → city level detail
            String urlStr = String.format(
                    "https://nominatim.openstreetmap.org/reverse"
                            + "?lat=%.6f&lon=%.6f&format=json&zoom=10",
                    lat, lng);

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "TravelVideoGenerator/1.0"); // required by Nominatim
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            if (conn.getResponseCode() != 200) return null;

            String json = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Parse city (try multiple field names since Nominatim varies by country)
            String city = getField(json, "city");
            if (city == null) city = getField(json, "town");
            if (city == null) city = getField(json, "village");
            if (city == null) city = getField(json, "state");

            String country = getField(json, "country");

            String result = null;
            if (city != null && country != null)      result = city + ", " + country;
            else if (country != null)                  result = country;

            System.out.println("[GeoUtils] " + lat + ", " + lng + " → " + result);
            return result;

        } catch (Exception e) {
            System.out.println("[GeoUtils] Error: " + e.getMessage());
            return null;
        }
    }

    //return the city name
    public static String getCountry(double lat, double lng) {
        try {
            // zoom=3 → country level
            String urlStr = String.format(
                    "https://nominatim.openstreetmap.org/reverse"
                            + "?lat=%.6f&lon=%.6f&format=json&zoom=3",
                    lat, lng);

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "TravelVideoGenerator/1.0");
            conn.setConnectTimeout(10_000);
            conn.setReadTimeout(10_000);

            if (conn.getResponseCode() != 200) return null;

            String json = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return getField(json, "country");

        } catch (Exception e) {
            System.out.println("[GeoUtils] getCountry error: " + e.getMessage());
            return null;
        }
    }

    //extract values
    private static String getField(String json, String key) {
        String marker = "\"" + key + "\":\"";
        int start = json.indexOf(marker);
        if (start == -1) return null;
        start += marker.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return null;
        return json.substring(start, end);
    }
}