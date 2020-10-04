package net.nodium.mcmods.mcah_manualgrass;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public static long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    
    public static int map(int x, int in_min, int in_max, int out_min, int out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    
    public static double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

//    public static int wrap(int x, int lower, int upper) {
//        upper += 1;
//        if (x < lower) {
//            return (upper * (-x / upper + 1) + x) % upper;
//        } else {
//            return x % upper;
//        }
//    }

    public static int wrap(int x, int lower, int upper) {
        if (x < lower) {
            return upper;
        } else {
            return x % (upper + 1);
        }
    }

    public static String postStringToUrl(String url, String contents) {
        try {
            URL urlObj = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("charset", "utf-8");

            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(contents.getBytes());
            os.flush();
            os.close();

            sendPlayerChatMessage("file upload successful");

            return conn.getResponseMessage();
        } catch (Exception e) {
            sendPlayerChatError("failed to upload file!");
            e.printStackTrace();
        }

        return "oof";
    }

    public static void writeStringToFile(String path, String contents) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);

        writer.append(contents);

        writer.flush();
        writer.close();
    }

    public static String readFileToString(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        reader.close();

        return sb.toString();
    }

    public static String readResourceToString(String name) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Utils.class.getResourceAsStream(name)
                )
        );
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void sendPlayerChatMessage(String message) {
        if (ManualGrass.mc.inGameHud == null) {
            System.out.println(message);
            return;
        }
        ManualGrass.mc.inGameHud.getChatHud().addMessage(new TranslatableText(message));
    }

    public static void sendPlayerChatError(String message) {
        if (ManualGrass.mc.inGameHud == null) {
            System.out.println(message);
            return;
        }
        ManualGrass.mc.inGameHud.getChatHud().addMessage(new TranslatableText(message).formatted(Formatting.RED));
    }
}
