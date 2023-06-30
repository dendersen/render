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
    g.strokeWeight(2);
    
    for (int y = startY; y < endY; y++) {
      if(y < 0 || y >= height){
        continue;
      }
      
      for (int x = startX; x < endX; x++) {
        if(x < 0 || x >= width){
          continue;
        }
        
        Color color = pixels[x][y];
        
        if(color != null){
          g.stroke(color.r,color.g,color.b);
        }else{
          g.stroke(0);
        }
        
        g.point(x, y);
      }
    }
  }
}
