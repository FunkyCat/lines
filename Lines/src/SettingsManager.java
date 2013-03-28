import java.util.HashMap;
import java.util.Map;

public class SettingsManager {
	public static final SettingsManager INSTANCE = new SettingsManager();
	
	private Map<String, Integer> _settingsInteger;
	private Map<String, Double> _settingsDouble;
	private Map<String, String> _settingsString;
	
	private SettingsManager() {
		_settingsInteger = new HashMap<String, Integer>();
		_settingsDouble = new HashMap<String, Double>();
		_settingsString = new HashMap<String, String>();
		setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		addString("WindowTitle", "Lines");
		addString("Author", "Konstantinova Antonina");
		addString("Version","0.1");
		addInteger("GameWidth", 600);
		addInteger("GameHeight", 432 + 8 * 2);
		addDouble("FPS", 120.0);
		addInteger("FieldX", 8);
		addInteger("FieldY", 8);
		addInteger("FieldNX", 9);
		addInteger("FieldNY", 9);
		addInteger("FieldCellWidth", 48);
		addInteger("FieldCellHeight", 48);
	}
	
	public void addInteger(String key, Integer value) {
		_settingsInteger.put(key, value);
	}
	
	public void addDouble(String key, Double value) {
		_settingsDouble.put(key, value);
	}
	
	public void addString(String key, String value) {
		_settingsString.put(key, value);
	}
	
	public Integer getInteger(String key, Integer defaultValue) {
		if (_settingsInteger.containsKey(key)) {
			return _settingsInteger.get(key);
		} else {
			return defaultValue;
		}
	}
	
	public Double getDouble(String key, Double defaultValue) {
		if (_settingsDouble.containsKey(key)) {
			return _settingsDouble.get(key);
		} else {
			return defaultValue;
		}
	}
	
	public String getString(String key, String defaultValue) {
		if (_settingsString.containsKey(key)) {
			return _settingsString.get(key);
		} else {
			return defaultValue;
		}
	}
	
}
