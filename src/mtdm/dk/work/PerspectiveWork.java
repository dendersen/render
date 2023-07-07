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
  Vector u;
  Vector v;
  float lensRadius;
  public PerspectiveWork(int x,int y,int adjustedWidth,int adjustedHeight,Vector lowerLeftCorner,Vector horizontal,Vector vertical,Vector origin, float lensRadius, Vector u, Vector v, int multiSampling){
  this.x = x;
  this.y = y;
  this.adjustedWidth = adjustedWidth;
  this.adjustedHeight = adjustedHeight;
  this.lowerLeftCorner = lowerLeftCorner;
  this.horizontal = horizontal;
  this.vertical = vertical;
  this.origin = origin;
  this.multiSampling = multiSampling;
  this.lensRadius = lensRadius;
  this.u = u;
  this.v = v;
  }

  @Override
  public void execute(int maxHit, KDTree renderObjectsTree) {
    Point pixel = new Point(x+adjustedWidth, y+adjustedHeight);
    Color[] listOfColors = new Color[multiSampling];
    // if(y == 100) {
    //   int idk = 1;
    // } else if (y == 200){
    //   int idk = 2;
    // }

    for (int i = 0; i < multiSampling; i++) {
      float s = ((float) x + (float) adjustedWidth + ThreadLocalRandom.current().nextFloat()) / (float) (adjustedWidth*2); 
      float t = -((float) y + (float) adjustedHeight + ThreadLocalRandom.current().nextFloat()) / (float) (adjustedHeight*2); 
      Ray ray = getRay(s, t, lensRadius, u, v);
      listOfColors[i] = work(ray, maxHit, renderObjectsTree);
    }

    Color finalColor = new Color(0, 0, 0);
    for (Color color : listOfColors) {
      finalColor.add(color, true);
    }

    Color colorToDraw = Color.writeColor(finalColor, multiSampling);
    
    Display.paint(pixel, adjustedWidth*2, adjustedHeight*2, colorToDraw);
  }

  private Ray getRay(float s, float t, float lensRadius, Vector u, Vector v) {
    Vector rd = Vector.randomInUnitDisk().multi(lensRadius);
    Vector offset = u.multi(rd.getX()).add(v.multi(rd.getY()), false);

    Vector target = lowerLeftCorner
                        .add(horizontal.multi(s), false)
                        .add(vertical.multi(t), false)
                        .sub(origin, false)
                        .sub(offset, false);
    return new Ray(origin.add(offset, false), target);
  }
}
