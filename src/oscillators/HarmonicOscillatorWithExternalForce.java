package oscillators;

import rungeKutta.State;
import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Harmonic oscillator with external force
 *
 * @author tadaki
 */
public class HarmonicOscillatorWithExternalForce extends AbstractOscillator {

    /**
     *
     * @param x initial value of deviation
     * @param v initial value of velocity
     * @param k k/m
     * @param exForce external force as a function of time
     */
    public HarmonicOscillatorWithExternalForce(double x, double v, double k,
            DoubleFunction<Double> exForce) {
        super(x, v);
        equation = (double t, double[] yy) -> {
            double dy[] = new double[2];
            dy[0] = yy[1];// dx/dt = v
            // dv/dt = - (k/m) x + exF(t)
            dy[1] = -k * yy[0] + exForce.apply(t);
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

        //external force
        double gamma = Math.sqrt(k) + 0.1;
        double beta = 0.;
        double f = .1;
        List<String> headers = new ArrayList<>();
        headers.add("external force");
        headers.add("omega = " + String.valueOf(Math.sqrt(k)));
        headers.add("gamma = " + String.valueOf(gamma));

        //defining the external force with Lambda expression
        DoubleFunction<Double> exForce
                = t -> f * Math.cos(gamma * t + beta);

        HarmonicOscillatorWithExternalForce sys
                = new HarmonicOscillatorWithExternalForce(x0, v0, k, exForce);
        double t = 256.;
        int nstep = (int) (t * 256);
        List<State> states = sys.evolution(t, nstep);
        String filename
                = HarmonicOscillatorWithExternalForce.class.getSimpleName()
                + "-output.txt";
        try ( PrintStream out = new PrintStream(filename)) {
            AbstractOscillator.printHeader(headers, out);
            states.forEach(s -> out.println(s.x() + " " + s.y()[0]));
        }
    }
}
