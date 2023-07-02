package mtdm.dk.vision;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.Ray;

public class WorkUnit {
  public Ray ray;
  public Point pixel;
  public int sampleX;
  public int sampleY;
  public Color color;
  public WorkUnit(Ray ray, Point pixel, int sampleX, int sampleY){
    this.ray = ray;
    this.pixel = pixel;
    this.sampleX = sampleX;
    this.sampleY = sampleY;
  }
}
