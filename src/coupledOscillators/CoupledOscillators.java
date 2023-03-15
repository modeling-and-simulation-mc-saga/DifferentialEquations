package coupledOscillators;

import java.io.PrintStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rungeKutta.*;

/**
 * Coupled oscillators
 *
 * @author tadaki
 */
public class CoupledOscillators {

    private final int numOscillators;//the number of oscillators
    private final double y[];//dependent variables of oscillators
    private final DifferentialEquation equation;
    private double t;//independent variable

    /**
     *
     * @param oscillators
     * @param k k/m
     * @param b natural length of springs
     */
    public CoupledOscillators(OscillatorState oscillators[],
            double k, double b) {
        numOscillators = oscillators.length;//
        y = new double[2 * numOscillators];
        t = oscillators[0].t();
        //even elements: deviations
        //odd elements: velocity
        //Setting initial values
        for (int i = 0; i < numOscillators; i++) {
            y[2 * i] = oscillators[i].x();//変位
            y[2 * i + 1] = oscillators[i].v();//速度
        }
        //differential equations
        equation = (double xx, double[] yy) -> {
            double dy[] = new double[2 * numOscillators];
            //0-th particle
            {
                int i = 0;
                int j = 2 * i;
                dy[j] = yy[j + 1];
                dy[j + 1] = -k * (2 * yy[j] - yy[j + 2]);
            }
            //particles between 1st to n-2-th
            for (int i = 1; i < numOscillators - 1; i++) {
                int j = 2 * i;
                dy[j] = yy[j + 1];
                dy[j + 1] = -k * (-yy[j - 2] + 2 * yy[j] - yy[j + 2]);
            }
            //n-1-th particle
            {
                int i = numOscillators - 1;
                int j = 2 * i;
                dy[j] = yy[j + 1];
                dy[j + 1] = -k * (-yy[j - 2] + 2 * yy[j]);
            }
            return dy;
        };
    }

    /**
     * Time evolution: 1 step
     *
     * @param dt one time step
     * @return updated values of dependent variables
     */
    public OscillatorState[] update(double dt) {
        State s = RungeKutta.rk4(new State(t, y), dt, equation);
        //値の更新
        for (int i = 0; i < 2 * numOscillators; i++) {
            y[i] = s.y()[i];
        }
        t += dt;
        return getOscillators();
    }

    /**
     * Time evolution: the time step is t/nsteps
     *
     * @param period duration
     * @param nstep the number of steps
     * @return
     */
    public List<OscillatorState[]> evolution(double period, int nstep) {
        List<OscillatorState[]> history
                = Collections.synchronizedList(new ArrayList<>());
        double dt = period / nstep;
        for (int i = 0; i < nstep; i++) {
            OscillatorState[] osc = update(dt);
            history.add(osc);
        }
        return history;
    }

    /**
     * Time evolution: the time step is t/nsteps
     *
     * @param period duration
     * @param nstep the number of steps
     * @param tic frequency at returning values
     * @return
     */
    public List<OscillatorState[]> evolution(double period, int nstep, int tic) {
        List<OscillatorState[]> history
                = Collections.synchronizedList(new ArrayList<>());
        double dt = period / nstep;
        for (int i = 0; i < nstep; i++) {
            OscillatorState[] o = update(dt);
            if (i % tic == 0) {
                history.add(o);
            }
        }
        return history;
    }

    public OscillatorState[] getOscillators() {
        OscillatorState oscillators[]
                = new OscillatorState[numOscillators];
        for (int i = 0; i < numOscillators; i++) {
            oscillators[i]
                    = new OscillatorState(t, y[2 * i], y[2 * i + 1]);
        }
        return oscillators;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //3 oscillators
        double t0 = 0.;
        OscillatorState[] initialOscillators = {
            new OscillatorState(t0, 1., 0.),
            new OscillatorState(t0, 2., 0.),
            new OscillatorState(t0, -1., 0.)
        };
        double b = 10.;
        double k = 1.;
        CoupledOscillators sys
                = new CoupledOscillators(initialOscillators, k, b);
        double period = 32;
        int nstep = 256 * 128;
        int tic = 200;
        List<OscillatorState[]> history
                = sys.evolution(period, nstep, tic);
        String filename = CoupledOscillators.class.getSimpleName()
                + "-z-output.txt";
        try ( PrintStream out = new PrintStream(filename)) {
            for (OscillatorState[] oscillators : history) {
                out.println(
                        oscillators[0].t() + " "//time 
                        + oscillators[0].x() + " "//1st oscillator
                        + oscillators[1].x() + " "//2nd oscillator
                        + oscillators[2].x()//3rd oscillator
                );
            }
        }
    }

}
