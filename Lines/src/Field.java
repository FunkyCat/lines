import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


public class Field implements Drawable, Clickable{
	private int _nX;
	private int _nY;
	private int _cellWidth;
	private int _cellHeight;
	private int _moveBallsCount;
	private int _minimalLineLength;
	ArrayList<Point> _lineDirections;

	private Ball _state[][];
	private Ball _selectedBall;
	private int _selectedCellX;
	private int _selectedCellY;
	
	private LinesGame _linesGame;
	private SettingsManager _settings;
	private BallFactory _ballFactory;
	
	private Sprite _cellSprite;
	private Sprite _cellSelectedSprite;
	
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
		_moveBallsCount = _settings.getInteger("MoveBallsCount", 3);
		_minimalLineLength = _settings.getInteger("MinimalLineLength", 5);
		_lineDirections = new ArrayList<Point>();
		_lineDirections.add(new Point(1, 0));
		_lineDirections.add(new Point(0, 1));
		_lineDirections.add(new Point(1, 1));
		_lineDirections.add(new Point(1, -1));
		
		_selectedCellX = _selectedCellY = -1;
		_selectedBall = null;
		_state = new Ball[_nY][_nX];
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				_state[i][j] = null;
			}
		}
		
		_cellSprite = new Sprite("CellBackground");
		_cellSelectedSprite = new Sprite("CellBackgroundSelected");
		
	}
	
	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				if (i == _selectedCellY && j == _selectedCellX) {
					_cellSelectedSprite.draw(graphics, dX + j * _cellWidth,  dY + i * _cellHeight);
				} else {
					_cellSprite.draw(graphics, dX + j * _cellWidth, dY + i * _cellHeight);
				}
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
	
	public void click(int mouseX, int mouseY, int dX, int dY) {
		if (mouseX >= dX && mouseX < dX + _nX * _cellWidth &&
				mouseY >= dY && mouseY < dY + _nY * _cellHeight) {
			int cellX = (mouseX - dX) / _cellWidth;
			int cellY = (mouseY - dY) / _cellHeight;
			if (_state[cellY][cellX] != null) {
				_selectedCellX = cellX;
				_selectedCellY = cellY;
				_selectedBall = _state[cellY][cellX];
			} else if (_state[cellY][cellX] == null && _selectedBall != null) {
				if (canMove(_selectedCellX, _selectedCellY, cellX, cellY)) {
					_state[cellY][cellX] = _selectedBall;
					_state[_selectedCellY][_selectedCellX] = null;
					_selectedBall = null;
					_selectedCellX = _selectedCellY = -1;
					checkMove(cellY, cellX);
				}
			}
		}
	}
	
	private boolean canMove(int fromX, int fromY, int toX, int toY) {
		return true;
	}
	
	private void checkMove(int cellY, int cellX) {
		int type = _state[cellY][cellX].getType();
		ArrayList<Point> cells = new ArrayList<Point>();
		int counter = 0;
		int dX, dY;
		int pX, pY;
		int aX, aY;
		
		for (int i = 0; i < _lineDirections.size(); i++) {
			counter = 1;
			dX = _lineDirections.get(i).x;
			dY = _lineDirections.get(i).y;
			pX = cellX - dX;
			pY = cellY - dY;
			while (pX >= 0 && pY >= 0 && pX < _nX && pY < _nY &&
					_state[pY][pX] != null && _state[pY][pX].getType() == type) {
				pX -= dX;
				pY -= dY;
				counter++;
			}
			aX = pX + dX;
			aY = pY + dY;
			pX = cellX + dX;
			pY = cellY + dY;
			while (pX >= 0 && pY >= 0 && pX < _nX && pY < _nY &&
					_state[pY][pX] != null && _state[pY][pX].getType() == type) {
				pX += dX;
				pY += dY;
				counter++;
			}
			if (counter >= _minimalLineLength) {
				for (int j = 0; j < counter; j++) {
					if (aX + dX * j != cellX || aY + dY * j != cellY) {
						cells.add(new Point(aX + dX * j, aY + dY * j));
					}
				}
			}
		}
		if (cells.size() != 0) {
			cells.add(new Point(cellX, cellY));
		}
		
		if (cells.size() != 0) {
			for (int i = 0; i < cells.size(); i++) {
				_state[cells.get(i).y][cells.get(i).x] = null; 
			}
			_linesGame.score(cells.size());
		} else {
			addRandomBalls(_moveBallsCount);
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
