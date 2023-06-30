package mtdm.dk;

import java.util.function.Function;

import mtdm.dk.vision.HitRecord;

public class Ray {
  private Vector origin;
  private Vector direction;
  private Function <Float,Vector> calc = t-> origin.add(direction.multi(t), false);
  private Point pixel;

  public Ray(Vector origin, Vector direction,Point pixel){
    this.origin = origin;
    this.direction = direction;
    this.pixel = pixel;
    calc = t-> origin.add(direction.multi(t), false);
  }

  public Vector calculate(float t){
    return calc.apply(t);
  }

    public Vector calculate(float t, Color color){
    Vector temp = calc.apply(t);
    temp.setColor(color);
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

  public Point getPixel(){
    return pixel;
  }

  public void bounce(HitRecord hit){
    this.origin = hit.getPoint();
    this.direction = this.direction.mirrorReflect(hit.getNormal());
  }

	public Point makeColor(HitRecord[] hits) {
		if(hits == null || hits[0] == null){
      return pixel;
    }
    return pixel.setColor(hits[0].getColor());
	}
}
