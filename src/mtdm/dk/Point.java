package mtdm.dk;

/**
 * Point
 */
public class Point {
  private int x,y;
  private Color color;
  public Point(int x, int y){
    this.x = x;
    this.y = y;
  }
  public Point setColor(Color color){
    this.color = color;
    return this;
  }
  public Color getColor(){
    if(color == null){
      return new Color(0,0,0);
    }
    return color;
  }
  public int getX(){
    return x;
  }
  public int getY(){
    return y;
  }
}