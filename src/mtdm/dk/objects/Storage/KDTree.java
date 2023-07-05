package mtdm.dk.objects.Storage;

import mtdm.dk.objects.Object;
import mtdm.dk.vision.HitRecord;
import mtdm.dk.Ray;
import java.util.List;

public class KDTree {
  KDNode root;
  
  public KDTree(List<Object> objects) {
    root = createTree(objects, 0);
  }

  private KDNode createTree(List<Object> objects, int depth) {
    if (objects.isEmpty()) return null;

    int axis = depth % 3;
    objects.sort((a, b) -> Float.compare(a.getCenter().get(axis), b.getCenter().get(axis)));

    KDNode node = new KDNode();
    node.axis = axis;
    node.split = objects.get(objects.size() / 2).getCenter().get(axis);

    if (objects.size() > 1) {
      node.left = createTree(objects.subList(0, objects.size() / 2), depth + 1);
      node.right = createTree(objects.subList(objects.size() / 2, objects.size()), depth + 1);
    }

    node.obj = objects.get(objects.size() / 2);
    return node;
  }

  public HitRecord nearestNeighborSearch(Ray ray, float min, float max) {
    return nearestNeighborSearch(root, ray, min, max, null);
  }

  private HitRecord nearestNeighborSearch(KDNode node, Ray ray, float min, float max, HitRecord best) {
    if (node == null) return best;

    // Check the intersection with the current object in the node
    HitRecord hit = node.obj.collision(ray, min, max);

    // If the current node is hit and it's closer, update the best hit
    if (hit != null && (best == null || hit.getT() < best.getT())) {
      best = hit;
    }

    // Determine which child node to visit first
    KDNode first = node.left;
    KDNode second = node.right;
    if (ray.getDirection().get(node.axis) > node.split) {
      first = node.right;
      second = node.left;
    }

    // Visit the child node, and possibly the other child node
    best = nearestNeighborSearch(first, ray, min, max, best);
    if (second != null && (best == null || Math.abs(node.split - ray.getOrigin().get(node.axis)) < best.getT())) {
      best = nearestNeighborSearch(second, ray, min, max, best);
    }
    return best;
  }
}
