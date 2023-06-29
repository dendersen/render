package mtdm.dk;

public class Color {
  public int r,g,b;
  public Color(int r, int g, int b){
    this.r = r;
    this.g = g;
    this.b = b;
  }
  public Color inverted(){
    return new Color(255-this.r, 255-this.g, 255-this.b);
  }
}
