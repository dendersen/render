package mtdm.dk.work;

import mtdm.dk.Point;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.vision.HitRecord;

public class OrthographicWork extends Work{
  int x;
  float Sx;
  int y;
  float Sy;
  int multiSampling;
  Vector origin;
  Vector direction;
  int adjustedHeight;
  int adjustedWidth;

  public OrthographicWork(int x, int Sx, int y, int Sy, int multiSampling, Vector origin, Vector direction, int adjustedHeight, int adjustedWidth){
    this.x = x;
    this.y = y;
    this.Sy = Sy;
    this.Sx = Sx;
    this.multiSampling = multiSampling;
    this.origin = origin;
    this.direction = direction;
    this.adjustedHeight = adjustedHeight;
    this.adjustedWidth = adjustedWidth;
  }

  
  public void execute(int maxHit, KDTree renderObjectsTree) {
    Ray ray = new Ray(origin.add(new Vector((int)(x+Sx/multiSampling),(int)(y+Sy/multiSampling),0),false),direction);
    Point pixel = new Point((int)x+adjustedWidth,(int)y+adjustedHeight);
    HitRecord[] hits = new HitRecord[maxHit]; 
    work(hits, ray, maxHit, renderObjectsTree, multiSampling, adjustedHeight, adjustedWidth, pixel, Sx, Sy);
  }
}
