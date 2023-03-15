package kuramoto;

import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author tadaki
 */
public class Observation {

    private final Kuramoto sys;
    private final int n;

    public Observation(Kuramoto sys, int n) {
        this.sys = sys;
        this.n = n;
    }

    public List<Point2D.Double> doObserve(int tmax, double h) {
        List<Point2D.Double> plist = 
                Collections.synchronizedList(new ArrayList<>());
        for (int t = 0; t < tmax; t++) {
            double th[] = sys.update(h);
            double rPart = 0.;
            double iPart = 0.;
            for (int i = 0; i < n ; i++) {
                rPart += Math.cos(th[i]) / n;
                iPart += Math.sin(th[i]) / n;
            }
            double d = Math.sqrt(rPart * rPart + iPart * iPart);
            plist.add(new Point2D.Double(t, d));
        }
        return plist;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int n = 32;
        int tmax = 10000;
        double h = 0.001;
        double theta[] = new double[n];
        double omega[] = new double[n];
        double k = 5.;
        for (int i = 0; i < n; i++) {
            omega[i] = 5. * (0.8 + 0.4 * Math.random());
            theta[i] = 2. * Math.PI * Math.random();
        }
        Observation observation = new Observation(
                new Kuramoto(theta, omega, k), n);

        List<Point2D.Double> plist = observation.doObserve(tmax, h);

        String filename = "Kuramoto"+Observation.class.getSimpleName()
                + "-" + String.valueOf(k) + "-output.txt";
        try ( PrintStream out = new PrintStream(filename)) {
            plist.forEach(p -> out.println(p.x+" "+p.y));
        }
    }

}
