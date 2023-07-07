package mtdm.dk.work;

import java.util.concurrent.ThreadLocalRandom;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.vision.Display;

public class OrthographicWork extends Work{
  int x;
  int y;
  int multiSampling;
  Vector origin;
  Vector direction;
  int adjustedHeight;
  int adjustedWidth;

  public OrthographicWork(int x, int y, int multiSampling, Vector origin, Vector direction, int adjustedHeight, int adjustedWidth){
    this.x = x;
    this.y = y;
    this.multiSampling = multiSampling;
    this.origin = origin;
    this.direction = direction;
    this.adjustedHeight = adjustedHeight;
    this.adjustedWidth = adjustedWidth;
  }

  
  public void execute(int maxHit, KDTree renderObjectsTree) {
    Point pixel = new Point((int)x+adjustedWidth,(int)y+adjustedHeight);
    Color[] listOfColors = new Color[multiSampling];
    for (int i = 0; i < multiSampling; i++) {
      Ray ray = new Ray(origin.add(new Vector((int)(x+i/multiSampling),(int)(y+i/multiSampling),0),false),direction); 
      listOfColors[i] = work(ray, maxHit, renderObjectsTree);
    }

    Color finalColor = new Color(0, 0, 0);
    for (Color color : listOfColors) {
      finalColor.add(color, false);
    }

    Display.paint(pixel, adjustedWidth*2, adjustedHeight*2, Color.writeColor(finalColor, multiSampling));
  }
}
