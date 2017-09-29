package timecounter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SettingsWindow {
	private JDialog frmSettings;
	private JTextField fpsText;
	private JTextField rText;
	private JTextField backgroundColorText;
	private JTextField clockTextPositionText;
	private JTextField clockTextFontText;
	private JTextField decorativeRingRMinText;
	private JTextField decorativeRingRMaxText;
	private JTextField decorativeRingStrokeWeightText;
	private JTextField decorativeRingPeriodText;
	private JTextField decorativeRingPosText;
	private JTextField soundFadingTimeText;
	private JTextField counterFadingTimeText;

	private Properties properties = new Properties();
	private final String propertiesPath;
	private MainWindow parent;
	
	public SettingsWindow(MainWindow parent, String propertiesPath) {
		this.propertiesPath = propertiesPath;
		this.parent = parent;
		initializeUI();
		try (InputStream stream = Files.newInputStream(Paths.get(propertiesPath))) {
			properties.load(stream);
			loadFromProperties();
			frmSettings.setVisible(true);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frmSettings, "Nie znaleziono pliku z ustawieniami: " + propertiesPath, "B³¹d", JOptionPane.PLAIN_MESSAGE);
			frmSettings.dispose();
		} 
	}
	
	private void initializeUI() {
		frmSettings = new JDialog(parent.frmTimecounter, true);
		frmSettings.setResizable(false);
		frmSettings.setTitle("Ustawienia");
		frmSettings.setBounds(100, 100, 499, 532);
		frmSettings.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frmSettings.getContentPane().setLayout(null);
		
		JLabel fpsLabel = new JLabel("FPS:");
		fpsLabel.setBounds(15, 16, 69, 20);
		frmSettings.getContentPane().add(fpsLabel);
		
		JLabel rLabel = new JLabel("Promie\u0144 zegara:");
		rLabel.setBounds(15, 52, 122, 20);
		frmSettings.getContentPane().add(rLabel);
		
		JLabel clockBackgroundLabel = new JLabel("Kolor t\u0142a zegara:");
		clockBackgroundLabel.setBounds(15, 88, 122, 20);
		frmSettings.getContentPane().add(clockBackgroundLabel);
		
		JLabel clockTextPositionLabel = new JLabel("Pozycja etykiety z czasem:");
		clockTextPositionLabel.setBounds(15, 124, 192, 20);
		frmSettings.getContentPane().add(clockTextPositionLabel);
		
		JLabel clockTextFontLabel = new JLabel("Czcionka etykiety z czasem:");
		clockTextFontLabel.setBounds(15, 160, 200, 20);
		frmSettings.getContentPane().add(clockTextFontLabel);
		
		JLabel decorativeRingRMinLabel = new JLabel("Promie\u0144 sekundnika:");
		decorativeRingRMinLabel.setBounds(15, 196, 192, 20);
		frmSettings.getContentPane().add(decorativeRingRMinLabel);
		
		JLabel decorativeRingRMaxLabel = new JLabel("Maksymalny promie\u0144 otoczki sekundnika:");
		decorativeRingRMaxLabel.setBounds(15, 232, 299, 20);
		frmSettings.getContentPane().add(decorativeRingRMaxLabel);
		
		JLabel decorativeRingStrokeWeightLabel = new JLabel("Szeroko\u015B\u0107 otoczki sekundnika:");
		decorativeRingStrokeWeightLabel.setBounds(15, 268, 244, 20);
		frmSettings.getContentPane().add(decorativeRingStrokeWeightLabel);
		
		JLabel decorativeRingPeriodLabel = new JLabel("Okres sekundnika:");
		decorativeRingPeriodLabel.setBounds(15, 304, 131, 20);
		frmSettings.getContentPane().add(decorativeRingPeriodLabel);
		
		JLabel decorativeRingPosLabel = new JLabel("Pozycja sekundnika:");
		decorativeRingPosLabel.setBounds(15, 340, 143, 20);
		frmSettings.getContentPane().add(decorativeRingPosLabel);
		
		JLabel soundFadingTimeLabel = new JLabel("Czas wyciszania d\u017Awi\u0119ku:");
		soundFadingTimeLabel.setBounds(15, 376, 192, 20);
		frmSettings.getContentPane().add(soundFadingTimeLabel);
		
		JLabel counterFadingTimeLabel = new JLabel("Czas wygaszania odliczania:");
		counterFadingTimeLabel.setBounds(15, 412, 244, 20);
		frmSettings.getContentPane().add(counterFadingTimeLabel);
		
		fpsText = new JTextField();
		fpsText.setToolTipText("Liczba klatek na sekund\u0119");
		fpsText.setBounds(333, 13, 143, 26);
		frmSettings.getContentPane().add(fpsText);
		fpsText.setColumns(10);
		
		rText = new JTextField();
		rText.setToolTipText("Promie\u0144 zegara (w pikselach)");
		rText.setBounds(333, 49, 143, 26);
		frmSettings.getContentPane().add(rText);
		rText.setColumns(10);
		
		backgroundColorText = new JTextField();
		backgroundColorText.setToolTipText("Format: r, g, b, liczby z zakresu 0, ..., 255");
		backgroundColorText.setBounds(333, 85, 143, 26);
		frmSettings.getContentPane().add(backgroundColorText);
		backgroundColorText.setColumns(10);
		
		clockTextPositionText = new JTextField();
		clockTextPositionText.setToolTipText("Pozycja tekstu z pozosta\u0142ym czasem. Format: x, y");
		clockTextPositionText.setBounds(333, 121, 143, 26);
		frmSettings.getContentPane().add(clockTextPositionText);
		clockTextPositionText.setColumns(10);
		
		clockTextFontText = new JTextField();
		clockTextFontText.setToolTipText("Format: nazwa czcionki, rozmiar");
		clockTextFontText.setBounds(333, 157, 143, 26);
		frmSettings.getContentPane().add(clockTextFontText);
		clockTextFontText.setColumns(10);
		
		decorativeRingRMinText = new JTextField();
		decorativeRingRMinText.setToolTipText("Promie\u0144 bia\u0142ego ko\u0142a na tarczy zegara (w pikselach)");
		decorativeRingRMinText.setBounds(333, 193, 143, 26);
		frmSettings.getContentPane().add(decorativeRingRMinText);
		decorativeRingRMinText.setColumns(10);
		
		decorativeRingRMaxText = new JTextField();
		decorativeRingRMaxText.setToolTipText("Maksymalny promie\u0144 przezroczystej otoczki sekundnika (w pikselach)");
		decorativeRingRMaxText.setBounds(333, 229, 143, 26);
		frmSettings.getContentPane().add(decorativeRingRMaxText);
		decorativeRingRMaxText.setColumns(10);
		
		decorativeRingStrokeWeightText = new JTextField();
		decorativeRingStrokeWeightText.setToolTipText("Szeroko\u015B\u0107 otoczki sekundnika");
		decorativeRingStrokeWeightText.setBounds(333, 265, 143, 26);
		frmSettings.getContentPane().add(decorativeRingStrokeWeightText);
		decorativeRingStrokeWeightText.setColumns(10);
		
		decorativeRingPeriodText = new JTextField();
		decorativeRingPeriodText.setToolTipText("Okres pe\u0142nego odliczenia sekundnika (w sekundach)");
		decorativeRingPeriodText.setBounds(333, 301, 143, 26);
		frmSettings.getContentPane().add(decorativeRingPeriodText);
		decorativeRingPeriodText.setColumns(10);
		
		decorativeRingPosText = new JTextField();
		decorativeRingPosText.setToolTipText("Format: x, y");
		decorativeRingPosText.setBounds(333, 337, 143, 26);
		frmSettings.getContentPane().add(decorativeRingPosText);
		decorativeRingPosText.setColumns(10);
		
		soundFadingTimeText = new JTextField();
		soundFadingTimeText.setToolTipText("Czas przy kt\u00F3rym d\u017Awi\u0119k zacznie by\u0107 stopniowo wyciszany");
		soundFadingTimeText.setBounds(333, 373, 143, 26);
		frmSettings.getContentPane().add(soundFadingTimeText);
		soundFadingTimeText.setColumns(10);
		
		counterFadingTimeText = new JTextField();
		counterFadingTimeText.setToolTipText("Czas przez jaki okno b\u0119dzie widoczne po zako\u0144czeniu odliczania");
		counterFadingTimeText.setBounds(333, 409, 143, 26);
		frmSettings.getContentPane().add(counterFadingTimeText);
		counterFadingTimeText.setColumns(10);
		
		JButton applyButton = new JButton("Zastosuj");
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int res = JOptionPane.showOptionDialog(frmSettings, 
						"Czy na pewno chcesz zapisaæ ustawienia? B³êdne wartoœci mog¹ zdestabilizowaæ aplikacjê.", 
						"Proœba o potwierdzenie",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE,
						null, 
						null,
						"default");
				if(res == JOptionPane.YES_OPTION) {
					saveToProperties();
					parent.loadProperties();
					frmSettings.dispose();
				}
			}
		});
		applyButton.setBounds(367, 451, 115, 29);
		frmSettings.getContentPane().add(applyButton);
		
		JButton cancelButton = new JButton("Anuluj");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				frmSettings.dispose();
			}
		});
		cancelButton.setBounds(243, 451, 115, 29);
		frmSettings.getContentPane().add(cancelButton);
		
		frmSettings.setLocationRelativeTo(parent.frmTimecounter);
	}
	
	private void loadFromProperties() {
		fpsText.setText(properties.getProperty("frameRate"));
		rText.setText(properties.getProperty("clockRadius"));
		backgroundColorText.setText(properties.getProperty("clockBackground"));
		clockTextPositionText.setText(properties.getProperty("clockTextPosition"));
		clockTextFontText.setText(properties.getProperty("clockTextFont"));
		decorativeRingRMinText.setText(properties.getProperty("decorativeRingRMin"));
		decorativeRingRMaxText.setText(properties.getProperty("decorativeRingRMax"));
		decorativeRingStrokeWeightText.setText(properties.getProperty("decorativeRingStrokeWeight"));
		decorativeRingPeriodText.setText(properties.getProperty("decorativeRingPeriod"));
		decorativeRingPosText.setText(properties.getProperty("decorativeRingPos"));
		soundFadingTimeText.setText(properties.getProperty("soundFadingTime"));
		counterFadingTimeText.setText(properties.getProperty("counterFadingTime"));
	}
	
	private void saveToProperties() {
		properties.setProperty("frameRate", fpsText.getText());
		properties.setProperty("clockRadius", rText.getText());
		properties.setProperty("clockBackground", backgroundColorText.getText());
		properties.setProperty("clockTextPosition", clockTextPositionText.getText());
		properties.setProperty("clockTextFont", clockTextFontText.getText());
		properties.setProperty("decorativeRingRMin", decorativeRingRMinText.getText());
		properties.setProperty("decorativeRingRMax", decorativeRingRMaxText.getText());
		properties.setProperty("decorativeRingStrokeWeight", decorativeRingStrokeWeightText.getText());
		properties.setProperty("decorativeRingPeriod", decorativeRingPeriodText.getText());
		properties.setProperty("decorativeRingPos", decorativeRingPosText.getText());
		properties.setProperty("soundFadingTime", soundFadingTimeText.getText());
		properties.setProperty("counterFadingTime", counterFadingTimeText.getText());
		try (OutputStream stream = Files.newOutputStream(Paths.get(propertiesPath))) {
			properties.store(stream, null);
		} catch (IOException e) {} //TODO!
	}
	
}
