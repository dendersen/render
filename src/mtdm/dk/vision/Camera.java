package mtdm.dk.vision;

import java.util.ArrayList;
import mtdm.dk.Vector;
import mtdm.dk.objects.Object;
import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.work.OrthographicWork;
import mtdm.dk.work.PerspectiveWork;

/**
 * The Camera class extends the Thread class and is responsible for 
 * creating a camera viewpoint in the rendered environment.
 */
public class Camera {
  private int width, height, adjustedWidth, adjustedHeight;
  private ArrayList<Object> renderObjects;
  private Vector origin = new Vector(0, 0, 0);
  private Vector lowerLeftCorner, horizontal, vertical;
  private float Rx = 0,Ry = 0,Rz = 0; //rotation angles
  private Calculator[] t;
  boolean frameSync = false;
  private Vector lookAt; // The point in 3D space that the camera looks at
  private float multiSampling;
  private static KDTree renderObjectsTree;
  private float aspectRatio;
  private float aperture;
  private float focusDist;
  private float lensRadius;
  private Vector u;
  private Vector v;

  public Camera(int w, int h, ArrayList<Object> renderObjects, Vector lookFrom, Vector lookAt, Vector vup, float vfov, float aperture, float focusDist){
    this.width = w;
    this.height = h;
    this.lookAt = lookAt;
    this.focusDist = focusDist;
    this.aperture = aperture;
    this.aspectRatio = (float) width / height; // Set aspect ratio before using it
    float theta = (float) Math.toRadians(vfov);
    float hNew = (float) Math.tan(theta / 2);
    float viewportHeight = 2.0f * hNew;
    float viewportWidth = aspectRatio * viewportHeight;

    Vector wVector = lookAt.sub(lookFrom, false).normalize(false); // direction from lookFrom to lookAt
    this.u = vup.cross(wVector).normalize(false); // right direction
    this.v = u.cross(wVector); // up direction

    this.origin = lookFrom;
    this.horizontal = u.multi(-viewportWidth).multi(focusDist);
    this.vertical = v.multi(-viewportHeight).multi(focusDist); // flip Y-axis
    this.lowerLeftCorner = origin.sub(horizontal.div(2), false)
                                .add(vertical.div(2), false) // flip Y-axis
                                .add(wVector.multi(focusDist), false); // flip Z-axis
    
    this.lensRadius = aperture/2f;

    this.renderObjects = renderObjects;
    
    this.adjustedWidth = width/2;
    this.adjustedHeight = height/2;
  }
  
  public void render(int threadCount, int maxHit, int multiSampling){
    t = new Calculator[threadCount];
    renderObjectsTree = new KDTree(renderObjects);

    Calculator.setRender(renderObjectsTree, maxHit);
    this.multiSampling = multiSampling;
    for (int i = 0; i < t.length; i++) {
      t[i] = new Calculator(i, width, height);
    }
    for (int i = 0; i < t.length; i++) {
      t[i].start();
    }
  }

  public void lookAt(Vector lookAt) {
    this.lookAt = lookAt;
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
    if (lookAt != null) {
      Vector direction = lookAt.sub(origin, false);
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
    origin.add(new Vector(Mx, My, Mz), true);
  }

  public Vector getOrigin(){
    return origin;
  }

  public void awaitFrame(int millis) {
    while(Calculator.hasWork()){
      System.out.print(Calculator.getWorkCount() + "w         \r");
      try {
        Thread.sleep(millis);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
    public void awaitFrame() {
    awaitFrame(5);
  }
  public void addFrame(boolean orthographic, boolean frameSync){
    System.out.print("adding work");
    long startTime = System.currentTimeMillis();
    if(orthographic){
      float adjustedSampling = multiSampling/2;
      Orthographic[] t = new Orthographic[10];
      for (int i = 0; i < t.length; i++) {
        t[i] = new Orthographic();
        t[i].start(i, adjustedSampling,-adjustedWidth + (width/t.length)*i, -adjustedWidth + (width/t.length)*(i+1));
      }
      if(frameSync){
        for (int i = 0; i < t.length; i++) {
          try {
            t[i].join();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }else{
      int adjustedSampling = (int)multiSampling/2;
      Perspective[] t = new Perspective[10];
      for (int i = 0; i < t.length; i++) {
        t[i] = new Perspective();
        t[i].start(i, adjustedSampling,-adjustedWidth + (width/t.length)*i, -adjustedWidth + (width/t.length)*(i+1));
      }
      if(frameSync){
        for (int i = 0; i < t.length; i++) {
          try {
            t[i].join();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
    System.out.println(" took " + ((System.currentTimeMillis()-startTime)/1000)+"sec");
  }
  public class Perspective extends Thread {
    int adjustedSampling;
    int start, end;
    public void start(int ID, int adjustedSampling, int start, int end){
      setName("workGenerator" + ID);
      this.adjustedSampling = adjustedSampling;
      this.start = start;
      this.end = end;
      super.start();
    }
    public void run(){
      for (int x = start; x < end; x++) {
        for (int y = -adjustedHeight; y < adjustedHeight; y++) {
          Calculator.addWork(new PerspectiveWork(x,y,adjustedWidth,adjustedHeight,lowerLeftCorner,horizontal,vertical,origin, lensRadius, u, v,(int)multiSampling));
        }
      }
    }
  }
  public class Orthographic extends Thread {
    float adjustedSampling;
    int start, end;
    public void start(int ID, float adjustedSampling, int start, int end){
      setName("workGenerator" + ID);
      this.adjustedSampling = adjustedSampling;
      this.start = start;
      this.end = end;
      super.start();
    }
    public void run(){
      for (int x = start; x < end; x++) {
        for (int y = -adjustedHeight; y < adjustedHeight; y++) {
          Calculator.addWork(new OrthographicWork(x, y, (int)multiSampling, origin, getDirection(), adjustedHeight, adjustedWidth));
        }
      }
    }
  }
}