package restaurante.presentacion;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;

public class BotonSemaforo extends JButton {
    private Color colorBase = new Color(0xFF5F57);
    private boolean hover;
    private static final int DIAMETRO = 19;

    public BotonSemaforo() {
        setPreferredSize(new Dimension(34, 34));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
        });
    }

    public Color getColorBase() { return colorBase; }
    public void setColorBase(Color c) { colorBase = c; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        if (hover) {
            Color halo = new Color(colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(), 165);
            Color medio = new Color(colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(), 105);
            Color suave = new Color(colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(), 38);
            Color edge = new Color(colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(), 0);
            g2.setPaint(new RadialGradientPaint(cx, cy, Math.min(getWidth(), getHeight()) / 2f,
                    new float[]{0f, 0.45f, 0.72f, 1f},
                    new Color[]{halo, medio, suave, edge}));
            g2.fillOval(0, 0, getWidth(), getHeight());
        }

        int x = Math.round(cx - DIAMETRO / 2f);
        int y = Math.round(cy - DIAMETRO / 2f);
        g2.setColor(hover ? colorBase.brighter() : colorBase);
        g2.fillOval(x, y, DIAMETRO, DIAMETRO);

        if (hover) {
            g2.setColor(new Color(255, 255, 255, 105));
            g2.fillOval(x + 3, y + 2, 4, 3);
        }
        g2.dispose();
    }
}
