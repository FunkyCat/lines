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
	
	private int _freeCells;
	
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
		_freeCells = _nY * _nX;
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
	
	private Point getFirstWithOtherType(int sX, int sY, int dX, int dY, int type) {
		int cX = sX + dX;
		int cY = sY + dY;
		while (cX >= 0 && cX < _nX && cY >= 0 && cY < _nY && _state[cY][cX] != null && _state[cY][cX].getType() == type) {
			cX += dX;
			cY += dY;
		}
		return new Point(cX, cY);
	}
	
	private int maxDist(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
	}
	
	private boolean compTypes(int type1, int type2) {
		if (type1 == Ball.TYPE_ANY || type2 == Ball.TYPE_ANY) {
			return true;
		}
		return type1 == type2;
	}
	
	private ArrayList<Point> checkLine(int sX, int sY, int dX, int dY, int type) {
		ArrayList<Point> ret = new ArrayList<Point>();
		
		for (int i = 0; i < 2; i++) {
			int cX = sX + dX;
			int cY = sY + dY;
			while (cX >= 0 && cX < _nX && cY >= 0 && cY < _nY &&
					_state[cY][cX] != null && compTypes(type, _state[cY][cX].getType())) {
				ret.add(new Point(cX, cY));
				cX += dX;
				cY += dY;
			}
			dX *= -1;
			dY *= -1;
		}
		
		return ret;
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
			
			if (type == Ball.TYPE_ANY) {
				int newTypeStraight = -1;
				Point straightPoint = getFirstWithOtherType(cellX, cellY, dX, dY, type);
				if (straightPoint.x >= 0 && straightPoint.x < _nX &&
						straightPoint.y >= 0 && straightPoint.y < _nY &&
						_state[straightPoint.y][straightPoint.x] != null) {
					newTypeStraight = _state[straightPoint.y][straightPoint.x].getType();
				}
				int newTypeForward = -1;
				Point forwardPoint = getFirstWithOtherType(cellX, cellY, -dX, -dY, type);
				if (forwardPoint.x >= 0 && forwardPoint.x < _nX &&
						forwardPoint.y >= 0 && forwardPoint.y < _nY &&
						_state[forwardPoint.y][forwardPoint.x] != null) {
					newTypeForward = _state[forwardPoint.y][forwardPoint.x].getType();
				}
				if (newTypeStraight == -1 && newTypeForward == -1) {
					if (maxDist(straightPoint.x, straightPoint.y, forwardPoint.x, forwardPoint.y) - 1 >= _minimalLineLength) {
						int cX = forwardPoint.x + dX;
						int cY = forwardPoint.y + dY;
						while (cX != straightPoint.x || cY != straightPoint.y) {
							if (cX != cellX || cY != cellY) {
								cells.add(new Point(cX, cY));
							}
							cX += dX;
							cY += dY;
						}
					}
				} else {
					ArrayList<Point> res = null;
					if (newTypeStraight == -1) {
						res = checkLine(cellX, cellY, dX, dY, newTypeForward);
					} else if (newTypeForward == -1) {
						res = checkLine(cellX, cellY, dX, dY, newTypeStraight);
					} else {
						ArrayList<Point> resStraight = checkLine(cellX, cellY, dX, dY, newTypeStraight);
						ArrayList<Point> resForward = checkLine(cellX, cellY, dX, dY, newTypeForward);
						if (resStraight.size() > resForward.size()) {
							res = resStraight;
						} else {
							res = resForward;
						}
					}
					if (res.size() + 1 >= _minimalLineLength) {
						for (int j = 0; j < res.size(); j++) {
							cells.add(res.get(j));
						}
					}
				}
			} else {
				ArrayList<Point> res = checkLine(cellX, cellY, dX, dY, type);
				if (res.size() + 1 >= _minimalLineLength) {
					for (int j = 0; j < res.size(); j++) {
						cells.add(res.get(j));
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
				_freeCells++;
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
		_freeCells = _nY * _nX;
	}
	
	public void addRandomBalls(int count) {
		int rndY, rndX;
		for (int i = 0; i < count; i++) {
			if (_freeCells == 0) {
				_linesGame.gameOver();
				break;
			}
			for(;;) {
				rndX = _random.nextInt(_nX);
				rndY = _random.nextInt(_nY);
				if (_state[rndY][rndX] == null) {
					_state[rndY][rndX] = getNextBall();
					_state[rndY][rndX].setFieldX(rndX);
					_state[rndY][rndX].setFieldY(rndY);
					_freeCells--;
					checkMove(rndY, rndX, false);
					break;
				}
			}
		}
		if (_freeCells == 0) {
			_linesGame.gameOver();
		}
	}
	
	public void addBall(int x, int y, int type) {
		_state[y][x] = new Ball();
		_state[y][x].init(type, x, y);
	}
	
	public int getRandomType() {
		return _random.nextInt(4);
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
