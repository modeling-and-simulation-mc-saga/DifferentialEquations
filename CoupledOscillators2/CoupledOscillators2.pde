coupledOscillators2.CoupledOscillators2 sys;
int height = 1000;
boolean stop=false;
double h=0.1;
float r = 40;
int n = 200;
java.awt.Point p[]= new java.awt.Point[n];


void setup(){
  size(1000,1000);
  smooth();
  frameRate(10);
  background(50,50,50);
  //initialization
  coupledOscillators2.Oscillator os[]=
    new coupledOscillators2.Oscillator[n];
  double m[]=new double[n];
  double k[]=new double[n];
  double lambda[][]=new double[n][n];
  for(int i=0;i<n;i++){
    int x = (int)(height*Math.random());
    int y = (int)(height*Math.random());
    p[i]=new java.awt.Point(x,y);
    double theta = 2*Math.PI*Math.random();
    os[i]=new coupledOscillators2.Oscillator(
      Math.cos(theta),Math.sin(theta));
    m[i]=1.;
    k[i]=0.9+0.2*Math.random();
    for(int j=0;j<n;j++){
      lambda[i][j]=0.05;
    }
  }
  sys = new coupledOscillators2.CoupledOscillators2(os,m,k,lambda);
  
}

void draw(){
  if(stop){noLoop();}
  background(50,50,50);
  coupledOscillators2.Oscillator os[]=sys.update(h);
  noStroke();
  for(int i=0;i<os.length;i++){
    fill(255,255,128);
    double z = os[i].y;
    if(z<0){
      fill(255,128,255);
      z *=-1;
    }
    int x = p[i].x;
    int y = p[i].y;
    ellipse(x,y,(float)(r*z),(float)(r*z));
    int dx = (int)(6*Math.random())-3;
    int dy = (int)(6*Math.random())-3;
    int xx = (x+dx+height)%height;
    int yy = (y+dy+height)%height;
    p[i].move(xx,yy);
  }
}
void mousePressed(){
  stop = !stop;
  if(!stop){
    loop();
  }
}