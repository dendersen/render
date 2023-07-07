package mtdm.dk;

import java.util.concurrent.ThreadLocalRandom;

public class Color {
  public float r,g,b;
  public static Color Black = new Color(0,0,0);
  public static Color White = new Color(1,1,1);
  public static Color Red = new Color(1,0,0);
  public static Color Green = new Color(0,1,0);
  public static Color Blue = new Color(0,0,1);
  public static Color DefaultGround = new Color(94/255f, 62/255f, 7/255f);
  public static Color DefaultSky = new Color(7/255f, 140/255f, 173/255f);

  public Color(float r, float g, float b){
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public static Color randomColor(float min, float max) {
    float r, g, b;
    while (true) {
      r = ThreadLocalRandom.current().nextFloat();
      g = ThreadLocalRandom.current().nextFloat();
      b = ThreadLocalRandom.current().nextFloat();
      if (r >= min && r <= max && g >= min && g <= max && b >= min && b <= max) {
        break;
      }
    } 
    return new Color(r, g, b);
  }


  public static Color skyBox(Ray ray){
    Vector unitDirection = ray.getDirection().normalize(false);
    float t = (unitDirection.getY() + 1f);
    return new Color(1, 1, 1).multi(1.0f-t).add(new Color(0.5f, 0.7f, 1.0f).multi(t), false);
  }

  public static Color writeColor(Color pixelColor, int samplesPerPixel) {
    double r = pixelColor.getR();
    double g = pixelColor.getG();
    double b = pixelColor.getB();

    // Divide the color by the number of samples and gamma-correct for gamma=2.0.
    double scale = 1.0 / samplesPerPixel;
    r = Math.sqrt(scale * r);
    g = Math.sqrt(scale * g);
    b = Math.sqrt(scale * b);

    // Write the translated [0,255] value of each color component.
    return new Color((int)(256 * clamp(r, 0.0, 0.999)), (int)(256 * clamp(g, 0.0, 0.999)), (int)(256 * clamp(b, 0.0, 0.999)));
  }

  // Helper function to clamp a value between a min and a max.
  private static double clamp(double x, double min, double max) {
    return Math.max(min, Math.min(max, x));
  }

  // public static Color multiplyTwoColors(Color color1, Color color2){
  //   return new Color(
  //     (color1.r * color2.r), 
  //     (color1.g * color2.g), 
  //     (color1.b * color2.b)
  //   );
  // }
  
  public float getR() {
    return this.r;
  }
  
  public float getG() {
    return this.g;
  }
  
  public float getB() {
    return this.b;
  }
  
  public Color inverted(){
    return new Color(1-this.r, 1-this.g, 1-this.b);
  }

  // Color addition, either in place or create new Color
  public Color add(Color addition, boolean inPlace){
    if(inPlace) {
      return this.add(addition);
    } else {
      return new Color(this.r + addition.getR(), this.g + addition.getG(), this.b + addition.getB());
    }
  }
  
  // Color subtraction, either in place or create new Color
  public Color sub(Color subtraction, boolean inPlace){
    if(inPlace) {
      return this.sub(subtraction);
    } else {
      return new Color(this.r - subtraction.getR(), this.g - subtraction.getG(), this.b - subtraction.getB());
    }
  }

  // In-place addition
  private Color add(Color addition){
    this.r += addition.getR();
    this.g += addition.getG();
    this.b += addition.getB();
    return this;
  }

  // In-place subtraction
  private Color sub(Color subtraction){
    this.r -= subtraction.getR();
    this.g -= subtraction.getG();
    this.b -= subtraction.getB();
    return this;
  }

  // Scalar multiplication
  public Color multi(float t) {
    return new Color(this.r * t, this.g * t, this.b * t);
  }

  // Color-Color multiplication, either in place or create new Color
  public Color multi(Color v, boolean inPlace){
    if(inPlace) {
      return this.multi(v);
    } else {
      return new Color(this.r * v.getR(), this.g * v.getG(), this.b * v.getB());
    }
  }

  // In-place Color-Color multiplication
  public Color multi(Color v) {
    float r = this.r * v.getR();
    float g = this.g * v.getG();
    float b = this.b * v.getB();
    return new Color(r, g, b);
  }
}
