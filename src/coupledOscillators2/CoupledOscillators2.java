package coupledOscillators2;

import java.io.IOException;
import java.io.BufferedWriter;
import myLib.rungeKutta.*;
import java.util.List;
import myLib.utils.Utils;
import myLib.utils.FileIO;

/**
 * 連成振動
 */
public class CoupledOscillators2 {

    private final int numOscillators;
    private final double y[];
    private final DifferentialEquation equation;
    private double t;

    /**
     * コンストラクタ
     * @param os 各振動子の位置と速度の初期値
     * @param m 各振動子の質量
     * @param k 各振動子のバネ定数
     * @param lambda 振動子間の結合
     */
    public CoupledOscillators2(Oscillator os[], 
            double m[], double k[], double lambda[][]) {
        numOscillators = os.length;
        y = new double[2 * numOscillators];
        t = 0;
        for (int i = 0; i < numOscillators; i++) {
            y[2 * i] = os[i].y;
            y[2 * i + 1] = os[i].v;
        }
        //微分方程式の定義
        equation = (double tt, double yy[]) -> {
            double dy[] = new double[2 * numOscillators];
            for (int i = 0; i < numOscillators; i++) {
                int j = 2 * i;
                dy[j] = yy[j + 1];
                double dyy = -(k[i] / m[i]) * yy[j];
                for (int kk = 0; kk < numOscillators; kk++) {
                    dyy -= (lambda[i][kk] / m[i]) *  (yy[j] - yy[2*kk]);
                }
                dy[j + 1] = dyy;
            }
            return dy;
        };
    }

    /**
     * 時間発展：1ステップ
     *
     * @param dt 時間
     * @return 更新後の独立変数の値
     */
    public Oscillator[] update(double dt) {
        //更新後の従属変数の値
        double yy[] = RungeKutta.rk4(t, y, dt, equation);
        //値の更新
        for (int i = 0; i < 2 * numOscillators; i++) {
            y[i] = yy[i];
        }
        t += dt;
        //現在の状態をOscillatorとして返す
        return getOscillators();
    }

    /**
     * 時間間隔とステップ数を指定して時間発展
     *
     * @param period 時間間隔
     * @param nstep ステップ数
     * @return
     */
    public List<Oscillator[]> evolution(double period, int nstep) {
        List<Oscillator[]> history = Utils.createList();
        double dt = period / nstep;
        for (int s = 0; s < nstep; s++) {
            Oscillator[] o = update(dt);
            history.add(o);
        }
        return history;
    }
    /**
     * 時間間隔とステップ数を指定して時間発展
     *
     * @param period 時間間隔
     * @param nstep ステップ数
     * @param tic 値を返すステップ間隔
     * @return
     */
    public List<Oscillator[]> evolution(double period, int nstep, int tic) {
        List<Oscillator[]> history = Utils.createList();
        double dt = period / nstep;
        for (int s = 0; s < nstep; s++) {
            Oscillator[] o = update(dt);
            if (s % tic == 0) {
                history.add(o);
            }
        }
        return history;
    }

    /**
     * 現在の状態をOscillatorとして返す
     *
     * @return
     */
    public Oscillator[] getOscillators() {
        Oscillator oscillators[] = new Oscillator[numOscillators];
        for (int i = 0; i < numOscillators; i++) {
            oscillators[i] = new Oscillator(y[2 * i], y[2 * i + 1], t);
        }
        return oscillators;
    }

    /**
     * 粒子の位置を文字列へ変換：スペース区切り
     *
     * @param oscillators 振動子の配列
     * @param b
     * @return
     */
    static public String positions2String(
            Oscillator oscillators[], double b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < oscillators.length; i++) {
            Oscillator o = oscillators[i];
            sb.append(o.y + (i + 1) * b).append(" ");
        }
        //余分な空白を削除
        int lastSpace = sb.lastIndexOf(" ");
        sb.deleteCharAt(lastSpace);
        return sb.toString();
    }

    public int getNumOscillators() {
        return numOscillators;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //3個の連成振動
        Oscillator[] initialOscillators = {
            new Oscillator(1., 0.),
            new Oscillator(1., 0.),
            new Oscillator(1., 0.)
        };
        double m[]={1.,1.,1.};
        double k[]={0.8,1.,1.2};
        double lambda[][]=new double[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                lambda[i][j]=0.5;
            }
        }
        CoupledOscillators2 sys=new CoupledOscillators2(initialOscillators, 
            m,k ,lambda);
                // 時間40を20000に区分して、積分を実行
        double period = 400.;
        int nstep = 200000;
        // 結果は200ステップ毎に取得
        int tic =200;
                List<Oscillator[]> history = sys.evolution(period, nstep, tic);
        //出力ファイル名の指定
        String filename = CoupledOscillators2.class.getSimpleName()
                + "-output.txt";
        // 結果をファイルへ出力
        try (BufferedWriter out = FileIO.openWriter(filename)) {
            for (Oscillator[] oscillators : history) {
                StringBuilder sb = new StringBuilder();
                double t = oscillators[0].t;
                String positionStr = positions2String(oscillators, 5.);
                FileIO.writeSSV(out, t, positionStr);
            }
        }

    }

}
