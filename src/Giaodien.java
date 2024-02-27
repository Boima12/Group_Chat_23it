import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Giaodien extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton Connect_BT;
	private JButton Disconnect_BT;
	private JButton GuiBT;
	private String username;
	private JTextField DiachiIP_Tf;
	private JTextField Giaodiennhap;
	private JTextArea Giaodienchat;
	
	private Socket socket1;
	private Client ClientObj;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
		      		String str="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		    		UIManager.setLookAndFeel(str);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Giaodien frame = new Giaodien();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Giaodien() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1170, 740);
		setTitle("GroupChat Github");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel ChatPanel = new JPanel();
		ChatPanel.setBounds(440, 10, 700, 683);
		contentPane.add(ChatPanel);
		ChatPanel.setLayout(null);
		
		Giaodiennhap = new JTextField();
		Giaodiennhap.setEditable(false);
		Giaodiennhap.setBackground(new Color(229, 229, 229));
		Giaodiennhap.setBounds(0, 641, 606, 42);
		ChatPanel.add(Giaodiennhap);
		Giaodiennhap.setColumns(10);
		
		GuiBT = new JButton("");
		GuiBT.setEnabled(false);
		GuiBT.setBounds(605, 641, 95, 42);
		GuiBT.setIcon(new ImageIcon(new ImageIcon(Giaodien.class.getResource("/images/send.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		ChatPanel.add(GuiBT);
		
		Giaodienchat = new JTextArea();
		Giaodienchat.setEditable(false);
		Giaodienchat.setBackground(new Color(238, 238, 238));
		Giaodienchat.setBorder(new MatteBorder(1, 1, 0, 1, (Color) new Color(0, 0, 0)));
		Giaodienchat.setLineWrap(true);
		Giaodienchat.setBounds(0, 0, 700, 641);
		Giaodienchat.setLineWrap(true);
		Giaodienchat.setWrapStyleWord(true);
		ChatPanel.add(Giaodienchat);
		
		Connect_BT = new JButton("Kết nối");
		Connect_BT.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Connect_BT.setBounds(94, 410, 236, 47);
		contentPane.add(Connect_BT);
		
		Disconnect_BT = new JButton("Hủy kết nối");
		Disconnect_BT.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Disconnect_BT.setEnabled(false);
		Disconnect_BT.setBounds(94, 477, 236, 47);
		contentPane.add(Disconnect_BT);
		
		DiachiIP_Tf = new JTextField();
		DiachiIP_Tf.setBackground(new Color(229, 229, 229));
		DiachiIP_Tf.setFont(new Font("Tahoma", Font.PLAIN, 12));
		DiachiIP_Tf.setBounds(94, 356, 236, 30);
		contentPane.add(DiachiIP_Tf);
		DiachiIP_Tf.setColumns(10);
		
		JLabel DiachiIP_Lb = new JLabel("Địa chỉ IP:");
		DiachiIP_Lb.setFont(new Font("Tahoma", Font.PLAIN, 12));
		DiachiIP_Lb.setBounds(94, 337, 85, 21);
		contentPane.add(DiachiIP_Lb);
		
		ConnectListener ConnectListenerList = new ConnectListener();
		Connect_BT.addActionListener(ConnectListenerList);

		SendingListener SendingListenerList = new SendingListener();
		Giaodiennhap.addActionListener(SendingListenerList);
		GuiBT.addActionListener(SendingListenerList);
		
		DisconnectListener DisconnectListenerList = new DisconnectListener();
		Disconnect_BT.addActionListener(DisconnectListenerList);
		
		JFrame DiaFrame = new JFrame("showDialog_Username's frame");
		showDialog_Username(DiaFrame);
	}
	
    private void showDialog_Username(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Enter your username", true);

		JPanel panel = new JPanel();
		panel.setBounds(10, 10, 290, 151);
		panel.setLayout(null);
		
		JLabel Username_Lb = new JLabel("Nhập tên:");
		Username_Lb.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Username_Lb.setBounds(26, 41, 85, 21);
		panel.add(Username_Lb);
		
		JTextField Username_Tf = new JTextField();
		Username_Tf.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Username_Tf.setColumns(10);
		Username_Tf.setBackground(new Color(229, 229, 229));
		Username_Tf.setBounds(22, 60, 236, 30);
		Username_Tf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String Username_Tf_Check = Username_Tf.getText();
				if (Username_Tf_Check.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Tên người dùng không được để trống!", "Nhập lại tên!", JOptionPane.PLAIN_MESSAGE);
				} else {
					setUsername(Username_Tf.getText());
					dialog.dispose();
				}
			}
		});
		panel.add(Username_Tf);
		
		JButton Dangnhap_Bt = new JButton("Đăng nhập");
		Dangnhap_Bt.setBounds(92, 110, 107, 31);
		Dangnhap_Bt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String Username_Tf_Check = Username_Tf.getText();
				if (Username_Tf_Check.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Tên người dùng không được để trống!", "Nhập lại tên!", JOptionPane.PLAIN_MESSAGE);
				} else {
					setUsername(Username_Tf.getText());
					dialog.dispose();
				}
			}
		});
		panel.add(Dangnhap_Bt);
        
        // Set the content of the dialog to the panel
        dialog.getContentPane().add(panel);

        // Set dialog properties
        dialog.setSize(320, 200);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }
	
	private class ConnectListener implements ActionListener {	
		@Override 
		public void actionPerformed(ActionEvent evt) {
			String DiachiIP_Tf_Check = DiachiIP_Tf.getText();
			if (DiachiIP_Tf_Check.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Địa chỉ IP không được để trống!", "Nhập lại địa chỉ IP", JOptionPane.PLAIN_MESSAGE);
				return;
			}
				
			try {
				Giaodienchat.setText("");
				Giaodiennhap.setText("");
				
				socket1 = new Socket(DiachiIP_Tf.getText(), 51002);
				ClientObj = new Client(socket1, username, Giaodienchat);
				
				ChatOn();
				
				// bat kha nang nghe
				ClientObj.Client_ReceiveMsg(Giaodienchat);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Couldn't connect to the server, mind checking your internet connection?", "Connection failed", JOptionPane.PLAIN_MESSAGE);
			}
				
		}
	}
	
	private class SendingListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {
			String Giaodiennhap_Check = Giaodiennhap.getText();
			if (Giaodiennhap_Check.isEmpty()) {
				ClientObj.Client_SendMsg(Giaodiennhap);
				Giaodiennhap.setText("");
				return;
			}
			
			ClientObj.Client_SendMsg(Giaodiennhap);
			Giaodiennhap.setText("");
		}
	}
	
	private class DisconnectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent evt) {			
			ChatOff();
			
			// close the socket
			try {
				if (socket1 != null) {
					socket1.close();
				}
				
				Giaodienchat.append("you have disconnected successfully!\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void ChatOn() {
		// turning on the chat
		Giaodiennhap.setEditable(true);
		GuiBT.setEnabled(true);
		Disconnect_BT.setEnabled(true);
		
		DiachiIP_Tf.setEditable(false);
		Connect_BT.setEnabled(false);
	}
	
	private void ChatOff() {
		// turning off the chat
		Giaodiennhap.setEditable(false);
		GuiBT.setEnabled(false);
		Disconnect_BT.setEnabled(false);
		
		DiachiIP_Tf.setEditable(true);
		Connect_BT.setEnabled(true);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public JTextField getDiachiIP_Tf() {
		return DiachiIP_Tf;
	}

	public void setDiachiIP_Tf(JTextField diachiIP_Tf) {
		DiachiIP_Tf = diachiIP_Tf;
	}
}




