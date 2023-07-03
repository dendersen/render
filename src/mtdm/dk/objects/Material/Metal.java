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
    public boolean scatter(Ray rIn, HitRecord rec, Color attenuation, Ray scattered) {
        Vector reflected = rIn.getDirection().normalize(false).mirrorReflect(rec.getNormal());
        scattered = new Ray(rec.getPoint(), reflected);
        attenuation = albedo;
        return (scattered.getDirection().dot(rec.getNormal()) > 0);
    }
}
