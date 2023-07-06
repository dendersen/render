package mtdm.dk.objects.Material;

import java.util.concurrent.ThreadLocalRandom;

import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

public class Dielectric implements Material {
  private float indexOfRefraction;

  public Dielectric(float indexOfRefraction) {
    this.indexOfRefraction = indexOfRefraction;
  }

  @Override
  public ScatterResult scatter(Ray rIn, HitRecord rec, Color attenuation) {
    Color outAttenuation = Color.White;
    float refractionRatio = rec.getFrontFace() ? (1.0f / indexOfRefraction) : indexOfRefraction;

    Vector unitDirection = rIn.getDirection().normalize(false);
    float cosTheta = Math.min(Vector.dot(unitDirection.getNegative(), rec.getNormal()), 1);
    float sinTheta = (float) Math.sqrt(1f-Math.pow(cosTheta, 2));

    boolean cannotRefract = refractionRatio * sinTheta > 1.0;
    Vector direction;

    if (cannotRefract || reflectance(cosTheta, refractionRatio) > ThreadLocalRandom.current().nextFloat())
      direction = Vector.reflect(unitDirection, rec.getNormal());
    else
      direction = Vector.refract(unitDirection, rec.getNormal(), refractionRatio);

    Ray scattered = new Ray(rec.getPoint(), direction);
    return new ScatterResult(scattered, outAttenuation);
  }

  @Override
  public Color getColor() {
    return Color.White; // assuming that the color for Dielectric is white as per your C++ code.
  }

  private float reflectance(float cosine, float refIdx) {
    // Use Schlick's approximation for reflectance.
    float r0 = (float) Math.pow((1f - refIdx) / (1f + refIdx),2);
    return r0 + (1f - r0) * (float) Math.pow(1f - cosine, 5);
  }
}
