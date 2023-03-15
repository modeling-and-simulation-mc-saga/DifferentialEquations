package oscillators;

import rungeKutta.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Abstract class of oscillators
 *
 * @author tadaki
 */
public class AbstractOscillator {

    protected final double x;//deviation
    protected final double v;//velocity
    protected DifferentialEquation equation;//diffential equation

    /**
     * Initializing with initial values
     *
     * @param x
     * @param v
     */
    public AbstractOscillator(double x, double v) {
        this.x = x;
        this.v = v;
    }

    /**
     * Time evolution: the time step is t/nsteps
     *
     * @param t duration
     * @param nstep the number of steps
     * @return
     */
    public List<State> evolution(double t, int nstep) {
        double[] y = new double[2];
        y[0] = x;
        y[1] = v;
        List<State> yy = RungeKutta.rkdumb(new State(0,y), t, nstep, equation);
        return yy;
    }

    /**
     * output head strings
     *
     * @param headers
     * @param out
     * @throws IOException
     */
    static public void printHeader(List<String> headers, PrintStream out)
            throws IOException {
        headers.forEach(s -> out.println("#" + s));
    }
}
