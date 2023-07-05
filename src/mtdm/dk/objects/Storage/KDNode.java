package mtdm.dk.objects.Storage;
import mtdm.dk.objects.Object;

public class KDNode {
  Object obj; // the object
  KDNode left, right; // the left and right child nodes
  float split; // the splitting plane
  int axis; // the axis of splitting
}
