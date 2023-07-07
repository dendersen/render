package mtdm.dk.work;

import java.util.concurrent.ThreadLocalRandom;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.vision.Display;

public class PerspectiveWork extends Work{
  int x;
  int y;
  int Sx;
  int Sy;
  int adjustedWidth;
  int adjustedHeight;
  int multiSampling;
  Vector lowerLeftCorner;
  Vector horizontal;
  Vector vertical;
  Vector origin;
  public PerspectiveWork(int x,int y,int adjustedWidth,int adjustedHeight,Vector lowerLeftCorner,Vector horizontal,Vector vertical,Vector origin, int multiSampling){
  this.x = x;
  this.y = y;
  this.adjustedWidth = adjustedWidth;
  this.adjustedHeight = adjustedHeight;
  this.lowerLeftCorner = lowerLeftCorner;
  this.horizontal = horizontal;
  this.vertical = vertical;
  this.origin = origin;
  this.multiSampling = multiSampling;
  }

  @Override
  public void execute(int maxHit, KDTree renderObjectsTree) {
    Point pixel = new Point(x+adjustedWidth, y+adjustedHeight);
    Color[] listOfColors = new Color[multiSampling];
    for (int i = 0; i < multiSampling; i++) {
      float u = ((float) x + (float) adjustedWidth + ThreadLocalRandom.current().nextFloat()) / (float) (adjustedWidth*2); 
      float v = -((float) y + (float) adjustedHeight + ThreadLocalRandom.current().nextFloat()) / (float) (adjustedHeight*2); 
      Ray ray = getRay(u, v);
      listOfColors[i] = work(ray, maxHit, renderObjectsTree);
    }

    Color finalColor = new Color(0, 0, 0);
    for (Color color : listOfColors) {
      finalColor.add(color, true);
    }

    Color colorToDraw = Color.writeColor(finalColor, multiSampling);
    
    Display.paint(pixel, adjustedWidth*2, adjustedHeight*2, colorToDraw);
  }

  private Ray getRay(float s, float t) {
    Vector direction = lowerLeftCorner
                        .add(horizontal.multi(s), false)
                        .add(vertical.multi(t), false) // flip Y-axis
                        .sub(origin, false);
    return new Ray(origin, direction);
  }
}
