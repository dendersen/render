package mtdm.dk.vision;

import mtdm.dk.Color;
import processing.core.PApplet;
import pthreading.PThread;
// The Painter class extends PThread and is responsible for
// rendering pixels with the appropriate colors onto the screen.
public class Painter extends PThread {
  private Color[][] pixels;
  private int startX,endX;
  private int startY,endY;
  private int width,height;
  public Painter(Color[][] pixels, PApplet p, int startX,int endX,int startY,int endY,int width,int height){
    super(p);
    this.pixels = pixels;
    this.startX = startX + width/2;
    this.endX = endX + width/2;
    this.startY = startY + height/2;
    this.endY = endY + height/2;
    this.width = width;
    this.height = height;
  }
  @Override
  protected void draw() {
    // Set the stroke weight for the lines
    g.strokeWeight(2);

    // Loop through all the y-coordinates from startY to endY
    for (int y = startY; y < endY; y++) {
      // If y is out of bounds, skip the current iteration
      if(y < 0 || y >= height){
        continue;
      }

      // Loop through all the x-coordinates from startX to endX
      for (int x = startX; x < endX; x++) {
        // If x is out of bounds, skip the current iteration
        if(x < 0 || x >= width){
          continue;
        }
        // Get the color of the pixel at the current position
        Color color = pixels[x][y];
        
        // If the color is not null, set the stroke color
        // to the color of the pixel
        if(color != null){
          g.stroke(color.r,color.g,color.b);
        }else{
          // If the color is null, set the stroke color to black
          g.stroke(0);
        }
        // Draw a point at the current position
        g.point(x, y);
      }
    }
  }
  
}
