package com.blossom.client.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.blossom.client.util.Session;

public class Login extends JFrame {

	private static final long serialVersionUID = 7053257767768086874L;
	
	private static final int text_size = 15;

	private final JLabel usernameLabel = new JLabel("用户名");
    private final JTextField usernameField = new JTextField("13809258758", text_size);
    private final JLabel passwordLabel = new JLabel("密码");
    private final JPasswordField passwordField = new JPasswordField("888@@@", text_size);
    private final JLabel serverLabel = new JLabel("服务器");
    private final JTextField serverField = new JTextField("chat.352.cn", text_size);
    private final JButton loginButton = new JButton("登陆");

    protected Login() {
    	setTitle("登陆");
    	setLayout(new FlowLayout());
    	add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);

        add(serverLabel);
        add(serverField);
        add(loginButton);
        
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (login()) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							new MainFrame();
						}
					});
					Login.this.dispose();
				}
			}
		});
        
        setSize(200, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

	private boolean login() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		String server = serverField.getText();
		ConnectionConfiguration config = new ConnectionConfiguration(server, 5222);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login(username, password);
			Session.getSession().init(connection, username, password);
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
    
	public static void main(String[] args) {
		new Login();
	}
}
