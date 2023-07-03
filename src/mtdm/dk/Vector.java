package mtdm.dk;

public class Vector {
  private float x,y,z;

  public Vector(int min, int max){
    while (true) {
      this.x = (float)min + (float)(max-min)*(float)Math.random();
      this.y = (float)min + (float)(max-min)*(float)Math.random();
      this.z = (float)min + (float)(max-min)*(float)Math.random();;
      if (this.lengthSquared() >= 1) continue;
      break;
    }
    this.normalize();
  }
  
  public Vector(float x, float y, float z){
    this.x = x;
    this.y = y;
    this.z = z; 
  }

  public static Vector randomInUnitSphere(){
    while (true) {
      float x = (float)-1 + (float)(1-(-1))*(float)Math.random();
      float y = (float)-1 + (float)(1-(-1))*(float)Math.random();
      float z = (float)-1 + (float)(1-(-1))*(float)Math.random();
      
      Vector random = new Vector(x, y, z);
      if (random.lengthSquared() >= 1) continue;
      return random;
    }
  }

  public static Vector randomInHemisphere(Vector normal){
    Vector inUnitSphere = randomInUnitSphere();
    if (Vector.dot(inUnitSphere, normal) > 0) {
      return inUnitSphere;
    }
    return inUnitSphere.getNegative();
  }

  public boolean nearZero() {
    // Return true if the vector is close to zero in all dimensions.
    final double s = 1e-8;
    return (Math.abs(this.x) < s) && (Math.abs(this.y) < s) && (Math.abs(this.z) < s);
  }
  
  public Vector add(Vector addition, boolean inPlace){
    if(inPlace) return add(addition);
    return add(this,addition);
  }
  private Vector add(Vector addition, Vector addition2) {
    return addition.copy().add(addition2, true);
  }
  private Vector add(Vector addition){
    this.x += addition.getX();
    this.y += addition.getY();
    this.z += addition.getZ();
    return this;
  }
  
  // Vector-scalar multiplication
  public Vector multi(float t) {
    return new Vector(this.x * t, this.y * t, this.z * t);
  }

  // Vector-vector multiplication (in-place)
  public Vector multi(Vector v) {
    this.x *= v.x;
    this.y *= v.y;
    this.z *= v.z;
    return this;
  }

  public Vector div(Vector v, boolean inPlace){
    if (inPlace) return div(v);
    return new Vector(this.x / v.x, this.y / v.y, this.z / v.z);
  }

  private Vector div(Vector v){
    this.x /= v.x;
    this.y /= v.y;
    this.z /= v.z;
    return this;
  }
  public Vector div(float dividen){
    return new Vector(this.x/dividen,this.y/dividen,this.z/dividen);
  }

  // Vector-vector multiplication (new vector)
  public Vector multi(Vector v, boolean inPlace){
    if(inPlace) return multi(v);
    return new Vector(this.x * v.x, this.y * v.y, this.z * v.z);
  }

  public Vector sub(Vector subtraction, boolean inPlace){
    if(inPlace) return sub(subtraction);
    return sub(this,subtraction);
  }
    private Vector sub(Vector positive, Vector negative) {
    return positive.copy().sub(negative, true);
  }
  private Vector sub(Vector negative){
    this.x -= negative.getX();
    this.y -= negative.getY();
    this.z -= negative.getZ();
    return this;
  }
  
  public Vector cross(Vector secondary){
    return new Vector(
      this.getY()*secondary.getZ()-this.getZ()*secondary.getY(),
      this.getZ()*secondary.getX()-this.getX()*secondary.getZ(),
      this.getX()*secondary.getY()-this.getY()*secondary.getX()
    );
  }

  public static Vector cross(Vector primary, Vector secondary){
    return primary.cross(secondary);
  }

  public Vector mirrorReflect(Vector normal) {
    // Assume V and N are already normalized
    return this.sub(normal.multi(2 * this.dot(normal)),false);
  } 

  public float dot(Vector complimentary){
    return this.x*complimentary.getX()+this.y*complimentary.getY()+this.z*complimentary.getZ();
  }
  public static float dot(Vector complimentary, Vector complimentary2){
    return complimentary.dot(complimentary2);
  }

  // get the normal vector with the same direction
  public Vector normalize(boolean inPlace){
    if(inPlace) return normalize();
    return normal();
  }
  private Vector normalize(){
    float dividen = length();
    this.x /= dividen;
    this.y /= dividen;
    this.z /= dividen;
    return this;
  }
  private Vector normal(){
    return copy().normalize(true);
  }

  public float lengthSquared(){
    return (float) (Math.pow(this.x,2)+Math.pow(this.y,2)+Math.pow(this.z,2));
  }

  public float length(){
    return (float) Math.sqrt(lengthSquared());
  }
  public float getZ() {
    return z;
  }
  public float getY() {
    return y;
  }
  public float getX() {
    return x;
  }
  public Vector getNegative(){
    return new Vector(-this.x, -this.y, -this.z);
  }
  public float getDistance(Vector point){
    return (float) Math.sqrt(Math.pow(point.getX()-this.getX(),2)+Math.pow(point.getY()-this.getY(),2)+Math.pow(point.getZ()-this.getZ(),2));
  }
  private Vector copy() {
    return new Vector(this.x, this.y, this.z);
  }
}
