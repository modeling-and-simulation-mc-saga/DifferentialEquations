package oscillators;

import rungeKutta.State;
import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Harmonic oscillator
 *
 * @author tadaki
 */
public class HarmonicOscillator extends AbstractOscillator {

    /**
     *
     * @param x initial value of deviation
     * @param v initial value of velocity
     * @param k k/m
     */
    public HarmonicOscillator(double x, double v, double k) {
        super(x, v);
        //differential equation
        equation = (double xx, double[] yy) -> {
            double dy[] = new double[2];
            dy[0] = yy[1];// dx/dt = v
            dy[1] = -k * yy[0];// dv/dt = - (k/m) x
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
        double k = 1.;// k/m
        List<String> headers = new ArrayList<>();
        headers.add("harmonic");
        headers.add("omega = " + String.valueOf(Math.sqrt(k)));

        HarmonicOscillator sys = new HarmonicOscillator(x0, v0, k);
        double t = 64;
        int nstep = (int) (t * 256);
        // integrating by deviding the duration t by nsteps
        // getting the result as a list
        List<State> states = sys.evolution(t, nstep);
        // output the result into file
        String filename = HarmonicOscillator.class.getSimpleName()
                + "-output.txt";
        try ( PrintStream out = new PrintStream(filename)) {
            AbstractOscillator.printHeader(headers, out);
            states.forEach(s -> out.println(s.x() + " " + s.y()[0]));
        }
    }
}
