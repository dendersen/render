package mtdm.dk;

public class Point {
  private float x,y,z;
  private Color color = new Color(0,0,0);
  
  public Point(float x, float y, float z){
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Point add(Point addition, boolean inPlace){
    if(inPlace) return add(addition);
    return add(this,addition);
  }
  private Point add(Point addition, Point addition2) {
    return new Point(addition.getX()+addition2.getX(),addition.getY()+addition2.getY(),addition.getZ()+addition2.getZ());
  }
  /**
   * in place addition of points.
   */
  public Point add(Point addition){
    this.x += addition.getX();
    this.y += addition.getY();
    this.z += addition.getZ();
    return this;
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
  public void setColor(Color color){
    this.color = color;
  }
  public Color getColor(){
    return this.color;
  }
  public float getDistance(Point point){
    return (float) Math.sqrt(Math.pow(point.getX()-this.getX(),2)+Math.pow(point.getY()-this.getY(),2)+Math.pow(point.getZ()-this.getZ(),2));
  }
}
