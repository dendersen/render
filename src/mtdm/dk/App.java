package mtdm.dk;

import mtdm.dk.vision.Display;
import processing.core.PApplet;

public class App {
    static Display draw = new Display();
    public static void main(String[] args) throws Exception {
        String[] processingArgs = {"Sketch"};
        PApplet.runSketch(processingArgs,draw);
    }
}
