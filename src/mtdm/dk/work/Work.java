package mtdm.dk.work;

import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.objects.Material.ScatterResult;
import mtdm.dk.objects.Storage.KDTree;
import mtdm.dk.vision.HitRecord;

public abstract class Work {
  public abstract void execute(int maxHit, KDTree renderObjectsTree);

  protected Color work(Ray ray, int maxHit, KDTree renderObjectsTree){
    Color color = calculateWhatWasHitAfterwards(ray, renderObjectsTree, maxHit);
    return color;
  }

  private static Color calculateWhatWasHitAfterwards(Ray ray, KDTree renderObjectsTree, int depth) {
    if (depth <= 0) return Color.Black;

    HitRecord closestCollision = renderObjectsTree.nearestNeighborSearch(ray, 0.001f, Float.MAX_VALUE);
    if (closestCollision != null) {
      ScatterResult result = closestCollision.getMatPtr().scatter(ray, closestCollision, closestCollision.getColor());
      if (result != null) {
        Color finalColor = result.getAttenuation().multi(calculateWhatWasHitAfterwards(result.scattered, renderObjectsTree, depth - 1));
        return finalColor;
      }
      return Color.Black;
    }
    return Color.DefaultSky;
  }
}
