import java.awt.Graphics;
import java.awt.Image;


public class GuiNewGame implements Drawable, Clickable {
	
	private Image _image;
	private LinesGame _linesGame;

	public GuiNewGame() {
		_image = ImagesManager.INSTANCE.getImage("GuiNewGame");
	}
	
	public void init(LinesGame linesGame) {
		_linesGame = linesGame;
	}
	
	@Override
	public void click(int mouseX, int mouseY, int dX, int dY) {
		if (mouseX >= dX && mouseX < dX + 150 &&
				mouseY >= dY && mouseY < dY + 150) {
			_linesGame.newGame();
		}
	}

	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		graphics.drawImage(_image, dX, dY, null);
	}

}
