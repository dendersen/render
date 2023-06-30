package mtdm.dk.objects;

import mtdm.dk.Color;
import mtdm.dk.Ray;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;

public class Triangle extends Plane{
  protected float floatingSafety = 0.0000001f; //this insures that the a point of collision can be off by a certain amount and still count as a collision
  protected Vector corner0, corner1, corner2;

  public Triangle(Vector corner0, Vector corner1, Vector corner2, Color color) {
    super(corner0, corner1, corner2, color);
    this.corner0 = corner0; 
    this.corner1 = corner1; 
    this.corner2 = corner2;

  }
  
  public HitRecord collision(Ray ray){
    // First, check for an intersection with the plane defined by the triangle.
    HitRecord test = super.collision(ray);
    
    if (test == null)
      return null;
    
    Vector relative0 = corner1.sub(corner0, false);
    Vector relative1 = corner2.sub(corner0, false);
    
    // Compute the vector from corner0 to the intersection point.
    Vector v2 = test.getPoint().sub(corner0, false);
    
    // Compute dot products.
    float d00 = relative0.dot(relative0);
    float d01 = relative0.dot(relative1);
    float d11 = relative1.dot(relative1);
    float d20 = v2.dot(relative0);
    float d21 = v2.dot(relative1);
    
    // Compute barycentric coordinates.
    float denom = d00 * d11 - d01 * d01;
    float v = (d11 * d20 - d01 * d21) / denom;
    float w = (d00 * d21 - d01 * d20) / denom;
    float u = 1.0f - v - w;
    
    // If the point lies inside the triangle, then all of its barycentric coordinates should be between 0 and 1.
    if (v >= -floatingSafety && w >= -floatingSafety && u >= -floatingSafety) {
      return test;
    }
    return null;
  }
}
