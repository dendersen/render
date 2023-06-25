package mtdm.dk;

import java.util.function.Function;

public class Ray {
  private float  x, y, z;
  private float Rx,Ry,Rz;
  private Function <Float,Point> calc = t-> new Point(x+Rx*t,y+Ry*t,z+Rz*t);

  public Ray(float x0, float y0, float z0, float Rx, float Ry, float Rz){
    this.x = x0;
    this.y = y0;
    this.z = z0;
    this.Rx = Rx;
    this.Ry = Ry;
    this.Rz = Rz;
  }

  public Point calculate(float t){
    return calc.apply(t);
  }

  public float getRx() {
    return Rx;
  }

  public float getRy() {
    return Ry;
  }

  public float getRz() {
    return Rz;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

}
