package kuramoto;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import myLib.utils.FileIO;
import myLib.utils.Utils;

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
        List<Point2D.Double> plist = Utils.createList();
        for (int t = 0; t < tmax; t++) {
            double th[] = sys.update(h);
            double d = 0.;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    //ここを記述

                }
            }
            plist.add(new Point2D.Double(t, d));
        }
        return plist;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //振動子系の定義
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
        //実行
        List<Point2D.Double> plist = observation.doObserve(tmax, h);
        //ファイへ出力
        String filename = Observation.class.getSimpleName()
                + "-" + String.valueOf(k) + "-output.txt";
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for (Point2D.Double p : plist) {
                FileIO.writeSSV(out, p.x, p.y);
            }
        }
    }

}
