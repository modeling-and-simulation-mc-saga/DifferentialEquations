package kuramoto;

import java.io.PrintStream;
import java.io.IOException;
import rungeKutta.*;

/**
 * Kuramoto model
 * 
 * @author tadaki
 */
public class Kuramoto {

    private final double theta[];
    private final double omega[];
    private final int n;
    private double t = 0.;
    protected DifferentialEquation equation;

    /**
     * @param theta initial values of phases
     * @param omega proper frequencies of oscillators
     * @param k interaction
     */
    public Kuramoto(double[] theta, double[] omega, double k) {
        n = theta.length;
        this.theta = new double[theta.length];
        this.omega = new double[omega.length];
        System.arraycopy(theta, 0, this.theta, 0, theta.length);
        System.arraycopy(omega, 0, this.omega, 0, omega.length);
        setK(k);
    }

    public final void setK(double k){
        equation = (double tt, double yy[]) -> {
            double dy[] = new double[n];
            for (int i = 0; i < n; i++) {
                dy[i] = omega[i];
                for (int j = 0; j < n; j++) {
                    dy[i] += (k / n) * Math.sin(yy[j] - yy[i]);
                }
            }
            return dy;
        };        
    }
    
    public double[] update(double h) {
        State s = RungeKutta.rk4(new State(t, theta), h, equation);
        for (int i = 0; i < n; i++) {
            theta[i] = s.y()[i];
        }
        t += h;
        return theta;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int n = 4;
        int tmax = 10000;
        double h = 0.001;
        double theta[] = new double[n];
        double omega[] = new double[n];
        for (int i = 0; i < n; i++) {
//            omega[i] = 5. + 2 * i;
            omega[i]=5.*(0.8+0.4*Math.random());
            theta[i] = 2. * Math.PI * Math.random();
        }
        double kArray[] = {0., 5.};
        for (double k : kArray) {
            Kuramoto sys = new Kuramoto(theta, omega, k);
            String filename = Kuramoto.class.getSimpleName()
                    + "-" + String.valueOf(k) + "-output.txt";
            try (PrintStream out = new PrintStream(filename)) {
                for (int t = 0; t < tmax; t++) {
                    double th[] = sys.update(h);
                    StringBuilder sb = new StringBuilder();
                    sb.append(t).append(" ");
                    for (int i = 0; i < n; i++) {
                        sb.append(Math.sin(th[i])).append(" ");
                    }
                    out.println(sb.toString());
                }
            }
        }
    }
}
