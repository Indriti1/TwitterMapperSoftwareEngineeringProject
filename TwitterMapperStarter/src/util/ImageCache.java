package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton that caches images loaded from twitter urls.
 */
public class ImageCache {
    private static ImageCache theInstance = new ImageCache();
    private BufferedImage defaultImage;

    public static ImageCache getInstance() {
        return theInstance;
    }

    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private Map<String, String> pathCache = new HashMap<>();

    private ImageCache() {

    }

    public BufferedImage getImage(String url) {
        BufferedImage image = imageCache.get(url);
        if (image == null) {
            image = Util.imageFromURL(url);
            imageCache.put(url, image);
        }
        return image;
    }

    public void loadImage(String url) {
        BufferedImage image = imageCache.get(url);
        if (image == null) {
            imageCache.put(url, defaultImage);
            Thread t = new Thread() {
                @Override
                public void run() {
                    BufferedImage image = Util.imageFromURL(url);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            imageCache.put(url, image);
                        }
                    });

                }
            };
            t.run();
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private String sha256(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = data.getBytes();
            messageDigest.update(bytes);
            byte[] hash = messageDigest.digest();
            return bytesToHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("Can't find SHA-256");
        }
    }
    private String hashURL(String url) {
        String hash = pathCache.get(url);
        if (hash == null) {
            hash = sha256(url);
        }
        return hash;
    }

    // I'm going to assume that hashing is good enough and collisions are rare enough
    private String saveImage(BufferedImage image, String path) {
        File directory = new File("data/imagecache");
        if (!directory.isDirectory()) {
            directory.mkdir();
        }
        String pathString = "data/imagecache/" + path + ".png";
        File file = new File(pathString);
        pathString = file.getAbsolutePath();
        if (file.canRead()) return pathString;
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return pathString;
    }

    public String imagePath(String url) {
        String path = hashURL(url);
        path = saveImage(getImage(url), path);
        return path;
    }

    public BufferedImage getDefaultImage() {
        return defaultImage;
    }
}