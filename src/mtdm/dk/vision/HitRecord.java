package mtdm.dk.vision;
import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.objects.Material.Material;

/**
 * HitRecord
 */
public class HitRecord {
  private Vector point;
  private Vector normal;
  private Material matPtr;
  private float t;
  private boolean frontFace;
  private Color color;

  // Constructor
  public HitRecord(Vector p, Vector normal, float t, Color color, Material material) {
    this.point = p;
    this.normal = normal;
    this.matPtr = material;
    this.t = t;
    this.color = color;
  }

  // Getters
  public Material getMatPtr() {
    return this.matPtr;
  }

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

  public void setColor(Color color) {
    this.color = color;
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

  public void reset() {
    this.point = null;
    this.normal = null;
    this.matPtr = null;
    this.t = 0f;
    this.color = null;
    this.frontFace = false;
  }
}
