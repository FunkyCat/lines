import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Background implements Drawable {

	private int _width;
	private int _height;
	
	private LinesGame _linesGame;
	private SettingsManager _settings;
	
	public Background(LinesGame linesGame) {
		init(linesGame);
	}
	
	public void init(LinesGame linesGame) {
		_linesGame = linesGame;
		_settings = SettingsManager.INSTANCE;
		_width = linesGame.getWidth();
		_height = linesGame.getHeight();
	}
	
	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		graphics.fillRect(0, 0, _width, _height);
	}

}
