import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class LinesGame extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private static final SettingsManager _settings = SettingsManager.INSTANCE;
	
	private boolean _running = false;
	private int _currentFps = 0;
	
	private Background _background;
	private Field _field;
	
	private int _fieldX;
	private int _fieldY;

	private BufferedImage _image;
		
	public static void main(String[] args) {
		LinesGame linesGame = new LinesGame();
		Dimension windowDim = new Dimension(
				_settings.getInteger("GameWidth", 600),
				_settings.getInteger("GameHeight", 400));
		linesGame.setPreferredSize(windowDim);
		linesGame.setMinimumSize(windowDim);
		linesGame.setMaximumSize(windowDim);
				
		JFrame frame = new JFrame(_settings.getString("WindowTitle", "NoTitle"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(linesGame);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
		linesGame.start();
	}
	
	public void start() {
		init();
		
		_running = true;
		new Thread(this).start();
	}
	
	public void stop() {
		_running = false;
	}
	
	private void init() {
		_image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		_fieldX = _settings.getInteger("FieldX", 0);
		_fieldY = _settings.getInteger("FieldY", 0);
		
		_background = new Background(this);
		_field = new Field(this);
		
		initInputs();
	}

	@Override
	public void run() {
		long fpsTimer = System.currentTimeMillis();
		int frames = 0;
		double nsPerTick = 1000000000.0 / _settings.getDouble("FPS", 60.0);
		long runNs;
		long lastNs;
		long nsToSleep = 0;
		double delta = 0;
		double deltaSum = 0;

		while (_running) {
			lastNs = System.nanoTime();
			runStep(delta);
			frames++;
			runNs = System.nanoTime();
			nsToSleep = (long) ((nsPerTick - (runNs - lastNs)));
			if (nsToSleep >= 0) {
				try {
					Thread.sleep(nsToSleep / 1000000,
							(int) (nsToSleep % 1000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			delta = (double) (System.nanoTime() - lastNs) / 1000000000;
			deltaSum += delta;

			if (System.currentTimeMillis() - fpsTimer >= 1000) {
				System.out.println(
						"FPS: " + frames + ", deltaSum: " + deltaSum);
				_currentFps = frames;
				deltaSum = 0;
				frames = 0;
				fpsTimer = System.currentTimeMillis();
			}
		}	
	}
	
	private void runStep(double delta) {
		tick(delta);
		draw();
	}
	
	private void tick(double delta) {
		
	}
	
	private void draw() {
		BufferStrategy bfs = getBufferStrategy();
		if (bfs == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}

		Graphics g = bfs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		_background.draw(g, 0, 0);
		_field.draw(g, _fieldX, _fieldY);
		
		g.dispose();
		bfs.show();
	}
	
	private void initInputs() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("click!");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
		});
	}
	
}
