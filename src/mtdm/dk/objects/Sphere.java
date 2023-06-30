package mtdm.dk.objects;

import mtdm.dk.Color;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;
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
  public HitRecord collision(Ray ray) {

    Vector rayToCenter = ray.getOrigin().sub(center, false);
    float lengthSquared = ray.getDirection().lengthSquared();
    float halfB = Vector.dot(rayToCenter, ray.getDirection());
    
    float discriminant = (float)Math.pow(halfB,2) - lengthSquared*(rayToCenter.lengthSquared() - radius*radius);
    
    float t1 = (float) ((-halfB - Math.sqrt(discriminant))/(lengthSquared)); 
    float t2 = (float) ((-halfB + Math.sqrt(discriminant))/(lengthSquared));
    
    Vector normal1 = ray.calculate(t1).sub(center, false).normalize(false);
    Vector normal2 = ray.calculate(t2).sub(center, false).normalize(false);
    
    if (discriminant < 0 || (!Float.isFinite(t1) && !Float.isFinite(t2))) {
      return null;
    }
    Vector collision1 = ray.calculate(t1);
    if(!Float.isFinite(t2)){
      return new HitRecord(collision1, normal1, t1, new Color(normal1));
    }
    Vector collision2 = ray.calculate(t2);
    if(!Float.isFinite(t1)){
      return new HitRecord(collision2, normal2, t2, new Color(normal2));
    }
    if(ray.getOrigin().getDistance(collision1) < ray.getOrigin().getDistance(collision2)){
      return new HitRecord(collision1, normal1, t1, new Color(normal1));
    }else{
      return new HitRecord(collision2, normal2, t2, new Color(normal2));
    }

  }
}
