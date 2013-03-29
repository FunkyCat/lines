import java.awt.Graphics;
import java.util.Random;


public class Field implements Drawable {
	private int _nX;
	private int _nY;
	private int _cellWidth;
	private int _cellHeight;

	private Ball _state[][];
	
	private LinesGame _linesGame;
	private SettingsManager _settings;
	private BallFactory _ballFactory;
	
	private Sprite _cellSprite;
	
	private Random _random;
	
	public Field(LinesGame linesGame) {
		init(linesGame);
	}
	
	public void init(LinesGame linesGame) {
		_random = new Random();
		_linesGame = linesGame;
		_settings = SettingsManager.INSTANCE;
		_ballFactory = _linesGame.getBallFactory();
		_nX = _settings.getInteger("FieldNX", 9);
		_nY = _settings.getInteger("FieldNY", 9);
		_cellWidth = _settings.getInteger("FieldCellWidth", 16);
		_cellHeight = _settings.getInteger("FieldCellHeight", 16);
		
		_state = new Ball[_nY][_nX];
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				_state[i][j] = null;
			}
		}
		
		_cellSprite = new Sprite("CellBackground");
		
	}
	
	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				_cellSprite.draw(graphics, dX + j * _cellWidth, dY + i * _cellHeight);
			}
		}
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				if (_state[i][j] != null) {
					_state[i][j].draw(graphics, dX + j * _cellWidth, dY + i * _cellHeight);
				}
			}
		}
	}
	
	public void clear() {
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				_state[i][j] = null;
			}
		}
	}
	
	public void addRandomBalls(int count) {
		int rndY, rndX;
		for (int i = 0; i < count; i++) {
			for(;;) {
				rndX = _random.nextInt(_nX);
				rndY = _random.nextInt(_nY);
				if (_state[rndY][rndX] == null) {
					_state[rndY][rndX] = _ballFactory.getNextBall();
					_state[rndY][rndX].setFieldX(rndX);
					_state[rndY][rndX].setFieldY(rndY);
					break;
				}
			}
		}
	}
	
	public void addBall(int x, int y, int type) {
		_state[y][x] = new Ball();
		_state[y][x].init(type, x, y);
	}

}
