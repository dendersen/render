package mtdm.dk.vision;

import java.util.ArrayList;
import mtdm.dk.objects.Object;
import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

/**
 * Calculators
 */
public class Calculator extends Thread{
  private static ArrayList<Ray> workPool = new ArrayList<Ray>();
  private static ArrayList<Object> renderObjects;
  private static int maxHit;
  private int ID;
  private Ray currentWork[];
  private HitRecord[] hits;
  
  public Calculator(int ID){
    this.ID = ID;
    setName("Calc:"+this.ID);
    setPriority(Thread.MIN_PRIORITY + 1);
  }

  @Override
  public void run(){
    while(true){
      hits = new HitRecord[maxHit];
      currentWork = getWork();
      {//calculates collisions
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
              Display.paint(currentWork[work].makeColor(hits));
              break;
            }
            currentWork[work].bounce(hits[currentHit]);
          }
          Display.paint(currentWork[work].makeColor(hits));
        }
      }
    }
  }

  public static synchronized void addWork(Ray work){
    workPool.add(work);
  }
  
  private static Ray[] getWork(){
    synchronized(null){

    }
    if(workPool.size() > 0){
      Ray[] work = new Ray[10];
      for (int i = 0; i < 10; i++) {
        work[i] = workPool.remove(0);
      }
      return work;
    }
    return null;
  }
  public static boolean hasWork(){
    return workPool.size()>0;
  } 
  
  public static synchronized void setRender(ArrayList<Object> renderObjects, int maxHit){
    Calculator.renderObjects = renderObjects;
    Calculator.maxHit = maxHit;
  }
}