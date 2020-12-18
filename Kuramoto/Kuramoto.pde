kuramoto.Kuramoto sys;
final int L = 1300;
final int n = 8;
final float len = 100.;
final double k=2.;
final float r = L/n;
double h = 0.01;
boolean stop=false;
int t=0;

void setup(){
  size(1300,1300);
  smooth();
  frameRate(100000);
  double theta[]=new double[n*n];
  double omega[]=new double[n*n];
  for(int i=0;i<n*n;i++){
    omega[i]=5.*(.8+.4*Math.random());
    theta[i]=2.*Math.PI*Math.random();
  }
  sys = new kuramoto.Kuramoto(theta,omega,k);
}

void draw(){
  if(stop){noLoop();}
  
  double tt[]=sys.update(h);
  if(t%10==0){
    background(#FFEE00);
    stroke(#000000);
    strokeWeight(2);
    for(int i=0;i<n;i++){
      for(int j=0;j<n;j++){
        line(r*(j+.5)-len/2,r*(i+.5),
          r*(j+.5)+len/2,r*(i+.5));
      }
    }
    fill(#FF0099);
    strokeWeight(1);
    for(int i=0;i<n;i++){
      for(int j=0;j<n;j++){
        int kk=n*j+i;
//      print(tt[kk]);
        ellipse(
          (float)(r*(j+.5)+.5*len*Math.cos(tt[kk])),
          r*(i+.5),20.,20.);
      }
    }
    t=0;
  }
  t++;
}

void mousePressed(){
  stop = !stop;
  if(!stop){
    loop();
  }
}
