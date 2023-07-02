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
      currentWork = getWork();
      if (currentWork == null) {
        continue;
      }
      for (int i = 0; i < currentWork.length; i++) {
        hits = new HitRecord[maxHit];
        hits = calculateWhatWasHitAfterwards(currentWork[i].ray, renderObjects, hits, 0, maxHit);
        currentWork[i].color = Color.fromHits(hits, currentWork[i].ray);
        Display.paint(currentWork[i], width, height);
      }
    }
  }

  public static HitRecord[] calculateWhatWasHitAfterwards(Ray ray, ArrayList<Object> renderObjects, HitRecord[] hits, int hitCount, int maxHit) {
    if (maxHit <= 0) {
      return hits;
    }
    
    HitRecord closestCollision = null;
    HitRecord tempCollision = null;
    for (int r = 0; r < renderObjects.size(); r++) {
      // Calculate if the current Ray (currentWork[i]) collides with the current object (renderObjects.get(r)).
      tempCollision = renderObjects.get(r).collision(ray, 0.001f, Float.MAX_VALUE);

      // If there was a collision...
      if (tempCollision != null) {
        if (closestCollision == null) {
          closestCollision = tempCollision;
        }
        if (ray.getOrigin().getDistance(tempCollision.getPoint()) < ray.getOrigin().getDistance(closestCollision.getPoint())) {
          closestCollision = tempCollision;
        }   
      }
    }
    
    if(closestCollision != null){
      hits[hitCount] = closestCollision;
      ray.bounce(closestCollision);
      calculateWhatWasHitAfterwards(ray, renderObjects, hits, hitCount+1, maxHit-1);
    } 
    return hits;
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
