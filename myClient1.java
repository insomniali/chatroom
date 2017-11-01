package �ͻ���;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;





public class myClient1 {
	public static String savepath="C:\\Users\\L\\Desktop\\����ϵͳ\\�ͻ���\\src\\�ͻ���\\�����ļ�";
    public static void main(String[] args) {
    	try {
       Socket socket = new Socket("localhost", 9996);
       JFrame frame=new JFrame("������");
 	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 	   frame.setBounds(500, 300, 250, 150);
 	   frame.setResizable(false);
 	   
 	   //���ÿ�Ƭ����
 	   frame.setLayout(new CardLayout());
 	   //�������
 	   JPanel p=new JPanel();
 	   JPanel p1=new JPanel();
 	   
 	   frame.getContentPane().add(p);
 	  
 	   
 	   p.add(p1);
 	   
 	   //p1��¼����
 	   JLabel account=new JLabel("�˺�");
 	   JLabel password=new JLabel("����");
 	   JButton LayIn=new JButton("��¼");
 	   JButton LayOut=new JButton("�˳�");
 	   
 	   JTextField accountenter=new JTextField(30);
 	   JTextField passwordenter=new JTextField(30);
 	   
 	   accountenter.setColumns(6);
 	   passwordenter.setColumns(6);
 	   p1.setLayout(new GridLayout(3,2,0,10));
 	   
 	   
 	   
 	   p1.add(account);
 	   p1.add(accountenter);
 	   p1.add(password);
 	   p1.add(passwordenter);
 	   p1.add(LayIn);
 	   p1.add(LayOut);
 	   
 	   LayIn.addActionListener(new ActionListener() {
 		
 		@Override
 		public void actionPerformed(ActionEvent e) {
 			// TODO Auto-generated method stub
 			ClientReadThread chatsocket=new ClientReadThread(socket, accountenter.getText());
 			while(true) {
 			int tag=0;
 			String key;
 			key=accountenter.getText()+passwordenter.getText();
 			chatsocket.sendkey(key);
 			tag=chatsocket.getkey();
 			if(tag==1) {
 				chatsocket.start();
 				frame.dispose();
 				break;
 			}
 			else {
 				JOptionPane.showMessageDialog(frame, "�û������������");
 	 			break;
 			}
 				//new ClientReadThread(socket, accountenter.getText()).start();
 			
 	      }
 	    }
 	}); 
 	   
 	   //�˳���Ӧ
 	   LayOut.addActionListener(new ActionListener() {
 			
 			@Override
 			public void actionPerformed(ActionEvent e) {
 				// TODO Auto-generated method stub
 				frame.dispose();
 			}
 		});
 	    frame.setVisible(true);
 	    }catch(IOException f) {
 	    	f.printStackTrace();
 	    }
    }
}

class ClientReadThread extends Thread{
	private Socket socket;
	private String accountenter;
	private String []usergroup;
	private JFrame chatjframe=new JFrame();
	private JPanel roleUI=new JPanel();
	private JPanel listUI=new JPanel();
	private JList<String>friend=new JList<>();
	private JTextArea message=new JTextArea(15,60);
	private JTextArea sent=new JTextArea(7,60);
	private JButton sentbutton=new JButton("����");
	private JButton cancel=new JButton("ȡ��ѡ��");
	private JButton chatrecord=new JButton("�����¼");
	private JButton filebutton=new JButton("�ļ�");
	private JList<String>filelist=new JList<>();
	private String []filegroup;
	public ClientReadThread(Socket socket, String accounterter) {
		this.socket = socket;
		this.accountenter = accounterter;
	}
	
	
	//��ʼ��
	public void init() {
	      chatjframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  chatjframe.setBounds(200, 100, 800, 500);
		  chatjframe.setTitle("������");
		  chatjframe.setResizable(false);
		  message.setEditable(false);
		  message.setLineWrap(true);
		  chatjframe.setLayout(new BorderLayout());
		  friend.setFixedCellWidth(120);
		  listUI.add(new JScrollPane(friend)); 
		  roleUI.add(new JScrollPane(message));
		  roleUI.add(new JScrollPane(sent));
		  roleUI.add(sentbutton);
		  roleUI.add(cancel);
		  roleUI.add(chatrecord);
		  roleUI.add(filebutton);
		  chatjframe.add(roleUI,BorderLayout.CENTER);
		  chatjframe.add(listUI,BorderLayout.EAST);
		  chatjframe.setVisible(true);
		  
		  
		  sentbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					//���������
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					if(friend.isSelectionEmpty()) {
						dos.writeInt(2);
						dos.writeUTF(sent.getText());
						dos.flush();
						sent.setText("");
					}
					else {
						//���ѡ���Լ��ͻ�����ʱ
						if (friend.getSelectedValue().equals(accountenter)) {
							message.append("-------���ܺ��Լ�����-------\n");
						} 
						else {
							dos.writeInt(3);
							dos.writeUTF(friend.getSelectedValue());
							dos.writeUTF(sent.getText());
							dos.flush();
							sent.setText("");
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		  
	      cancel.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			friend.clearSelection();
		}
	});  
	  
	      chatrecord.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			    JFrame chatrecordUI=new JFrame("�����¼");
				chatrecordUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				chatrecordUI.setBounds(200, 100, 800, 500);
				chatrecordUI.setResizable(false);
				chatrecordUI.setLayout(new BorderLayout());
				JPanel recordUI=new JPanel();
				JTextArea record=new JTextArea(15,60);
				JButton close=new JButton("�ر�");
				record.setEditable(false);
				record.setLineWrap(true);
				chatrecordUI.add(recordUI,BorderLayout.CENTER);
				recordUI.add(new JScrollPane(record));
				recordUI.add(close);
				chatrecordUI.setVisible(true);
				try {
					BufferedReader brin=new BufferedReader(new FileReader("C:\\Users\\L\\Desktop\\����ϵͳ\\�ͻ���\\src\\�ͻ���\\�����¼.txt"));
					String recordReader;
					while((recordReader=brin.readLine())!=null){
						record.append(recordReader+"\n");
					}
					brin.close();
				} catch (IOException f) {
					// TODO Auto-generated catch block
					f.printStackTrace();
				}
				
				close.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						chatrecordUI.dispose();
					}
				});
			}
		});
	      
	      filebutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame fileUI=new JFrame("�ļ�����");
				JLabel tips=new JLabel("�����ļ�");
				JButton sent=new JButton("�ϴ�");
			 	JButton load=new JButton("����");
			 	JButton cancel=new JButton("ȡ��ѡ��");
				fileUI.add(tips);
				fileUI.add(new JScrollPane(filelist));
				fileUI.add(sent);
				fileUI.add(load);
				fileUI.add(cancel);
				fileUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				fileUI.setBounds(300, 200, 400, 300);
				fileUI.setResizable(false);
				fileUI.setLayout(new GridLayout(3,2,0,10));
				fileUI.setVisible(true);
				
				try {
					DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
					dos.writeInt(5);
					dos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				sent.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JFrame find=new JFrame("�ϴ�");
						JFileChooser findchoose=new JFileChooser();
						int vRal=findchoose.showOpenDialog(find);
						if(vRal==findchoose.APPROVE_OPTION) {
							try {
								String filename=findchoose.getSelectedFile().getName();
							    String filepath=findchoose.getCurrentDirectory().toString();
							    DataInputStream fileread=new DataInputStream(new FileInputStream(filepath+"\\"+filename));
							    DataOutputStream filesent=new DataOutputStream(socket.getOutputStream());
							    filesent.writeInt(4);
                                int buffsize=1024;
							    byte []buff=new byte[buffsize];
							    while(true) {
							    	int read=0;
							    	if(fileread!=null) {
							    		read=fileread.read(buff);
							    	}
							    	if(read==-1) {
							    		break;
							    	}
							    	filesent.write(buff, 0, read);
							    }
							    filesent.flush();							    filesent.writeUTF(filename);
							    
							    fileread.close();
							   
							    JOptionPane.showMessageDialog(find, "�ϴ��ɹ�");
								}catch(IOException d) {
									d.printStackTrace();
							}
						}
					}
				});
				
				load.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(!filelist.getSelectedValue().isEmpty()) {
							try {
								DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
								DataInputStream dis=new DataInputStream(socket.getInputStream());
								dos.writeInt(6);
								dos.writeUTF(filelist.getSelectedValue());
								
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else {
							JOptionPane.showMessageDialog(fileUI, "ѡ����Ϊ��");
						}
					}
				});
				
				cancel.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						filelist.clearSelection();
					}
				});  
			}
		});
	}
	
	public void run() {
		init();
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeInt(1);
			dos.writeUTF(accountenter);
			dos.flush();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			while(true) {
				int flag=dis.readInt();
				switch (flag) {
				case 1:
					//�����б����/�����û�����
					usergroup= dis.readUTF().split(",");
					friend.setListData(usergroup);
					break;
				case 2:
					//��ʾ�ͻ��˶Ի�
					String msg = dis.readUTF();
					this.save(msg);
					message.append(msg);
					break;
				case 3:
					//�����ļ��б�
					filegroup=dis.readUTF().split(",");
					filelist.setListData(filegroup);
					break;
				case 4:
					String filename=dis.readUTF();
					int buffsize=1024;
					byte []buff=new byte[1024];
					DataInputStream fileload=new DataInputStream(socket.getInputStream());
					DataOutputStream filesave=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(myClient1.savepath+"\\"+filename)));
					while(true) {
						int read=0;
						if(fileload!=null) {
							read=fileload.read();
						}
						if(read==-1) {
							break;
						}
						filesave.write(buff, 0, read);
					}
					filesave.close();
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�����˺�����
	public void sendkey(String key) {
		try {
			PrintWriter dos=new PrintWriter(socket.getOutputStream());
			dos.println(key);
			dos.flush();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//��ȡ�Ƿ��˺�����ƥ��
	public int getkey() {
		int tag = 0;
		try {
			DataInputStream dis=new DataInputStream(socket.getInputStream());
			tag=dis.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tag;
	}
	
	//���������¼
    public void save(String save) {
			try {
				BufferedWriter brout=new BufferedWriter(new FileWriter("C:\\Users\\L\\Desktop\\����ϵͳ\\�ͻ���\\src\\�ͻ���\\�����¼.txt",true));
				brout.write(save);	
				brout.newLine();
				brout.flush();
				brout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
