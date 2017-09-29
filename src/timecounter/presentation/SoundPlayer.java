package timecounter.presentation;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.JavaSoundAudioDeviceFactory;
import javazoom.jl.player.Player;
import timecounter.exceptions.SoundPlayerException;

public class SoundPlayer {
	private Player player;
	private Thread playerWorker;
	private JavaSoundAudioDevice device;
	private boolean isPlaying = false;
	private float duration;
	
	public SoundPlayer(String path) throws SoundPlayerException {
		try {
			JavaSoundAudioDeviceFactory factory = new JavaSoundAudioDeviceFactory();
			device = (JavaSoundAudioDevice)factory.createAudioDevice();
			player = new Player(Files.newInputStream(Paths.get(path)), device);
			duration = loadDurationFromFile(path);
		} catch (JavaLayerException | IOException e) {
			throw new SoundPlayerException("Error during loading sound file");
		}
	}
	
	public void play() {
		isPlaying = true;
		playerWorker = new Thread(() -> { 
			try {
				while(isPlaying)
					player.play(1);
			} catch (JavaLayerException e) {} 
		});
		playerWorker.start();
	}
	
	public void stop() {
		isPlaying = false;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public void setVolume(float volume) {
		device.setVolume(volume);
	}
	
	private float loadDurationFromFile(String path) {
		Header header;	
		try(InputStream stream = Files.newInputStream(Paths.get(path))) {
			Bitstream bitstream = new Bitstream(stream);
			header = bitstream.readFrame();
			int size = header.calculate_framesize();
			return header.total_ms(size) / 1000;
		} catch (IOException | BitstreamException e) {}
		return 0;
	}
}
