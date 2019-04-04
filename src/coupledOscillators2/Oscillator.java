package coupledOscillators2;

/**
 *
 * @author tadaki
 */
public class Oscillator {

    public final double y;//平衡位置からのずれ
    public final double v;//速度
    public final double t;//時刻

    public Oscillator(double y, double v) {
        this.y = y;
        this.v = v;
        t = 0;
    }

    public Oscillator(double y, double v, double t) {
        this.y = y;
        this.v = v;
        this.t = t;
    }

}
