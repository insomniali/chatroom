package ������;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;


public class myServer1 {
   public static final Map<Socket, String> onLine = new HashMap<>();
   public static String savapath="C:\\Users\\L\\Desktop\\����ϵͳ\\������\\src\\������\\�����ļ�";
   public static void main(String[] args) {
	   try {
	      ServerSocket serversocket=new ServerSocket(9996);
	      while(true) {
		   Socket socket = serversocket.accept();
		   SocketThread s=new SocketThread(socket);
		   s.start();
	      }
	   }catch(IOException e){
		   e.printStackTrace();
	   }
   }
}


class SocketThread extends Thread{
	private Socket socket;
	public  SocketThread(Socket socket) {
		this.socket=socket;
	}
	
	public void run(){
		    while(true) {
		    int tag=0;
		    tag=this.judge();
			   if(tag==1) {
				  break;
			   } 
			}
		    try {
		    while(true) {
		    	//�������������տͻ���������û���
		    	DataInputStream dis = new DataInputStream(socket.getInputStream());
				long time = System.currentTimeMillis();
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String timer = timeFormat.format(time);
				int flag = dis.readInt();
				String msg;
				switch (flag) {
				case 1:
					//�ѿͻ��˷��������û�ͼ
					myServer1.onLine.put(socket, dis.readUTF()); 
					updata(1);
					updata(2);
					break;
				case 2:
					//���տͻ��˶Ի�
					msg = myServer1.onLine.get(socket) + "  " + timer + ":\n" + dis.readUTF() + "\n";
					this.save(msg);
					for (Socket sk : myServer1.onLine.keySet()) {
						DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
						dos.writeInt(2);
						dos.writeUTF(msg);
						dos.flush();
					}
					break;
				case 3:
					//�Ȼ�ȡ�û���
					String user = dis.readUTF();
					String me = myServer1.onLine.get(socket);
					msg = myServer1.onLine.get(socket) + " �� " + user + " ��˽����Ϣ" + timer + ":\n" + dis.readUTF() + "\n";
					this.save(msg);
					for(Socket sk : myServer1.onLine.keySet()) {
						if(user.equals(myServer1.onLine.get(sk)) || me.equals(myServer1.onLine.get(sk))) {
							DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
							dos.writeInt(2);
							dos.writeUTF(msg);
							dos.flush();
						}
					}
					break;
				 case 4:
					String filename=dis.readUTF();
					int buffsize=1024;
					byte []buff=new byte[1024];
					DataInputStream fileload=new DataInputStream(socket.getInputStream());
					DataOutputStream filesave=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(myServer1.savapath+"\\"+filename)));
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
				 case 5:
					 String str2="";
					 File file=new File(myServer1.savapath);
					 for(String sk:file.list()) {
					    str2=str2+sk+",";
					    System.out.println(str2);
					 }
					 DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					 dos.writeInt(3);
					 dos.writeUTF(str2);
					 dos.flush(); 
				    break;
				 case 6:
					 DataInputStream receive=new DataInputStream(socket.getInputStream());
					 String filename2=receive.readUTF();
					 DataInputStream fileread=new DataInputStream(new FileInputStream(myServer1.savapath+"\\"+filename2));
					 DataOutputStream filesent=new DataOutputStream(socket.getOutputStream());
					 filesent.writeInt(4);
					 filesent.writeUTF(filename2);
					 int buffsize2=1024;
					 byte []buff2=new byte[buffsize2];
					 while(true) {
					    	int read=0;
					    	if(fileread!=null) {
					    		read=fileread.read(buff2);
					    	}
					    	if(read==-1) {
					    		break;
					    	}
					    	filesent.write(buff2, 0, read);
					    }
					    filesent.flush();
					    fileread.close();
					 break;
				}
		    }
		 }catch(IOException e) {
			 //�ͻ��˶Ͽ��쳣
			 System.out.println(myServer1.onLine.get(socket) + "����");
			 updata(3);
			 myServer1.onLine.remove(socket);
			 updata(2);
		 }
	}
	
	//�ж��˺������Ƿ�ƥ��
	public int judge(){
		int tag=0;
		String key;
		
		try {
			BufferedReader dis=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			key=dis.readLine();
			BufferedReader equaltry=new BufferedReader(new FileReader("C:\\Users\\L\\Desktop\\����ϵͳ\\����\\src\\��¼����\\��¼.txt"));
			String keys=equaltry.readLine();
			
			while(keys!=null&&key!=null) {
				if(keys.equals(key)) {
					tag=1;
					break;
				}
				keys=equaltry.readLine();
			}
			
			equaltry.close();
			returntag(tag);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return tag;
	}
	//����ȷ��ֵ���ͻ���
	public void returntag(int tag) {
		try {
		DataOutputStream dis=new DataOutputStream(socket.getOutputStream());
		dis.writeInt(tag);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//�������߿ͻ�ͼ
	public void updata(int flag) {
		switch(flag) {
		case 1://֪ͨ����
			try {
			   String str=myServer1.onLine.get(this.socket)+" "+"����������\n";
			   this.save(str);
			   for (Socket sk : myServer1.onLine.keySet()) {
				  DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
				  dos.writeInt(2);
				  dos.writeUTF(str);
				  dos.flush();
			   }
			}catch(IOException e) {
				e.printStackTrace();
			}
			break;
			
		case 2://֪ͨ�û��б����
		String str2 = "";
		try {
			for (Socket sk : myServer1.onLine.keySet()) {
				str2 = str2 + myServer1.onLine.get(sk) + ",";
			}
			for (Socket sk : myServer1.onLine.keySet()) {
				DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
				dos.writeInt(1);
				dos.writeUTF(str2);
				dos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		break;
		
		case 3://֪ͨ����
			try {
			   String str3=myServer1.onLine.get(this.socket)+" "+"�뿪������\n";
			   this.save(str3);
			   for (Socket sk : myServer1.onLine.keySet()) {
				  DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
				  dos.writeInt(2);
				  dos.writeUTF(str3);
				  dos.flush();
			   }
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//���������¼
	public void save(String save) {
		try {
			BufferedWriter brout=new BufferedWriter(new FileWriter("C:\\Users\\L\\Desktop\\����ϵͳ\\������\\src\\������\\�����¼.txt",true));
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