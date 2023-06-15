import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.time.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

class Receiver extends Thread{//�û���������󣬽��շ������˷��ص��ַ����������ı���������
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
	boolean end=false;//�����̵߳�ֹͣ
	Receiver(Socket s,JTextArea t,JTextArea ta2,JButton button1,JButton button2,JButton button3,JTextField t1,JTextField t2,JTextField t3,String user){//���캯��
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
				String text=data.readUTF();//���շ������˴��ݵ��ַ���
				if(text.equals("����Ա�����쵽�˽������ټ���"+"\n")){//��������������endʱ���������˻ᴫ����仰���ָ���ť���ı����ĳ�ʼ״̬
					button3.setEnabled(false);
					ta2.setEditable(false);
					button1.setEnabled(true);
					button2.setEnabled(false);
					t1.setEnabled(true);
					t2.setEnabled(true);
					t3.setEnabled(true);
				}
				String[] tmp=text.split(" ");//�ݴ��ַ���
				if(tmp.length==3){
					if(tmp[0].equals("��������")&&tmp[2].equals("�ѱ��߳�Ⱥ��")){//�ж��Ƿ����û���ִ���߳��Ĳ���
						flag2=1;
						if(user.equals(tmp[1])){//�ж��Ƿ�Ϊ���ͻ��˵��û����߳�������ǣ��򽫰�ť���ı��ָ���ʼ״̬
							button3.setEnabled(false);
							ta2.setEditable(false);
							button1.setEnabled(true);
							button2.setEnabled(false);
							t1.setEnabled(true);
							t2.setEnabled(true);
							t3.setEnabled(true);
							t.append(tmp[1]+" "+tmp[2]+"\n");//�����ʱ��ȥ���涨��־������Ա��
							flag=1;
						}
					}
				}
				if(flag==0){//���û�б�ִ�б��߳��Ĳ����������յ����������
					if(flag2==0){
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
					LocalTime localTime = LocalTime.now();
					t.append("["+dtf.format(localTime)+"]"+text);
					}else{
						t.append(tmp[1]+" "+tmp[2]+"\n");//�����ʱ��ȥ���涨��־������Ա��
					}
				}
			}catch(IOException e){
			}
		}
	}
}

class MyDialog extends JDialog{//��ʾȺ������ͬ�ǳƵĴ���
	MyDialog(){
		Container container=getContentPane();
		//container.setVisible(true);
		JLabel l=new JLabel("Ⱥ��������ͬ���ǳƣ�����ģ�");
		Font font=new Font("����",Font.BOLD,20);
		l.setFont(font);
		container.add(l);
		setTitle("��ʾ");
		setBounds(200,100,350,100);
	}
}

class MyDialog2 extends JDialog{//���û������IP��ַ���߶˿ںŴ���ʱ����ʾ
	MyDialog2(){
		Container container=getContentPane();
		//container.setVisible(true);
		JLabel l=new JLabel("IP��ַ��˿ںŴ���");
		Font font=new Font("����",Font.BOLD,20);
		l.setFont(font);
		container.add(l);
		setTitle("��ʾ");
		setBounds(200,100,350,100);
	}
}

public class Client extends JFrame{
	JPanel area1=new JPanel();
	JLabel l1=new JLabel("IP:");//IP:��ǩ
	JTextField t1=new JTextField();//IP�ı���
	JLabel l2=new JLabel("�˿�:");//�˿ڣ���ǩ
	JTextField t2=new JTextField();//�ı���
	JLabel l3=new JLabel("�ǳ�:");//�ǳƱ�ǩ
	JTextField t3=new JTextField();//�ı���
	JButton button1=new JButton("����������");//���������Ұ�ť
	JButton button2=new JButton("�˳�������");//�˳������Ұ�ť
	JTextArea ta1=new JTextArea();//�ı���1
	JLabel l4=new JLabel("��ʾ��˽�ĸ�ʽ��@˽�Ķ���[�ո�]���� ʾ����@li hello!");
	JPanel area2=new JPanel();
	JPanel area3=new JPanel();//�м�����
	JTextArea ta2=new JTextArea();//�ı���2
	JButton button3=new JButton("����");//���Ͱ�ť
	Client(){
		Font font=new Font("����",Font.BOLD,20);//��������1
		Font font2=new Font("����",Font.BOLD,17);//��������2
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
		setVisible(true);//�������
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{//�쳣����
		ArrayList<Socket> sockets=new ArrayList<Socket>();//�б��С���Ϊ1���𵽱�־��ǰ�Ƿ������Ϸ�����������
		this.addWindowListener(new WindowAdapter(){//��д���ڹرհ�ť�������¼�
			public void windowClosing(WindowEvent e){
				if(sockets.size()>0){//�����ǰ�Ѿ������Ϸ�����
					try{
						Socket socket=sockets.get(0);
						OutputStream outputStream=socket.getOutputStream();
						DataOutputStream dataout=new DataOutputStream(outputStream);
						String text="�ͻ���"+" "+t3.getText()+" "+"�˳�������";//��������˴��ݣ����ͻ��˳������ң����С��ͻ��ˡ�Ϊ��־
						sockets.remove(0);//���б����Ƴ�
						dataout.writeUTF(text);//������������
					}catch(IOException e0){
					}
				}
				System.exit(0);//�˳�
			}
		});
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String ip="127.0.0.1";//Ĭ��IP
				if(!t1.getText().equals("")){
					ip=t1.getText();
				}
				int num=2428;//Ĭ�϶˿ں�
				if(!t2.getText().equals("")){
					num=Integer.valueOf(t2.getText());
				}
				//System.out.println(ip+" "+String.valueOf(num));
				if(!ip.equals("127.0.0.1")||num!=2428){
					new MyDialog2().setVisible(true);//������Ӳ��ϣ��������ʾ
				}
				String user=t3.getText();//�õ��û���
				//System.out.println(user);
				try{
					if(sockets.size()!=0){//��֤�����µ�����֮ǰ��socktes���б�Ϊ��
						for(int j=0;j<sockets.size();j++){
							sockets.remove(j);
						}
					}
					Socket socket=new Socket(ip,num);
					OutputStream output=socket.getOutputStream();
					DataOutputStream data=new DataOutputStream(output);
					data.writeUTF(user);//���������������û���
					data.flush();
					//System.out.println(user);
					InputStream input=socket.getInputStream();
					DataInputStream datain=new DataInputStream(input);//�������˻᷵��һ�������û����Ƿ��Ѿ����ڵ��ж�ֵ
					String text=datain.readUTF();//�õ��ж�ֵ
					//System.out.println(text);
                                                                                if(text.equals("1")){//�ж�ֵΪ1���������ظ�
						new MyDialog().setVisible(true);//������ʾ
					}else{
						sockets.add(socket);
						new Receiver(socket,ta1,ta2,button1,button2,button3,t1,t2,t3,user).start();//�����߳̿�ʼ
						button3.setEnabled(true);//�ɷ���
						ta2.setEditable(true);
						button1.setEnabled(false);//���ɽ����µ�����
						button2.setEnabled(true);//���˳�
						t1.setEnabled(false);
						t2.setEnabled(false);
						t3.setEnabled(false);
					}
				}catch(IOException e1){
				}
			}
		});
		button2.addActionListener(new ActionListener(){//�˳�������
			public void actionPerformed(ActionEvent e){
				try{
					Socket socket=sockets.get(0);
					OutputStream outputStream=socket.getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					String text="�ͻ���"+" "+t3.getText()+" "+"�˳�������";//�����˳�������Ѷ�ţ����С��ͻ��ˡ�Ϊ��ʶ��Ϣ
					sockets.remove(0);
					dataout.writeUTF(text);
					button3.setEnabled(false);//���ɷ���
					ta2.setEditable(false);
					button1.setEnabled(true);//�ɽ����µ�����
					button2.setEnabled(false);
					t1.setEnabled(true);
					t2.setEnabled(true);
					t3.setEnabled(true);
				}catch(IOException e1){
				}
			}
		});
		button3.addActionListener(new ActionListener(){//���Ͱ�ť
			public void actionPerformed(ActionEvent e){
				try{
					Socket socket=sockets.get(0);
					OutputStream outputStream=socket.getOutputStream();
					DataOutputStream dataout=new DataOutputStream(outputStream);
					String text=ta2.getText();
					dataout.writeUTF(text);//����������������
					ta2.setText("");//��շ�����
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