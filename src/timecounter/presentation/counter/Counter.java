package timecounter.presentation.counter;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timecounter.exceptions.InvalidPropertiesException;
import timecounter.exceptions.InvalidTimeException;
import timecounter.exceptions.SoundPlayerException;
import timecounter.presentation.AnimationWindow;
import timecounter.presentation.Color;
import timecounter.presentation.SoundPlayer;

public class Counter {	
	private float currentAngle = 0;
	private float r;
	private float frameRate;
	private float startTime;
	private float timeElapsed = 0;
	
	private AnimationWindow window;
	private TimeText timeText;
	private DecorativeRing decorativeRing;
	private CounterColor currentColor;
	private Color backgroundColor;
	private float counterFadingTime;
	
	private SoundPlayer player;
	private float soundFadingTime;
	
	private final float PI = (float) Math.PI;
	private final float START_ANGLE = 3 * PI / 2;
	
	public Counter(AnimationWindow window, Properties props, String time, String soundPath) 
			throws InvalidTimeException, InvalidPropertiesException, SoundPlayerException {
		this.window = window;
		this.startTime = parseTime(time);
		this.currentColor = new CounterColor(startTime);
		if(!soundPath.isEmpty()) 
			player = new SoundPlayer(soundPath);
		loadFromProperties(props);
	}
	
	public double getTime() {
		return 0;
	}
	
	public float getFrameRate() {
		return frameRate;
	}
	
	private float parseTime(String time) throws InvalidTimeException {
		Pattern pattern = Pattern.compile("^((?<minutes>\\d+):)?(?<seconds>\\d+)$");
		Matcher matcher = pattern.matcher(time);
		if(!matcher.matches())
			throw new InvalidTimeException("Invalid format of time");
		String minutesAsStr = matcher.group("minutes");
		float result = Float.parseFloat(matcher.group("seconds"));
		if(minutesAsStr != null)
			result += 60 * Float.parseFloat(minutesAsStr);
		return result;
	}
	
	public void draw() {
		float x = window.width / 2;
		float y = window.height / 2;
		float alpha = 255.f;
		if(isFinishing())
			alpha -= (timeElapsed - startTime) / counterFadingTime * 255.f;
		window.fill(backgroundColor.r, backgroundColor.g, backgroundColor.b, alpha);
		window.arc(x, y, r, r, 0, 2 * PI);
		window.fill(currentColor.r, currentColor.g, currentColor.b, alpha);
		window.arc(x, y, r, r, START_ANGLE, START_ANGLE + currentAngle);
		timeText.draw(alpha);
		decorativeRing.draw(x, y, alpha);
	}
	
	public void update(float dt) {
		timeElapsed += dt;
		currentAngle += (dt / startTime) * 2 * PI;
		if(!isFinishing()) {
			timeText.update(dt);
			currentColor.update(dt);
		}
		decorativeRing.update(dt);
		if(player != null) {
			if(!player.isPlaying() && timeElapsed + player.getDuration() >= startTime + counterFadingTime) 
				player.play();
			if(player.isPlaying() && timeElapsed + soundFadingTime >= startTime + counterFadingTime) 
				player.setVolume(5 * (startTime + counterFadingTime - timeElapsed) / soundFadingTime);
		}
	}
	
	public boolean isFinished() {
		return timeElapsed >= startTime + counterFadingTime;
	}
	
	private boolean isFinishing() {
		return timeElapsed >= startTime;
	}
	
	public void stop() {
		if(player != null)
			player.stop();
	}
	
	public void setFont() {
		timeText.setFont();
	}
	
	private void loadFromProperties(Properties props) throws InvalidPropertiesException {
		try {
			String[] textPos = props.getProperty("clockTextPosition").split(",");
			String[] textFont = props.getProperty("clockTextFont").split(",");
			TimeTextBuilder builder = new TimeTextBuilder();
			builder.setX(Integer.parseInt(textPos[0].trim()))
				.setY(Integer.parseInt(textPos[1].trim()))
				.setFontName(textFont[0].trim())
				.setFontSize(Integer.parseInt(textFont[1].trim()))
				.setWindow(window);
			timeText = builder.newTimeText(startTime);
			
			r = Integer.parseInt(props.getProperty("clockRadius").trim());
			frameRate = Integer.parseInt(props.getProperty("frameRate").trim());
			String[] bgColorStrs = props.getProperty("clockBackground").split(",");
			backgroundColor = new Color(Float.parseFloat(bgColorStrs[0].trim()), 
					Float.parseFloat(bgColorStrs[1].trim()), 
					Float.parseFloat(bgColorStrs[2].trim()));
			float decorativeRingRMin = Float.parseFloat(props.getProperty("decorativeRingRMin").trim());
			float decorativeRingRMax = Float.parseFloat(props.getProperty("decorativeRingRMax").trim());
			float decorativeRingPeriod = Float.parseFloat(props.getProperty("decorativeRingPeriod").trim());
			float decorativeRingStrokeWeight = Float.parseFloat(props.getProperty("decorativeRingStrokeWeight").trim());
			String[] decorativeRingPosStrs = props.getProperty("decorativeRingPos").split(",");
			decorativeRing = new DecorativeRing(Float.parseFloat(decorativeRingPosStrs[0].trim()), 
					Float.parseFloat(decorativeRingPosStrs[1].trim()), 
					decorativeRingRMin, 
					decorativeRingRMax, 
					decorativeRingPeriod, 
					decorativeRingStrokeWeight,
					window);
			soundFadingTime = Float.parseFloat(props.getProperty("soundFadingTime").trim());
			counterFadingTime = Float.parseFloat(props.getProperty("counterFadingTime").trim());
		} catch (Exception e) {
			throw new InvalidPropertiesException("Error during loading counter configuration from properties file.");
		}
	}
	
}
