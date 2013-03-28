import java.awt.*;
import javax.swing.JFrame;

public class LinesGame extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private static final SettingsManager _settings = SettingsManager.INSTANCE;
	
	private boolean _running = false;
	private int _currentFps = 0;
	
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
		
	}

}
