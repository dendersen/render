package mtdm.dk.vision;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
  private int threadCount = 10;
  private int maxHit = 50;
  private static int multiSampling = 500;
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
    // long awaitingPixels = height*width;
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
    Material material_ground = new Lambertian(new Color(0.8f, 0.8f, 0.0f));
    Material material_center = new Lambertian(new Color(0.9f, 0.3f, 0.3f));
    Material material_left   = new Metal(new Color(0.8f, 0.8f, 0.8f), 0.3f);;
    Material material_right  = new Metal(new Color(0.8f, 0.6f, 0.2f), 0);
    
    // Material material_left   = new Lambertian(new Color(0, 0, 1));
    // Material material_right  = new Lambertian(new Color(1, 0, 0));

    // Add the spheres to your object list
    renderObjects.add(new Sphere(new Vector( 0, 100.5f, 1), 100.0f, material_ground));
    renderObjects.add(new Sphere(new Vector( 0,   0, 1),   0.5f, material_center));
    renderObjects.add(new Sphere(new Vector(-1,   0, 1),   0.5f, material_left));
    // renderObjects.add(new Sphere(new Vector(-1,   0, 1),   -0.4f, material_left));
    renderObjects.add(new Sphere(new Vector( 1,   0, 1),   0.5f, material_right));

    renderObjects.add(new Sphere(new Vector( 0,   0.25f, 0.5f),   0.25f, material_right));
    renderObjects.add(new Sphere(new Vector(-1,   0.25f, 0.5f),   0.25f, material_center));
    renderObjects.add(new Sphere(new Vector(1,   0.25f, 0.5f),   0.25f, material_center));

    startTime = System.currentTimeMillis();
    background(0);
    strokeWeight(2);

    Vector lookfrom = new Vector(0,-1,-9);
    Vector lookat = new Vector(0,0,0);
    Vector vup = new Vector(0,-1,0);
    float distToFocus = 9;
    float aperture = 0.1f;

    camera = new Camera(width, height, renderObjects, lookfrom, lookat, vup, 20, aperture, distToFocus);
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

  public ArrayList<Object> randomScene() {
    ArrayList<Object> world = new ArrayList<>();

    Material groundMaterial = new Lambertian(new Color(0.3f, 0.3f, 0));
    world.add(new Sphere(new Vector(0, 1000, 0), 999.9f, groundMaterial));

    // for (int a = -2; a < 2; a++) {
    //     for (int b = -2; b < 2; b++) {
    //         float chooseMat = ThreadLocalRandom.current().nextFloat();
    //         Vector center = new Vector(a + 0.9f * ThreadLocalRandom.current().nextFloat(), -1f, b + 0.9f * ThreadLocalRandom.current().nextFloat());

    //         if (center.sub(new Vector(4, 0.2f, 0), false).length() > 0.9) {
    //             Material sphereMaterial;

    //             if (chooseMat < 0.8f) {
    //                 // diffuse
    //                 Color albedo = new Color(ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat())
    //                 .multi(new Color(ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat()));
    //                 sphereMaterial = new Lambertian(albedo);
    //                 world.add(new Sphere(center, 0.2f, sphereMaterial));
    //             } else if (chooseMat < 0.95f) {
    //                 // metal
    //                 Color albedo = Color.randomColor(0.5f, 1f);
    //                 float fuzz = ThreadLocalRandom.current().nextFloat() * 0.5f;
    //                 sphereMaterial = new Metal(albedo, fuzz);
    //                 world.add(new Sphere(center, 0.2f, sphereMaterial));
    //             } else {
    //                 // glass
    //                 sphereMaterial = new Dielectric(1.5f);
    //                 world.add(new Sphere(center, 0.2f, sphereMaterial));
    //             }
    //         }
    //     }
    // }

    Material material1 = new Lambertian(new Color(0f, 0f, 1f));
    world.add(new Sphere(new Vector(0, -1, 0), 1.0f, material1));

    Material material2 = new Dielectric(1.5f); 
    world.add(new Sphere(new Vector(-2, -1, 0), 1.0f, material2));

    Material material3 = new Lambertian(new Color(1, 0, 0));
    world.add(new Sphere(new Vector(2, -1, 0), 1.0f, material3));
    return world;
  }
}
