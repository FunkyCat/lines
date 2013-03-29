import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


public class ImagesManager {
	public static final ImagesManager INSTANCE = new ImagesManager(); 
	
	private Map<String, Image> _images;
	
	private ImagesManager() {
		_images = new HashMap<String, Image>();
		
		loadAllImages();
	}
	
	public void loadImage(String path, String key) {
		BufferedImage bufImage;
		try {
			URL url = this.getClass().getClassLoader().getResource(path);
			bufImage = ImageIO.read(url);
			Image image = Toolkit.getDefaultToolkit().createImage(bufImage.getSource());
			_images.put(key, image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Image getImage(String key) {
		if (_images.containsKey(key)) {
			return _images.get(key);
		} else {
			return null;
		}
	}

	public void loadAllImages() {
		loadImage("cellBg.png", "CellBackground");
		
		loadImage("ballRed.png", "BallRed");
		loadImage("ballGreen.png", "BallGreen");
		loadImage("ballBlue.png", "BallBlue");
	}
	
}
