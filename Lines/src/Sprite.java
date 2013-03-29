import java.awt.Graphics;
import java.awt.Image;


public class Sprite implements Drawable{
	private Image _image = null;
	
	public Sprite() {
	}
	
	public Sprite(String imageKey) {
		_image = ImagesManager.INSTANCE.getImage(imageKey);
	}
	
	public void draw(Graphics graphics, int dX, int dY) {
		if (_image != null) {
			graphics.drawImage(_image, dX, dY, null);
		}
	}
	
	public int getWidth() {
		return _image.getWidth(null);
	}
	
	public int getHeight() {
		return _image.getHeight(null);
	}
	
}
