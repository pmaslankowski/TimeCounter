package timecounter.presentation.counter;

import timecounter.presentation.AnimationWindow;

public class TimeTextBuilder {
	public TimeTextBuilder() {
		
	}
	
	public TimeText newTimeText(float startTime) {
		return new TimeText(window, x, y, fontName, fontSize, startTime);
	}
	
	public TimeTextBuilder setX(float x) {
		this.x = x;
		return this;
	}

	public TimeTextBuilder setY(float y) {
		this.y = y;
		return this;
	}

	public TimeTextBuilder setWindow(AnimationWindow window) {
		this.window = window;
		return this;
	}

	public TimeTextBuilder setFontName(String fontName) {
		this.fontName = fontName;
		return this;
	}

	public TimeTextBuilder setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}
	
	private float x, y;
	private AnimationWindow window;
	private String fontName;
	private float fontSize;
}
