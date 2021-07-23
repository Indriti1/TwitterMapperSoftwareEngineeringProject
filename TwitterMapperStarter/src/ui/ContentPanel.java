package ui;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContentPanel extends JPanel {
    private JSplitPane topLevelSplitPane;
    private JSplitPane querySplitPane;
    private JPanel newQueryPanel;
    private JPanel existingQueryList;
    private JMapViewer map;
    private Application app;

    public ContentPanel(Application app) {
        this.app = app;
        setMap(100, 50);
        setLayout(new BorderLayout());
        newQueryPanel = new NewQueryPanel(app);

        // NOTE: We wrap existingQueryList in a container so it gets a pretty border.
        wrapExistingQueryPanel();
        JPanel layerPanelContainer = setLayerPanelContainer(existingQueryList, BorderLayout.NORTH);
        setQuerySplitPanel(newQueryPanel, layerPanelContainer);
        setTopLevelSplitPane(querySplitPane, map);
        add(topLevelSplitPane, "Center");
        revalidate();
        repaint();
    }

    // Add a new query to the set of queries and update the UI to reflect the new query.
    public void addQuery(Query query) {
        JPanel newQueryPanel = new JPanel();
        newQueryPanel.setLayout(new GridBagLayout());
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(query.getColor());
        colorPanel.setPreferredSize(new Dimension(30, 30));
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(30, 30));
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.terminateQuery(query);
                query.terminate();
                existingQueryList.remove(newQueryPanel);
                revalidate();
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        newQueryPanel.add(colorPanel, c);

        c = new GridBagConstraints();
        JCheckBox checkbox = new JCheckBox(query.getQueryString());
        checkbox.setSelected(true);
        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.updateVisibility();
            }
        });
        query.setCheckBox(checkbox);
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        newQueryPanel.add(checkbox, c);
        newQueryPanel.add(removeButton);

        existingQueryList.add(newQueryPanel);
        validate();
    }

    public JMapViewer getViewer() {
        return map;
    }

    public void setQuerySplitPanel(JPanel topPanel, JPanel bottomPanel){
        querySplitPane = new JSplitPane(0);
        querySplitPane.setDividerLocation(150);
        querySplitPane.setTopComponent(topPanel);
        querySplitPane.setBottomComponent(bottomPanel);
    }

    public void setTopLevelSplitPane(JSplitPane splitPane, JMapViewer map){
        topLevelSplitPane = new JSplitPane(1);
        topLevelSplitPane.setDividerLocation(150);
        topLevelSplitPane.setLeftComponent(splitPane);
        topLevelSplitPane.setRightComponent(map);
    }

    public void setMap(int dimension1, int dimension2){
        map = new JMapViewer();
        map.setMinimumSize(new Dimension(dimension1, dimension2));
    }

    public void wrapExistingQueryPanel(){
        existingQueryList = new JPanel();
        existingQueryList.setLayout(new javax.swing.BoxLayout(existingQueryList, javax.swing.BoxLayout.Y_AXIS));
    }

    public JPanel setLayerPanelContainer(JPanel existingQueryList, String borderLayout){
        JPanel layerPanelContainer = new JPanel();
        layerPanelContainer.setLayout(new BorderLayout());
        layerPanelContainer.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Current Queries"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));
        layerPanelContainer.add(existingQueryList, borderLayout);
        return layerPanelContainer;
    }
}
