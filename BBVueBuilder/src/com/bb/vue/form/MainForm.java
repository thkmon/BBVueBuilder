package com.bb.vue.form;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.bb.vue.util.ConsoleUtil;

public class MainForm extends JFrame {

	
	// 폴더경로
	public static JTextField textField1 = null;
	
	public static String labelText1 = "Vue Project Dir";
	public static JButton pathButton1 = null;
	
	// 우측상단 버튼
	private static JButton buildButton = null;
	
	private int textFieldHeight = 35;
	private int buttonHeight = 35;
	
	// 콘솔용 텍스트영역
	public static JTextArea textArea = null;
	
	// 콘솔용 스크롤
	public static JScrollPane scrollPane = null;
	
	
	// 폰트 설정
	private Font basicFont = new Font("돋움", 0, 14);
	private Font smallFont = new Font("돋움", 0, 12);
	
	// 연한회색 색상 설정
	private Color lightGrayColor = new Color(240, 240, 240);
	
	
	private int defaultHeight = 400;
	private int textAreaTop = 60;
	private int textAreaHeightGap = 120;
	
	
	public MainForm(String title, String version, String[] args) {
		
		// 폼 생성
		if (title == null) {
			title = "";
		}
		
		if (version != null && version.length() > 0) {
			title = title + "_" + version;
		}
		
		String defaultDirPath = "";
		if (args != null && args.length > 0 && args[0] != null && args[0].length() > 0) {
			defaultDirPath = args[0];
		}
		
		this.setLayout(null);
		this.setTitle(title);
		
		addBasicWindowListener();
		
		addLabel();
		addButton();
		addTextField(defaultDirPath);
		addTextArea();
		
		this.setBounds(0, 0, 800, defaultHeight);
		this.setVisible(true);
	}
	
	
	private void addBasicWindowListener() {
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// 사용자 명령으로 종료
				System.out.println("사용자 명령으로 종료합니다.");
				System.exit(0);
			}
		});
		
		
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				
				Rectangle rectangle = e.getComponent().getBounds();
				
				int formWidth = (int) rectangle.getWidth();
				int formHeight = (int) rectangle.getHeight();
				
				if (textField1 != null) {
					textField1.setBounds(130, 10, formWidth - 335, textFieldHeight);
				}
				
				if (pathButton1 != null) {
					pathButton1.setBounds(formWidth - 200, 10, 35, textFieldHeight);
				}
				
				if (buildButton != null) {
					buildButton.setBounds(formWidth - 155, 10, 120, buttonHeight);
				}
				
				if (scrollPane != null) {
					scrollPane.setBounds(10, textAreaTop, formWidth - 45, formHeight - textAreaHeightGap);
				}
				
				if (textArea != null) {
					textArea.setBounds(10, textAreaTop, formWidth - 45, formHeight - textAreaHeightGap);
				}
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	private void addLabel() {
		// 좌측상단 레이블 추가
		JLabel label1 = new JLabel(labelText1);
		label1.setFont(basicFont);
		label1.setBounds(10, 10, 150, textFieldHeight);
		this.getContentPane().add(label1);
	}
	
	
	private void addButton() {
		
		// gray color
		Color buttonColor = new Color(230, 230, 230);
		
		pathButton1 = new JButton("...");
		pathButton1.setFont(basicFont);
		pathButton1.setBackground(buttonColor);
		pathButton1.setBounds(600, 10, 35, textFieldHeight);
		this.getContentPane().add(pathButton1);
		
		pathButton1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedPath = new DialogWrapper().openFolderDialog(textField1.getText());
				if (selectedPath != null && selectedPath.length() > 0) {
					textField1.setText(selectedPath);
				}
			}
		});
		
		// 우측상단 버튼 추가
		buildButton = new JButton("Build");
		buildButton.setFont(basicFont);
		buildButton.setBackground(buttonColor);
		buildButton.setBounds(645, 10, 120, buttonHeight);
		this.getContentPane().add(buildButton);
		
		
		/**
		 * Build 버튼 기능
		 */
		buildButton.addActionListener(new ActionListener() {
			
			// 버튼 클릭시 이벤트 수행
			@Override
			public void actionPerformed(ActionEvent e) {

				// 아직 로딩 전이면 버튼 동작하지 않는다.
				if (textField1 == null) {
					ConsoleUtil.print("Loading...");
					return;
				}
				
				BuildThead buildThead = new BuildThead();
				buildThead.start();
			}
		});
	}
	
	
	private void addTextField(String defaultDirPath) {
		// 폴더경로 추가
		textField1 = new JTextField();
		textField1.setFont(smallFont);
		this.getContentPane().add(textField1);
		textField1.setText(defaultDirPath);
		textField1.setBounds(130, 10, 465, 35);
		
		// 포커싱
		textField1.requestFocus();
	}
	
	
	private void addTextArea() {
		// 하단 콘솔용 텍스트영역 추가
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(lightGrayColor);
		textArea.setBounds(0, 0, 100, 100);
		textArea.setFont(basicFont);
		textArea.setLineWrap(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(lightGrayColor);
		this.getContentPane().add(scrollPane);
		scrollPane.setBounds(10, textAreaTop, 755, defaultHeight - textAreaHeightGap);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	
	public static void setFormDisable() {
		textField1.setEnabled(false);
		pathButton1.setEnabled(false);
		buildButton.setEnabled(false);
	}
	
	
	public static void setFormEnable() {
		textField1.setEnabled(true);
		pathButton1.setEnabled(true);
		buildButton.setEnabled(true);
	}
}