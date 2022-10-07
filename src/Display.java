import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class Display {

    //length*height of the grid
    private int length;
    private int height;

    //number of people on the grid following these values : 2^exponent
    private static final int exponent = 0;

    private JFrame frame;
    private JPanel[][] jPanelList;

    public Display(int length, int height) {
        this.length = length;
        this.height = height;
        this.frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        jPanelList = new JPanel[length][height];
    }

    /**
     * Create the grid and its appearance
     *
     * @return JFrame
     */
    public void updateGraphicGrid(MatrixEntity matrixEntity){
        //get the matrix
        Optional<Entity>[][] matrix = matrixEntity.getMatrix();

        //initialize the grid appearance
        for(int i = 0 ; i < length ; i ++){
            for(int j = 0 ; j < height ; j ++){
                JPanel p = new JPanel();
                p.setBorder(BorderFactory.createLineBorder(Color.orange));
                if(matrix[i][j].isPresent())
                    p.setBackground(Color.red);
                jPanelList[i][j] = p;
                frame.getContentPane().add(p); // Adds Button to content pane of frame
            }
        }
        frame.setLayout(new GridLayout(3,3));
    }

    void updateGridV2(ArrayList<Entity> listEntity){
        for(Entity e: listEntity){
            if(e.hasMove()){
                Position lastPos = e.getLastPosition().get();
                Position currentPos = e.getCurrentPosition();

                jPanelList[lastPos.getX()][lastPos.getY()].setBackground(Color.lightGray);
                jPanelList[currentPos.getX()][currentPos.getY()].setBackground(Color.RED);
            }
        }
    }

    /**
     * Display and update the grid
     */
    void displayGrid(MatrixEntity matrixEntity) {
        updateGraphicGrid(matrixEntity);
        frame.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
