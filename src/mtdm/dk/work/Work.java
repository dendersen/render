package mtdm.dk.work;

import mtdm.dk.Color;
import mtdm.dk.Point;
import mtdm.dk.Ray;
import mtdm.dk.objects.Material.ScatterResult;
import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.vision.Display;
import mtdm.dk.vision.HitRecord;

public abstract class Work {
  public abstract void execute(int maxHit, KDTree renderObjectsTree);

  protected void work(HitRecord[] hits,Ray ray, int maxHit, KDTree renderObjectsTree, int multiSampling,int adjustedHeight,int adjustedWidth,Point pixel, float Sx, float Sy){
    hits = calculateWhatWasHitAfterwards(ray, renderObjectsTree, hits, 0, maxHit);
    Color color = Color.fromHits(hits, ray, multiSampling*2);
    Display.paint(pixel, adjustedWidth*2, adjustedHeight*2,(int)Sx+multiSampling/2,(int)Sy+multiSampling/2, color);
  }
  
  private static HitRecord[] calculateWhatWasHitAfterwards(Ray ray, KDTree renderObjectsTree, HitRecord[] hits, int hitCount, int maxHit) {
    if (maxHit <= 0) return null;
    HitRecord closestCollision;
    // Query the KDTree to find the closest object that the ray collides with.
    closestCollision = renderObjectsTree.nearestNeighborSearch(ray, 0.001f, Float.MAX_VALUE);
    // If there was a collision...
    if (closestCollision == null) return hits;
    ScatterResult result = closestCollision.getMatPtr().scatter(ray, closestCollision, closestCollision.getColor());
    
    if (result == null) return null;
    hits[hitCount] = closestCollision;
    hits[hitCount].setColor(result.attenuation);
    calculateWhatWasHitAfterwards(result.scattered, renderObjectsTree, hits, hitCount + 1, maxHit - 1);
    return hits;
  }
}
