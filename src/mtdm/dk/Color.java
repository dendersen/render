package mtdm.dk;

import mtdm.dk.vision.HitRecord;

public class Color {
  public int r,g,b;
  public static Color Black = new Color(0,0,0);
  public static Color White = new Color(255,255,255);
  public static Color Red = new Color(255,0,0);
  public static Color Green = new Color(0,255,0);
  public static Color Blue = new Color(0,0,255);
  public static Color DefaultGround = new Color(94, 62, 7);
  public static Color DefaultSky = new Color(7, 140, 173);
  public Color(int r, int g, int b){
    this.r = r;
    this.g = g;
    this.b = b;
  }
  public static Color FromNormal(Vector normal){
    int r = Math.max(Math.min(Math.round((0.5f*normal.getX()+1)*255),255),0);
    int g = Math.max(Math.min(Math.round((0.5f*normal.getY()+1)*255),255),0);
    int b = Math.max(Math.min(Math.round((0.5f*normal.getZ()+1)*255),255),0); 
    return new Color(r,g,b);
  }
  public static Color average(Color[] average){
    
    int r = 0;
    int g = 0;
    int b = 0;
    for (int i = 0; i < average.length; i++) {
      if(average[i] == null) continue;
      r += average[i].r;
      g += average[i].g;
      b += average[i].b;
    }
    if(r == 0 && g == 0 && b == 0){
      return Black;
    }
    r /= average.length;
    g /= average.length;
    b /= average.length;
    return new Color(r,g,b);
  }
  
  public static Color fromHits(HitRecord[] hits, Ray ray, int samplesPerPixel) {
    if(hits == null || hits[0] == null){
      return DefaultSky;
    }

		Color color = hits[0].getColor();    
    for (int i = 1; i < hits.length && hits[i] != null; i++) {
      color = multiplyTwoColors(color, hits[i].getColor());
    }
    return writeColor(color, samplesPerPixel);
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

  public static Color multiplyTwoColors(Color color1, Color color2){
    return new Color(
      (int)(color1.r * color2.r), 
      (int)(color1.g * color2.g), 
      (int)(color1.b * color2.b)
    );
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
  
  public Color inverted(){
    return new Color(255-this.r, 255-this.g, 255-this.b);
  }
}
