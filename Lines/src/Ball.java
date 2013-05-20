import java.awt.Graphics;
import java.awt.Image;


public class Ball implements Drawable{
	
	public static final int TYPE_RED = 0;
	public static final int TYPE_GREEN = 1;
	public static final int TYPE_BLUE = 2;
	public static final int TYPE_ANY = 3;
	
	private static String KEY_RED = "BallRed";
	private static String KEY_GREEN = "BallGreen";
	private static String KEY_BLUE = "BallBlue";
	private static String KEY_ANY = "BallAny";
		
	private int _fieldX;
	private int _fieldY;
	
	private int _type;
	private Image _image;
	
	public void init(int type) {
		_fieldX = _fieldY = -1;
		setType(type);
	}
	
	public void init(int type, int fieldX, int fieldY) {
		_fieldX = fieldX;
		_fieldY = fieldY;
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
		case TYPE_ANY:
			_image = imgMng.getImage(KEY_ANY);
			break;
		default:
			_type = TYPE_RED;
			_image = imgMng.getImage(KEY_RED);
		}
	}
	
	public int getType() {
		return _type;
	}
	
	public void setFieldX(int fieldX) {
		_fieldX = fieldX;
	}
	
	public int getFieldX() {
		return _fieldX;
	}
	
	public void setFieldY(int fieldY) {
		_fieldY = fieldY;
	}
	
	public int getFieldY() {
		return _fieldY;
	}

}
