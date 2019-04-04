package kuramoto;

import java.io.BufferedWriter;
import java.io.IOException;
import myLib.rungeKutta.DifferentialEquation;
import myLib.rungeKutta.RungeKutta;
import myLib.utils.FileIO;

/**
 * 引き込み現象の蔵本モデル
 * 
 * @author tadaki
 */
public class Kuramoto {

    private final double theta[];
    private final int n;
    private double t = 0.;
    protected DifferentialEquation equation;

    /**
     * コンストラクタ
     * @param theta 位相の初期値
     * @param omega 振動子の固有振動数
     * @param k 結合の強さ
     */
    public Kuramoto(double[] theta, double[] omega, double k) {
        n = theta.length;
        this.theta = new double[theta.length];
        System.arraycopy(theta, 0, this.theta, 0, theta.length);
        equation = (double t, double y[]) -> {
            double dy[] = new double[n];
            for (int i = 0; i < n; i++) {
                dy[i] = omega[i];
                for (int j = 0; j < n; j++) {
                    dy[i] += (k / n) * Math.sin(y[j] - y[i]);
                }
            }
            return dy;
        };
    }

    public double[] update(double h) {
        double[] th2 = RungeKutta.rk4(t, theta, h, equation);
        for (int i = 0; i < n; i++) {
            theta[i] = th2[i];
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
            omega[i] = 5. + 2 * i;
            theta[i] = 2. * Math.PI * Math.random();
        }
        double kArray[] = {0., 5.};
        for (double k : kArray) {
            Kuramoto sys = new Kuramoto(theta, omega, k);
            String filename = Kuramoto.class.getSimpleName()
                    + "-" + String.valueOf(k) + "-output.txt";
            try (BufferedWriter out = FileIO.openWriter(filename)) {
                for (int t = 0; t < tmax; t++) {
                    double th[] = sys.update(h);
                    StringBuilder sb = new StringBuilder();
                    sb.append(t).append(" ");
                    for (int i = 0; i < n; i++) {
                        sb.append(Math.sin(th[i])).append(" ");
                    }
                    out.write(sb.toString());
                    out.newLine();
                }
            }
        }
    }
}
