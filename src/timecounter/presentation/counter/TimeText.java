package timecounter.presentation.counter;


import processing.core.PFont;
import timecounter.presentation.AnimationWindow;

public class TimeText {
	
	public TimeText(AnimationWindow window, float x, float y, String fontName, float fontSize, float startTime) {
		this.window = window;
		this.x = x;
		this.y = y;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.time = startTime;
	}
	
	public void update(float dt) {
		time -= dt;
	}
	
	public void draw(float alpha) {
		window.textFont(font);
		window.fill(255, 255, 255, alpha);
		window.textAlign(AnimationWindow.CENTER);
		window.text(getTimeAsString(time), window.width / 2 + x, window.height / 2 + y);
	}
	
	public void setFont() {
		font = window.createFont(fontName, fontSize);
	}
	
	private String getTimeAsString(float time) {
		int t = (int) time;
		return String.format("%02d:%02d", t / 60, t % 60);
	}
	
	private AnimationWindow window;
	private PFont font;
	private String fontName;
	private float fontSize;
	private float time;
	private float x, y;
}
