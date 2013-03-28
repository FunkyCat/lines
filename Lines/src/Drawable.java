import java.awt.image.BufferedImage;

public interface Drawable {
	public int _drawX = 0;
	public int _drawY = 0;
	
	public void draw(BufferedImage canvas, int dX, int dY);
}
