package mtdm.dk.work;

import java.util.concurrent.ThreadLocalRandom;

import mtdm.dk.Point;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.vision.HitRecord;

public class PerspectiveWork extends Work{
  int x;
  int y;
  int Sx;
  int Sy;
  int adjustedWidth;
  int adjustedHeight;
  int multisampling;
  Vector lowerLeftCorner;
  Vector horizontal;
  Vector vertical;
  Vector origin;
  public PerspectiveWork(int x,int y,int Sx,int Sy,int adjustedWidth,int adjustedHeight,Vector lowerLeftCorner,Vector horizontal,Vector vertical,Vector origin, int multisampling){
  this.x = x;
  this.y = y;
  this.Sx = Sx;
  this.Sy = Sy;
  this.adjustedWidth = adjustedWidth;
  this.adjustedHeight = adjustedHeight;
  this.lowerLeftCorner = lowerLeftCorner;
  this.horizontal = horizontal;
  this.vertical = vertical;
  this.origin = origin;
  this.multisampling = multisampling;
  }

  @Override
  public void execute(int maxHit, KDTree renderObjectsTree) {
    float u = ((float) x + (float) adjustedWidth + ThreadLocalRandom.current().nextFloat()) / (float) (adjustedWidth*2); 
    float v = -((float) y + (float) adjustedHeight + ThreadLocalRandom.current().nextFloat()) / (float) (adjustedHeight*2); 
    Ray ray = getRay(u, v);
    Point pixel = new Point(x+adjustedWidth, y+adjustedHeight);
    HitRecord[] hits = new HitRecord[maxHit];
    work(hits, ray, maxHit, renderObjectsTree, multisampling, adjustedHeight, adjustedWidth, pixel, Sx, Sy);
  }
  private Ray getRay(float u, float v) {
    Vector direction = lowerLeftCorner.
    add(horizontal.multi(u), false).
    add(vertical.multi(v), false).
    sub(origin, false);
    return new Ray(origin, direction);
  }
}
