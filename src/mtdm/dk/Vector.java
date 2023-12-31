package mtdm.dk;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 3D Vector class with basic vector operations.
 */
public class Vector {
  private float x, y, z;

  // Constructors
  public Vector(float x, float y, float z){
    this.x = x;
    this.y = y;
    this.z = z; 
  }

  // Random vector within a range
  public Vector(int min, int max){
    while (true) {
      this.x = min + (max-min) * ThreadLocalRandom.current().nextFloat();
      this.y = min + (max-min) * ThreadLocalRandom.current().nextFloat();
      this.z = min + (max-min) * ThreadLocalRandom.current().nextFloat();
      if (this.lengthSquared() < 1) {
        this.normalize();
        break;
      }
    }
  }

  // Getters
  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  public float get(int index) {
  switch(index) {
    case 0: return x;
    case 1: return y;
    case 2: return z;
    default: throw new IndexOutOfBoundsException("Index: " + index + ", Size: 3");
  }
}


  // Get a copy of the vector
  public Vector copy() {
    return new Vector(this.x, this.y, this.z);
  }

  // Get the negative of the vector
  public Vector getNegative() {
    return new Vector(-this.x, -this.y, -this.z);
  }

  // Get distance to another point
  public float getDistance(Vector point){
    return (float) Math.sqrt(Math.pow(point.getX()-this.getX(),2)+Math.pow(point.getY()-this.getY(),2)+Math.pow(point.getZ()-this.getZ(),2));
  }

  // Check if vector is near zero
  public boolean nearZero() {
    final double s = 1e-8;
    return (Math.abs(this.x) < s) && (Math.abs(this.y) < s) && (Math.abs(this.z) < s);
  }

  // Vector addition, either in place or create new vector
  public Vector add(Vector addition, boolean inPlace){
    if(inPlace) {
      return this.add(addition);
    } else {
      return new Vector(this.x + addition.getX(), this.y + addition.getY(), this.z + addition.getZ());
    }
  }
  
  // Vector subtraction, either in place or create new vector
  public Vector sub(Vector subtraction, boolean inPlace){
    if(inPlace) {
      return this.sub(subtraction);
    } else {
      return new Vector(this.x - subtraction.getX(), this.y - subtraction.getY(), this.z - subtraction.getZ());
    }
  }

  // In-place addition
  private Vector add(Vector addition){
    this.x += addition.getX();
    this.y += addition.getY();
    this.z += addition.getZ();
    return this;
  }

  // In-place subtraction
  private Vector sub(Vector subtraction){
    this.x -= subtraction.getX();
    this.y -= subtraction.getY();
    this.z -= subtraction.getZ();
    return this;
  }

  // Scalar multiplication
  public Vector multi(float t) {
    return new Vector(this.x * t, this.y * t, this.z * t);
  }

  // Vector-vector multiplication, either in place or create new vector
  public Vector multi(Vector v, boolean inPlace){
    if(inPlace) {
      return this.multi(v);
    } else {
      return new Vector(this.x * v.getX(), this.y * v.getY(), this.z * v.getZ());
    }
  }

  // In-place vector-vector multiplication
  public Vector multi(Vector v) {
    this.x *= v.getX();
    this.y *= v.getY();
    this.z *= v.getZ();
    return this;
  }

  // Vector division by another vector, either in place or create new vector
  public Vector div(Vector v, boolean inPlace){
    if (inPlace) {
      return this.div(v);
    } else {
      return new Vector(this.x / v.getX(), this.y / v.getY(), this.z / v.getZ());
    }
  }

  // In-place vector division
  private Vector div(Vector v){
    this.x /= v.getX();
    this.y /= v.getY();
    this.z /= v.getZ();
    return this;
  }

  // Vector division by a scalar
  public Vector div(float dividen){
    return new Vector(this.x / dividen, this.y / dividen, this.z / dividen);
  }

  // Compute the dot product
  public float dot(Vector complimentary){
    return this.x * complimentary.getX() + this.y * complimentary.getY() + this.z * complimentary.getZ();
  }

  // Compute the cross product
  public Vector cross(Vector secondary){
    return new Vector(
      this.y * secondary.getZ() - this.z * secondary.getY(),
      this.z * secondary.getX() - this.x * secondary.getZ(),
      this.x * secondary.getY() - this.y * secondary.getX()
    );
  }

  // Normalize the vector, either in place or create new vector
  public Vector normalize(boolean inPlace){
    if(inPlace) {
      return this.normalize();
    } else {
      float length = this.length();
      return new Vector(this.x / length, this.y / length, this.z / length);
    }
  }

  // In-place normalization
  private Vector normalize(){
    float length = this.length();
    this.x /= length;
    this.y /= length;
    this.z /= length;
    return this;
  }

  // Compute the refraction of a vector
  public static Vector refract(Vector uv, Vector normal, float etaiOverEtat) {
    float cosTheta = Math.min(uv.getNegative().dot(normal), 1.0f);
    Vector rOutPerp = uv.add(normal.multi(cosTheta)).multi(etaiOverEtat);
    Vector rOutPerallel = normal.multi((float)-Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSquared())));
    return rOutPerp.add(rOutPerallel,false);
  }

  public Vector scale(float scalar) {
    return new Vector(x * scalar, y * scalar, z * scalar);
  }

  // Compute the square of the length
  public float lengthSquared(){
    return this.x * this.x + this.y * this.y + this.z * this.z;
  }

  // Get the squared distance to another point
  public float distanceSquared(Vector point) {
    float dx = this.x - point.getX();
    float dy = this.y - point.getY();
    float dz = this.z - point.getZ();
    return dx*dx + dy*dy + dz*dz;
  }

  // Compute the length
  public float length(){
    return (float) Math.sqrt(this.lengthSquared());
  }

  // Reflect the vector against a normal
  public Vector mirrorReflect(Vector normal) {
    return this.sub(normal.multi(2 * this.dot(normal)),false);
  }

  // Static methods
  public static Vector reflect(Vector v, Vector normal) {
    return v.sub(normal.multi(2*dot(v, normal)));
  }

  public static float dot(Vector complimentary, Vector complimentary2){
    return complimentary.dot(complimentary2);
  }

  public static Vector cross(Vector primary, Vector secondary){
    return primary.cross(secondary);
  }

  public static Vector randomInUnitSphere(){
    while (true) {
      float x = -1 + 2*ThreadLocalRandom.current().nextFloat();
      float y = -1 + 2*ThreadLocalRandom.current().nextFloat();
      float z = -1 + 2*ThreadLocalRandom.current().nextFloat();
      
      Vector random = new Vector(x, y, z);
      if (random.lengthSquared() < 1) {
        return random;
      }
    }
  }

  public static Vector randomInUnitDisk() {
    while (true) {
      float x = ThreadLocalRandom.current().nextFloat() * 2 - 1; // random float between -1 and 1
      float y = ThreadLocalRandom.current().nextFloat() * 2 - 1; // random float between -1 and 1
      Vector p = new Vector(x, y, 0);
      if (p.lengthSquared() >= 1) continue;
      return p;
    }
  }

  public static Vector randomInHemisphere(Vector normal){
    Vector inUnitSphere = randomInUnitSphere();
    if (Vector.dot(inUnitSphere, normal) > 0) {
      return inUnitSphere;
    } else {
      return inUnitSphere.getNegative();
    }
  }
}
