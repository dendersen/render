package mtdm.dk.vision;

import java.util.ArrayList;
import mtdm.dk.objects.Object;
import mtdm.dk.Color;
import mtdm.dk.Ray;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Calculators
 */
public class Calculator extends Thread{
  private static BlockingQueue<WorkUnit> workPool = new LinkedBlockingQueue<>();
  private static ArrayList<Object> renderObjects;
  private static int maxHit;
  private int ID;
  private WorkUnit[] currentWork;
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
      for (int i = 0; i < currentWork.length; i++) {
        HitRecord[] collisions = new HitRecord[maxHit];
        for (int j = 0; j < maxHit; j++) {
          if(j > 0 && hits[j-1] == null){
            continue;
          }
          for (int r = 0; r < renderObjects.size(); r++) {
            
            // Calculate if the current Ray (currentWork[i]) collides with the current object (renderObjects.get(r)).
            HitRecord tempCollision = renderObjects.get(r).collision(currentWork[i].ray);
            
            // If there was a collision...
            if (tempCollision != null) {
              if (collisions[j] == null) {
              collisions[j] = tempCollision;
              continue;
              }
              if (currentWork[i].ray.getOrigin().getDistance(tempCollision.getPoint()) < currentWork[i].ray.getOrigin().getDistance(collisions[j].getPoint())) {
                collisions[j] = tempCollision;
              }
            }
          }
          if(hits[j] != null){
            currentWork[i].ray.bounce(collisions[i]);
          }
        }
        // Once all objects have been checked for collisions, call the paint() method of the Display class to display the result.
        // Note: If no collision was found, 'collision' will be null. This situation should be handled inside the paint() method.
        currentWork[i].color = Color.fromHits(collisions, currentWork[i].ray);
        Display.paint(currentWork[i], width, height);
      }
    }
  }

  public static void addWork(WorkUnit work){
    if(work == null) return;
    workPool.add(work);
  }
  
  private static WorkUnit[] getWork(){
    try {
      WorkUnit[] work = new WorkUnit[10];
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

  public static int getWorkCount() {
    return workPool.size();
  }
}