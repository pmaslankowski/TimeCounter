package timecounter.presentation;
import java.awt.Frame;

import processing.awt.PSurfaceAWT.SmoothCanvas;
import processing.core.PApplet;
import timecounter.MainWindow;
import timecounter.presentation.counter.Counter;

public class AnimationWindow extends PApplet {
	private final int displayId;
	private MainWindow parent;
	private Counter counter;
	private int timeOfLastFrame;
	
	public AnimationWindow(int displayId, MainWindow parent) {
		this.displayId = displayId;
		this.parent = parent;
	}
	
	public void runAnimation() {
		PApplet.runSketch(new String[] {"--display=" + displayId, "AnimationWindow" + displayId}, this);	
	}
	
	public void stopAnimation() {
		counter.stop();
		Frame frame = ((SmoothCanvas)(surface.getNative())).getFrame(); // workaround, because PApplet dispose method simply doesn't work. 
		frame.dispose();
		dispose();
	}
	
	public void setup() {
		frameRate(counter.getFrameRate());
		counter.setFont();
	}
	
	public void settings() {
		fullScreen();
	}
	
	public void draw() {
		background(0, 0, 0);
		counter.draw();
		if(counter.isFinished()) {
			stopAnimation();
			parent.resetButtons();
		}
		counter.update((millis() - timeOfLastFrame) / 1000.f);
		timeOfLastFrame = millis();
	}

	public Counter getCounter() {
		return counter;
	}

	public void setCounter(Counter counter) {
		this.counter = counter;
	}
}
