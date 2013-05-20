import java.awt.Graphics;
import java.awt.Image;


public class GuiGameOver implements Drawable {
	
	private Image _image;
	
	public GuiGameOver() {
		_image = ImagesManager.INSTANCE.getImage("GuiGameOver");
	}

	@Override
	public void draw(Graphics graphics, int dX, int dY) {
		if (_image != null) {
			graphics.drawImage(_image, dX, dY, null);
		}
	}

}
