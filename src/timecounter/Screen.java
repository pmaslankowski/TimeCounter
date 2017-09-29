package timecounter;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;

public class Screen {
	private GraphicsDevice device;
	
	public Screen(GraphicsDevice device) {
		this.device = device;
	}
	
	@Override
	public String toString() {
		DisplayMode mode = device.getDisplayMode();
		return String.format("Ekran %d x %d, %dhz", mode.getWidth(), mode.getHeight(), mode.getRefreshRate());
	}
}
