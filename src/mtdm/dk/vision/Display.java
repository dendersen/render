package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Color;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Sphere;
import mtdm.dk.objects.Material.Lambertian;
import mtdm.dk.objects.Material.Material;
import mtdm.dk.objects.Material.Metal;
import processing.core.PApplet;

public class Display extends PApplet{
  private ArrayList<Object> renderObjects= new ArrayList<Object>(); 
  Camera camera;
  private static Color[][] pixels;
  private int threadCount = 200;
  private int maxHit = 50;
  private static int multiSampling = 12;
  private boolean orthographic = false;
  private int screenHeight = 1000;
  private int screenWidth = 1000;
  private long startTime;
  
  @Override
  public void draw() {
    camera.awaitFrame();
    // render();
    for (int y = 0; y < height; y++) {
      if(y < 0 || y >= height){
        continue;
      }
      for (int x = 0; x < width; x++) {
        if(x < 0 || x >= width){
          continue;
        }
        Color[] colors = new Color[(int)Math.pow(multiSampling,2)];
        for (int i = 0; i < multiSampling; i++) {
          for (int j = 0; j < multiSampling; j++) {
            colors[i*multiSampling+j] = pixels[x*multiSampling+i][y*multiSampling+j];
          }
        }
        Color out = Color.average(colors);
        if(out != null){
          g.stroke(out.r,out.g,out.b);
        }
        g.point(x, y);
      }
    }
    // camera.move(10, 0, 0);
    camera.addFrame(orthographic);
    System.out.println((double)(System.currentTimeMillis()-startTime)/(double)(frameCount+1)/1000 + " sec/frame");
  }
  
  @Override
  public void setup() {
    // Scale color values by 255 for each material
    Material material_ground = new Lambertian(new Color(188, 188, 0));
    Material material_center = new Lambertian(new Color(255, 0, 0));
    Material material_left   = new Metal(new Color(188, 188, 188), 0.3f);
    Material material_right  = new Metal(new Color(188, 125, 52), 1.0f);

    // Add the spheres to your object list
    renderObjects.add(new Sphere(new Vector( 0.0f, 100.5f, 1.0f), 100.0f, material_ground));
    renderObjects.add(new Sphere(new Vector( 0.0f,    0.0f, 1.0f),   0.5f, material_center));
    renderObjects.add(new Sphere(new Vector(-1.0f,    0.0f, 1.0f),   0.5f, material_left));
    renderObjects.add(new Sphere(new Vector( 1.0f,    0.0f, 1.0f),   0.5f, material_right));

    startTime = System.currentTimeMillis();
    background(0);
    strokeWeight(2);
    camera = new Camera(width, height, renderObjects);
    pixels = new Color[width*multiSampling][height*multiSampling];
    camera.render(threadCount,maxHit,multiSampling);
    camera.addFrame(orthographic);
  }
  
  @Override
  public void settings() {
    size(screenWidth, screenHeight);
  }
  public static Vector getCamera(){
    return new Vector(0, 0, 0); //temporary
  }

  public static void paint(WorkUnit Work, int width, int height){
    if(Work.pixel.getX() < 0  || Work.pixel.getX() > width){
      return;
    }
    if(Work.pixel.getY() < 0  || Work.pixel.getY() > height){
      return;
    }
    pixels[
      Work.pixel.getX()*multiSampling + Work.sampleX
    ][
      Work.pixel.getY()*multiSampling + Work.sampleY
    ] = Work.color;
  }
}
