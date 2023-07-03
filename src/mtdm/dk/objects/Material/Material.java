package mtdm.dk.objects.Material;

import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.vision.HitRecord;

public interface Material {
  boolean scatter(Ray rIn, HitRecord rec, Color attenuation, Ray scattered);
}

