import java.io.*;
import java.net.*;
import java.util.*;

class MySocket{//����MySocket�࣬���Է���socket��Ӧ���û���
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
				DataInputStream datain=new DataInputStream(inputStream);//���տͻ��˷���������
				if(end){
					datain.close();
				}
				String user=socket.getUser();//��ȡ�û���
				String text=datain.readUTF();//��ȡ�������Ϣ
				if(text.indexOf("@")==0){
					String[] tmp=text.split(" ");
					for(int i=0;i<tmp.length;i++){
						for(int j=0;j<sockets.size();j++){
							if(tmp[i].equals("@"+sockets.get(j).getUser())){//�ж�Ϊ˽��
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
						if(tmp2[0].equals("�ͻ���")&&tmp2[2].equals("�˳�������")){//�ж�Ϊ�û�����˳������һ�رհ�ť
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
						for(int i=0;i<sockets.size();i++){//Ⱥ��
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
						dataout.writeUTF("����Ա�����쵽�˽������ټ���"+"\n");
					}
					System.out.println("�ѽ������򣬶Ͽ��������������пͻ��˵�����");
					System.exit(0);
				}
				if(req.equals("count")){
					System.out.println("��ǰ������������Ϊ"+sockets.size());
				}
				if(req.equals("chatters")){
					for(int i=0;i<sockets.size();i++){
						String s=String.valueOf(i+1);
						System.out.println(s+" "+sockets.get(i).getUser());
					}
				}
				if(req.contains("kickout")){
					String[] array=req.split(" ");
					String name=array[1];//��ȡ���߳����û�����
					int i=0;
					while(i<sockets.size()){//��ȡ�����û������ӵ��±�
						if(sockets.get(i).getUser().equals(name)){
							break;
						}
						i++;
					}
					//System.out.println(i);
					if(i!=sockets.size()){
						int j;
						int tmp=0;
						for(int x=0;x<sockets.size();x++){//�����пͻ��˷���ĳ�û����߳�Ⱥ�ĵ���Ϣ
							Socket ns=sockets.get(x).getSocket();
							OutputStream outputStream=ns.getOutputStream();
							DataOutputStream dataout=new DataOutputStream(outputStream);
							dataout.writeUTF("��������"+" "+name+" "+"�ѱ��߳�Ⱥ��");
						}
						sockets.remove(i);//manager���Ƴ�
						for(j=0;j<sts.size();j++){
							if(sts.get(j).socket.getUser().equals(name)){
								sts.get(j).socket.getSocket().close();
								sts.get(j).end=true;//ֹͣ�߳�
								Server.sts.remove(j);//���������Ƴ����߳�
							}
						}
						System.out.println("�ѽ�"+name+"�߳�������");
					}else{
						System.out.println("���û�������");
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
			Socket socket=ss.accept();//������������
			flag=0;
			InputStream inputStream=socket.getInputStream();
			DataInputStream datain=new DataInputStream(inputStream);
			String user=datain.readUTF();//�����û���
			//System.out.println(user);
			int i=0;
			while(i<sockets.size()){//�ж��û����Ƿ����
				if(sockets.get(i).getUser().equals(user)){
					OutputStream outputStream=socket.getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					dataout.writeUTF("1");//����������ͻ��˱�־1
					dataout.flush();
					flag=1;
					break;
				}
				i++;
			}
			if(flag==0){
				OutputStream output=socket.getOutputStream();
				DataOutputStream data=new DataOutputStream(output);
				data.writeUTF("0");//������������ͻ��˱�־0
				data.flush();
				MySocket ms=new MySocket(socket,user);//����MySocket�����
				sockets.add(ms);//��������ӵ��б���
				for(i=0;i<sockets.size();i++){//��ǰ���������ϵĿͻ������
					OutputStream outputStream=sockets.get(i).getSocket().getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					dataout.writeUTF("��ӭ"+user+"��������"+"\n");
				}
				ServerThread.sockets=sockets;
				ServerThread st=new ServerThread(ms);//�����߳̿�ʼ
				sts.add(st);//�����߳���ӵ��߳��б���
				st.start();
				if(mas.size()!=0){
					if(sockets.size()>mas.get(0).sockets.size()){
						mas.get(0).exit=true;
						mas.remove(0);
					}
				}
				//System.out.println(mas.size());
				if(mas.size()==0||sockets.size()!=mas.get(0).sockets.size()){
					Manager m=new Manager(sockets,sts);//����Ա�߳̿�ʼ
					mas.add(m);
					System.out.println("������ָ�end--��������count--������������chatters--�г����������ߣ�kichout[�ո�]�ǳ�--�߳�������");
					m.start();
				}
			}
		}
	}
}