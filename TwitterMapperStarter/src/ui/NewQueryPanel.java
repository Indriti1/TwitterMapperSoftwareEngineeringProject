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
        this.app = app;
        this.colorSetter = new JPanel();

        random = new Random();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        queryLabel.setLabelFor(newQuery);
        GridBagConstraints constraints = new GridBagConstraints();
        setGridBagWidthFillAndCoordinates(constraints, GridBagConstraints.RELATIVE, GridBagConstraints.NONE, 0,0);
        add(queryLabel, constraints);

        setGridBagWidthFillAndX(constraints, GridBagConstraints.REMAINDER, GridBagConstraints.HORIZONTAL, 1);
        newQuery.setMaximumSize(new Dimension(200, 20));
        add(newQuery, constraints);

        add(Box.createRigidArea(new Dimension(5, 5)));

        JLabel colorLabel = new JLabel("Select Color: ");
        colorSetter.setBackground(getRandomColor());
        setGridBagWidthFillAndCoordinates(constraints, GridBagConstraints.RELATIVE, GridBagConstraints.NONE, 1, 0);
        add(colorLabel, constraints);

        setGridBagWidthFillAndX(constraints, GridBagConstraints.REMAINDER, GridBagConstraints.BOTH, 1);
        colorSetter.setMaximumSize(new Dimension(200, 20));
        add(colorSetter, constraints);

        add(Box.createRigidArea(new Dimension(5, 5)));

        JButton addQueryButton = new JButton("Add New Search");
        setGridBagWidthAndCoordinates(constraints, 2, GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE);
        add(addQueryButton, constraints);

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
}
