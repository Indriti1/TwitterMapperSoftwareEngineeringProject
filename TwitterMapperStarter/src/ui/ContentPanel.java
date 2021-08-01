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
    private Application application;

    public ContentPanel(Application application) {
        this.application = application;
        setMap(100, 50);
        setLayout(new BorderLayout());
        newQueryPanel = new NewQueryPanel(application);

        // NOTE: We wrap existingQueryList in a container so it gets a pretty border. We also set querySplitPanel and topLevelSplitPane
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
        JPanel newQueryPanel = setNewQueryPanel();
        JPanel colorPanel = setColorPanel(query, 30, 30);
        JButton removeButton = setRemoveButton(30, 30, application, query, existingQueryList, newQueryPanel);
        GridBagConstraints constraints = addGridBagConstraints(newQueryPanel, colorPanel);
        JCheckBox checkbox = setCheckBox(query);
        addToNewQueryPanel(newQueryPanel, checkbox, constraints, removeButton);
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

    public JPanel setNewQueryPanel(){
        JPanel newQueryPanel = new JPanel();
        newQueryPanel.setLayout(new GridBagLayout());
        return newQueryPanel;
    }

    public JPanel setColorPanel(Query query, int dimension1, int dimension2){
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(query.getColor());
        colorPanel.setPreferredSize(new Dimension(dimension1, dimension2));
        return colorPanel;
    }

    public JButton setRemoveButton(int dimension1, int dimension2, Application application, Query query, JPanel existingQueryList, JPanel newQueryPanel){
        JButton removeButton = new JButton("X");
        removeButton.setPreferredSize(new Dimension(dimension1, dimension2));
        removeButton.setMargin(new Insets(0, 0, 0, 0));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application.terminateQuery(query);
                query.terminate();
                existingQueryList.remove(newQueryPanel);
                revalidate();
            }
        });
        return removeButton;
    }

    public JCheckBox setCheckBox(Query query){
        JCheckBox checkbox = new JCheckBox(query.getQueryString());
        checkbox.setSelected(true);
        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                application.updateVisibility();
            }
        });
        query.setCheckBox(checkbox);
        return checkbox;
    }

    public GridBagConstraints addGridBagConstraints(JPanel newQueryPanel, JPanel colorPanel){
        GridBagConstraints constraints = new GridBagConstraints();
        newQueryPanel.add(colorPanel, constraints);
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        return constraints;
    }

    public void addToNewQueryPanel(JPanel newQueryPanel, JCheckBox checkBox, GridBagConstraints constraints, JButton removeButton){
        newQueryPanel.add(checkBox, constraints);
        newQueryPanel.add(removeButton);
    }

}
