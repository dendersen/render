package mtdm.dk.objects.Material;


import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

public class Lambertian implements Material {
  public Color albedo;

  public Lambertian(Color a) {
    this.albedo = a;
  }

  @Override
  public boolean scatter(Ray rIn, HitRecord rec, Color attenuation, Ray scattered) {
    Vector scatterDirection = rec.getNormal().add(new Vector(-1, 1), false);
    if (scatterDirection.nearZero()) {
      scatterDirection = rec.getNormal();
    }
    scattered = new Ray(rec.getPoint(), scatterDirection);
    attenuation = albedo;
    return true;
  }
}
