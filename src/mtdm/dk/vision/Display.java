package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Sphere;
import mtdm.dk.objects.Material.Dielectric;
import mtdm.dk.objects.Material.Lambertian;
import mtdm.dk.objects.Material.Material;
import mtdm.dk.objects.Material.Metal;
import processing.core.PApplet;

public class Display extends PApplet{
  private ArrayList<Object> renderObjects= new ArrayList<Object>(); 
  Camera camera;
  private static Color[][] pixels;
  private int threadCount = 20;
  private int maxHit = 50;
  private static int multiSampling = 100;
  private boolean orthographic = false;
  private int screenHeight = 1000;
  private int screenWidth = 1000;
  private long startTime;
  private boolean frameSync = true;

  @Override
  public void draw() {
    if(!Calculator.hasWork()){
      startTime = System.currentTimeMillis();
      camera.addFrame(orthographic,frameSync);
    }
    if(frameSync){
      camera.awaitFrame();
    }
    // render();
    long awaitingPixels = height*width;
    // System.out.println();
    for (int y = 0; y < height; y++) {
      // if(y < 0 || y >= height){
      //   continue;
      // }
      for (int x = 0; x < width; x++) {
        // if(x < 0 || x >= width){
        //   continue;
        // }

        Color out = pixels[x][y];
        if(out != null){
          g.stroke(out.r,out.g,out.b);
        }
        g.point(x, y);
        // System.out.print((awaitingPixels-x-y*width)+ "p   \r");
      }
    }
    System.out.println((double)(System.currentTimeMillis()-startTime)/1d/1000d + " sec/frame");
    // camera.move(0,0,-10);
  }
  
  @Override
  public void setup() {
    // Scale color values by 255 for each material
    Material material_ground = new Lambertian(new Color(0.8f, 0.8f, 0));
    Material material_center = new Lambertian(new Color(0.1f, 0.2f, 0.5f));
    Material material_left   = new Dielectric(1.5f);
    Material material_right  = new Metal(new Color(0.8f, 0.6f, 0.2f), 0);

    // Add the spheres to your object list
    renderObjects.add(new Sphere(new Vector( 0, 100.5f, 1), 100.0f, material_ground));
    renderObjects.add(new Sphere(new Vector( 0,   0, 1),   0.5f, material_center));
    renderObjects.add(new Sphere(new Vector(-1,   0, 1),   0.5f, material_left));
    renderObjects.add(new Sphere(new Vector(-1,   0, 1),   -0.4f, material_left));
    renderObjects.add(new Sphere(new Vector( 1,   0, 1),   0.5f, material_right));

    startTime = System.currentTimeMillis();
    background(0);
    strokeWeight(2);
    camera = new Camera(width, height, renderObjects);
    pixels = new Color[width][height];
    camera.render(threadCount,maxHit,multiSampling);
  }
  
  @Override
  public void settings() {
    size(screenWidth, screenHeight);
  }
  public static Vector getCamera(){
    return new Vector(0, 0, 0); //temporary
  }

  public static void paint(Point Pixel, int width, int height, Color color){
    if(Pixel.getX() < 0  || Pixel.getX() > width){
      return;
    }
    if(Pixel.getY() < 0  || Pixel.getY() > height){
      return;
    }
    pixels[Pixel.getX()][Pixel.getY()] = color;
  }
}
