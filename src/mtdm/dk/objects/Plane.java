package mtdm.dk.objects;

import mtdm.dk.Color;
import mtdm.dk.Vector;
import mtdm.dk.vision.HitRecord;
import mtdm.dk.Ray;

public class Plane extends Object{
  float a,b,c,d;
  Vector normal;
  
  public Plane(float a, float b, float c, float d, Color color){
    super(color);
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

  public Plane(Vector A, Vector B, Vector C, Color color){
    super(color);
    Vector AB = B.sub(A, false);
    Vector AC = C.sub(A, false);
    this.normal = AB.cross(AC);
    this.a = this.normal.getX();
    this.b = this.normal.getY();
    this.c = this.normal.getZ();
    this.d = this.normal.getNegative().dot(A);
  }
  
  @Override
  public HitRecord collision(Ray ray) {
    float tæller = this.a*ray.getX()+this.b*ray.getY()+this.c*ray.getZ()+this.d;
    float nævner = this.a*ray.getRx()+this.b*ray.getRy()+this.c*ray.getRz();
    Float t = -(tæller/nævner);
    
    if(!Float.isFinite(t) || t < 0) return null;
    
    Vector collision = ray.calculate(t);
    return new HitRecord(collision, this.normal, t, color);
  }
}
