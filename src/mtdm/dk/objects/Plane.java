package mtdm.dk.objects;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.Ray;

public class Plane extends Object{
  float a,b,c,d;
  
  public Plane(float a, float b, float c, float d, Color color){
    super(color);
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  @Override
  public Point collision(Ray ray) {
    Float t = -this.a*ray.getRx()-this.b*ray.getRy()-this.c*ray.getRz();
    if(!Float.isFinite(t) && t <= 0) return null;
    
    Point collision = ray.calculate(t);
    collision.setColor(color);
    return collision;
  }
}
