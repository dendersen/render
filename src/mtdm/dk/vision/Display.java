package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Color;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Plane;
import mtdm.dk.objects.Square;
import mtdm.dk.objects.Sphere;
import mtdm.dk.objects.Triangle;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Display extends PApplet{
  private ArrayList<Object> renderObjects= new ArrayList<Object>(); 
  Camera camera;
  boolean start = true;
  public static boolean canDraw = false;
  private static boolean frameSync = false;
  private long frameCount = 0;

  @Override
  public void draw() {
    canDraw = true;
    if(start){
      camera = new Camera(1000,1000, getGraphics(),renderObjects);
      camera.render(200, 200, frameSync);
      start = false;
      camera.lookAt(new Vector(10, 10, 300));
    }
    if(frameSync){
      camera.awaitFrame();
    }else{
      try {
        Thread.sleep(25);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    canDraw = false;
    frameCount++;
    // if(frameCount%200 == 0){
    //   camera.addRotation(0, 0.1f, 0);
    // }
    if(frameCount%4000 < 1000){
      camera.move(0,0,1f);
    }else if(frameCount%2000 < 2000){
      camera.move(1f,0,0);
    }else if(frameCount%2000 < 3000){
      camera.move(0,0,-1f);
    }else{
      camera.move(-1f,0,0);
    }
  }
  
  @Override
  public void setup() {
    // renderObjects.add(new Plane(4, -2, 2, -4020,new Color(255, 0, 0)));
    // renderObjects.add(new Plane(4,8, 6, -12120,new Color(0, 255, 0)));
    renderObjects.add(new Sphere(new Vector(10, 10, 300), 300f, new Color(0, 0, 255)));
    // renderObjects.add(new Sphere(new Vector(0, -4, 12), 10f, new Color(255, 0, 0)));
    // renderObjects.add(new Triangle(new Vector(-100, 1, 2), new Vector(4, 100, 50), new Vector(5, 7, 6), new Color(255, 255, 0)));
    background(0);
    strokeWeight(2);
  }
  
  @Override
  public void settings() {
    size(1000, 1000);
  }
  public static Vector getCamera(){
    return new Vector(0, 0, 0); //temporary
  }
  public static synchronized void paint(int x,int y,Color color, PGraphics g){
    g.stroke(color.r,color.g,color.b);
    try {
      while(!Display.canDraw){Thread.sleep(0,20);}
      g.point(x, y);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
