package mtdm.dk;

public class Vector {
  private float x,y,z;
  private Color color = new Color(0,0,0);
  
  public Vector(float x, float y, float z){
    this.x = x;
    this.y = y;
    this.z = z; 
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
  public float dot(Vector complimentary){
    return this.x*complimentary.getX()+this.y*complimentary.getY()+this.z*complimentary.getZ();
  }
  public static float dot(Vector complimentary, Vector complimentary2){
    return complimentary.dot(complimentary2);
  }
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
  public float length(){
    return (float)Math.sqrt(Math.pow(this.x,2)+Math.pow(this.y,2)+Math.pow(this.z,2));
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
  public void setColor(Color color){
    this.color = color;
  }
  public Color getColor(){
    return this.color;
  }
  public float getDistance(Vector point){
    return (float) Math.sqrt(Math.pow(point.getX()-this.getX(),2)+Math.pow(point.getY()-this.getY(),2)+Math.pow(point.getZ()-this.getZ(),2));
  }
  private Vector copy() {
    return new Vector(this.x, this.y, this.z);
  }
}
