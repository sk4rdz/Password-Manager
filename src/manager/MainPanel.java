package manager;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainPanel extends JPanel implements ActionListener{
	public MainPanel() {
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JPanel panel = new JPanel();
		JLabel label = new JLabel(getPassword());
		add(panel);
		panel.add(label);
	}
private String getPassword(){
	BufferedReader wis = null;
	try {
		File file = new File(System.getProperty("user.dir") + "\\etc\\pwd\\a.dat");
		SecretKey key = getKey();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, generator.MainPanel.IV);
		FileInputStream fis = new FileInputStream(file);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		wis = new BufferedReader(new InputStreamReader(cis, "UTF-8"));
		return wis.readLine();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			wis.close();
		} catch (IOException e) {}
	}
	return null;
}
private SecretKey getKey(){
	File dir = new File(System.getProperty("user.dir") + "\\etc");
	File file = new File(dir + "\\key.bin");
	try(FileInputStream fis = new FileInputStream(file)){
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
	return null;
}
@Override
public void actionPerformed(ActionEvent e) {
	
}
}
