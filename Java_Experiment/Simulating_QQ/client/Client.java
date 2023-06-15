import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.time.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

class Receiver extends Thread{//用户进入聊天后，接收服务器端返回的字符串，并在文本区中体现
	Socket socket;
	JTextArea t;
	JTextArea ta2;
	JButton button1;
	JButton button2;
	JButton button3;
	JTextField t1;
	JTextField t2;
	JTextField t3;
	String user;
	boolean end=false;//用于线程的停止
	Receiver(Socket s,JTextArea t,JTextArea ta2,JButton button1,JButton button2,JButton button3,JTextField t1,JTextField t2,JTextField t3,String user){//构造函数
		socket=s;
		this.t=t;
		this.ta2=ta2;
		this.button1=button1;
		this.button2=button2;
		this.button3=button3;
		this.t1=t1;
		this.t2=t2;
		this.t3=t3;
		this.user=user;
	}
	public void End(){
		end=true;
	}
	public void run(){
		while(!end){
			try{
				int flag=0;
				int flag2=0;
				InputStream input=socket.getInputStream();
				DataInputStream data=new DataInputStream(input);
				String text=data.readUTF();//接收服务器端传递的字符串
				if(text.equals("管理员：聊天到此结束，再见！"+"\n")){//当服务器端输入end时，服务器端会传递这句话，恢复按钮和文本区的初始状态
					button3.setEnabled(false);
					ta2.setEditable(false);
					button1.setEnabled(true);
					button2.setEnabled(false);
					t1.setEnabled(true);
					t2.setEnabled(true);
					t3.setEnabled(true);
				}
				String[] tmp=text.split(" ");//暂存字符串
				if(tmp.length==3){
					if(tmp[0].equals("服务器端")&&tmp[2].equals("已被踢出群聊")){//判断是否有用户被执行踢出的操作
						flag2=1;
						if(user.equals(tmp[1])){//判断是否为本客户端的用户被踢出，如果是，则将按钮和文本恢复初始状态
							button3.setEnabled(false);
							ta2.setEditable(false);
							button1.setEnabled(true);
							button2.setEnabled(false);
							t1.setEnabled(true);
							t2.setEnabled(true);
							t3.setEnabled(true);
							t.append(tmp[1]+" "+tmp[2]+"\n");//输出的时候，去掉规定标志“管理员”
							flag=1;
						}
					}
				}
				if(flag==0){//如果没有被执行被踢出的操作，将接收到的数据输出
					if(flag2==0){
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
					LocalTime localTime = LocalTime.now();
					t.append("["+dtf.format(localTime)+"]"+text);
					}else{
						t.append(tmp[1]+" "+tmp[2]+"\n");//输出的时候，去掉规定标志“管理员”
					}
				}
			}catch(IOException e){
			}
		}
	}
}

class MyDialog extends JDialog{//提示群中有相同昵称的窗口
	MyDialog(){
		Container container=getContentPane();
		//container.setVisible(true);
		JLabel l=new JLabel("群中已有相同的昵称，请更改！");
		Font font=new Font("宋体",Font.BOLD,20);
		l.setFont(font);
		container.add(l);
		setTitle("提示");
		setBounds(200,100,350,100);
	}
}

class MyDialog2 extends JDialog{//当用户输入的IP地址或者端口号错误时的提示
	MyDialog2(){
		Container container=getContentPane();
		//container.setVisible(true);
		JLabel l=new JLabel("IP地址或端口号错误！");
		Font font=new Font("宋体",Font.BOLD,20);
		l.setFont(font);
		container.add(l);
		setTitle("提示");
		setBounds(200,100,350,100);
	}
}

public class Client extends JFrame{
	JPanel area1=new JPanel();
	JLabel l1=new JLabel("IP:");//IP:标签
	JTextField t1=new JTextField();//IP文本框
	JLabel l2=new JLabel("端口:");//端口：标签
	JTextField t2=new JTextField();//文本框
	JLabel l3=new JLabel("昵称:");//昵称标签
	JTextField t3=new JTextField();//文本框
	JButton button1=new JButton("进入聊天室");//进入聊天室按钮
	JButton button2=new JButton("退出聊天室");//退出聊天室按钮
	JTextArea ta1=new JTextArea();//文本区1
	JLabel l4=new JLabel("提示：私聊格式：@私聊对象[空格]内容 示例：@li hello!");
	JPanel area2=new JPanel();
	JPanel area3=new JPanel();//中间容器
	JTextArea ta2=new JTextArea();//文本区2
	JButton button3=new JButton("发送");//发送按钮
	Client(){
		Font font=new Font("宋体",Font.BOLD,20);//设置字体1
		Font font2=new Font("宋体",Font.BOLD,17);//设置字体2
		area1.setLayout(new FlowLayout(FlowLayout.CENTER,10,0));
		ta1.setEditable(false);
		ta2.setEditable(false);
		button2.setEnabled(false);
		button3.setEnabled(false);
		l1.setPreferredSize(new Dimension(50,40));
		l1.setFont(font);
		area1.add(l1);
		t1.setFont(font);
		t1.setPreferredSize(new Dimension(120,40));
		area1.add(t1);
		l2.setPreferredSize(new Dimension(60,40));
		l2.setFont(font);
		area1.add(l2);
		t2.setPreferredSize(new Dimension(120,40));
		t2.setFont(font);
		area1.add(t2);
		l3.setPreferredSize(new Dimension(60,40));
		l3.setFont(font);
		t3.setPreferredSize(new Dimension(120,40));
		t3.setFont(font);
		area1.add(l3);
		area1.add(t3);
		button1.setFont(font);
		area1.add(button1);
		button2.setFont(font);
		area1.add(button2);
		l4.setFont(font2);
		l4.setPreferredSize(new Dimension(800,20));
		area3.setPreferredSize(new Dimension(800,250));
		area3.add(l4,BorderLayout.NORTH);
		area2.setPreferredSize(new Dimension(1000,250));
		//area2.setLayout(new FlowLayout(FlowLayout.CENTER));
		ta2.setPreferredSize(new Dimension(800,230));
		ta2.setFont(font);
		area3.add(new JScrollPane(ta2),BorderLayout.SOUTH);
		area2.add(area3,BorderLayout.WEST);
		ta1.setFont(font);
		button3.setFont(font);
		button3.setPreferredSize(new Dimension(150,240));
		area2.add(button3,BorderLayout.EAST);
		setTitle("Client");
		setSize(1000,1000);
		add(area1,BorderLayout.NORTH);
		add(new JScrollPane(ta1),BorderLayout.CENTER);
		add(area2,BorderLayout.SOUTH);
		setVisible(true);//界面设计
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{//异常处理
		ArrayList<Socket> sockets=new ArrayList<Socket>();//列表大小最大为1，起到标志当前是否连接上服务器的作用
		this.addWindowListener(new WindowAdapter(){//重写窗口关闭按钮触发的事件
			public void windowClosing(WindowEvent e){
				if(sockets.size()>0){//如果当前已经连接上服务器
					try{
						Socket socket=sockets.get(0);
						OutputStream outputStream=socket.getOutputStream();
						DataOutputStream dataout=new DataOutputStream(outputStream);
						String text="客户端"+" "+t3.getText()+" "+"退出聊天室";//向服务器端传递，本客户退出聊天室，其中“客户端”为标志
						sockets.remove(0);//从列表中移除
						dataout.writeUTF(text);//向服务器端输出
					}catch(IOException e0){
					}
				}
				System.exit(0);//退出
			}
		});
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String ip="127.0.0.1";//默认IP
				if(!t1.getText().equals("")){
					ip=t1.getText();
				}
				int num=2428;//默认端口号
				if(!t2.getText().equals("")){
					num=Integer.valueOf(t2.getText());
				}
				//System.out.println(ip+" "+String.valueOf(num));
				if(!ip.equals("127.0.0.1")||num!=2428){
					new MyDialog2().setVisible(true);//如果连接不上，会给有提示
				}
				String user=t3.getText();//得到用户名
				//System.out.println(user);
				try{
					if(sockets.size()!=0){//保证建立新的连接之前，socktes的列表为空
						for(int j=0;j<sockets.size();j++){
							sockets.remove(j);
						}
					}
					Socket socket=new Socket(ip,num);
					OutputStream output=socket.getOutputStream();
					DataOutputStream data=new DataOutputStream(output);
					data.writeUTF(user);//先向服务器端输出用户名
					data.flush();
					//System.out.println(user);
					InputStream input=socket.getInputStream();
					DataInputStream datain=new DataInputStream(input);//服务器端会返回一个关于用户名是否已经存在的判断值
					String text=datain.readUTF();//得到判断值
					//System.out.println(text);
                                                                                if(text.equals("1")){//判断值为1，代表有重复
						new MyDialog().setVisible(true);//给予提示
					}else{
						sockets.add(socket);
						new Receiver(socket,ta1,ta2,button1,button2,button3,t1,t2,t3,user).start();//接收线程开始
						button3.setEnabled(true);//可发送
						ta2.setEditable(true);
						button1.setEnabled(false);//不可建立新的连接
						button2.setEnabled(true);//可退出
						t1.setEnabled(false);
						t2.setEnabled(false);
						t3.setEnabled(false);
					}
				}catch(IOException e1){
				}
			}
		});
		button2.addActionListener(new ActionListener(){//退出聊天室
			public void actionPerformed(ActionEvent e){
				try{
					Socket socket=sockets.get(0);
					OutputStream outputStream=socket.getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					String text="客户端"+" "+t3.getText()+" "+"退出聊天室";//发送退出聊天室讯号，其中“客户端”为标识信息
					sockets.remove(0);
					dataout.writeUTF(text);
					button3.setEnabled(false);//不可发送
					ta2.setEditable(false);
					button1.setEnabled(true);//可建立新的连接
					button2.setEnabled(false);
					t1.setEnabled(true);
					t2.setEnabled(true);
					t3.setEnabled(true);
				}catch(IOException e1){
				}
			}
		});
		button3.addActionListener(new ActionListener(){//发送按钮
			public void actionPerformed(ActionEvent e){
				try{
					Socket socket=sockets.get(0);
					OutputStream outputStream=socket.getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					String text=ta2.getText();
					dataout.writeUTF(text);//向服务器端输出内容
					ta2.setText("");//清空发送区
				}catch(IOException e1){
				}
			}
		});
		
		}catch(Exception e2){
		}
	}
	public static void main(String[] args){
		new Client();
	}
}