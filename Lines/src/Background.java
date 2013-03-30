import java.awt.Graphics;

public class Background implements Drawable {

	private int _width;
	private int _height;
	
	private LinesGame _linesGame;
	
	public Background(LinesGame linesGame) {
		init(linesGame);
	}
	
	public void init(LinesGame linesGame) {
		_linesGame = linesGame;
		_width = linesGame.getWidth();
		_height = linesGame.getHeight();
	}
	
	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		graphics.fillRect(0, 0, _width, _height);
	}

}
