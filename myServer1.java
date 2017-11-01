package 服务器;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;


public class myServer1 {
   public static final Map<Socket, String> onLine = new HashMap<>();
   public static String savapath="C:\\Users\\L\\Desktop\\聊天系统\\服务器\\src\\服务器\\接收文件";
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
		    	//创建输入流接收客户端输入的用户名
		    	DataInputStream dis = new DataInputStream(socket.getInputStream());
				long time = System.currentTimeMillis();
				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String timer = timeFormat.format(time);
				int flag = dis.readInt();
				String msg;
				switch (flag) {
				case 1:
					//把客户端放入在线用户图
					myServer1.onLine.put(socket, dis.readUTF()); 
					updata(1);
					updata(2);
					break;
				case 2:
					//接收客户端对话
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
					//先获取用户名
					String user = dis.readUTF();
					String me = myServer1.onLine.get(socket);
					msg = myServer1.onLine.get(socket) + " 对 " + user + " 的私聊信息" + timer + ":\n" + dis.readUTF() + "\n";
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
			 //客户端断开异常
			 System.out.println(myServer1.onLine.get(socket) + "下线");
			 updata(3);
			 myServer1.onLine.remove(socket);
			 updata(2);
		 }
	}
	
	//判断账号密码是否匹配
	public int judge(){
		int tag=0;
		String key;
		
		try {
			BufferedReader dis=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			key=dis.readLine();
			BufferedReader equaltry=new BufferedReader(new FileReader("C:\\Users\\L\\Desktop\\聊天系统\\界面\\src\\登录界面\\登录.txt"));
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
	//返回确认值给客户端
	public void returntag(int tag) {
		try {
		DataOutputStream dis=new DataOutputStream(socket.getOutputStream());
		dis.writeInt(tag);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//更新在线客户图
	public void updata(int flag) {
		switch(flag) {
		case 1://通知上线
			try {
			   String str=myServer1.onLine.get(this.socket)+" "+"加入聊天室\n";
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
			
		case 2://通知用户列表更新
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
		
		case 3://通知下线
			try {
			   String str3=myServer1.onLine.get(this.socket)+" "+"离开聊天室\n";
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
	
	//保存聊天记录
	public void save(String save) {
		try {
			BufferedWriter brout=new BufferedWriter(new FileWriter("C:\\Users\\L\\Desktop\\聊天系统\\服务器\\src\\服务器\\聊天记录.txt",true));
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