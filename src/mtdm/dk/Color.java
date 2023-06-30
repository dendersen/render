package mtdm.dk;

import java.io.PrintStream;

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

  public Color setColorToNormal(Vector normal){
    int red = Math.max(Math.min(Math.round((0.5f*normal.getX()+1)*255),255),0);
    int green = Math.max(Math.min(Math.round((0.5f*normal.getY()+1)*255),255),0);
    int blue = Math.max(Math.min(Math.round((0.5f*normal.getZ()+1)*255),255),0); 
    return new Color(red, green, blue);
  }

  public int getR() {
    return this.r;
  }

  public int getG() {
    return this.g;
  }

  public int getB() {
    return this.b;
  }

  public float clamp(float x, float min, float max) {
    if (x < min) return min;
    if (x > max) return max;
    return x;
  }

  public void writeColor(PrintStream out, Color pixelColor, int samplesPerPixel) {
    float r = pixelColor.getR();
    float g = pixelColor.getG();
    float b = pixelColor.getB();

    // Divide the color by the number of samples.
    float scale = 1.0f / samplesPerPixel;
    r *= scale;
    g *= scale;
    b *= scale;

    // Write the translated [0,255] value of each color component.
    out.println((int)(256 * clamp(r, 0.0f, 0.999f)) + " "
            + (int)(256 * clamp(g, 0.0f, 0.999f)) + " "
            + (int)(256 * clamp(b, 0.0f, 0.999f)));
  }

  public Color inverted(){
    return new Color(255-this.r, 255-this.g, 255-this.b);
  }
}
