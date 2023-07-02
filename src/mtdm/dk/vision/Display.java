package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Color;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Sphere;
import processing.core.PApplet;
import pthreading.PThreadManager;

public class Display extends PApplet{
  private ArrayList<Object> renderObjects= new ArrayList<Object>(); 
  Camera camera;
  private static Color[][] pixels;
  private PThreadManager painters;
  private int threadCount = 11;
  private int maxHit = 50;
  private static int multiSampling = 2;
  private boolean orthographic = false;
  private int screenHeigth = 1050;
  private int screenWidth = 1050;
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
    renderObjects.add(new Sphere(new Vector(0, 0, 1), 0.5f, Color.Blue));
    renderObjects.add(new Sphere(new Vector(0, 100.5f, 1), 100, Color.DefaultGround));
    // renderObjects.add(new Sphere(new Vector(0, 0, 350), 300, Color.DefaultGround));
    // renderObjects.add(new Sphere(new Vector(0, 600, 350), 300, Color.Green));
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
    size(screenWidth, screenHeigth);
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
