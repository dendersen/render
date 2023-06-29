package mtdm.dk.vision;

import java.util.ArrayList;

import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import processing.core.PGraphics;

/**
 * The Camera class extends the Thread class and is responsible for 
 * creating a camera viewpoint in the rendered environment.
 */
public class Camera {
  private int width, height;
  private PGraphics g;
  private ArrayList<Object> renderObjects;
  private Vector location = new Vector(0, 0, 0);
  private float Rx = 0,Ry = 0,Rz = 0; //rotation angles
  private Render[] t;
  boolean frameSync = false;
  private Vector target; // The point in 3D space that the camera looks at

  public Camera(int w, int h, PGraphics g,ArrayList<Object> renderObjects){
    this.width = w;
    this.height = h;
    this.g = g;
    this.renderObjects = renderObjects;
  }

  public void render(int threadWidth, int threadHeight, boolean frameSync){
    this.frameSync = frameSync;
    t = new Render[(int)(Math.ceil(width/(float)threadWidth)*Math.ceil(height/(float)threadHeight))];

    int i = 0;
    for(int y = -height/2; y < height/2; y+=threadHeight){
      for(int x = -width/2; x < width/2; x+=threadWidth){
        t[i] = new Render(x, y, x+threadWidth, y+threadHeight, width, height, renderObjects, this, g, frameSync);
        i++;
      }
    }
    for (Render render : t) {
      if(!frameSync){
        render.setPriority(3);
      }
      render.start();
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
      Vector direction = target.sub(location, false);
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
    location.add(new Vector(Mx, My, Mz), true);
  }

  public Vector getLocation(){
    return location;
  }

  public void awaitFrame(int millis) {
    for (int i = 0; i < t.length; i++) {
      while(t[i].getReady()){}
    }
    for (int i = 0; i < t.length; i++) {
      t[i].startFrame();
    }
  }
    public void awaitFrame() {
    awaitFrame(10);
  }
}