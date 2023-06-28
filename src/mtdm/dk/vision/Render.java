package mtdm.dk.vision;

import java.util.ArrayList;
import mtdm.dk.objects.Object;
import processing.core.PGraphics;
import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;

public class Render extends Thread{
  private int startX,startY,endX,endY;
  private Camera parent;
  private ArrayList<Object> renderObjects;
  private int width,height;
  private PGraphics g;
  boolean frameSync;
  boolean frame = true;
  public Render(int startX,int startY,int endX,int endY, int width, int height, ArrayList<Object> renderObjects, Camera parent, PGraphics g, boolean frameSync){
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    this.parent = parent;
    this.renderObjects = renderObjects;
    this.width = width;
    this.height = height;
    this.g = g;
    this.frameSync = frameSync;
  }
  public void run(){
    while(true){
      while(!frame){
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      ArrayList<Vector> options = new ArrayList<Vector>();
      // Loop through each pixel in the width and height
      for (int y = startY; y <= endY; y++) {
        for (int x = startX; x < endX; x++) {
          Vector location = parent.getLocation();
          Vector direction = parent.getDirection();
          // Create a ray for each pixel from the camera location
          Ray ray = new Ray(location.getX()+x,location.getY()+y,location.getZ(),direction.getX(), direction.getY(), direction.getZ());
          
          // Loop through each renderObject to check for collisions with the ray
          for (int i = 0; i < this.renderObjects.size(); i++) {
            Vector collisions = this.renderObjects.get(i).collision(ray);
            if(collisions == null){
              continue;
            }
            options.add(collisions);
          }
          
          // If there are no collisions, continue to the next pixel
          if(options.size() == 0){
            Display.paint(x+width/2,y+height/2, new Color(0, 0, 0), g);
            continue;
          }
          
          // If there is only one collision, render it
          if(options.size() == 1){
            Display.paint(x+width/2,y+height/2,options.get(0).getColor(),g);
            options.clear();
            continue;
          }
          
          // If there are multiple collisions, draw the closest one
          Vector draw = options.get(0);
          for (int i = 1; i < options.size(); i++) {
            if(draw.getDistance(location)>options.get(i).getDistance(location)){
              draw = options.get(i);
            }
          }
          
          // Paint the pixel in the image with the color of the nearest collision
          Display.paint(x+width/2,y+height/2,draw.getColor(),g);
          options.clear();
        }
      }
      if(frameSync){
        frame = false;
      }
    }
  }
  
  public void startFrame(){
    frame = true;
  }
  public void endFrame(){
    frame = false;
  }
  public synchronized boolean getReady(){
    return frame;
  }
}
