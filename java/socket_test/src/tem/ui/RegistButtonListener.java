package tem.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import tem.Constants;
import tem.Constants.OP_CODE;
import tem.domain.Protocol;
import tem.domain.User;
import tem.util.ProtocolUtil;

public class RegistButtonListener extends MouseAdapter {

	private Client client;
	
	public RegistButtonListener(Client client) {
		super();
		this.client = client;
	}

	public void mouseClicked(MouseEvent e) {
		String name = client.getNameField().getText();
		String pass = new String(client.getPassField().getPassword());
		String age = client.getAgeField().getText();
		int gender = client.getGenderBox().getSelectedIndex();
		String nickName = client.getNickField().getText();
		
		User user = new User(name, pass, age, gender, nickName);
		
		Protocol protocol = new Protocol();
		protocol.setCode(OP_CODE.REGIST);
		protocol.setContent(user.toJSONString());
		try {
			protocol.setLength(user.toJSONString().getBytes(Constants.charset).length);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Socket socket = new Socket("127.0.0.1", Constants.port);
			ProtocolUtil.write(socket.getOutputStream(), protocol);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
