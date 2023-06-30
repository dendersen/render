package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.RandomGenerator;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Plane;
import mtdm.dk.objects.Sphere;
import mtdm.dk.objects.Triangle;
import processing.core.PApplet;
import pthreading.PThreadManager;
import mtdm.dk.Ray;

public class Display extends PApplet{
  private ArrayList<Object> renderObjects= new ArrayList<Object>(); 
  Camera camera;
  private long frameCount = 0;
  private static Color[][] pixels;
  private PThreadManager painters;
  private int threadCount = 200;
  private int maxHit = 1;
  

  @Override
  public void draw() {
    while(Calculator.hasWork()){
      try {
        Thread.sleep(15);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    // render();
    for (int y = 0; y < height; y++) {
      if(y < 0 || y >= height){
        continue;
      }
      for (int x = 0; x < width; x++) {
        if(x < 0 || x >= width){
          continue;
        }

        Color color = pixels[x][y];
        if(color != null){
          g.stroke(color.r,color.g,color.b);
        }else{
          g.stroke(0);
        }
        g.point(x, y);
      }
    }
    // painters.draw();
    System.out.println(frameRate);
    // frameCount++;
    // if(frameCount%4000 < 1000){
    //   camera.move(0,0,10f);
    // }else if(frameCount%2000 < 2000){
    //   camera.move(10f,0,0);
    // }else if(frameCount%2000 < 3000){
    //   camera.move(0,0,-10f);
    // }else{
    //   camera.move(-10f,0,0);
    // }
    camera.addFrame(false);
  }
  
  @Override
  public void setup() {
    // renderObjects.add(new Plane(4, -2, 2, -4020,new Color(255, 0, 0)));
    // renderObjects.add(new Plane(new Vector(0, 0, 0), new Vector(0, 0, 1), new Vector(0, 0, 3),new Color(255, 0, 0)));
    renderObjects.add(new Sphere(new Vector(0, 0, 1), 0.5f, new Color(0, 0, 255)));
    // renderObjects.add(new Sphere(new Vector(0, 0, 3), 2f, new Color(0, 255, 0)));
    // renderObjects.add(new Triangle(new Vector(-100, 1, 2), new Vector(4, 100, 50), new Vector(5, 7, 6), new Color(255, 255, 0)));
    
    background(0);
    strokeWeight(2);
    camera = new Camera(width, height, renderObjects);
    // camera.lookAt(new Vector(0, 0, 0));
    pixels = new Color[width][height];
    // painters = new PThreadManager(this);
    // for(int y = -height/2; y < height/2; y += painterHeight){
    //   for(int x = -width/2; x < width/2; x+=painterWidth){
    //     Painter painter = new Painter(pixels,this,x,x+painterWidth,y,y+painterHeight,width,height);
    //     painters.addThread(painter);
    //   }
    // }
    camera.render(threadCount,maxHit);
  }
  
  @Override
  public void settings() {
    size(1000, 1000);
  }
  public static Vector getCamera(){
    return new Vector(0, 0, 0); //temporary
  }
  public static void paint(Point pixel, int width, int height){
    if(pixel.getX()+width/2 < 0  || pixel.getX()+width/2 > width){
      return;
    }
    if(pixel.getY()+height/2 < 0  || pixel.getY()+height/2 > height){
      return;
    }
    pixels[pixel.getX()+width/2][pixel.getY()+height/2] = pixel.getColor();
  }

  // private void render(){
  //   Ray[][] rays = new Ray[width][height]; 
  //   for (int x = -width/2; x < width/2; x++) {
  //     for (int y = -height/2; y < height/2; y++) {
  //       rays[x+width/2][y+height/2] = new Ray(new Vector(x,y,0),new Vector(0,0,1),new Point(x,y));
  //     }
  //   }
    
  //   for (int i = 0; i < rays.length; i++) {
  //     for (int j = 0; j < rays[0].length; j++) {
  //       for (int o = 0; o < renderObjects.size(); o++) {
  //         HitRecord collision = renderObjects.get(o).collision(rays[i][j]);
  //         paint(rays[i][j].makeColor(new HitRecord[]{collision}),width,height);
  //       }
  //     }
  //   }
  // }
}
