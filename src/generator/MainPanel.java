package generator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class MainPanel extends JPanel implements ActionListener {
	public static final IvParameterSpec IV = new IvParameterSpec("akJzSjAxXv38msfg".getBytes());
	private String outputPass;
	private Font font1 = new Font(Font.DIALOG, Font.PLAIN, 15);
	private Font font2 = new Font(Font.DIALOG, Font.PLAIN, 20);
	private JPanel panel1, panel2, panel3, panel4;
	private JLabel label1, label2;
	private JButton button1, button2, button3;
	private JCheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
	private JSpinner spinner;
	private Component strut1, strut2, strut3, verticalStrut1, verticalStrut2;
	private int digitNum = 1;
	private boolean useNumber = false;
	private boolean useLower = false;
	private boolean useUpper = false;
	private boolean useAllCharType = false;
	private boolean useCharOnlyOnce = false;
	public MainPanel() {
		readConfig();
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		label1 = new JLabel(Messages.getString("Label1"));
		label2 = new JLabel(Messages.getString("Label2"));
		button1 = new JButton(Messages.getString("Button1"));
		button2 = new JButton(Messages.getString("Button2"));
		button3 = new JButton(Messages.getString("Button3"));
		checkBox1 = new JCheckBox(Messages.getString("CheckBox1"), useNumber);
		checkBox2 = new JCheckBox(Messages.getString("CheckBox2"), useLower);
		checkBox3 = new JCheckBox(Messages.getString("CheckBox3"), useUpper);
		checkBox4 = new JCheckBox(Messages.getString("CheckBox4"), useAllCharType);
		checkBox5 = new JCheckBox(Messages.getString("CheckBox5"), useCharOnlyOnce);
		spinner = new JSpinner();
		strut1 = Box.createHorizontalStrut(10);
		strut2 = Box.createHorizontalStrut(10);
		strut3 = Box.createHorizontalStrut(20);
		verticalStrut1 = Box.createVerticalStrut(10);
		verticalStrut2 = Box.createVerticalStrut(5);
		setComponent();
	}
	private void setComponent() {
		button1.setPreferredSize(new Dimension(80, 30));
		button2.setPreferredSize(new Dimension(80, 30));
		button3.setPreferredSize(new Dimension(200, 30));
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		label2.setHorizontalAlignment(JTextField.CENTER);
		spinner.setModel(new SpinnerNumberModel(digitNum, 1, 30, 1));
		label1.setFont(font1);
		label2.setFont(font2);
		button1.setFont(font1);
		button2.setFont(font1);
		button3.setFont(font1);
		checkBox1.setFont(font1);
		checkBox2.setFont(font1);
		checkBox3.setFont(font1);
		checkBox4.setFont(font1);
		checkBox5.setFont(font1);
		add(panel1);
		add(panel2);
		add(verticalStrut1);
		add(panel3);
		add(verticalStrut2);
		add(panel4);
		panel1.add(checkBox1);
		panel1.add(strut1);
		panel1.add(checkBox2);
		panel1.add(strut2);
		panel1.add(checkBox3);
		panel1.add(strut3);
		panel1.add(spinner);
		panel2.add(checkBox4);
		panel2.add(checkBox5);
		panel1.add(label1);
		panel3.add(button1);
		panel3.add(button2);
		panel3.add(button3);
		panel4.add(label2);
	}
	private void save(String name) {
		File dir = new File(System.getProperty("user.dir") + "\\etc\\pwd");
		File file = new File(dir + "\\" + name + ".dat");
		SecretKey key = getKey();
		Cipher cipher = null;
		OutputStreamWriter wos = null;
		if (!dir.exists())
			dir.mkdir();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, IV);
			FileOutputStream fos = new FileOutputStream(file);
			CipherOutputStream cos = new CipherOutputStream(fos,cipher);
			wos = new OutputStreamWriter(cos);
			wos.write(outputPass);
			wos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				wos.close();
			} catch (Exception e) {}
		}
	}
	private SecretKey getKey(){
		File dir = new File(System.getProperty("user.dir") + "\\etc");
		File file = new File(dir + "\\key.bin");
		if (!dir.exists())
			dir.mkdir();
		if (!file.exists()) {
			try(FileOutputStream fos = new FileOutputStream(file)){
				file.createNewFile();
				KeyGenerator kg = KeyGenerator.getInstance("AES");
				SecretKey key = kg.generateKey();
				fos.write(key.getEncoded());
				fos.flush();
				return key;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try(FileInputStream fis = new FileInputStream(file)) {
				byte[] key = new byte[16];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while (fis.read(key) > 0) {
					baos.write(key);
				}
				baos.close();
				return new SecretKeySpec(key, "AES");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	private boolean existPasswordFile(String name){
		File file = new File(System.getProperty("user.dir") + "\\etc\\pwd\\" + name + ".dat");
		if (file.exists())
			return true;
		else 
			return false;
	}
	private void saveConfig() {
		File dir = new File(System.getProperty("user.dir")+ "\\etc\\");
		File file = new File(dir + "\\generator.cfg");
		Properties p = new Properties();
		p.setProperty(Messages.getString("Property1"), String.valueOf(checkBox1.isSelected()));
		p.setProperty(Messages.getString("Property2"), String.valueOf(checkBox2.isSelected()));
		p.setProperty(Messages.getString("Property3"), String.valueOf(checkBox3.isSelected()));
		p.setProperty(Messages.getString("Property4"), String.valueOf(spinner.getValue()));
		p.setProperty(Messages.getString("Property5"), String.valueOf(checkBox4.isSelected()));
		p.setProperty(Messages.getString("Property6"), String.valueOf(checkBox5.isSelected()));
		if (!dir.exists())
			dir.mkdir();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try(Writer w = new FileWriter(file)) {
			p.store(w, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readConfig() {
		File dir = new File(System.getProperty("user.dir")+ "\\etc\\");
		File file = new File(dir + "\\generator.cfg");
		Properties p = new Properties();
		if (!dir.exists())
			dir.mkdir();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try (Reader r = new FileReader(file)) {
				p.load(r);
				useNumber = Boolean.valueOf(p.getProperty(Messages.getString("Property1")));
				useLower = Boolean.valueOf(p.getProperty(Messages.getString("Property2")));
				useUpper = Boolean.valueOf(p.getProperty(Messages.getString("Property3")));
				digitNum = Integer.parseInt(p.getProperty(Messages.getString("Property4")));
				useAllCharType = Boolean.valueOf(p.getProperty(Messages.getString("Property5")));
				useCharOnlyOnce = Boolean.valueOf(p.getProperty(Messages.getString("Property6")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == button1 && outputPass != null) {
			String name = JOptionPane.showInputDialog(this, Messages.getString("Message1"),
				Messages.getString("Title2"), JOptionPane.PLAIN_MESSAGE);
			if (name != null) {
				if (name.length() <= 0)
					JOptionPane.showMessageDialog(this, Messages.getString("Message1"),
							null, JOptionPane.PLAIN_MESSAGE);
				else {
					if (existPasswordFile(name)) {
						int confirm = JOptionPane.showConfirmDialog(this, Messages.getString("Message5"),
								null, JOptionPane.YES_NO_OPTION);
						if (confirm == JOptionPane.YES_OPTION) {
							save(name);
							JOptionPane.showMessageDialog(this, Messages.getString("Message6"),
								null, JOptionPane.PLAIN_MESSAGE);
						}
					}
					else {
						save(name);
						JOptionPane.showMessageDialog(this, Messages.getString("Message2"),
								null, JOptionPane.PLAIN_MESSAGE);
					}
				}
			}
		}
		if (o == button2 && outputPass != null) {
			StringSelection stringSelection = new StringSelection(outputPass);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
			JOptionPane.showMessageDialog(this, Messages.getString("Message3"),
					null, JOptionPane.PLAIN_MESSAGE);
		}
		if (o == button3) {
			digitNum = (int) spinner.getValue();
			useNumber = checkBox1.isSelected();
			useLower = checkBox2.isSelected();
			useUpper = checkBox3.isSelected();
			useAllCharType = checkBox4.isSelected();
			useCharOnlyOnce = checkBox5.isSelected();
			String pass = GeneratePassword.getPassword(digitNum, useNumber, useLower,
					useUpper, useAllCharType, useCharOnlyOnce);
			if (pass == null) {
				outputPass = null; 
				JOptionPane.showMessageDialog(this, Messages.getString("Message4"),
						null, JOptionPane.PLAIN_MESSAGE);
			}
			else {
				outputPass = pass; 
				label2.setText(outputPass);
			}
		}
	}
	public void windowClosing() {
		saveConfig();
	}
}
