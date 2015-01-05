package tem.ui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Client extends JFrame {

	private static final long serialVersionUID = 7539154473381373176L;
	
	JLabel nameLabel = new JLabel("name:");
	JLabel passLabel = new JLabel("password:");
	JLabel genderLabel = new JLabel("gender");
	JLabel ageLabel = new JLabel("age");
	JLabel nickLabel = new JLabel("nickname");
	
	JTextField nameField = new JTextField(30);
	JPasswordField passField = new JPasswordField(20);
	JComboBox genderBox = new JComboBox(new String[]{"保密", "男", "女"});
	JTextField ageField = new JTextField(3);
	JTextField nickField = new JTextField(30);
	
	JButton registerButton = new JButton("确定");
	JButton cancelButton = new JButton("取消");
	
	public void init() {
		setTitle("a");
		setSize(200, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addCom();
		addListener();
		pack();
		//location center
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addCom() {
		setLayout(new GridLayout(6, 2, 10, 20));
		add(nameLabel);
		add(nameField);
		add(passLabel);
		add(passField);
		add(genderLabel);
		add(genderBox);
		add(ageLabel);
		add(ageField);
		add(nickLabel);
		add(nickField);
		add(registerButton);
		add(cancelButton);
	}
	
	private void addListener() {
		registerButton.addMouseListener(new RegistButtonListener(this));
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.init();
	}

	public JTextField getNameField() {
		return nameField;
	}

	public JPasswordField getPassField() {
		return passField;
	}

	public JComboBox getGenderBox() {
		return genderBox;
	}

	public JTextField getAgeField() {
		return ageField;
	}

	public JTextField getNickField() {
		return nickField;
	}
}