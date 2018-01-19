import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Desktop;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


public class Main {

	private JFrame frmEnkriptimidekriptimiIFajllave;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmEnkriptimidekriptimiIFajllave.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEnkriptimidekriptimiIFajllave = new JFrame();
		frmEnkriptimidekriptimiIFajllave.setTitle("Enkriptimi/Dekriptimi i Fajllave RSA");
		frmEnkriptimidekriptimiIFajllave.setBounds(100, 100, 695, 498);
		frmEnkriptimidekriptimiIFajllave.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEnkriptimidekriptimiIFajllave.getContentPane().setLayout(null);
		
		JLabel lblSelectedFile = new JLabel("Nuk keni zgjedhur asgje");
		lblSelectedFile.setBounds(152, 30, 517, 14);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(lblSelectedFile);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(23, 214, 627, 183);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(textPane);
		
		JButton btnUpload = new JButton("Zgjedh Fajllin");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				fc.setFileFilter(filter);
				
				fc.setCurrentDirectory(new java.io.File("user.home"));
                fc.setDialogTitle("Zgjedheni fjallin");
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                
                if (fc.showOpenDialog(btnUpload) == JFileChooser.APPROVE_OPTION) {
                    lblSelectedFile.setText(fc.getSelectedFile().getAbsolutePath());
                }
                            
                FileInputStream fis = null;
                
                try {
                	
                	fis = new FileInputStream(fc.getSelectedFile());
    				ByteArrayOutputStream result = new ByteArrayOutputStream();
    				byte[] buffer = new byte[1024];
    				int length;
    				while ((length = fis.read(buffer)) != -1) {
    				    result.write(buffer, 0, length);
    				}
    				String fileContent = result.toString("UTF-8");
    				
    				textPane.setText(fileContent);
				} catch (Exception e) {
					// TODO: handle exception
				}
                

			}
		});
		btnUpload.setBounds(23, 26, 119, 23);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(btnUpload);
		
		
		
		JButton btnEncrypt = new JButton("Enkripto");
		btnEncrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ObjectInputStream inputStream = null;
				
				
				try {
					inputStream = new ObjectInputStream(new FileInputStream(RSA.public_key_file));
					final PublicKey publicKey = (PublicKey) inputStream.readObject();
					
					final String cipherText = Base64.getEncoder().encodeToString(RSA.encrypt(textPane.getText().getBytes("UTF-8"), publicKey));
					textPane.setText(cipherText);
					
					inputStream.close();
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(null, "Fajlli nuk mund te enkriptohet! Mjafton qe enkriptimi te behet vetem nje here!");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Nuk keni zgjedhur asnje fjallë");
				} catch (Exception e1) {
					e1.getStackTrace();
				}
			}
		});
		btnEncrypt.setBounds(23, 75, 89, 41);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(btnEncrypt);
		
		JButton btnDecrypt = new JButton("Dekripto");
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ObjectInputStream inputStream = null;
				
				try {
					inputStream = new ObjectInputStream(new FileInputStream(RSA.PRIVATE_KEY_FILE));
					final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
					
					final String plainText = RSA.decrypt(Base64.getDecoder().decode(textPane.getText()), privateKey);
					
					textPane.setText(plainText);
					
					inputStream.close();
					
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(null, "Fajlli nuk mund te enkriptohet! Mjafton qe enkriptimi te behet vetem nje here!");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Nuk keni zgjedhur asnje fjallë");
				} catch (Exception e1) {
					e1.getStackTrace();
				}
			}
		});
		btnDecrypt.setBounds(23, 127, 89, 41);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(btnDecrypt);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Celesat", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(462, 55, 170, 138);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblCelesibyte = new JLabel("Madhesia Celesit:");
		lblCelesibyte.setBounds(21, 19, 119, 14);
		panel.add(lblCelesibyte);
		
		JButton btnShowKeys = new JButton("Shfaq Celesat");
		btnShowKeys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(new File("C:\\keys"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnShowKeys.setBounds(21, 96, 119, 23);
		panel.add(btnShowKeys);
		
		JRadioButton rb1024 = new JRadioButton("1024 Bit");
		rb1024.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RSA.keySize = 1024;
				RSA.generateKey();
			}
		});
		buttonGroup.add(rb1024);
		rb1024.setBounds(21, 40, 109, 23);
		panel.add(rb1024);
		
		JRadioButton rb2048 = new JRadioButton("2048 Bit");
		rb2048.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RSA.keySize = 2048;
				RSA.generateKey();
			}
		});
		buttonGroup.add(rb2048);
		rb2048.setBounds(21, 66, 109, 23);
		panel.add(rb2048);
		
		// Zgjedhja e radio button-it
		File privateKeyFile = new File(RSA.PRIVATE_KEY_FILE);
		long keyFileSize;
		
		if(RSA.areKeysPresent()){
			keyFileSize = privateKeyFile.length();
			
			if (keyFileSize < 1024) {
				rb1024.setSelected(true);
			} else {
				rb2048.setSelected(true);
			}
		} else {
			rb1024.doClick();
			JOptionPane.showMessageDialog(null, "Celesi nuk mund te gjendet, ne do te ju krijojme nje cift celesash 1024bit!");
		}
		
		
		JLabel lblCelesiPublik = new JLabel("Celesi Publik:");
		lblCelesiPublik.setBounds(171, 88, 83, 14);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(lblCelesiPublik);
		
		JLabel lblPublicKey = new JLabel(RSA.public_key_file);
		lblPublicKey.setBounds(171, 105, 275, 14);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(lblPublicKey);
		
		JButton btnChangePublicKey = new JButton("Ndrysho");
		btnChangePublicKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Key Files", "key", "keys");
				fc.setFileFilter(filter);
				
				
				
				fc.setCurrentDirectory(new java.io.File("user.home"));
                fc.setDialogTitle("Zgjedheni Celesin Publik");
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                
                try {
                	if (fc.showOpenDialog(btnUpload) == JFileChooser.APPROVE_OPTION) {
                        lblPublicKey.setText(fc.getSelectedFile().getAbsolutePath());
                    }
                    
                    if (!fc.getSelectedFile().getAbsolutePath().equals("C:/keys/public.key")) {
                    	btnDecrypt.setEnabled(false);
                    }
                    
                    RSA.public_key_file = fc.getSelectedFile().getAbsolutePath();
                    
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		btnChangePublicKey.setBounds(171, 123, 89, 23);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(btnChangePublicKey);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(140, 75, 2, 93);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(separator);
		
		JButton btnPastro = new JButton("Reseto");
		btnPastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblSelectedFile.setText("");
				textPane.setText("");
				RSA.public_key_file = "C:/keys/public.key";
				lblPublicKey.setText(RSA.public_key_file);
				btnDecrypt.setEnabled(true);
			}
		});
		btnPastro.setBounds(462, 408, 89, 23);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(btnPastro);
		
		JButton btnRuaj = new JButton("Ruaj");
		btnRuaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileOutputStream fop = null;
				File file;

				try {
					file = new File(lblSelectedFile.getText());
					
					fop = new FileOutputStream(file);
					fop.write(textPane.getText().getBytes("UTF-8"));
					fop.flush();
					fop.close();
					
					JOptionPane.showMessageDialog(null, "Ruajtja eshte bere me sukses!");
					btnPastro.doClick();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnRuaj.setBounds(561, 408, 89, 23);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(btnRuaj);
		
		JLabel lblPermbajtjaEFajllit = new JLabel("Permbajtja e fajllit:");
		lblPermbajtjaEFajllit.setBounds(23, 189, 190, 14);
		frmEnkriptimidekriptimiIFajllave.getContentPane().add(lblPermbajtjaEFajllit);
		

	}
}
