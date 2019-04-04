package oscillators;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * 調和振動子＋強制振動
 *
 * @author tadaki
 */
public class HarmonicOscillatorWithExternalForce extends AbstractOscillator {

    private final double omega;

    /**
     * コンストラクタ
     *
     * @param x 振幅の初期値
     * @param v 速度の初期値
     * @param k k/mに相当
     * @param exForce 外力：時間の関数
     */
    public HarmonicOscillatorWithExternalForce(double x, double v, double k,
            DoubleFunction<Double> exForce) {
        super(x, v);
        this.omega = Math.sqrt(k);
        //微分方程式の記述
        equation = (double t, double[] yy) -> {
            double dy[] = new double[2];
            dy[0] = yy[1];// dx/dt = v
            // dv/dt = - omega^2 x + exF(t)
            dy[1] = -omega * omega * yy[0] + exForce.apply(t);
            return dy;
        };
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        double x0 = 0.;
        double v0 = 1.;
        double k = 1.;// k/mに相当

        //外力の定義
        double gamma = Math.sqrt(k)+0.1;
        double beta = 0.;
        double f = .1;
        DoubleFunction<Double> exForce = (double t) -> {
            return f * Math.cos(gamma * t + beta);
        };
        
        HarmonicOscillatorWithExternalForce sys
                = new HarmonicOscillatorWithExternalForce(x0, v0, k, exForce);
        double t = 200.;
        int nstep = 10000;
        // 時間50を10000に区分して、積分を実行
        // 結果を(t,x)のリストで得る
        List<Point2D.Double> points = sys.evolution(t, nstep);
        // 結果をファイルへ出力
        String filename
                = HarmonicOscillatorWithExternalForce.class.getSimpleName()
                + "-output.txt";
        AbstractOscillator.printData(points, filename);
    }
}
