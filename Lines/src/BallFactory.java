import java.awt.Graphics;
import java.util.Random;


public class BallFactory implements Drawable {
	
	private Ball _queue[];
	private int _queueSize;
	
	private Sprite _cellBgSprite;
	private int _queueCellWidth;
	
	private LinesGame _linesGame;
	private SettingsManager _settings;
	
	private Random _random;
	
	public void init(LinesGame linesGame) {
		_random = new Random();
		_linesGame = linesGame;
		_settings = SettingsManager.INSTANCE;
		_queueSize = _settings.getInteger("BellQueueSize", 3);
		_queueCellWidth = _settings.getInteger("BallQueueCellWidth", 48);
		_queue = new Ball[_queueSize];
		for (int i = 0; i < _queueSize; i++) {
			_queue[i] = new Ball();
			_queue[i].init(getRandomType());
		}
		ImagesManager imgMng = ImagesManager.INSTANCE;
		_cellBgSprite = new Sprite("CellBackground");
	}
	
	public Ball getNextBall() {
		Ball ret = _queue[0];
		for (int i = 0; i < _queueSize - 1; i++) {
			_queue[i] = _queue[i + 1];
		}
		_queue[_queueSize - 1] = new Ball();
		_queue[_queueSize - 1].init(getRandomType());
		return ret;
	}
	
	public int getRandomType() {
		return _random.nextInt(3);
	}
	
	public void draw(Graphics graphics, int dX, int dY) {
		for (int i = 0; i < _queueSize; i++) {
			_cellBgSprite.draw(graphics, dX + i * _queueCellWidth, dY);
			_queue[i].draw(graphics, dX + i * _queueCellWidth, dY);
		}
	}
}
