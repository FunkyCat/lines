import java.awt.Graphics;


public class Field implements Drawable {
	private int _nX;
	private int _nY;
	private int _cellWidth;
	private int _cellHeight;

	private int _state[][];
	
	private LinesGame _linesGame;
	private SettingsManager _settings;
	
	private Ball _tstBall;
	
	private Sprite _cellSprite;
	
	public Field(LinesGame linesGame) {
		init(linesGame);
	}
	
	public void init(LinesGame linesGame) {
		_linesGame = linesGame;
		_settings = SettingsManager.INSTANCE;
		_nX = _settings.getInteger("FieldNX", 9);
		_nY = _settings.getInteger("FieldNY", 9);
		_cellWidth = _settings.getInteger("FieldCellWidth", 16);
		_cellHeight = _settings.getInteger("FieldCellHeight", 16);
		
		_state = new int[_nY][_nX];
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				_state[i][j] = Cell.STATE_FREE;
			}
		}
		
		_cellSprite = new Sprite("CellBackground");
		
		_tstBall = new Ball();
		_tstBall.init(Ball.TYPE_BLUE);
	}
	
	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				_cellSprite.draw(graphics, dX + j * _cellWidth, dY + i * _cellHeight);
			}
		}
		_tstBall.draw(graphics, dX, dY);
	}

}
