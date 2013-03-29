import java.awt.Graphics;
import java.awt.Image;


public class Ball implements Drawable{
	
	public static final int TYPE_RED = 0;
	public static final int TYPE_GREEN = 1;
	public static final int TYPE_BLUE = 2;
	
	private static String KEY_RED = "BallRed";
	private static String KEY_GREEN = "BallGreen";
	private static String KEY_BLUE = "BallBlue";
	
	private int _type;
	private Image _image;
	
	public void init(int type) {
		setType(type);
	}
	
	public void draw(Graphics graphics, int dX, int dY) {
		if (_image != null) {
			graphics.drawImage(_image, dX, dY, null);
		}
	}
	
	public void setType (int type) {
		_type = type;
		ImagesManager imgMng = ImagesManager.INSTANCE;
		switch (type) {
		case TYPE_RED:
			_image = imgMng.getImage(KEY_RED);
			break;
		case TYPE_GREEN:
			_image = imgMng.getImage(KEY_GREEN);
			break;
		case TYPE_BLUE:
			_image = imgMng.getImage(KEY_BLUE);
			break;
		default:
			_type = TYPE_RED;
			_image = imgMng.getImage(KEY_RED);
		}
	}

}
