import java.io.*;
import java.net.*;
import java.util.*;

class MySocket{//构造MySocket类，可以返回socket对应的用户名
	Socket socket;
	String user;
	MySocket(Socket s,String u){
		socket=s;
		user=u;
	}
	public Socket getSocket(){
		return socket;
	}
	public String getUser(){
		return user;
	}
}

class ServerThread extends Thread{
	MySocket socket;
	static ArrayList<MySocket> sockets;
	public volatile boolean end=false;
	ServerThread(MySocket s){
		socket=s;
	}
	public void run(){
		while(!end){
			try{
				int flag=0;
				int flag2=0;
				InputStream inputStream=socket.getSocket().getInputStream();
				DataInputStream datain=new DataInputStream(inputStream);//接收客户端发来的数据
				if(end){
					datain.close();
				}
				String user=socket.getUser();//获取用户名
				String text=datain.readUTF();//获取输入的信息
				if(text.indexOf("@")==0){
					String[] tmp=text.split(" ");
					for(int i=0;i<tmp.length;i++){
						for(int j=0;j<sockets.size();j++){
							if(tmp[i].equals("@"+sockets.get(j).getUser())){//判断为私聊
								Socket ns=sockets.get(j).getSocket();
								OutputStream outputStream=ns.getOutputStream();
								DataOutputStream dataout=new DataOutputStream(outputStream);
								dataout.writeUTF(user+":"+text+"\n");	
								Socket ns2=socket.getSocket();
								OutputStream outputStream2=ns2.getOutputStream();
								DataOutputStream dataout2=new DataOutputStream(outputStream2);
								dataout2.writeUTF(user+":"+text+"\n");	
								flag=1;
							}
						}
					}
				}
				if(flag==0){
					String[] tmp2=text.split(" ");
					if(tmp2.length==3){
						if(tmp2[0].equals("客户端")&&tmp2[2].equals("退出聊天室")){//判断为用户点击退出聊天室或关闭按钮
							for(int j=0;j<sockets.size();j++){
								if(tmp2[1].equals(sockets.get(j).getUser())){
									for(int x=0;x<sockets.size();x++){
										Socket ns=sockets.get(x).getSocket();
										OutputStream outputStream=ns.getOutputStream();
										DataOutputStream dataout=new DataOutputStream(outputStream);
										dataout.writeUTF(tmp2[1]+" "+tmp2[2]+"\n");
									}
									socket.getSocket().close();
									end=true;
									sockets.remove(socket);
									Server.sts.remove(this);
									flag2=1;
								}
							}
						}
					}
					if(flag2==0){
						for(int i=0;i<sockets.size();i++){//群聊
							Socket ns=sockets.get(i).getSocket();
							OutputStream outputStream=ns.getOutputStream();
							DataOutputStream dataout=new DataOutputStream(outputStream);
							dataout.writeUTF(user+":"+text+"\n");
						}
					}
				}
				if(end){
					datain.close();
				}
			}catch(IOException e){
			}
		}
	}
}

class Manager extends Thread{
	ArrayList<MySocket> sockets;
	ArrayList<ServerThread> sts;
	volatile boolean exit=false;
	Manager(ArrayList<MySocket> ss,ArrayList<ServerThread> st){
		sockets=ss;
		sts=st;
	}
	public void run(){
		while(!exit){
			try{
				InputStreamReader is=new InputStreamReader(System.in);
				BufferedReader br=new BufferedReader(is);
				if(exit){
					br.close();
				}
				String req=br.readLine();
				if(req.equals("end")){
					for(int x=0;x<sockets.size();x++){
						Socket ns=sockets.get(x).getSocket();
						OutputStream outputStream=ns.getOutputStream();
						DataOutputStream dataout=new DataOutputStream(outputStream);
						dataout.writeUTF("管理员：聊天到此结束，再见！"+"\n");
					}
					System.out.println("已结束程序，断开服务器端与所有客户端的连接");
					System.exit(0);
				}
				if(req.equals("count")){
					System.out.println("当前现有聊天人数为"+sockets.size());
				}
				if(req.equals("chatters")){
					for(int i=0;i<sockets.size();i++){
						String s=String.valueOf(i+1);
						System.out.println(s+" "+sockets.get(i).getUser());
					}
				}
				if(req.contains("kickout")){
					String[] array=req.split(" ");
					String name=array[1];//获取被踢出的用户姓名
					int i=0;
					while(i<sockets.size()){//获取被踢用户的连接的下标
						if(sockets.get(i).getUser().equals(name)){
							break;
						}
						i++;
					}
					//System.out.println(i);
					if(i!=sockets.size()){
						int j;
						int tmp=0;
						for(int x=0;x<sockets.size();x++){//向所有客户端发送某用户被踢出群聊的消息
							Socket ns=sockets.get(x).getSocket();
							OutputStream outputStream=ns.getOutputStream();
							DataOutputStream dataout=new DataOutputStream(outputStream);
							dataout.writeUTF("服务器端"+" "+name+" "+"已被踢出群聊");
						}
						sockets.remove(i);//manager中移除
						for(j=0;j<sts.size();j++){
							if(sts.get(j).socket.getUser().equals(name)){
								sts.get(j).socket.getSocket().close();
								sts.get(j).end=true;//停止线程
								Server.sts.remove(j);//主函数中移除此线程
							}
						}
						System.out.println("已将"+name+"踢出聊天室");
					}else{
						System.out.println("该用户不存在");
					}
				}
				if(exit){
					br.close();
				}
			}catch(IOException e){
			}
		}
	}
}

public class Server{
	static ArrayList<MySocket> sockets=new ArrayList<MySocket>();
	static ArrayList<ServerThread> sts=new ArrayList<ServerThread>();
	static ArrayList<Manager> mas=new ArrayList<Manager>();
	public static void main(String[] args) throws IOException{
		ServerSocket ss=new ServerSocket(2428);
		int flag=1;
		while(true){
			Socket socket=ss.accept();//接收连接请求
			flag=0;
			InputStream inputStream=socket.getInputStream();
			DataInputStream datain=new DataInputStream(inputStream);
			String user=datain.readUTF();//接收用户名
			//System.out.println(user);
			int i=0;
			while(i<sockets.size()){//判断用户名是否存在
				if(sockets.get(i).getUser().equals(user)){
					OutputStream outputStream=socket.getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					dataout.writeUTF("1");//存在输出至客户端标志1
					dataout.flush();
					flag=1;
					break;
				}
				i++;
			}
			if(flag==0){
				OutputStream output=socket.getOutputStream();
				DataOutputStream data=new DataOutputStream(output);
				data.writeUTF("0");//不存在输出至客户端标志0
				data.flush();
				MySocket ms=new MySocket(socket,user);//构造MySocket类对象
				sockets.add(ms);//将对象添加到列表中
				for(i=0;i<sockets.size();i++){//向当前所有连接上的客户端输出
					OutputStream outputStream=sockets.get(i).getSocket().getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					dataout.writeUTF("欢迎"+user+"加入聊天"+"\n");
				}
				ServerThread.sockets=sockets;
				ServerThread st=new ServerThread(ms);//接收线程开始
				sts.add(st);//将此线程添加到线程列表中
				st.start();
				if(mas.size()!=0){
					if(sockets.size()>mas.get(0).sockets.size()){
						mas.get(0).exit=true;
						mas.remove(0);
					}
				}
				//System.out.println(mas.size());
				if(mas.size()==0||sockets.size()!=mas.get(0).sockets.size()){
					Manager m=new Manager(sockets,sts);//管理员线程开始
					mas.add(m);
					System.out.println("请输入指令：end--结束程序；count--聊天者数量；chatters--列出所有聊天者；kichout[空格]昵称--踢出聊天室");
					m.start();
				}
			}
		}
	}
}