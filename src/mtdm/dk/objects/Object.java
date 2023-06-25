package mtdm.dk.objects;

import mtdm.dk.Ray;
import mtdm.dk.Color;
import mtdm.dk.Point;

public abstract class Object {
  protected Color color;
  public Color getColor() {
    return color;
  }
  public Object(Color color){
    this.color = color;
  }
  abstract public Point collision(Ray ray);
  
}
