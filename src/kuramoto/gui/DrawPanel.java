package kuramoto.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/**
 *
 * @author tadaki
 */
public class DrawPanel extends javax.swing.JPanel {
    private BufferedImage image;
    /**
     * Creates new form DrawPanel
     */
    public DrawPanel() {
        initComponents();
    }

    public void setImgage(BufferedImage image) {
        this.image = image;
        repaint();
    }
    public void initImage() {
        int width = getWidth();
        int height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (java.awt.Graphics2D) image.getGraphics();
        g.setColor(this.getBackground());
        g.fillRect(0, 0, width, height);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D gg=(Graphics2D)g;
//        gg.translate(0, getWidth());
//        gg.scale(1, -1);
        gg.drawImage(image, null,10,10);
    }

    public BufferedImage getImage() {
        return image;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
