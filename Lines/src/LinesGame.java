import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class LinesGame extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
	private static final SettingsManager _settings = SettingsManager.INSTANCE;
	
	private boolean _running = false;
	private int _currentFps = 0;
	private int _score = 0;
	
	private GameLogic _gameLogic;
	private boolean _isGameOver;
	
	private Background _background;
	private Field _field;
	private BallFactory _ballFactory;
	
	private GuiGameOver _guiGameOver;
	private GuiNewGame _guiNewGame;
	
	private int _fieldX;
	private int _fieldY;
	private int _guiGameOverX;
	private int _guiGameOverY;
	private int _guiNewGameX;
	private int _guiNewGameY;
	private int _ballFactoryX;
	private int _ballFactoryY;
		
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
	
	public void newGame() {
		_gameLogic.clear();
		_gameLogic.addRandomBalls(_settings.getInteger("StartGameBalls", 3));
		_score = 0;
		_isGameOver = false;
	}
	
	public void gameOver() {
		_isGameOver = true;
	}
	
	public void score(int count) {
		_score += 10 + (count - _settings.getInteger("MinimalLineLength", 5)) * 3;  
	}
	
	public BallFactory getBallFactory() {
		return _ballFactory;
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
		_fieldX = _settings.getInteger("FieldX", 0);
		_fieldY = _settings.getInteger("FieldY", 0);
		
		_gameLogic = new GameLogic(this);
		
		_ballFactory = new BallFactory();
		_ballFactory.init(this);
		
		_background = new Background(this);
		_field = new Field(this);
		
		_guiGameOver = new GuiGameOver();
		_guiGameOverX = _settings.getInteger("GuiGameOverX", 74);
		_guiGameOverY = _settings.getInteger("GuiGameOverY", 134);
		
		_guiNewGame = new GuiNewGame();
		_guiNewGame.init(this);
		_guiNewGameX = _settings.getInteger("GuiNewGameX", 74);
		_guiNewGameY = _settings.getInteger("GuiNewGameY", 300);
		
		_ballFactoryX = _settings.getInteger("BallFactoryX", 452);
		_ballFactoryY = _settings.getInteger("BallFactoryY", 152);
		
		initInputs();
		
		newGame();
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
			
			if (System.currentTimeMillis() - fpsTimer >= 1000) {
				_currentFps = frames;
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
		_ballFactory.draw(g, _ballFactoryX, _ballFactoryY);
		
		if (_isGameOver) {
			_guiGameOver.draw(g, _guiGameOverX, _guiGameOverY);
		}
		
		_guiNewGame.draw(g, _guiNewGameX, _guiNewGameY);
		
		drawFps(g);
		
		g.dispose();
		bfs.show();
	}
	
	private void click(int mouseX, int mouseY) {
		if (!_isGameOver) {
			_field.click(mouseX, mouseY, _fieldX, _fieldY);
		}
		_guiNewGame.click(mouseX, mouseY, _guiNewGameX, _guiNewGameY);
	}
	
	private void drawFps(Graphics graphics) {
		String fpsString = new String("FPS: " + _currentFps);
		graphics.setColor(Color.white);
		graphics.drawChars(fpsString.toCharArray(), 0, fpsString.length(), getWidth() - 50, 20);
		String scoreString = new String("Score: " + _score);
		graphics.drawChars(scoreString.toCharArray(), 0, scoreString.length(), getWidth() - 150, 50);
	}
	
	private void initInputs() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				click(e.getX(), e.getY());
			}

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) { }

			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { }
			
		});
	}
	
	public GameLogic getGameLogic() {
		return _gameLogic;
	}
	
}
