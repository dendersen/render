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
  private Color(int r, int g, int b){
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
  
  public static Color fromHits(HitRecord[] hits, Ray ray) {
    if(hits == null || hits[0] == null){
      return DefaultSky;
      // Vector direction = ray.getDirection();
      // direction.normalize(true);
      // float t = 0.5f * direction.getY() + 1f;
      // return new Color((int)((255f-t)+t*0.5f), (int)((255f-t)+t*0.7f), (int)((255f-t)+t*1f));
    }
		Color color = hits[0].getColor();    
    for (int i = 1; i < hits.length && hits[i] != null; i++) {
      color = weightedAverage(color, hits[i].getColor(), 0.5f);
    }
    return color;
    // Color here = FromNormal(hits[0].getNormal()); // normally should be hits[0].getColor()
    // int r = here.r;
    // int g = here.g;
    // int b = here.b;
    // return new Color(r,g,b);
	}

  public static Color weightedAverage(Color color1, Color color2, float weight){
    return new Color(
      (int)(color1.r * (1-weight) + color2.r * weight), 
      (int)(color1.g * (1-weight) + color2.g * weight), 
      (int)(color1.b * (1-weight) + color2.b * weight)
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
