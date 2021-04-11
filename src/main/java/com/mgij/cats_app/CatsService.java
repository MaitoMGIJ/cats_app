package com.mgij.cats_app;

import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CatsService {

    public static void getCats() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String responseJson = response.body().string();

        responseJson = responseJson.substring(1);
        responseJson = responseJson.substring(0, responseJson.length()-1);

        Gson gson = new Gson();
        Cat kitten = gson.fromJson(responseJson, Cat.class);

        Image image;
        try{
            URL url = new URL(kitten.getUrl());
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage bufferedImage = ImageIO.read(httpcon.getInputStream());

            ImageIcon backgroundIcon = new ImageIcon(bufferedImage);

            if(backgroundIcon.getIconWidth() > 640){
                Image background = backgroundIcon.getImage();
                Image backgroundModified = background.getScaledInstance(640, 480, Image.SCALE_SMOOTH);
                backgroundIcon = new ImageIcon(backgroundModified);
            }

            String menu = "Options: \n "+
                    "1. Get other image \n" +
                    "2. Favourite \n" +
                    "3. Return";
            String[] buttons = {
                    "1. Get Other Image",
                    "2. Favourite",
                    "3. Return"
            };
            String idCat = String.valueOf(kitten.getId());
            String option = (String) JOptionPane.showInputDialog(null, menu, idCat, JOptionPane.INFORMATION_MESSAGE, backgroundIcon, buttons, buttons[0]);

            int selection = -1;
            if(option!=null) {
                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])) {
                        selection = i;
                        break;
                    }
                }
            }

            switch (selection){
                case 0:
                    getCats();
                    break;
                case 1:
                    favouriteCat(kitten);
                    break;
                default:
                    break;
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void favouriteCat(Cat cat){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\": \"" + cat.getId() + "\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cat.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
