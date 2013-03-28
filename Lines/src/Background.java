import java.awt.Canvas;
import java.awt.Color;
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
		_width = _settings.getInteger("GameWidth", 600);
		_height = _settings.getInteger("GameHeight", 400);
	}
	
	@Override
	public void draw(BufferedImage image, int dX, int dY) {
		Color color = new Color(0, 0, 0);
		int rgb = color.getRGB();
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
				.getData();
		for (int i = 0; i < _height; i++) {
			for (int j = 0; j < _width; j++) {
				pixels[i * _width + j] = rgb;
			}
		}
	}

}
