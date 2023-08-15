package project.tables.UI;

;

import java.awt.*;

public class Histogram extends Canvas {
    private Graphics2D g2d;

    private final int[] heretics = new int[5];

    private final String[] occupations = {"MINSTREL", "KNIGHT", "PEASANT", "SHRUBBER", "ENCHANTER"};

    public Histogram(int minstrel, int knight, int peasant, int shrubber, int enchanter) {
        heretics[0] = minstrel;
        heretics[1] = knight;
        heretics[2] = peasant;
        heretics[3] = shrubber;
        heretics[4] = enchanter;

        this.setPreferredSize(new Dimension(Controller.WIDTH, Controller.HEIGHT));
    }

    public void paint(Graphics graphics) {
        g2d = (Graphics2D) graphics;

        g2d.setStroke(new BasicStroke(2));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Controller.WIDTH, Controller.HEIGHT);

        drawHistogram(g2d);
    }
    public void drawHistogram(Graphics2D g2d) {
        Font font = new Font("Calibri", Font.PLAIN, 20);
        g2d.setFont(font);
        int x = 10, y = Controller.HEIGHT - (int)(Controller.HEIGHT * 0.1) , maxHeretics = 0;

        for (int i = 0; i < 5; i++) {
            if (heretics[i] > maxHeretics) {
                maxHeretics = heretics[i];
            }
        }

        for (int i = 0; i <= 4; i++) {
            g2d.setColor(Color.WHITE);
            int num = (maxHeretics * i / 4);
            g2d.drawString(num + "", x, y - (int)(y * 0.9 * i / 4));
        }

        x = x + 10 + g2d.getFontMetrics(font).stringWidth(maxHeretics + "");
        int half = (int)(Controller.WIDTH * 0.95 - x) / 5;
        float multi = (float) (y * 0.9 / maxHeretics);

        for (int i = 0; i < 5; i++) {
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawString(occupations[i], x - (g2d.getFontMetrics().stringWidth(occupations[i]) / 2) + half / 2, y + 20);
            int height = (int)(heretics[i] * multi);
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(1));
            Rectangle rectangle = new Rectangle(x, y - height, half, height);
            g2d.draw(rectangle);
            g2d.fill(rectangle);
            x += half;
        }

        x =  20 + g2d.getFontMetrics(font).stringWidth(maxHeretics + "");
        y = Controller.HEIGHT - (int)(Controller.HEIGHT * 0.1);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.lightGray);
        int top = y - (int)(y * 0.9);
        g2d.drawRect(x, top,0, y - top);
        g2d.drawRect(x, y ,(int)(Controller.WIDTH * 0.95 - x), 0);
    }

}
