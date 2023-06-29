package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Color;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Plane;
import mtdm.dk.objects.Sphere;
import mtdm.dk.objects.Triangle;
import processing.core.PApplet;
import processing.core.PGraphics;
import pthreading.PThreadManager;

public class Display extends PApplet{
  private ArrayList<Object> renderObjects= new ArrayList<Object>(); 
  Camera camera;
  boolean start = true;
  private static boolean frameSync = false;
  private long frameCount = 0;
  private static Color[][] pixels;
  private PThreadManager painters;
  private int computeWidth = 500;
  private int computeHeight = 500;
  private int painterWidth = 500;
  private int painterHeight = 500;


  @Override
  public void draw() {
    if(start){
    }
    if(frameSync){
      camera.awaitFrame();
    }
    painters.draw();
    frameCount++;
    if(frameCount%4000 < 1000){
      camera.move(0,0,10f);
    }else if(frameCount%2000 < 2000){
      camera.move(10f,0,0);
    }else if(frameCount%2000 < 3000){
      camera.move(0,0,-10f);
    }else{
      camera.move(-10f,0,0);
    }
    System.out.println(frameCount);
  }
  
  @Override
  public void setup() {
    renderObjects.add(new Plane(4, -2, 2, -4020,new Color(255, 0, 0)));
    renderObjects.add(new Plane(4,8, 6, -12120,new Color(0, 255, 0)));
    renderObjects.add(new Sphere(new Vector(10, 10, 300), 300f, new Color(0, 0, 255)));
    renderObjects.add(new Triangle(new Vector(-100, 1, 2), new Vector(4, 100, 50), new Vector(5, 7, 6), new Color(255, 255, 0)));
    background(0);
    strokeWeight(2);
    camera = new Camera(1000,1000, getGraphics(),renderObjects);
    camera.lookAt(new Vector(10, 10, 300));
    pixels = new Color[width][height];
    painters = new PThreadManager(this);
    for(int y = -height/2; y < height/2; y += painterHeight){
      for(int x = -width/2; x < width/2;x+=painterWidth){
        Painter painter = new Painter(pixels,this,x,x+painterWidth,y,y+painterHeight,width,height);
        painters.addThread(painter);
      }
    }
    camera.render(computeWidth, computeHeight, frameSync);
    start = false;
  }
  
  @Override
  public void settings() {
    size(1000, 1000);
  }
  public static Vector getCamera(){
    return new Vector(0, 0, 0); //temporary
  }
  public static void paint(int x,int y,Color color, PGraphics g){
    if(x < 0 || y < 0) return;
    if(x >= pixels.length ||y >= pixels[0].length) return;
    pixels[x][y] = color;
  }
}
