package util;

import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

/**
 * Spherical Geometry Utilities
 */
public class SphericalGeometry {
    private static final int RADIUS = 6371000;   // radius of earth in metres

    /**
     * Find distance in metres between two lat/lon points
     *
     * @param p1  first point
     * @param p2  second point
     * @return distance between p1 and p2 in metres
     */
    //Haversine's Formula
    public static double distanceBetween(ICoordinate p1, ICoordinate p2) {
        double latitude1 = p1.getLat() / 180.0 * Math.PI;
        double latitude2 = p2.getLat() / 180.0 * Math.PI;
        double deltaLongitude = (p2.getLon() - p1.getLon()) / 180.0 * Math.PI;
        double deltaLatitude = (p2.getLat() - p1.getLat()) / 180.0 * Math.PI;

        double a = Math.sin(deltaLatitude / 2.0) * Math.sin(deltaLatitude / 2.0)
                + Math.cos(latitude1) * Math.cos(latitude2)
                * Math.sin(deltaLongitude / 2.0) * Math.sin(deltaLongitude / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return c * RADIUS;
    }
}
