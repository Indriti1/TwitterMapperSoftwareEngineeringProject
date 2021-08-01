package ui;

import query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * A UI panel for entering new queries.
 */
public class NewQueryPanel extends JPanel {
    private final JTextField newQuery = new JTextField(10);
    private final JLabel queryLabel = new JLabel("Enter Search: ");
    private final JPanel colorSetter;
    private final Application app;
    private Random random;

    public NewQueryPanel(Application app) {
        //Setting initial variables needed from the application
        this.app = app;
        this.colorSetter = new JPanel();
        random = new Random();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        queryLabel.setLabelFor(newQuery);
        JLabel colorLabel = new JLabel("Select Color: ");
        JButton addQueryButton = new JButton("Add New Search");
        colorSetter.setBackground(getRandomColor());

        //Adding Constraints to 2 JLabels, 1 JTextField, 1 JPanel and 1 JButton
        addConstraintsToJLabel(queryLabel, constraints, GridBagConstraints.RELATIVE, GridBagConstraints.NONE, 0,0);
        addConstraintsToJTextField(newQuery, constraints, GridBagConstraints.REMAINDER, GridBagConstraints.HORIZONTAL, 1);
        add(Box.createRigidArea(new Dimension(5, 5)));
        addConstraintsToJLabel(colorLabel, constraints, GridBagConstraints.RELATIVE, GridBagConstraints.NONE, 1, 0);
        addConstraintsToJPanel(colorSetter, constraints, GridBagConstraints.REMAINDER, GridBagConstraints.BOTH, 1);
        add(Box.createRigidArea(new Dimension(5, 5)));
        addConstraintsToJButton(addQueryButton, constraints, 2, GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE);

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("New Search"),
                        BorderFactory.createEmptyBorder(5,5,5,5)));

        addQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!newQuery.getText().equals("")) {
                    addQuery(newQuery.getText().toLowerCase());
                    newQuery.setText("");
                }
            }
        });

        // This makes the "Enter" key submit the query.
        app.getRootPane().setDefaultButton(addQueryButton);

        colorSetter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Color newColor = JColorChooser.showDialog(
                            app,
                            "Choose Background Color",
                            colorSetter.getBackground());
                    // newColor is "null" if user clicks "Cancel" button on JColorChooser popup.
                    if (newColor != null) {
                        colorSetter.setBackground(newColor);
                    }
                }
            }
        });
    }

    private void addQuery(String newQuery) {
        Query query = new Query(newQuery, colorSetter.getBackground(), app.map());
        app.addQuery(query);
        colorSetter.setBackground(getRandomColor());
    }

    public Color getRandomColor() {
        // Pleasant colors: https://stackoverflow.com/questions/4246351/creating-random-colour-in-java#4246418
        final float hue = random.nextFloat();
        final float saturation = (random.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance);
    }

    public void setGridBagWidthFillAndCoordinates(GridBagConstraints constraints, int width, int fill, int x, int y){
        constraints.gridwidth = width;
        constraints.fill = fill;
        constraints.gridy = x;
        constraints.gridx = y;
    }

    public void setGridBagWidthFillAndX(GridBagConstraints constraints, int width, int fill, int x){
        constraints.gridwidth = width;
        constraints.fill = fill;
        constraints.gridy = x;
    }

    public void setGridBagWidthAndCoordinates(GridBagConstraints constraints, int width, int x, int y){
        constraints.gridwidth = width;
        constraints.gridy = x;
        constraints.gridx = y;
    }

    public void addConstraintsToJLabel(JLabel jLabel, GridBagConstraints constraints, int width, int fill, int x, int y){
        setGridBagWidthFillAndCoordinates(constraints, width, fill, x,y);
        add(jLabel, constraints);
    }

    public void addConstraintsToJTextField(JTextField jQuery, GridBagConstraints constraints, int width, int fill, int x){
        setGridBagWidthFillAndX(constraints, width, fill, x);
        jQuery.setMaximumSize(new Dimension(200, 20));
        add(jQuery, constraints);
    }

    public void addConstraintsToJPanel(JPanel jPanel, GridBagConstraints constraints, int width, int fill, int x){
        setGridBagWidthFillAndX(constraints, width, fill, x);
        jPanel.setMaximumSize(new Dimension(200, 20));
        add(jPanel, constraints);
    }

    public void addConstraintsToJButton(JButton jButton, GridBagConstraints constraints, int width, int x, int y){
        setGridBagWidthAndCoordinates(constraints, width, x, y);
        add(jButton, constraints);
    }
}
