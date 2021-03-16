package coupledOscillators2.gui;

import coupledOscillators2.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;
import myLib.utils.Utils;

/**
 *
 * @author tadaki
 */
public class Simulation implements Runnable {

    private final int n;
    private final int height;
    private double r = 40.;
    private double h = 0.01;
    private volatile boolean running = false;
    private final int sleepTime = 10;

    private final Random random;
    private CoupledOscillators2 sys = null;
    private List<Point2D.Double> pList = null;
    private DrawPanel drawPanel;

    public Simulation(int n, int height, Random random) {
        this.n = n;
        this.height = height;
        this.random = random;
        prepare();
        preparePoint();
    }

    private void prepare() {
        Oscillator os[] = new Oscillator[n];
        double m[] = new double[n];
        double k[] = new double[n];
        double lambda[][] = new double[n][n];
        for (int i = 0; i < n; i++) {
            double theta = 2. * Math.PI * random.nextDouble();
            os[i] = new Oscillator(Math.cos(theta), Math.sin(theta));
            m[i] = 1.;
            k[i] = 0.2 * random.nextDouble() + .9;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    lambda[i][j] = 0.05;
                }
            }
        }
        sys = new CoupledOscillators2(os, m, k, lambda);
    }

    private void preparePoint() {
        pList = Utils.createList();
        for (int i = 0; i < n; i++) {
            pList.add(new Point2D.Double(height * random.nextDouble(),
                    height * random.nextDouble()));
        }
    }

    public void update() {
        sys.update(h);
    }

    public void createImage(BufferedImage image) {
        Oscillator os[] = sys.getOscillators();
        Color color = new Color(255, 255, 128);
        Graphics2D g = (Graphics2D) image.getGraphics();
        for (int i = 0; i < os.length; i++) {
            color = new Color(255, 255, 128);
            double z = os[i].y;
            if (z < 0.) {
                color = new Color(255, 128, 255);
                z *= -1;
            }
            g.setColor(color);
            Point2D.Double p = pList.get(i);
            g.fill(new Ellipse2D.Double(p.x, p.y, r * z, r * z));
            double dx = 6 * random.nextDouble() - 3.;
            double dy = 6 * random.nextDouble() - 3.;
            double x = (p.x + dx + height) % height;
            double y = (p.y + dy + height) % height;
            p.setLocation(x, y);
        }
    }

    public void setDrawPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
    }

    @Override
    public void run() {
        while (running) {
            update();
            drawPanel.initImage();
            BufferedImage image = drawPanel.getImage();
            createImage(image);
            drawPanel.setImgage(image);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
