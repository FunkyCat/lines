import java.awt.Color;
import java.awt.image.BufferedImage;


public class Field implements Drawable {
	private int _nX;
	private int _nY;
	private int _cellWidth;
	private int _cellHeight;

	private int _state[][];
	
	private LinesGame _linesGame;
	private SettingsManager _settings;
	
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
	}
	
	@Override
	public void draw(BufferedImage image, int dX, int dY) {
		Color color = new Color(255, 0, 0);
		int rgb = color.getRGB();
		for (int i = 0; i <= _nX; i++) {
			for (int j = 0; j < _nY * _cellHeight; j++) {
				image.setRGB(dX + i * _cellWidth, dY + j, rgb);
			}
		}
		for (int i = 0; i <= _nY; i++) {
			for (int j = 0; j < _nX * _cellWidth; j++) {
				image.setRGB(dX + j, dY + i *_cellHeight , rgb);
			}
		}
	}

}
