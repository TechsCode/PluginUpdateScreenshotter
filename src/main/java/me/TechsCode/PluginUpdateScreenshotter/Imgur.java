package me.TechsCode.PluginUpdateScreenshotter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Imgur {

    private String clientID;

    public Imgur(String clientId) {
        this.clientID = clientId;
    }

    public String upload(BufferedImage bufferedImage){
        try {
            URL url = new URL("https://api.imgur.com/3/image");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(Base64.encodeBase64String(outputStream.toByteArray()), "UTF-8");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + clientID);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();
            StringBuilder stb = new StringBuilder();
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                stb.append(line).append("\n");
            }

            wr.close();
            rd.close();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(stb.toString());
            JsonObject dataObject = jsonObject.getAsJsonObject("data");
            return dataObject.get("link").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
