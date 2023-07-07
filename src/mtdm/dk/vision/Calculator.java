package mtdm.dk.vision;

import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.work.Work;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Calculators
 */
public class Calculator extends Thread{
  private static BlockingQueue<Work> workPool = new LinkedBlockingQueue<>();
  private static int maxHit;
  private int ID;
  private Work[] currentWork;
  private static KDTree renderObjectsTree;

  public Calculator(int ID, int width, int height){
    this.ID = ID;
    setName("Calc:"+this.ID);
    setPriority(Thread.MAX_PRIORITY - 1);
  }

  @Override
  public void run(){
    while(!Thread.currentThread().isInterrupted()){
      currentWork = getWork();
      if (currentWork == null) {
        continue;
      }
      for (int i = 0; i < currentWork.length; i++) {
        if(currentWork == null) continue;
        currentWork[i].execute(maxHit, renderObjectsTree);
      }
    }
  }

  public static void addWork(Work work){
    workPool.add(work);
  }
  
  private static Work[] getWork(){
    try {
      Work[] work = new Work[100];
      for (int i = 0; i < work.length; i++) {
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
  
  public static void setRender(KDTree renderObjectsTree, int maxHit){
    Calculator.renderObjectsTree = renderObjectsTree;
    Calculator.maxHit = maxHit;
}

  public static int getWorkCount() {
    return workPool.size();
  }
}

