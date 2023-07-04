package mtdm.dk.objects;

import mtdm.dk.Vector;
import mtdm.dk.objects.Material.Material;
import mtdm.dk.vision.HitRecord;
import mtdm.dk.Ray;

public class Sphere extends Object {
  private Vector center;
  private float radius;
  private Material material;
  public Sphere(Vector center, float radius, Material material) {
    super(material.getColor());
    this.center = center;
    this.radius = radius;
    this.material = material;
  }

  @Override
  public HitRecord collision(Ray ray, float tMin, float tMax) {

    Vector rayToCenter = ray.getOrigin().sub(center, false);
    float lengthSquared = ray.getDirection().lengthSquared();
    float halfB = Vector.dot(rayToCenter, ray.getDirection());
    
    float discriminant = (float)Math.pow(halfB,2) - lengthSquared*(rayToCenter.lengthSquared() - radius*radius);

    // Find the nearest root that lies in the acceptable range.
    float root = (float) ((-halfB - Math.sqrt(discriminant))/(lengthSquared));
    if (root < tMin || tMax < root) {
      root = (float) ((-halfB + Math.sqrt(discriminant))/(lengthSquared));
      if (root < tMin || tMax < root)
        return null;
    }

    if (discriminant < 0 || !Float.isFinite(root) || root < 0) {
      return null;
    }

    Vector normal = ray.calculate(root).sub(center, false).normalize(false);
    HitRecord hit = new HitRecord(ray.calculate(root, color), normal, root, color, material);
    hit.setFaceNormal(ray, normal);
    return hit;
  }
}
