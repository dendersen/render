package mtdm.dk.objects;

import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.Color;
import mtdm.dk.vision.HitRecord;

public abstract class Object {
  protected Color color;
  public Color getColor() {
    return color;
  }
  public Object(Color color){
    this.color = color;
  }
  abstract public HitRecord collision(Ray ray, float tMin, float tMax);

  public abstract Vector getCenter(); // Declare abstract getCenter method
}
