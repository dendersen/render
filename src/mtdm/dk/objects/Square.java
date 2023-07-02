package mtdm.dk.objects;

import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

public class Square extends Plane{
  // This offset ensures that a collision point can be slightly off and still count as a collision.
  private float floatingSafety = 0.0000001f;
  private Vector corner0, corner1, corner3;

  public Square(Vector corner0, Vector corner1, Vector corner2, Color color) {
    super(corner0, corner1, corner2, color);
    this.corner0 = corner0;
    this.corner1 = corner1;
    // Calculate the fourth corner assuming the rectangle sides are parallel to the vectors
    // (corner1 - corner0) and (corner2 - corner0).
    this.corner3 = corner0.add(corner1.sub(corner0,false).add(corner2.sub(corner0, false), false), false) ;
  }

  public HitRecord collision(Ray ray, float tMin, float tMax){
    // First, check for an intersection with the plane defined by the square.
    HitRecord test = super.collision(ray, tMin, tMax);
    
    if (test == null)
      return null;
    
    Vector relative0 = corner1.sub(corner0, false);
    Vector relative1 = corner3.sub(corner0, false);
    
    // Compute the vector from corner0 to the intersection point.
    Vector v2 = test.getPoint().sub(corner0, false);
    
    // Compute dot products.
    float d00 = relative0.dot(relative0);
    float d01 = relative0.dot(relative1);
    float d11 = relative1.dot(relative1);
    float d20 = v2.dot(relative0);
    float d21 = v2.dot(relative1);
    
    // Compute the parameters for the parametric equation of the plane.
    float denom = d00 * d11 - d01 * d01;
    float s = (d11 * d20 - d01 * d21) / denom;
    float t = (d00 * d21 - d01 * d20) / denom;

    // Check if the point lies within the rectangle by checking if both s and t are between 0 and 1 (inclusive).
    // We use a small offset to account for potential floating point errors.
    if (s >= -floatingSafety && s <= 1 + floatingSafety && t >= -floatingSafety && t <= 1 + floatingSafety) {
      return test;
    }

    // If we reach this point, the intersection point was outside the rectangle.
    return null;
  }
}
