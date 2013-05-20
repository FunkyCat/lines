import java.awt.Graphics;

public class BallFactory implements Drawable {
	
	private int _queueSize;
	
	private Sprite _cellBgSprite;
	private int _queueCellWidth;
	
	private GameLogic _gameLogic;
	private SettingsManager _settings;
	
	public void init(LinesGame linesGame) {
		_gameLogic = linesGame.getGameLogic();
		_settings = SettingsManager.INSTANCE;
		_queueSize = _settings.getInteger("BellQueueSize", 3);
		_queueCellWidth = _settings.getInteger("BallQueueCellWidth", 48);
	
		_cellBgSprite = new Sprite("CellBackground");
	}
	
	public void draw(Graphics graphics, int dX, int dY) {
		for (int i = 0; i < _queueSize; i++) {
			_cellBgSprite.draw(graphics, dX + i * _queueCellWidth, dY);
			_gameLogic.getQueueBall(i).draw(graphics, dX + i * _queueCellWidth, dY);
		}
	}
}
