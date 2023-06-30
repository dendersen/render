package mtdm.dk;

public class Color {
  public int r,g,b;
  public Color(int r, int g, int b){
    this.r = r;
    this.g = g;
    this.b = b;
  }
  public Color(Vector normal){
    this.r = Math.max(Math.min(Math.round((0.5f*normal.getX()+1)*255),255),0);
    this.g = Math.max(Math.min(Math.round((0.5f*normal.getY()+1)*255),255),0);
    this.b = Math.max(Math.min(Math.round((0.5f*normal.getZ()+1)*255),255),0); 
  }
  public Color inverted(){
    return new Color(255-this.r, 255-this.g, 255-this.b);
  }
}
