package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.RandomGenerator;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;

/**
 * The Camera class extends the Thread class and is responsible for 
 * creating a camera viewpoint in the rendered environment.
 */
public class Camera {
  private int width, height;
  private ArrayList<Object> renderObjects;
  private Vector origin = new Vector(0, 0, 0);
  private Vector lowerLeftCorner, horizontal, vertical;
  private float Rx = 0,Ry = 0,Rz = 0; //rotation angles
  private Calculator[] t;
  boolean frameSync = false;
  private Vector target; // The point in 3D space that the camera looks at
  private int samplesPerPixel = 100;

  public Camera(int w, int h, ArrayList<Object> renderObjects){
    this.width = w;
    this.height = h;
    this.renderObjects = renderObjects;

    float aspectRatio = width / height;

    float viewportHeight = 2.0f;
    float viewportWidth = aspectRatio * viewportHeight;
    float focalLength = 1.0f;

    this.horizontal = new Vector(viewportWidth, 0, 0);
    this.vertical = new Vector(0, -viewportHeight, 0); // Y is flipped
    this.lowerLeftCorner = origin.sub(horizontal.div(2), false).add(vertical.div(2), false).add(new Vector(0, 0, focalLength), false); // Z is flipped
  }

  public void render(int threadCount, int maxHit){
    t = new Calculator[threadCount];
    Calculator.setRender(renderObjects, maxHit);
    for (int i = 0; i < t.length; i++) {
      t[i] = new Calculator(i, width, height);
    }
    for (int i = 0; i < t.length; i++) {
      t[i].start();
    }
  }

  public void lookAt(Vector target) {
    this.target = target;
  }

  // Method to update rotation around the X-axis
  public void setRx(float Rx) {
    this.Rx = Rx;
  }

  // Method to update rotation around the Y-axis
  public void setRy(float Ry) {
    this.Ry = Ry;
  }

  // Method to update rotation around the Z-axis
  public void setRz(float Rz) {
    this.Rz = Rz;
  }

  public Vector getDirection(){
    // If a target point has been set, ignore the Rx, Ry, Rz rotation angles and instead
    // calculate the direction vector from the camera location to the target point.
    if (target != null) {
      Vector direction = target.sub(origin, false);
      return direction.normalize(true); // Normalize the direction vector to ensure it has unit length
    } else {
      // If no target point has been set, calculate the direction vector based on the rotation angles.
      Vector direction = new Vector(0, 0, 1);
      direction = rotateX(direction, Rx);
      direction = rotateY(direction, Ry);
      direction = rotateZ(direction, Rz);
      return direction;
    }
  }

  public Ray getRay(float u, float v, Point pixel) {
    return new Ray(origin, lowerLeftCorner.add(horizontal.multi(u),false).add(vertical.multi(v), false).sub(origin, false), pixel);
  }

  private Vector rotateX(Vector v, float angle) {
    float y = (float) (v.getY() * Math.cos(angle) - v.getZ() * Math.sin(angle));
    float z = (float) (v.getY() * Math.sin(angle) + v.getZ() * Math.cos(angle));
    return new Vector(v.getX(), y, z);
  }

  private Vector rotateY(Vector v, float angle) {
    float z = (float) (v.getZ() * Math.cos(angle) - v.getX() * Math.sin(angle));
    float x = (float) (v.getZ() * Math.sin(angle) + v.getX() * Math.cos(angle));
    return new Vector(x, v.getY(), z);
  }

  private Vector rotateZ(Vector v, float angle) {
    float x = (float) (v.getX() * Math.cos(angle) - v.getY() * Math.sin(angle));
    float y = (float) (v.getX() * Math.sin(angle) + v.getY() * Math.cos(angle));
    return new Vector(x, y, v.getZ());
  }
  public void addRotation(float Rx,float Ry,float Rz){
    this.Rx += Rx;
    this.Ry += Ry;
    this.Rz += Rz;
  }
  public void move(float Mx,float My,float Mz){
    origin.add(new Vector(Mx, My, Mz), true);
  }

  public Vector getOrigin(){
    return origin;
  }

  public void awaitFrame(int millis) {
    while(Calculator.hasWork()){
      try {
        Thread.sleep(millis);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
    public void awaitFrame() {
    awaitFrame(10);
  }
  public void addFrame(boolean orthographic){
    if(orthographic){
      for (int x = -width/2; x < width/2; x++) {
        for (int y = -height/2; y < height/2; y++) {

          Calculator.addWork(new Ray(new Vector(x,y,0),new Vector(0,0,1),new Point(x,y)));
        }
      }
      return;
    }
    for (int x = -width/2; x < width/2; x++) {
      for (int y = -height/2; y < height/2; y++) {
        float u = (x + width / 2) / (float) width;
        float v = -(y + height / 2) / (float) height;
        Vector direction = lowerLeftCorner.add(horizontal.multi(u), false).add(vertical.multi(v), false).sub(origin, false);
        Calculator.addWork(new Ray(origin, direction, new Point(x, y)));
      }
    }
  }

  private void antialias(int x, int y) {
    Color pixelColor = new Color(0, 0, 0);
    for (int s = 0; s < samplesPerPixel; ++s) {
      float u = (x + width / 2 + RandomGenerator.getRandom()) / (float) width;
      float v = -(y + height / 2 + RandomGenerator.getRandom()) / (float) height;
      Ray r = getRay(u, v, new Point(x, y));
      pixelColor += rayColor(r, world);
      }
      write_color(std::cout, pixel_color, samples_per_pixel);
  }
}