package mtdm.dk;

import java.util.ArrayList;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Plane;
import mtdm.dk.objects.Sphere;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Display extends PApplet{
  private ArrayList<Object> renderObjects= new ArrayList<Object>(); 
  Thread t;
  boolean start = true;
  public static boolean canDraw = false;
  private static Point camera = new Point(0, 0, 0);
  @Override
  public void draw() {
    canDraw = true;
    if(start){
      t = new InnerDisplay(1000,1000, getGraphics(),renderObjects);
      t.start();
      start = false;
    }
    try {
      Thread.sleep(25);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    canDraw = false;
  }
  
  @Override
  public void setup() {
    // renderObjects.add(new Plane(3,5f, 1, 0,new Color(0, 0, 255)));
    // renderObjects.add(new Plane(6,3f, 5, 0,new Color(255, 0, 0)));
    // renderObjects.add(new Plane(1,3f, 10, 3,new Color(0, 255, 0)));
    renderObjects.add(new Sphere(new Point(0, 0, 15), 5, new Color(255, 0, 255)));
    background(0);
    strokeWeight(2);
  }
  
  @Override
  public void settings() {
    size(1000, 1000);
  }
  public static void paint(int x,int y,Color color, PGraphics g){
    try{
      g.stroke(color.r,color.g,color.b);
    }catch(NullPointerException e){
      // System.out.println(color);
    }
    try {
      while(!canDraw){Thread.sleep(0,20);}
      g.point(x, y);
    } catch (NullPointerException e) {
      // System.out.println(x);
      // System.out.println(y);
      // System.out.println(g);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  public static Point getCamera(){
    return camera;
  }
  /**
   * InnerDisplay
   */
  public class InnerDisplay extends Thread {
    int width;
    int height;
    PGraphics g;
    ArrayList<Object> renderObjects;
    public InnerDisplay(int w, int h, PGraphics g,ArrayList<Object> renderObjects){
      this.width = w;
      this.height = h;
      this.g = g;
      this.renderObjects = renderObjects;
    }
    public void run(){
      while(true){
        ArrayList<Point> options = new ArrayList<Point>();
        for (int y = -this.height/2; y < this.height/2; y++) {
          for (int x = -this.width/2; x < this.width/2; x++) {
            Ray ray = new Ray(camera.getX(),camera.getY(),camera.getZ(),x / 16f, y / 16f, 1);
            for (int i = 0; i < this.renderObjects.size(); i++) {
              Point collisions = this.renderObjects.get(i).collision(ray);
              if(collisions == null){
                continue;
              }
              options.add(collisions);
            }
            if(options.size() == 0){
              continue;
            }
            if(options.size() == 1){
              Display.paint(x+width/2,y+width/2,options.get(0).getColor(),g);
              options.clear();
              continue;
            }
            Point draw = options.get(0);
            for (int i = 1; i < options.size(); i++) {
              if(draw.getDistance(camera)>options.get(i).getDistance(camera)){
                draw = options.get(i);
              }
            }
            Display.paint(x+width/2,y+width/2,draw.getColor(),g);
            options.clear();
          }
        }
      }
    }
  }
}