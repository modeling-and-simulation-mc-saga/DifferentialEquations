package rungeKutta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 4th order Runge-Kutta method
 *
 * @author tadaki
 */
public class RungeKutta {

    /**
     * One step from x to x + h
     *
     * @param s initial state
     * @param h step
     * @param eq class contains differential equations
     * @return next values of dependent valiables
     */
    public static State rk4(
            State s, double h, DifferentialEquation eq) {
        double x = s.x();
        double[] y = s.y();
        int n = y.length;
        double hh = h / 2.;
        double h6 = h / 6.;
        double k1[] = eq.rhs(x, y);
        double xh = x + hh;
        double yt[] = new double[n];
        for (int i = 0; i < n; i++) {
            yt[i] = y[i] + hh * k1[i];
        }
        double k2[] = eq.rhs(xh, yt);
        for (int i = 0; i < n; i++) {
            yt[i] = y[i] + hh * k2[i];
        }
        double k3[] = eq.rhs(xh, yt);
        for (int i = 0; i < n; i++) {
            yt[i] = y[i] + h * k3[i];
        }
        double k4[] = eq.rhs(x + h, yt);
        double yy[] = new double[n];
        for (int i = 0; i < n; i++) {
            yy[i] = y[i] + h6 * (k1[i] + 2. * k2[i] + 2. * k3[i] + k4[i]);
        }
        return new State(x + h, yy);
    }

    /**
     * solve by rk4 from x1 to x2 with nstep
     *
     * @param vstart start values of dependent valiables
     * @param x1 initial value of independent valiable
     * @param x2 final value of independent valiable
     * @param nstep the number of steps between x1 and x3
     * @param eq class contains differential equations
     * @return sequence of states
     */
    public static List<State> rkdumb(
            double vstart[], double x1, double x2, int nstep,
            DifferentialEquation eq) {

        return rkdumb(new State(x1, vstart), x2, nstep, eq);
    }

    public static List<State> rkdumb(
            State initial, double x2, int nstep,
            DifferentialEquation eq) {

        List<State> sequence = Collections.synchronizedList(new ArrayList<>());

        double x = initial.x();
        double h = (x2 - x) / nstep;
        State s = initial;
        sequence.add(s);
        for (int t = 1; t < nstep; t++) {
            State newState = rk4(s, h, eq);
            if ((double) (x + h) == x) {
                System.err.println("too small step");
            }
            x += h;

            s = newState;
            sequence.add(s);
        }
        return sequence;
    }
}
