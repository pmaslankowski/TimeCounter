package timecounter;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import timecounter.exceptions.InvalidPropertiesException;
import timecounter.exceptions.InvalidTimeException;
import timecounter.exceptions.SoundPlayerException;
import timecounter.presentation.AnimationWindow;
import timecounter.presentation.counter.Counter;

public class MainWindow {
	MainWindow that = this;

	final String PROPERTIES_PATH = "main.properties";
	private Properties properties;
	JFrame frmTimecounter;
	private JTextField timeTextField;
	private JTextField soundPathTextField;
	private JButton startButton;
	private JButton stopButton;
	
	private JFileChooser fileChooser;
	
	private List<AnimationWindow> animationWindows = new ArrayList<AnimationWindow>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainWindow window = new MainWindow();
					window.frmTimecounter.setLocationRelativeTo(null);
					window.frmTimecounter.setVisible(true);
				} catch (Exception e) {}
			}
		});
	}

	public MainWindow() {
		initializeUI();
		properties = new Properties();
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Plik z dŸwiêkiem (*.mp3)", "mp3"));
		loadProperties();
	}

	private void initializeUI() {
		frmTimecounter = new JFrame();
		frmTimecounter.setResizable(false);
		frmTimecounter.setTitle("TimeCounter");
		frmTimecounter.setBounds(100, 100, 517, 273);
		frmTimecounter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTimecounter.getContentPane().setLayout(null);
		
		JLabel timeLabel = new JLabel("Czas:");
		timeLabel.setBounds(15, 16, 69, 20);
		frmTimecounter.getContentPane().add(timeLabel);
		
		JLabel screenLabel = new JLabel("Ekran:");
		screenLabel.setBounds(15, 88, 69, 20);
		frmTimecounter.getContentPane().add(screenLabel);
		
		JLabel soundLabel = new JLabel("Plik z d\u017Awi\u0119kiem:");
		soundLabel.setBounds(15, 52, 129, 20);
		frmTimecounter.getContentPane().add(soundLabel);
		
		timeTextField = new JTextField();
		timeTextField.setHorizontalAlignment(SwingConstants.CENTER);
		timeTextField.setBounds(170, 13, 328, 26);
		frmTimecounter.getContentPane().add(timeTextField);
		timeTextField.setColumns(10);
		
		soundPathTextField = new JTextField();
		soundPathTextField.setBounds(170, 49, 207, 26);
		frmTimecounter.getContentPane().add(soundPathTextField);
		soundPathTextField.setColumns(10);
		
		JButton browseButton = new JButton("Przegl\u0105daj...");
		browseButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		browseButton.setBounds(388, 48, 110, 29);
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int result = fileChooser.showOpenDialog(frmTimecounter);
				if(result == JFileChooser.APPROVE_OPTION) {
					soundPathTextField.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});
		frmTimecounter.getContentPane().add(browseButton);
		
		JScrollPane screenListPane = new JScrollPane();
		screenListPane.setBounds(170, 88, 328, 100);
		frmTimecounter.getContentPane().add(screenListPane);
		
		DefaultListModel<Screen> screenListModel = new DefaultListModel<>();
		JList<Screen> screenList = new JList<>(screenListModel);
		screenList.setToolTipText("Aby zaznaczy\u0107 wi\u0119cej ni\u017C jeden ekran, przytrzymaj klawisz CTRL");
		screenListPane.setViewportView(screenList);
		loadScreenList(screenListModel);
		
		startButton = new JButton("Start");
		startButton.setToolTipText("Przycisk zostanie odblokowany, gdy ustawisz wszystkie niezb\u0119dne parametry odliczania");
		startButton.setBounds(383, 197, 115, 29);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int[] displayIds = screenList.getSelectedIndices();
				if(displayIds.length == 0) {
					JOptionPane.showMessageDialog(frmTimecounter, "Nie wybrano ¿adnego ekranu.", "B³¹d", JOptionPane.PLAIN_MESSAGE);
					return;
				}
				for(int displayId : displayIds) {
					try {
						AnimationWindow animationWindow = new AnimationWindow(displayId + 1, that); // display 0 = share across all screens, displays are indexed from 1
						Counter counter = new Counter(animationWindow, properties, timeTextField.getText(), soundPathTextField.getText());
						animationWindow.setCounter(counter);
						animationWindows.add(animationWindow);
						animationWindow.runAnimation();
						stopButton.setEnabled(true);
						startButton.setEnabled(false);
					} catch(InvalidTimeException e) {
						JOptionPane.showMessageDialog(frmTimecounter,  "Wprowadzono nieprawid³owy czas.", "B³¹d", JOptionPane.PLAIN_MESSAGE);
					} catch(InvalidPropertiesException e) {
						JOptionPane.showMessageDialog(frmTimecounter, "Ustawienia aplikacji s¹ nieprawid³owe.", "B³¹d", JOptionPane.PLAIN_MESSAGE);
					} catch(SoundPlayerException e) {
						JOptionPane.showMessageDialog(frmTimecounter, "B³¹d podczas ³adowania lub odtwarzania pliku z dŸwiêkiem.", "B³¹d", JOptionPane.PLAIN_MESSAGE);
					}
				}
			}
		});
		frmTimecounter.getContentPane().add(startButton);
		
		stopButton = new JButton("Stop");
		stopButton.setToolTipText("Przycisk zostanie odblokowany, gdy odliczanie b\u0119dzie aktywne");
		stopButton.setEnabled(false);
		stopButton.setBounds(261, 197, 115, 29);
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println(animationWindows.size());
				animationWindows.forEach(window -> window.stopAnimation());
				animationWindows.clear();
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
			}
		});
		frmTimecounter.getContentPane().add(stopButton);
		
		JButton settingsButton = new JButton("Ustawienia");
		settingsButton.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent event) {
				EventQueue.invokeLater(() -> { SettingsWindow settingsWindow = new SettingsWindow(that, PROPERTIES_PATH); });
			}
		});
		settingsButton.setBounds(15, 197, 115, 29);
		frmTimecounter.getContentPane().add(settingsButton);
	}
	
	private void loadScreenList(DefaultListModel<Screen> model) {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for(GraphicsDevice device : devices)
			model.addElement(new Screen(device));
	}
	
	public void loadProperties() {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(PROPERTIES_PATH))) {
			properties.load(reader);
		} catch (IOException e) { 
			JOptionPane.showMessageDialog(frmTimecounter, "Nie znaleziono pliku z ustawieniami: " + PROPERTIES_PATH, "B³¹d", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
	}
	
	public void resetButtons() {
		stopButton.setEnabled(false);
		startButton.setEnabled(true);
	}
}
