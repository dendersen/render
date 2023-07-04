package mtdm.dk.objects.Material;
import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

public class Metal implements Material {
  public Color albedo;
  private float fuzz;

  public Metal(Color a, float fuzz) {
    this.albedo = a;
    this.fuzz = fuzz;
  }

  @Override
  public ScatterResult scatter(Ray rIn, HitRecord rec, Color attenuation) {
    Vector reflected = Vector.reflect(rIn.getDirection().normalize(false), rec.getNormal());
    Vector random = Vector.randomInUnitSphere();
    Vector scatteredDirection = reflected.add(random.multi(fuzz), false);
    Ray scattered = new Ray(rec.getPoint(), scatteredDirection);
    attenuation = albedo;
    boolean scatteredValid = (scattered.getDirection().dot(rec.getNormal()) > 0);
    return scatteredValid ? new ScatterResult(scattered, attenuation) : null;
  }

  @Override
  public Color getColor() {
    return albedo;
  }
}
