package mtdm.dk.objects;

import mtdm.dk.Color;
import mtdm.dk.Vector;
import mtdm.dk.vision.Display;
import mtdm.dk.Ray;

public class Sphere extends Object {
  private Vector center;
  private float radius;
  public Sphere(Vector center, float radius, Color color) {
    super(color);
    this.center = center;
    this.radius = radius;
  }

  @Override
  public Vector collision(Ray ray) {
    // Vi finder det der svarer til sqrt(d)
    double sqrt = 
    Math.sqrt(
      -Math.pow(center.getX(),2)*Math.pow(ray.getRy(),2) 
      -Math.pow(center.getX(),2)*Math.pow(ray.getRz(),2) 
      +2*center.getX()*center.getY()*ray.getRx()*ray.getRy() 
      +2*center.getX()*center.getZ()*ray.getRx()*ray.getRz() 
      -2*center.getX()*ray.getRx()*ray.getRy()*ray.getY() 
      -2*center.getX()*ray.getRx()*ray.getRz()*ray.getZ() 
      +2*center.getX()*Math.pow(ray.getRy(),2)*ray.getX() 
      +2*center.getX()*Math.pow(ray.getRz(),2)*ray.getX() 
      -Math.pow(center.getY(),2)*Math.pow(ray.getRx(),2) 
      -Math.pow(center.getY(),2)*Math.pow(ray.getRz(),2) 
      +2*center.getY()*center.getZ()*ray.getRy()*ray.getRz() 
      +2*center.getY()*Math.pow(ray.getRx(),2)*ray.getY() 
      -2*center.getY()*ray.getRx()*ray.getRy()*ray.getX() 
      -2*center.getY()*ray.getRy()*ray.getRz()*ray.getZ() 
      +2*center.getY()*Math.pow(ray.getRz(),2)*ray.getY() 
      -Math.pow(center.getZ(),2)*Math.pow(ray.getRx(),2) 
      -Math.pow(center.getZ(),2)*Math.pow(ray.getRy(),2) 
      +2*center.getZ()*Math.pow(ray.getRx(),2)*ray.getZ() 
      -2*center.getZ()*ray.getRx()*ray.getRz()*ray.getX() 
      +2*center.getZ()*Math.pow(ray.getRy(),2)*ray.getZ() 
      -2*center.getZ()*ray.getRy()*ray.getRz()*ray.getY() 
      +Math.pow(ray.getRx(),2)*Math.pow(radius,2) 
      -Math.pow(ray.getRx(),2)*Math.pow(ray.getY(),2) 
      -Math.pow(ray.getRx(),2)*Math.pow(ray.getZ(),2) 
      +2*ray.getRx()*ray.getRy()*ray.getX()*ray.getY() 
      +2*ray.getRx()*ray.getRz()*ray.getX()*ray.getZ() 
      +Math.pow(ray.getRy(),2)*Math.pow(radius,2) 
      -Math.pow(ray.getRy(),2)*Math.pow(ray.getX(),2) 
      -Math.pow(ray.getRy(),2)*Math.pow(ray.getZ(),2) 
      +2*ray.getRy()*ray.getRz()*ray.getY()*ray.getZ() 
      +Math.pow(ray.getRz(),2)*Math.pow(radius,2) 
      -Math.pow(ray.getRz(),2)*Math.pow(ray.getX(),2) 
      -Math.pow(ray.getRz(),2)*Math.pow(ray.getY(),2)
    );

    // Vi finder det der svarer til -b
    double math = 
    (
      +center.getX()*ray.getRx() 
      +center.getY()*ray.getRy() 
      +center.getZ()*ray.getRz() 
      -ray.getRx()*ray.getX() 
      -ray.getRy()*ray.getY() 
      -ray.getRz()*ray.getZ()
    );
    
    // Vi finder det der svarer til 2a
    double divident = 
    (
      +Math.pow(ray.getRy(),2) 
      +Math.pow(ray.getRz(),2) 
      +Math.pow(ray.getRz(),2)
    );
    
    // Vi løser andengradsligningen t = (-b +- sqrt(d)) / 2a
    float t1 = (float) ((math - sqrt)/(divident)); 
    float t2 = (float) ((math + sqrt)/(divident));
    
    // We check whether or not we want the points to be displayed
    if(
      Float.isFinite(t1) &&
      t2 > 0 && 
      ray.calculate(t1).getDistance(Display.getCamera())
      <=
      ray.calculate(t2).getDistance(Display.getCamera())
    ){
      Vector collision = ray.calculate(t1);
      collision.setColor(color);
      return collision;
    }
    if(
      Float.isFinite(t2) && 
      t2 > 0
    ){
      Vector collision = ray.calculate(t2);
      collision.setColor(color);
      return collision;
    }
    return null;
  }
}
