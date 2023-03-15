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
public class HarmonicOscillatorWithExternalForce2 {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        double x0 = 0.;
        double v0 = 1.;
        double k = 1.;// k/m

        //external force
        double gamma = 1.1 * Math.sqrt(k);
        double alpha = 0.01;
        double delta = alpha * 2 * Math.PI / gamma;
        double f = 1 / delta;
        List<String> headers = new ArrayList<>();
        headers.add("little-fast");
        headers.add("omega = " + String.valueOf(Math.sqrt(k)));
        headers.add("gamma = " + String.valueOf(gamma));

        //defining the external force with Lambda expression
        DoubleFunction<Double> exForce
                = t -> {
                    double xx = t % (2. * Math.PI / gamma);
                    if (xx < delta) {
                        return f;
                    }
                    return 0.;
                };

        HarmonicOscillatorWithExternalForce sys
                = new HarmonicOscillatorWithExternalForce(x0, v0, k, exForce);
        double t = 512.;
        int nstep = (int) (t * 256);
        List<State> states = sys.evolution(t, nstep);
        String filename
                = "blow-output-" + headers.get(0) + ".txt";
        try ( PrintStream out = new PrintStream(filename)) {
            AbstractOscillator.printHeader(headers, out);
            states.forEach(s -> out.println(s.x() + " " + s.y()[0]));
        }

    }
}
