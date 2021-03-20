package kuramoto.gui;

import coupledOscillators2.gui.DrawPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import kuramoto.Kuramoto;

/**
 *
 * @author tadaki
 */
public class Simulation implements Runnable {

    private final int n = 8;//n*n oscillators
    private double k = 3.;//interaction strength
    private final int L;//size of drawPanel
    private double len = 60;//amplitude
    private final double gap;//gap between elements
    private final double h = 0.01;//time step in Runge-Kutta method
    private int t = 0;//time
    private final Kuramoto sys;
    private DrawPanel drawPanel;
    private volatile boolean running = false;
    private final int sleepTime = 1;

    /**
     * Constructor
     * @param L
     * @param random 
     */
    public Simulation(int L, Random random) {
        this.L = L;
        gap = (L - 10.) / n;
        double theta[] = new double[n * n];
        double omega[] = new double[n * n];
        for (int i = 0; i < n * n; i++) {
            omega[i] = 5. + 1. * random.nextGaussian();
            theta[i] = 2. * Math.PI * random.nextDouble();
        }
        sys = new Kuramoto(theta, omega, k);
    }

    /**
     * Create new image
     * @param image 
     */
    public void createImage(BufferedImage image) {
        double tt[] = sys.update(h);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.gray);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Line2D.Double s = new Line2D.Double(
                        gap * (j + .5) - len / 2, gap * (i + .5),
                        gap * (j + .5) + len / 2, gap * (i + .5));
                g.draw(s);
            }
        }

        g.setColor(Color.red);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int kk = n * j + i;
                Ellipse2D.Double s = new Ellipse2D.Double(
                        gap * (j + .5) + .5 * len * Math.cos(tt[kk]) - 5.,
                        gap * (i + .5) - 5, 10., 10.);
                g.fill(s);
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            if (t % 10 == 0) {
                drawPanel.initImage();
                BufferedImage image = drawPanel.getImage();
                createImage(image);
                drawPanel.setImgage(image);
                t = 0;
            }
            t++;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setDrawPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
        len = drawPanel.getPreferredSize().width / 10.;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setK(double k) {
        running = false;
        this.k = k;
        sys.setK(k);
    }

}
