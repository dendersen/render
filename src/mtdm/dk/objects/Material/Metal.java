package mtdm.dk.objects.Material;
import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

public class Metal implements Material {
  public Color albedo;

  public Metal(Color a) {
    this.albedo = a;
  }

  @Override
  public ScatterResult scatter(Ray rIn, HitRecord rec, Color attenuation) {
    Vector reflected = Vector.reflect(rIn.getDirection().normalize(false), rec.getNormal());
    Ray scattered = new Ray(rec.getPoint(), reflected);
    attenuation = albedo;
    boolean scatteredValid = (scattered.getDirection().dot(rec.getNormal()) > 0);
    return scatteredValid ? new ScatterResult(scattered, attenuation) : null;
  }

  @Override
  public Color getColor() {
    return albedo;
  }
}
