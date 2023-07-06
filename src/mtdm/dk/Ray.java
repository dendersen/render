package mtdm.dk;

import mtdm.dk.objects.Material.ScatterResult;
import mtdm.dk.vision.HitRecord;

public class Ray {
  private Vector origin;
  private Vector direction;

  public Ray(Vector origin, Vector direction){
    this.origin = origin;
    this.direction = direction;
  }

    public Vector calculate(float t){
    Vector temp = origin.add(direction.multi(t), false);
    return temp;
  }

  public Vector getOrigin() {
    return origin;
  }

  public Vector getDirection() {
    return direction;
  }

  public float getRx() {
    return direction.getX();
  }

  public float getRy() {
    return direction.getY();
  }

  public float getRz() {
    return direction.getZ();
  }

  public float getX() {
    return origin.getX();
  }

  public float getY() {
    return origin.getY();
  }

  public float getZ() {
    return origin.getZ();
  }

  public ScatterResult bounce(HitRecord rec, Ray ray, Color color){
    ScatterResult result = rec.getMatPtr().scatter(ray, rec, color);
    return result;

    // this.origin = rec.getPoint();
    // Vector random = new Vector(-1, 1); // Could also be Vector.randomInHemisphere(rec.getNormal())
    // Vector target = rec.getPoint().add(rec.getNormal(), false).add(random, false); 
    // this.direction = target.sub(rec.getPoint(), false);
    // // this.direction = this.direction.mirrorReflect(rec.getNormal());
  }
}
