package oscillators;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import myLib.rungeKutta.*;
import myLib.utils.FileIO;
import myLib.utils.Utils;

/**
 * 振動子の抽象クラス
 * @author tadaki
 */
public class AbstractOscillator {

    protected final double x;//変位
    protected final double v;//速度
    protected DifferentialEquation equation;//微分方程式

    /**
     * 初期値を用いて初期化
     * @param x
     * @param v 
     */
    public AbstractOscillator(double x, double v) {
        this.x = x;
        this.v = v;
    }

    /**
     * 時間発展：t/nstepを時間幅として4次のRKを実行
     * @param t 
     * @param nstep 
     * @return 
     */
    public List<Point2D.Double> evolution(double t, int nstep) {
        double[] y = new double[2];
        y[0] = x;
        y[1] = v;
        double[][] yy = RungeKutta.rkdumb(y, 0., t, nstep, equation);
        List<Point2D.Double> points = Utils.createList();
        double dt = t / nstep;
        for (int i = 0; i < nstep; i++) {
            double tt = i * dt;
            points.add(new Point2D.Double(tt, yy[0][i]));
        }
        return points;
    }

    /**
     * 結果のリストをファイルへ書きだす
     * @param points
     * @param filename
     * @throws IOException 
     */
    static public void printData(List<Point2D.Double> points,String filename) 
            throws IOException{
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for(Point2D.Double p:points){
                String s = p.x+" "+p.y;
                out.write(s);
                out.newLine();
            }
        }
    }
}
