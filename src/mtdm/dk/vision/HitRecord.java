package mtdm.dk.vision;
import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;

/**
 * HitRecord
 */
public class HitRecord {
  private Vector point;
  private Vector normal;
  private float t;
  private boolean frontFace;
  private Color color;

  // Constructor
  public HitRecord(Vector p, Vector normal, float t, Color color) {
    this.point = p;
    this.normal = normal;
    this.t = t;
    this.color = color;
  }

  // Getters
  public Vector getPoint() {
    return this.point;
  }

  public Vector getNormal() {
    return this.normal;
  }

  public float getT() {
    return this.t;
  }

  // Setters
  public void setPoint(Vector p) {
    this.point = p;
  }

  public void setNormal(Vector normal) {
    this.normal = normal;
  }

  public void setFaceNormal(Ray ray, Vector outwardNormal) {
    this.frontFace = Vector.dot(ray.getDirection(), outwardNormal) < 0;
    this.normal = frontFace ? outwardNormal : outwardNormal.getNegative();
  }

  public void setT(Float t) {
    this.t = t;
  }
  public Color getColor(){
    return color;
  }
}
