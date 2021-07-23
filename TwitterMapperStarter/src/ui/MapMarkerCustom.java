package ui;

import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class MapMarkerCustom extends MapMarkerCircle {
    private static final int markerSize = 25;
    private final int borderSize = 6;
    private Color color;
    private Coordinate coordinate;
    private long identifier;
    private String imageURL;
    private Image image;
    private String tweet;

    public MapMarkerCustom(Layer layer, Color color, Coordinate coordinate, long identifier, String imageURL, Image image, String tweet){
        super(layer, null, coordinate, markerSize, STYLE.FIXED, getDefaultStyle());
        this.color = color;
        this.coordinate = coordinate;
        this.identifier = identifier;
        this.imageURL = imageURL;
        this.image = image;
        this.tweet = tweet;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getText() {
        return tweet;
    }

    @Override
    public void paint(Graphics g, Point position, int radius) {
        int sizeImage = markerSize;
        int halfSizeImage = sizeImage / 2;
        int xImage = position.x - halfSizeImage;
        int yImage = position.y - halfSizeImage;

        int size = sizeImage + borderSize * 2;
        int x = xImage - borderSize;
        int y = yImage - borderSize;

        g.setClip(new Ellipse2D.Float(x, y, size, size));
        g.setColor(color);
        g.fillOval(x, y, size, size);

        g.setClip(new Ellipse2D.Float(xImage, yImage, sizeImage, sizeImage));
        g.drawImage(image, xImage, yImage, sizeImage, sizeImage, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MapMarkerCustom)) return false;

        MapMarkerCustom that = (MapMarkerCustom) obj;

        return identifier == that.identifier;
    }

    @Override
    public int hashCode() {
        return (int) (identifier ^ (identifier >>> 32));
    }
}