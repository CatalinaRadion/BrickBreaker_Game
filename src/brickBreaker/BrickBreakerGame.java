package brickBreaker;

import javax.swing.*;

public class BrickBreakerGame {

    public static void main(String[] args) {

        //create JFrame and set properties
        JFrame frame = new JFrame();
        frame.add(new GamePlay()); // insert GamePlay panel in the frame
        frame.pack(); // set frame PreferredSize
        frame.setTitle("Breakout Ball");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


    }
}
