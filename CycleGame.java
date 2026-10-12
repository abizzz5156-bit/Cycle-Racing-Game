import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

 class CycleRacingGame extends JPanel
        implements ActionListener, KeyListener {

    private int playerX = 150;
    private final int playerY = 350;

    private int opponentX = 270;
    private int opponentY = -80;

    private int score = 0;
    private int roadLineY = 0;

    private boolean running = false;
    private boolean gameOver = false;

    private final Timer timer;
    private final Random random = new Random();

    private JButton startButton;
    private JButton restartButton;

    public CycleRacingGame() {
        setPreferredSize(new Dimension(450, 500));
        setLayout(null);
        setFocusable(true);
        addKeyListener(this);

        startButton = new JButton("Start Game");
        startButton.setBounds(150, 200, 150, 40);
        startButton.addActionListener(e -> startGame());
        add(startButton);

        restartButton = new JButton("Restart");
        restartButton.setBounds(150, 250, 150, 40);
        restartButton.addActionListener(e -> startGame());
        restartButton.setVisible(false);
        add(restartButton);

        timer = new Timer(40, this);
    }

    private void startGame() {
        playerX = 150;
        playerX = 150;
        opponentX = 270;
        opponentY = -80;
        score = 0;
        roadLineY = 0;

        running = true;
        gameOver = false;

        startButton.setVisible(false);
        restartButton.setVisible(false);

        timer.start();
        requestFocusInWindow();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(40, 150, 60));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.DARK_GRAY);
        g.fillRect(75, 0, 300, getHeight());

        g.setColor(Color.WHITE);
        g.fillRect(75, 0, 5, getHeight());
        g.fillRect(370, 0, 5, getHeight());

        for (int y = -80; y < getHeight(); y += 80) {
            int lineY = y + roadLineY;
            g.fillRect(220, lineY, 8, 40);
        }

        drawCycle(g, playerX, playerY, Color.CYAN);

        if (running || gameOver) {
            drawCycle(g, opponentX, opponentY, Color.RED);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);

        if (!running && !gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("CYCLE RACING", 115, 130);
        }

        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(75, 150, 300, 170);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER", 130, 195);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Final Score: " + score, 140, 230);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Use LEFT and RIGHT arrows", 125, 485);
    }

    private void drawCycle(Graphics g, int x, int y, Color color) {
        g.setColor(Color.BLACK);
        g.fillOval(x, y, 30, 30);
        g.fillOval(x, y + 40, 30, 30);

        g.setColor(Color.WHITE);
        g.drawOval(x, y, 30, 30);
        g.drawOval(x, y + 40, 30, 30);

        g.setColor(color);
        g.drawLine(x + 15, y + 15, x + 5, y + 40);
        g.drawLine(x + 5, y + 40, x + 25, y + 40);
        g.drawLine(x + 25, y + 40, x + 15, y + 15);
        g.drawLine(x + 15, y + 15, x + 15, y + 40);

        g.drawLine(x + 15, y + 15, x + 25, y + 10);
        g.drawLine(x + 8, y + 12, x + 20, y + 12);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running) {
            return;
        }

        opponentY += 6;
        roadLineY = (roadLineY + 6) % 80;

        if (opponentY > getHeight()) {
            opponentY = -80;
            opponentX = 90 + random.nextInt(250);
            score += 10;
        }

        Rectangle player = new Rectangle(
                playerX, playerY, 30, 70);

        Rectangle opponent = new Rectangle(
                opponentX, opponentY, 30, 70);

        if (player.intersects(opponent)) {
            running = false;
            gameOver = true;
            timer.stop();
            restartButton.setVisible(true);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!running) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 85) {
            playerX -= 15;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT
                && playerX < 335) {
            playerX += 15;
        }

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cycle Racing Game");
            CycleRacingGame game = new CycleRacingGame();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);

            game.requestFocusInWindow();
        });
    }
}