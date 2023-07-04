package mtdm.dk;

import java.util.function.Function;

import mtdm.dk.objects.Material.ScatterResult;
import mtdm.dk.vision.HitRecord;
import mtdm.dk.vision.WorkUnit;

public class Ray {
  private Vector origin;
  private Vector direction;
  private Function <Float,Vector> calc = t-> origin.add(direction.multi(t), false);

  public Ray(Vector origin, Vector direction){
    this.origin = origin;
    this.direction = direction;
    calc = t-> origin.add(direction.multi(t), false);
  }

  public Vector calculate(float t){
    return calc.apply(t);
  }

    public Vector calculate(float t, Color color){
    Vector temp = calc.apply(t);
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

  public ScatterResult bounce(HitRecord rec, WorkUnit workUnit){
    ScatterResult result = rec.getMatPtr().scatter(workUnit.ray, rec, workUnit.color);
    return result;

    // this.origin = rec.getPoint();
    // Vector random = new Vector(-1, 1); // Could also be Vector.randomInHemisphere(rec.getNormal())
    // Vector target = rec.getPoint().add(rec.getNormal(), false).add(random, false); 
    // this.direction = target.sub(rec.getPoint(), false);
    // // this.direction = this.direction.mirrorReflect(rec.getNormal());
  }
}
