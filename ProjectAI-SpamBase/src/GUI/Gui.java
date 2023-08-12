package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import SpamFilters.KNNFilter;
import SpamFilters.NaiveBayesFilter;
import SpamFilters.RandomForestFilter;
import SpamFilters.SpamFilterInterface;


public class Gui {
	
	private static void init() {
		JFrame jframe = new JFrame("SpamBase");
		jframe.setSize(900, 750);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setLocationRelativeTo(null);
		jframe.setLayout(new BorderLayout());
		
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenu_file = new JMenu("File");
		JMenuItem jMenuItem_open = new JMenuItem("Open");
		JMenuItem jMenuItem_exit = new JMenuItem("Exit");
		
		jMenu_file.add(jMenuItem_open);
		jMenu_file.add(jMenuItem_exit);
		
		
		
		jMenuBar.add(jMenu_file);
		
		
		JPanel jpanel_north = new JPanel();
		jpanel_north.setLayout(new FlowLayout());
		TitledBorder titledBorder = new TitledBorder("Tên Mail:");
		JTextField jTextField = new JTextField(50);
		
		jpanel_north.setBorder(titledBorder);
		jpanel_north.add(jTextField);
		
		jframe.add(jpanel_north, BorderLayout.NORTH);
		
		JPanel jpanel_center = new JPanel();
		TitledBorder titledBorder2 = new TitledBorder("Nội dung Mail:");
		JTextArea jTextArea = new JTextArea(30,100);
		jTextArea.setBackground(Color.LIGHT_GRAY);
		
		jTextArea.setLineWrap(true);
			
		jpanel_center.setBorder(titledBorder2);
		JScrollPane scrollPane = new JScrollPane(jTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jpanel_center.add(scrollPane);
		jframe.add(jpanel_center, BorderLayout.CENTER);
		
		JPanel jpanel_east = new JPanel();
		JButton btn_bayes = new JButton("Naive Bayes");
		btn_bayes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				if(!jTextArea.getText().isEmpty() && !jTextField.getText().isEmpty()) {
					convertToTXT(nameFile(jTextField.getText()), jTextArea.getText());
					SpamFilterInterface naive = new NaiveBayesFilter();
					String mess = naive.getAccuracy();
					JOptionPane.showMessageDialog(jframe, mess);
					jTextArea.setText("");
					jTextField.setText("");
				}
				
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		JButton btn_knn = new JButton("KNN");
		btn_knn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(!jTextArea.getText().isEmpty() && !jTextField.getText().isEmpty()) {
						convertToTXT(nameFile(jTextField.getText()), jTextArea.getText());
						SpamFilterInterface knn = new KNNFilter();
						String mess = knn.getAccuracy();
						JOptionPane.showMessageDialog(jframe, mess);
						jTextArea.setText("");
						jTextField.setText("");
						
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		JButton btn_rdforest = new JButton("Random Forest");
		btn_rdforest.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(!jTextArea.getText().isEmpty() && !jTextField.getText().isEmpty()) {
						convertToTXT(nameFile(jTextField.getText()), jTextArea.getText());
						SpamFilterInterface randomFr = new RandomForestFilter();
						String mess = randomFr.getAccuracy();
						JOptionPane.showMessageDialog(jframe, mess);
						jTextArea.setText("");
						jTextField.setText("");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		jMenuItem_open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(jframe) == JFileChooser.APPROVE_OPTION) {
					try {
						String[] rs = load(chooser.getSelectedFile().getPath());
						jTextField.setText(rs[0]);
						jTextArea.setText(rs[1]);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				
			}
		});
		
		jMenuItem_exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		

		
		
		
		jpanel_east.setLayout(new FlowLayout());
		jpanel_east.add(btn_bayes);
		jpanel_east.add(btn_knn);
		jpanel_east.add(btn_rdforest);
		jframe.setJMenuBar(jMenuBar);
		jframe.add(jpanel_east, BorderLayout.SOUTH);
		
		jframe.setVisible(true);
	}
	
	public static boolean convertToTXT(String name, String content) throws IOException {
		String path = new File("").getAbsolutePath()+"\\src\\IncomingMailServer\\test\\";
		File file = new File(path+name+".txt");
		if(file.isDirectory()) {
			return false;
		} else {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter ost = new OutputStreamWriter(fos, "UTF-8");
			PrintWriter pw = new PrintWriter(ost);
			
			pw.write(content);
			pw.close();
			return true;
		}
	}
	
	public static String[] load(String path) throws IOException {
		String[] rs = new String[2];
		FileInputStream fis = new FileInputStream(path);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line;
		while((line = br.readLine()) != null) {
			rs[1] += line + "\n";
		}
		String name = new File(path).getName();
		String typeFile = name.substring(name.lastIndexOf("."));
		rs[0] = name.split(typeFile)[0];
		return rs;
	}
	
	public static String nameFile(String name) {
		char array[] = {'*',':','<','>','|','?',':','/', '"'};
		for(int i =0; i< array.length; i++) {
			name = name.replace(array[i], ' ');
		}
		return name;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		init();
	}
}
