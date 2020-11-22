import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Window extends JFrame {
    private JPanel topPanel, bottomPanel;
    private JLabel bestFitnessLabel, numOfGensLabel;
    private int maxWidth = 1000;
    private int maxHeight = 650;

    public Window(int boardSize, int bestFitness, int numOfGens, Individual bestSolution) {
        super("Baby Lizards Problem | Evolutionary Algorithm");
        formatTopPanel(bestFitness, numOfGens);
        formatBottomPanel(boardSize, bestSolution);
        formatWindow();
    }

    private void formatTopPanel(int bestFitness, int numOfGens) {
        topPanel = new JPanel(new GridLayout(1, 2, 20, 20));

        bestFitnessLabel = new JLabel("Best Fitness Found: " + bestFitness);
        bestFitnessLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bestFitnessLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        bestFitnessLabel.setFont(new Font("Courier", Font.BOLD, 16));
        topPanel.add(bestFitnessLabel);

        numOfGensLabel = new JLabel("Number of Generations: " + numOfGens);
        numOfGensLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numOfGensLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        numOfGensLabel.setFont(new Font("Courier", Font.BOLD, 16));
        topPanel.add(numOfGensLabel);
    }

    private void formatBottomPanel(int boardSize, Individual bestSolution) {
        bottomPanel = new JPanel(new GridLayout(boardSize, boardSize));

        // scale images to fit window
        int imageHeight = (int) Math.round((double) (maxHeight - 35 - (boardSize * 3)) / (double) boardSize);
        int imageWidth = (int) Math.round((double) maxWidth / (double) boardSize);
        ImageIcon grassIcon = new ImageIcon(((new ImageIcon("images/grass2.png")).getImage())
                .getScaledInstance(imageWidth, imageHeight, java.awt.Image.SCALE_SMOOTH));
        ImageIcon lizardIcon = new ImageIcon(((new ImageIcon("images/lizard.png")).getImage())
                .getScaledInstance(imageWidth, imageHeight, java.awt.Image.SCALE_SMOOTH));
        ImageIcon treeIcon = new ImageIcon(((new ImageIcon("images/tree2.png")).getImage())
                .getScaledInstance(imageWidth, imageHeight, java.awt.Image.SCALE_SMOOTH));

        // add images to grid cells
        JLabel[] cellLabels = new JLabel[boardSize * boardSize];
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (bestSolution.gene[i / boardSize][i % boardSize] == 1) {
                cellLabels[i] = new JLabel(lizardIcon);
                bottomPanel.add(cellLabels[i]);
            } else if (bestSolution.gene[i / boardSize][i % boardSize] == 2) {
                cellLabels[i] = new JLabel(treeIcon);
                bottomPanel.add(cellLabels[i]);
            } else {
                cellLabels[i] = new JLabel(grassIcon);
                bottomPanel.add(cellLabels[i]);
            }
            cellLabels[i].setBorder(border);
        }
    }

    private void formatWindow() {
        getContentPane().setPreferredSize(new Dimension(maxWidth, maxHeight));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}