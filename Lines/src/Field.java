import java.awt.Graphics;

public class Field implements Drawable, Clickable{
	private int _nX;
	private int _nY;
	private int _cellWidth;
	private int _cellHeight;
	
	private GameLogic _gameLogic;
	
	private LinesGame _linesGame;
	private SettingsManager _settings;
	
	private Sprite _cellSprite;
	private Sprite _cellSelectedSprite;
	
	public Field(LinesGame linesGame) {
		init(linesGame);
	}
	
	public void init(LinesGame linesGame) {
		_linesGame = linesGame;
		_gameLogic = _linesGame.getGameLogic();
		_settings = SettingsManager.INSTANCE;
		_nX = _settings.getInteger("FieldNX", 9);
		_nY = _settings.getInteger("FieldNY", 9);
		_cellWidth = _settings.getInteger("FieldCellWidth", 16);
		_cellHeight = _settings.getInteger("FieldCellHeight", 16);
		
		_cellSprite = new Sprite("CellBackground");
		_cellSelectedSprite = new Sprite("CellBackgroundSelected");	
	}
	
	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				if (i == _gameLogic.getSelectedCellY() && j == _gameLogic.getSelectedCellX()) {
					_cellSelectedSprite.draw(graphics, dX + j * _cellWidth,  dY + i * _cellHeight);
				} else {
					_cellSprite.draw(graphics, dX + j * _cellWidth, dY + i * _cellHeight);
				}
			}
		}
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				Ball ball = _gameLogic.getBall(j, i);
				if (ball != null) {
					ball.draw(graphics, dX + j * _cellWidth, dY + i * _cellHeight);
				}
			}
		}
	}
	
	public void click(int mouseX, int mouseY, int dX, int dY) {
		if (mouseX >= dX && mouseX < dX + _nX * _cellWidth &&
				mouseY >= dY && mouseY < dY + _nY * _cellHeight) {
			int cellX = (mouseX - dX) / _cellWidth;
			int cellY = (mouseY - dY) / _cellHeight;
			_gameLogic.clickOn(cellX, cellY);
		}
	}
	
}
