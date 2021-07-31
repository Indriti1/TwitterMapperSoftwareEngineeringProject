package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import query.Query;
import twitter.LiveTwitterSource;
import twitter.PlaybackTwitterSource;
import twitter.TwitterSource;
import util.SphericalGeometry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * The Twitter viewer application
 * Derived from a JMapViewer demo program written by Jan Peter Stotz
 */
public class Application extends JFrame {
    // The content panel, which contains the entire UI
    private final ContentPanel contentPanel;
    // The provider of the tiles for the map, we use the Bing source
    private BingAerialTileSource bing;
    // All of the active queries
    private List<Query> queries = new ArrayList<>();
    // The source of tweets, a TwitterSource, either live or playback
    private TwitterSource twitterSource;

    private void initialize() {
        // To use the live twitter stream, use the following line
        twitterSource = new LiveTwitterSource();

        queries = new ArrayList<>();
    }

    /**
     * A new query has been entered via the User Interface
     * @param   query   The new query object
     */
    public void addQuery(Query query) {
        queries.add(query);
        Set<String> allterms = getQueryTerms();
        twitterSource.setFilterTerms(allterms);
        contentPanel.addQuery(query);
        // TODO: This is the place where you should connect the new query to the twitter source
        twitterSource.addObserver(query);
    }

    /**
     * return a list of all terms mentioned in all queries. The live twitter source uses this
     * to request matching tweets from the Twitter API.
     * @return
     */
    private Set<String> getQueryTerms() {
        Set<String> queryTerms = new HashSet<>();
        for (Query query : queries) {
            queryTerms.addAll(query.getFilter().terms());
        }
        return queryTerms;
    }

    /**
     * Constructs the {@code Application}.
     */
    public Application() {
        super("Twitter content viewer");
        setSize(300, 300);
        initialize();

        bing = new BingAerialTileSource();

        // Do UI initialization
        contentPanel = new ContentPanel(this);
        setContentPanelParameters(contentPanel);
        setMapParameters(bing);

        //NOTE This is so that the map eventually loads the tiles once Bing attribution is ready.
        Coordinate coordinate = new Coordinate(0, 0);

        Timer bingTimer = new Timer();
        TimerTask bingAttributionCheck = new TimerTask() {
            @Override
            public void run() {
                // This is the best method we've found to determine when the Bing data has been loaded.
                // We use this to trigger zooming the map so that the entire world is visible.
                if (!bing.getAttributionText(0, coordinate, coordinate).equals("Error loading Bing attribution data")) {
                    map().setZoom(2);
                    bingTimer.cancel();
                }
            }
        };
        bingTimer.schedule(bingAttributionCheck, 100, 200);

        // Set up a motion listener to create a tooltip showing the tweets at the pointer position
        map().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point point = e.getPoint();
                ICoordinate position = map().getPosition(point);
                // TODO: Use the following method to set the text that appears at the mouse cursor
                List<MapMarker> mapMarkers = getMarkersCovering(position, pixelWidth(point));
                if(!mapMarkers.isEmpty()){
                    String text = "<html>";
                    for (MapMarker mapMarker: mapMarkers) {
                        MapMarkerCustom mapMarkerCustom = (MapMarkerCustom) mapMarker;
                        text += "<p><img src=" + mapMarkerCustom.getImageURL() + ">";
                        text += mapMarkerCustom.getText() + "</p>";
                    }
                    text += "</html>";
                    map().setToolTipText(text);
                }
            }
        });
    }

    // How big is a single pixel on the map?  We use this to compute which tweet markers
    // are at the current most position.
    private double pixelWidth(Point point) {
        ICoordinate center = map().getPosition(point);
        ICoordinate edge = map().getPosition(new Point(point.x + 1, point.y));
        return SphericalGeometry.distanceBetween(center, edge);
    }

    // Get those layers (of tweet markers) that are visible because their corresponding query is enabled
    private Set<Layer> getVisibleLayers() {
        Set<Layer> visibleLayers = new HashSet<>();
        for (Query query : queries) {
            if (query.getVisible()) {
                visibleLayers.add(query.getLayer());
            }
        }
        return visibleLayers;
    }

    // Get all the markers at the given map position, at the current map zoom setting
    private List<MapMarker> getMarkersCovering(ICoordinate position, double pixelWidth) {
        List<MapMarker> mapMarkers = new ArrayList<>();
        Set<Layer> visibleLayers = getVisibleLayers();
        for (MapMarker mapMarker : map().getMapMarkerList()) {
            if (!visibleLayers.contains(mapMarker.getLayer())) continue;
            double distance = SphericalGeometry.distanceBetween(mapMarker.getCoordinate(), position);
            if (distance < mapMarker.getRadius() * pixelWidth) {
                mapMarkers.add(mapMarker);
            }
        }
        return mapMarkers;
    }

    public JMapViewer map() {
        return contentPanel.getViewer();
    }

    /**
     * @param args Application program arguments (which are ignored)
     */
    public static void main(String[] args) {
        new Application().setVisible(true);
    }

    // Update which queries are visible after any checkBox has been changed
    public void updateVisibility() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Recomputing visible queries");
                for (Query query : queries) {
                    JCheckBox box = query.getCheckBox();
                    Boolean state = box.isSelected();
                    query.setVisible(state);
                }
                map().repaint();
            }
        });
    }

    // A query has been deleted, remove all traces of it
    public void terminateQuery(Query query) {
        // TODO: This is the place where you should disconnect the expiring query from the twitter source
        queries.remove(query);
        Set<String> allterms = getQueryTerms();
        twitterSource.setFilterTerms(allterms);
        twitterSource.deleteObserver(query);
    }

    public void setMapParameters(BingAerialTileSource bing){
        // Map markers and zoom controls are made visible and scrolling is allowed. Bing is also used as tile provider
        map().setMapMarkerVisible(true);
        map().setZoomContolsVisible(true);
        map().setScrollWrapEnabled(true);
        map().setTileSource(bing);
    }

    public void setContentPanelParameters(ContentPanel contentPanel){
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
