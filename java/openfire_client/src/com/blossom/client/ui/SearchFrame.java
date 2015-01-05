package com.blossom.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.blossom.client.packet.GroupApplyIQ;
import com.blossom.client.packet.GroupQueryIQ;
import com.blossom.client.util.Constants;
import com.blossom.client.util.Session;

public class SearchFrame extends BaseFrame {

	private static final long serialVersionUID = 6408934789919566835L;
	
	private static final int text_size = 15;

	private Session session = Session.getSession();
	
	private JLabel idLabel = new JLabel("id");
	private JTextField idText = new JTextField(text_size);
	private JLabel nameLabel = new JLabel("name");
	private JTextField nameText = new JTextField(text_size);
	
	private JButton button = new JButton("查找");
	
	private JMenuBar menu = new JMenuBar();
	JMenu group_menu = new JMenu("群");
	JMenuItem group_apply = new JMenuItem("加入");
	
	String[] headers = {"id", "name", "jid"};
	Object[][] cellData = {};
	private DefaultTableModel tableModel = new DefaultTableModel(cellData, headers);
	private JTable list = new JTable(tableModel);

	public SearchFrame() {
		setTitle("查找群");
		setSize(400, 300);
		
		group_menu.add(group_apply);
		menu.add(group_menu);
		group_apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyGroup();
			}
		});
		setJMenuBar(menu);
		
		setLayout(new BorderLayout());
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.setSize(300, 30);
		panel1.add(idLabel, BorderLayout.WEST);
		panel1.add(idText, BorderLayout.CENTER);
		
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.setSize(300, 30);
		panel2.add(nameLabel, BorderLayout.WEST);
		panel2.add(nameText, BorderLayout.CENTER);
		
		JPanel panel3 = new JPanel(new BorderLayout());
		panel3.setSize(400, 60);
		panel3.add(panel1, BorderLayout.NORTH);
		panel3.add(panel2, BorderLayout.CENTER);
		panel3.add(button, BorderLayout.EAST);
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		
		add(panel3, BorderLayout.NORTH);
		
		JScrollPane panel4 = new JScrollPane();
		panel4.setSize(400, 200);
		panel4.getViewport().add(list);
		list.setRowSelectionAllowed(true);
		
		add(panel4, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation( (screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        
        init();
	}
	
	private void init() {
	}

	private void applyGroup() {
		int row = list.getSelectedRow();
		String jid = (String)tableModel.getValueAt(row, 2);
		String name = (String)tableModel.getValueAt(row, 1);
		System.out.println(jid + "\t" + name);
		
		String reason = JOptionPane.showInputDialog("申请理由");
		if (reason != null) {
			GroupApplyIQ applyIQ = GroupApplyIQ.createApplyIq(jid.split("@")[0], reason);
			session.addIq(applyIQ);
			session.getConnection().sendPacket(applyIQ);
		}
	}
	
	protected void onLoadGroups(GroupQueryIQ groupQueryIQ) {
		
		for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
		for (GroupQueryIQ.Item item : groupQueryIQ.getItems()) {
			tableModel.addRow(new String[]{item.getJid().split("@")[0], item.getName(), item.getJid()});
		}
	}
	
	private void search() {
		String id = idText.getText();
		String name = nameText.getText();
		GroupQueryIQ groupQueryIQ = new GroupQueryIQ();
		groupQueryIQ.setTo(Constants.SERVER_NAME);
		groupQueryIQ.setCondition(new GroupQueryIQ.Condition(id, name));
		session.getConnection().sendPacket(groupQueryIQ);
	}
	
	public static void main(String[] args) {
		new SearchFrame();
	}
}
