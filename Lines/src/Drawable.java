import java.awt.Graphics;

public interface Drawable {
	public int _drawX = 0;
	public int _drawY = 0;
	
	public void draw(Graphics graphics, int dX, int dY);
}
