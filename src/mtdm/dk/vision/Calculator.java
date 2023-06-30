package mtdm.dk.vision;

import java.util.ArrayList;
import mtdm.dk.objects.Object;
import mtdm.dk.Ray;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Calculators
 */
public class Calculator extends Thread{
  private static BlockingQueue<Ray> workPool = new LinkedBlockingQueue<>();
  private static ArrayList<Object> renderObjects;
  private static int maxHit;
  private int ID;
  private Ray currentWork[];
  private HitRecord[] hits;
  private int width, height;
  
  public Calculator(int ID, int width, int height){
    this.width = width;
    this.height = height;
    this.ID = ID;
    setName("Calc:"+this.ID);
    setPriority(Thread.MIN_PRIORITY + 1);
  }

  @Override
  public void run(){
    while(!Thread.currentThread().isInterrupted()){
      hits = new HitRecord[maxHit];
      currentWork = getWork();
      if (currentWork == null) {
        continue;
      }
      // For each Ray in the currentWork array.
      for (int i = 0; i < currentWork.length; i++) {
        // Initialize a HitRecord object which will keep track of the closest collision that the ray encounters.
        HitRecord collision = null;

        for (int r = 0; r < renderObjects.size(); r++) {
          // Calculate if the current Ray (currentWork[i]) collides with the current object (renderObjects.get(r)).
          HitRecord tempCollision = renderObjects.get(r).collision(currentWork[i]);
          
          // If there was a collision...
          if (tempCollision != null) {
            // If it's the first collision we have found...
            if (collision == null) {
              // Record it and continue to the next iteration of the loop.
              collision = tempCollision;
              continue;
            }

            // If the current collision is closer than the previously recorded collision...
            if (currentWork[i].getOrigin().getDistance(tempCollision.getPoint()) < currentWork[i].getOrigin().getDistance(collision.getPoint())) {
              // Update our record of the closest collision.
              collision = tempCollision;
            }
          }
        }

        // Once all objects have been checked for collisions, call the paint() method of the Display class to display the result.
        // Note: If no collision was found, 'collision' will be null. This situation should be handled inside the paint() method.
        Display.paint(currentWork[i].makeColor(new HitRecord[] { collision }), width, height);
      }
    }
  }

  public static void addWork(Ray work){
    if(work == null) return;
    workPool.add(work);
  }
  
  private static Ray[] getWork(){
    try {
      Ray[] work = new Ray[10];
      for (int i = 0; i < 10; i++) {
        work[i] = workPool.take();
      }
      return work;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt(); // propagate interruption
      return null;
    }
  }

  public static boolean hasWork(){
    return !workPool.isEmpty();
  } 
  
  public static void setRender(ArrayList<Object> renderObjects, int maxHit){
    Calculator.renderObjects = renderObjects;
    Calculator.maxHit = maxHit;
  }
}