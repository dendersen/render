package mtdm.dk.objects.Storage;

import mtdm.dk.objects.Object;
import mtdm.dk.Vector;
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

  public Object nearestNeighborSearch(Vector point) {
    return nearestNeighborSearch(root, point, root.obj);
  } 

  private Object nearestNeighborSearch(KDNode node, Vector point, Object best) {
    if (node == null) return best;

    // Check the distance to the current object in the node
    float d = node.obj.getCenter().distanceSquared(point);
    float bestDistance = best.getCenter().distanceSquared(point);

    // If the current node is closer, update the best node
    if (d < bestDistance) {
      best = node.obj;
      bestDistance = d;
    }

    // Determine which child node to visit first
    KDNode first = node.left;
    KDNode second = node.right;
    if (point.get(node.axis) > node.split) {
      first = node.right;
      second = node.left;
    }

    // Visit the child node, and possibly the other child node
    best = nearestNeighborSearch(first, point, best);
    if (Math.abs(node.split - point.get(node.axis)) < Math.sqrt(bestDistance)) {
      best = nearestNeighborSearch(second, point, best);
    }

    return best;
  }

}
