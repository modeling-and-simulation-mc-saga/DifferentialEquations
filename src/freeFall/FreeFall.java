package freeFall;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import rungeKutta.*;

/**
 * Example: free falling object
 *
 * @author tadaki
 */
public class FreeFall {

    protected final double yInit;//initial height
    protected final double vInit;//initial velocity
    protected final double g = 9.8;//gravitational constant
    protected DifferentialEquation equation;//differential equation

    public FreeFall(double yInit, double vInit) {
        this.yInit = yInit;
        this.vInit = vInit;
        equation = (double t, double y[]) -> {
            double dy[] = new double[2];
            dy[0] = y[1];
            dy[1] = -g;
            return dy;
        };
    }

    /**
     * Time evolution: the time step is t/nsteps
     *
     * @param t
     * @param nstep
     * @return
     */
    public List<State> evolution(double t, int nstep) {
        double[] y = new double[2];
        y[0] = yInit;
        y[1] = vInit;
        List<State> yy = RungeKutta.rkdumb(new State(0, y), t, nstep, equation);
        return yy;
    }

    public static void main(String args[]) throws IOException {
        FreeFall sys = new FreeFall(0., 10.);
        double t = 4;
        int nstep = (int) (t * 256);
        // integrating by deviding the duration t by nsteps
        // getting the result as a list
        List<State> states = sys.evolution(t, nstep);
        // output the result into file
        String filename = FreeFall.class.getSimpleName()
                + "-output.txt";
        try ( PrintStream out = new PrintStream(filename)) {
            states.forEach(s -> out.println(s.x() + " " + s.y()[0]));
        }

    }
}
