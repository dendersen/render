package mtdm.dk.vision;

import java.util.ArrayList;
import mtdm.dk.objects.Object;
import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

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
      //calculates collisions
      for (int work = 0; work < currentWork.length; work++) {
        for(int currentHit = 0; currentHit < maxHit; currentHit++){
          for (int i = 0; i < renderObjects.size(); i++) {
            HitRecord collision = renderObjects.get(i).collision(currentWork[work]);
            if(
              hits[currentHit] == null
              ||
              currentWork[work].getOrigin().getDistance(collision.getPoint()) 
              < 
              currentWork[work].getOrigin().getDistance(hits[currentHit].getPoint())
            ){
              hits[currentHit] = collision;
            }
          }
          if(hits[currentHit] == null){
            Display.paint(currentWork[work].makeColor(hits), width, height);
            break;
          }
          currentWork[work].bounce(hits[currentHit]);
        }
        Display.paint(currentWork[work].makeColor(hits), width, height);
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
  
  public static synchronized void setRender(ArrayList<Object> renderObjects, int maxHit){
    Calculator.renderObjects = renderObjects;
    Calculator.maxHit = maxHit;
  }
}