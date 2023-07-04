package mtdm.dk.objects.Material;

import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.vision.HitRecord;

public interface Material {
  ScatterResult scatter(Ray rIn, HitRecord rec, Color attenuation);
  Color getColor();
}
