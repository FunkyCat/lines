import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class GameLogic {
	
	private LinesGame _linesGame;
	private Random _random;
	private SettingsManager _settings;
	
	private int _nX, _nY;
	private int _moveBallsCount, _minimalLineLength;
	private ArrayList<Point> _lineDirections;
	private ArrayList<Point> _moveDirections;
	
	private Ball _state[][];
	
	private Ball _selectedBall = null;
	private int _selectedCellX;
	private int _selectedCellY;
	
	private Ball _queue[];
	private int _queueSize;
	
	public GameLogic (LinesGame linesGame) {
		init(linesGame);
	}
	
	public void init(LinesGame linesGame) {
		_linesGame = linesGame;
		_random = new Random();
		
		initSettings();
		
		_lineDirections = new ArrayList<Point>();
		_lineDirections.add(new Point(1, 0));
		_lineDirections.add(new Point(0, 1));
		_lineDirections.add(new Point(1, 1));
		_lineDirections.add(new Point(1, -1));
		_moveDirections = new ArrayList<Point>();
		_moveDirections.add(new Point(1, 0));
		_moveDirections.add(new Point(-1, 0));
		_moveDirections.add(new Point(0, 1));
		_moveDirections.add(new Point(0, -1));
		
		_state = new Ball[_nY][_nX];
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				_state[i][j] = null;
			}
		}
		
		_queue = new Ball[_queueSize];
		for (int i = 0; i < _queueSize; i++) {
			_queue[i] = new Ball();
			_queue[i].init(getRandomType());
		}
		
		_selectedCellX = _selectedCellY = -1;
		_selectedBall = null;
	}
	
	private void initSettings() {
		_settings = SettingsManager.INSTANCE;
		_nX = _settings.getInteger("FieldNX", 9);
		_nY = _settings.getInteger("FieldNY", 9);
		_moveBallsCount = _settings.getInteger("MoveBallsCount", 3);
		_minimalLineLength = _settings.getInteger("MinimalLineLength", 5);
		_queueSize = _settings.getInteger("BellQueueSize", 3);
	}
	
	private boolean canMove(int fromX, int fromY, int toX, int toY) {
		boolean used[][] = new boolean[_nY][_nX];
		for (int i = 0; i < _nY; i++) {
			for (int j = 0; j < _nX; j++) {
				used[i][j] = false;
			}
		}
		used[fromY][fromX] = true;
		LinkedList<Point> q = new LinkedList<Point>();
		q.offer(new Point(fromX, fromY));
		while (q.size() > 0) {
			Point point = q.poll();
			for (int dir = 0; dir < _moveDirections.size(); dir++) {
				int y = point.y + _moveDirections.get(dir).y;
				int x = point.x + _moveDirections.get(dir).x;
				if (y >= 0 && y < _nY && x >= 0 && x < _nX && !used[y][x] && _state[y][x] == null) {
					used[y][x] = true;
					q.offer(new Point(x, y));
				}
			}
		}
		return used[toY][toX];
	}
	
	private void checkMove(int cellY, int cellX, boolean addNew) {
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
		} else if (addNew){
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
					_state[rndY][rndX] = getNextBall();
					_state[rndY][rndX].setFieldX(rndX);
					_state[rndY][rndX].setFieldY(rndY);
					checkMove(rndY, rndX, false);
					break;
				}
			}
		}
	}
	
	public void addBall(int x, int y, int type) {
		_state[y][x] = new Ball();
		_state[y][x].init(type, x, y);
	}
	
	public int getRandomType() {
		return _random.nextInt(3);
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
	
	public Ball getBall(int x, int y) {
		return _state[y][x];
	}
	
	public int getSelectedCellX() {
		return _selectedCellX;
	}

	public int getSelectedCellY() {
		return _selectedCellY;
	}
	
	public Ball getQueueBall(int pos) {
		return _queue[pos];
	}

	
	public void clickOn(int cellX, int cellY) {
		Ball ball = _state[cellY][cellX];
		if (ball != null) {
			_selectedBall = ball;
			_selectedCellX = cellX;
			_selectedCellY = cellY;
			System.out.println("not null");
		} else if (ball == null && _selectedBall != null) {
			if (canMove(_selectedCellX, _selectedCellY, cellX, cellY)) {
				_state[cellY][cellX] = _selectedBall;
				_selectedBall.setFieldX(cellX);
				_selectedBall.setFieldY(cellY);
				_state[_selectedCellY][_selectedCellX] = null;
				_selectedBall = null;
				_selectedCellX = _selectedCellY = -1;
				checkMove(cellY, cellX, true);
			}
		}
	}
}
