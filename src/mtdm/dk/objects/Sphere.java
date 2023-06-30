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
    
    if (discriminant < 0 || (!Float.isFinite(t1) && !Float.isFinite(t2)) || (t1 < 0 && t2 < 0)) {
      return null;
    }
    Vector collision1 = ray.calculate(t1);
    if(!Float.isFinite(t2) || t2 < 0){
      HitRecord hit = new HitRecord(collision1, normal1, t1, color);
      hit.setFaceNormal(ray, normal1);
      return hit;
    }
    Vector collision2 = ray.calculate(t2);
    if(!Float.isFinite(t1) || t1 < 0){
      HitRecord hit = new HitRecord(collision2, normal2, t2, color);
      hit.setFaceNormal(ray, normal2);
      return hit;
    }
    if(ray.getOrigin().getDistance(collision1) < ray.getOrigin().getDistance(collision2)){
      HitRecord hit = new HitRecord(collision1, normal1, t1, color);
      hit.setFaceNormal(ray, normal1);
      return hit;
    }else{
      HitRecord hit = new HitRecord(collision2, normal2, t2, color);
      hit.setFaceNormal(ray, normal2);
      return hit;
    }
  }
}
