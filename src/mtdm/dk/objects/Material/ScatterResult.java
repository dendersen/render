package mtdm.dk.objects.Material;

import mtdm.dk.Color;
import mtdm.dk.Ray;

public class ScatterResult {
  public Ray scattered;
  private Color attenuation;

  public ScatterResult(Ray scattered, Color attenuation) {
    this.scattered = scattered;
    this.attenuation = attenuation;
  }

  public Color getAttenuation() {
    return this.attenuation;
  }
}
