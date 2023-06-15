import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.script.*;
import java.util.*;


class LeftBall extends Thread{//�̳�Thread��ʵ�ִӴ���������ֵĵ����Ļ��ơ��ƶ�
	JPanel LEFTPANEL;//�Ӵ���������ֵĵ��������
	public volatile boolean exit=false;//���ڽ����߳�
	private final Object lock = new Object();//������ͣ�߳�
	private boolean pause=false;//������ͣ�߳�
	int x;//�����ĺ�����
	static int x_increase=5;//�����ƶ�������
	int maxY;//y�����ȡֵ
	int y;//������������
	//Image offScreenImage;
	LeftBall(JPanel j){//���캯��
		LEFTPANEL=j;
		x=0;
		maxY=(int)LEFTPANEL.getBounds().getHeight()-40;//���㻭����ʱY����������
		Random rand=new Random();
		y=rand.nextInt(maxY-60)+40;//��0~maxY-19��Χ���������y����
		//offScreenImage=LEFTPANEL.createImage((int)LEFTPANEL.getBounds().getWidth(),(int)LEFTPANEL.getBounds().getHeight());
	}
	void pauseThread(){
        		pause=true;
	}
    	void resumeThread(){//�����߳�
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause() {//�����߳�
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){  //�̳�Thread��ʵ�ֵķ���
		Graphics g= LEFTPANEL.getGraphics();//���ͼ��
		while(!exit) {//���ñ�־�����˳�����
			while(pause){
				onPause();
			}
			//ѭ���ƶ�����
			g.setColor(Color.WHITE);//����һ�λ��ĵ�������
			g.fillOval(x,y,40,40);//������ָ�����ο��Բ
			//g.drawImage(offScreenImage,0,0,null);
			x=x+x_increase;//����ÿ��X���λ��
			if(x>=(int)LEFTPANEL.getBounds().getWidth()-40){
				exit=true;//�����߽�
			}else{
				g.setColor(Color.BLUE);//���õ�������ɫ
				g.fillOval(x,y,40,40);//������ָ�����ο��Բ
				//g.drawImage(offScreenImage,0,0,null);
			}
			while(pause){
				onPause();
			}
			try {
				Thread.sleep(100);//����һ��ʱ��
			}catch(Exception e) {
				//�����쳣
			}
			if(exit){//�����������뵼����ײ�󣬵����Ĳ���
				g.setColor(Color.WHITE);//���õ�������ɫ
				g.fillOval(x,y,40,40);//������ָ�����ο��Բ
				//g.drawImage(offScreenImage,0,0,null);
			}
		}
		
	}
}

class RightBall extends Thread{//�̳�Thread��ʵ�ִӴ���������ֵĵ����Ļ��ơ��ƶ�
	JPanel RIGHTPANEL;//�Ӵ���������ֵĵ��������
	public volatile boolean exit=false;//���ڽ����߳�
	private final Object lock = new Object();//������ͣ�߳�
	private boolean pause=false;//������ͣ�߳�
	int x;//�����ĺ�����
	int maxY;//���㻭����ʱY����������
	int y;//������������
	static int x_increase=-5;//�����ƶ�������
	RightBall(JPanel j){//���캯��
		RIGHTPANEL=j;
		maxY=(int)RIGHTPANEL.getBounds().getHeight()-40;//���㻭����ʱY����������
		x=(int)RIGHTPANEL.getBounds().getWidth();
		Random rand=new Random();
		y=rand.nextInt(maxY-60)+40;//�������������ֵ
	}
	void pauseThread(){
        		pause=true;
	}
    	void resumeThread(){//�����߳�
        		pause=false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){//�����߳�
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){  //�̳�Thread��ʵ�ֵķ���
		Graphics g= RIGHTPANEL.getGraphics();//���ͼ��
		while(!exit) {//��־�������˳��߳�
			while(pause){//������ͣ
				onPause();
			}
			//ѭ���ƶ�����
			g.setColor(Color.WHITE);//����һ�λ��ĵ�������
			g.fillOval(x,y,40,40);//������ָ�����ο��Բ
			x=x+x_increase;//����ÿ��X���λ��
			if(x<=0){//�ж��Ƿ�������Ե
				exit=true;
			}else{
				g.setColor(Color.BLUE);//���õ�������ɫ
				g.fillOval(x,y,40,40);//������ָ�����ο��Բ
			}
			while(pause){
				onPause();
			}
			try {
				Thread.sleep(100);//����һ��ʱ��
			}catch(Exception e) {
				//�����쳣
			}
			if(exit){//�����������͵�����ײ�󡣵����Ĳ���
				g.setColor(Color.WHITE);//���õ�������ɫ
				g.fillOval(x,y,40,40);//������ָ�����ο��Բ
			}
		}
		
	}
}

class YBall extends Thread{//�̳�Thread��ʵ���������Ļ��ƺ��ƶ�
	JPanel YPANEL;//�Ӵ���������ֵ������������
	public volatile boolean exit=false;//��־�����ڽ����߳�
	private final Object lock = new Object();//������ͣ�߳�
	private boolean pause=false;//��־��������ͣ�߳�
	static int y_increase=-10;//�������ƶ�������
	int x;//������������
	int maxY;//���㻭������ʱY����������
	int y;//������������
	YBall(JPanel j){//���캯��
		YPANEL=j;
		x=(int)(YPANEL.getBounds().getWidth()/2.0)-2;
		maxY=(int)YPANEL.getBounds().getHeight();
		y=maxY;
	}
	void pauseThread(){//������ͣ�߳�
        		pause=true;
	}
    	void resumeThread(){//���ڻ����߳�
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){//������ͣ�߳�
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){  //�̳�Thread��ʵ�ֵķ���
		Graphics g=YPANEL.getGraphics();//���ͼ��
		while(!exit) {
			while(pause){//������ͣ�߳�
				onPause();
			}
			//ѭ���ƶ�������
			g.setColor(Color.WHITE);//����һ�λ�������������
			g.fillOval(x,y,30,30);//������ָ�����ο��Բ
			y=y+y_increase;//������ÿ��y���λ��
			if(y<=0){//�ж��Ƿ������߽�
				exit=true;
			}else{
				g.setColor(Color.RED);//��������������ɫ
				g.fillOval(x,y,30,30);//������ָ�����ο��Բ
			}
			try {
				Thread.sleep(100);//����һ��ʱ��
			}catch(Exception e) {
				//�����쳣
			}
			if(exit){
				g.setColor(Color.WHITE);//��������������ɫ
				g.fillOval(x,y,30,30);//������ָ�����ο��Բ
			}
		}
		
	}
}

class BallRun extends Thread{//���ֵ����̵߳Ĵ���
	JPanel area1;
	private final Object lock = new Object();//������ͣ�߳�
	boolean pause=false;//������ͣ�߳�
	static ArrayList<RightBall> rlist=new ArrayList<RightBall>();//���ҳ��ֵĵ����߳��б�
	static ArrayList<LeftBall> llist=new ArrayList<LeftBall>();//������ֵĵ����߳��б�
	BallRun(JPanel j){
		area1=j;
	}
	void pauseThread(){//������ͣ�߳�
        		pause=true;
	}
    	void resumeThread(){//�����߳�
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
		for(int i=0;i<rlist.size();i++){
			rlist.get(i).resumeThread();
		}
		for(int i=0;i<llist.size();i++){
			llist.get(i).resumeThread();
		}
    	}
	void onPause(){//�����߳�
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){
	Random random=new Random();
	while(true){
		while(pause){//��ͣ�߳�
			for(int i=0;i<rlist.size();i++){//ע���ʱҪ�����б���ÿ�������̶߳���ͣ
				rlist.get(i).pauseThread();
			}
			for(int i=0;i<llist.size();i++){//ע���ʱҪ�����б���ÿ�������̶߳���ͣ
				llist.get(i).pauseThread();
			}
			onPause();
		}
		int z=random.nextInt(2);//����һ���������ʹ������������߽��룬�����ȡֵ��ΧΪ1,0
		if(z==1){//Ϊ1����������ҳ��ֵĵ���
			RightBall r=new RightBall(area1);//ʵ�����ұߵĵ�������
			rlist.add(r);
			r.start();//�����߳�
			try{
				r.sleep(2000);
			}catch(Exception e1){
			}
		}else {//Ϊ0�������������ֵĵ���
			LeftBall ball=new LeftBall(area1);//ʵ������ߵĵ�������
			llist.add(ball);
			ball.start();//�����߳�
			try{
				ball.sleep(2000);
			}catch(Exception e2){
			}
		}
		while(pause){
			for(int i=0;i<rlist.size();i++){
				rlist.get(i).pauseThread();
			}
			for(int i=0;i<llist.size();i++){
				llist.get(i).pauseThread();
			}
			onPause();
		}
	}
	}
}

class TimeCount extends Thread{//��ʱ���߳�
	private final Object lock = new Object();//������ͣ�߳�
	boolean pause=false;//������ͣ�߳�
	JTextField tf;
	long start;//��ʼʱ��
	long current;//��ǰʱ��
	long time;//��ʱ
	TimeCount(long s,JTextField t1){//���캯��
		tf=t1;
		start=s;//��һ�ΰ��¿�ʼ��ťʱ������ǰʱ�䴫�룬��Ϊ��ʼʱ��
	}
	void pauseThread(){
        		pause=true;
	}
    	void resumeThread(){//�����߳�
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){
        		synchronized(lock){//�����߳�
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){
		while(true){
			current=System.currentTimeMillis();
			time=(current-start)/1000;
			while(pause){
				onPause();
				start+=System.currentTimeMillis()-current;//��ͣʱ������start,��֤time���̻߳���ʱ�ܹ�������֮ͣǰ��ʱ����м�ʱ
			}
			//System.out.println(start);
			tf.setText(String.valueOf(time));//��ʱ�����뵽�ı�����
			try{
				sleep(1000);
			}catch(Exception e2){
			}
		}
	}
}

class ImpactCount extends Thread{//��¼��ײ�����߳�
	private final Object lock = new Object();//������ͣ�߳�
	boolean pause=false;//��־��������ͣ�߳�
	boolean exit=false;//��־�����ڽ����߳�
	ArrayList<RightBall> rlist=new ArrayList<RightBall>();//���ҳ��ֵĵ������б�
	ArrayList<LeftBall> llist=new ArrayList<LeftBall>();//������ֵĵ������б�
	ArrayList<YBall> ylist=new ArrayList<YBall>();//�������б�
	JTextField tf;//�ı������
	JButton button;//��ť����
	static int count;//����
	int sum;//�ѷ��������������
	ImpactCount(JTextField t2,ArrayList<YBall> ylist,int j,JButton button3){//���캯��
		this.rlist=BallRun.rlist;
		this.llist=BallRun.llist;
		this.ylist=ylist;
		tf=t2;
		button=button3;
		sum=j;
	}
	void pauseThread(){
        		pause=true;
	}
    	void resumeThread(){//�����߳�
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){//�����߳�
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){
		while(!exit){
			while(pause){
				onPause();
			}
			if(ylist.size()==10){//��ǰ��10�������ƶ���������ʱ�������ٷ���������
				button.setEnabled(false);
			}
			if(ylist.size()<10){//��ǰ����10�������ƶ���������ʱ�����ٷ���������
				button.setEnabled(true);
			}
			for(int i=0;i<ylist.size();i++){//�������߽�ʱ���̴߳��б����Ƴ�
				if(ylist.get(i).y<=0){
					ylist.remove(i);
				}
			}
			for(int i=0;i<ylist.size();i++){//�����������б��뵼�����б��ж϶����Ƿ���ײ
				for(int j=0;j<rlist.size();j++){
					if(((ylist.get(i).x+15)-(rlist.get(j).x+20))*((ylist.get(i).x+15)-(rlist.get(j).x+20))+((ylist.get(i).y+15)-(rlist.get(j).y+20))*((ylist.get(i).y+15)-(rlist.get(j).y+20))<=1600){
						ylist.get(i).exit=true;//������ǰ�������߳�
						rlist.get(j).exit=true;//������ǰ�������߳�
						ylist.remove(i);//�б����Ƴ�
						rlist.remove(j);
						count++;//��ײ��������
						tf.setText(String.valueOf(count)+"/"+String.valueOf(sum));//����ײ�����͵�ǰ����������������ı�����
						exit=false;
					}
				}
			}
			for(int i=0;i<ylist.size();i++){
				for(int k=0;k<llist.size();k++){
					if(((ylist.get(i).x+15)-(llist.get(k).x+20))*((ylist.get(i).x+15)-(llist.get(k).x+20))+((ylist.get(i).y+15)-(llist.get(k).y+20))*((ylist.get(i).y+15)-(llist.get(k).y+20))<=1600){
						ylist.get(i).exit=true;
						llist.get(k).exit=true;
						ylist.remove(i);
						llist.remove(k);
						count++;
						tf.setText(String.valueOf(count)+"/"+String.valueOf(sum));
						exit=false;
					}
				}
			}
			if(ylist.size()<10){
				button.setEnabled(true);
			}
			tf.setText(String.valueOf(count)+"/"+String.valueOf(sum));
			if(ylist.size()==10){
				button.setEnabled(false);
			}
			try{
				sleep(100);
			}catch(Exception e2){
			}
		}
	}
}

public class RedFlag extends JFrame{//������
	int b=1;
	private class MyCanvas extends Canvas{
		public void paint(Graphics g){
			Graphics2D g2=(Graphics2D)g;
			setBackground(Color.WHITE);//���ñ���
		}
	} 
	JLabel l1=new JLabel("��ʱ(��):");
	JTextField t1=new JTextField("0");
	JButton button1=new JButton("��ʼ");
	JButton button2=new JButton("����");
	JButton button3=new JButton("����");
	JButton button4=new JButton("����");
	JButton button5=new JButton("ֹͣ");
	JLabel l2=new JLabel("����(��):");
	JTextField t2=new JTextField("0/0");
	JPanel area1=new JPanel();
	TimeCount  t;
	ImpactCount p;
	int i;
	int j;
	RedFlag(){
		BallRun b=new BallRun(area1);
		ArrayList<YBall> ylist=new ArrayList<YBall>();
		button4.setEnabled(false);
		button5.setEnabled(false);
		button1.addActionListener(new ActionListener(){//��ʼ
			public void actionPerformed(ActionEvent e){
				if(!b.pause){//��ֹͣ���ٴΰ��¿�ʼ
					long s=System.currentTimeMillis();
					b.start();
					t=new TimeCount(s,t1);
					t.start();
					button5.setEnabled(true);
					button1.setEnabled(false);
				}else{//ֹͣ���ٴΰ��¿�ʼ
					b.resumeThread();
					for(int i=0;i<ylist.size();i++){
						ylist.get(i).resumeThread();//���������������߳�
					}
					t.resumeThread();
					button5.setEnabled(true);
					button1.setEnabled(false);
					button3.setEnabled(true);
				}
			}
		});
		button2.addActionListener(new ActionListener(){//����
			public void actionPerformed(ActionEvent e){
				i++;
				if(i>=1&&i<=5){
					RightBall.x_increase-=5;
					LeftBall.x_increase+=5;
					YBall.y_increase-=10;
					button4.setEnabled(true);
				}
				if(i==5){
					button2.setEnabled(false);
				}
			}
		});
		button3.addActionListener(new ActionListener(){//����
			public void actionPerformed(ActionEvent e){
				j++;
				if(j>1){//�����ͻ�������µļ�¼�������̲߳���ʱ������һ����¼�߳̽���
					p.exit=true;
				}
				YBall yb=new YBall(area1);//ʵ��������������
				ylist.add(yb);
				yb.start();//�����߳�
				p=new ImpactCount(t2,ylist,j,button3);//��¼��ײ�������߳�
				p.start();
			}
		});
		button4.addActionListener(new ActionListener(){//����
			public void actionPerformed(ActionEvent e){
				if(i>=1&&i<=5){
					i--;
					RightBall.x_increase+=5;
					LeftBall.x_increase-=5;
					YBall.y_increase+=10;
					if(i==0){
						button4.setEnabled(false);
					}
				}
				if(i<5){
					button2.setEnabled(true);
				}			
			}
		});
		button5.addActionListener(new ActionListener(){//ֹͣ
			public void actionPerformed(ActionEvent e){
				b.pauseThread();
				for(int i=0;i<ylist.size();i++){
					ylist.get(i).pauseThread();
				}
				t.pauseThread();
				button5.setEnabled(false);
				button1.setEnabled(true);
				button3.setEnabled(false);
			}
		});
		area1.setBackground(Color.WHITE);
		Font font=new Font("����",Font.BOLD,20);
		Font font2=new Font("����",Font.BOLD,15);
		JPanel south=new JPanel();
		south.setLayout(new FlowLayout(FlowLayout.CENTER,25,0));
		l1.setPreferredSize(new Dimension(75, 40));
		l1.setFont(font2);
		south.add(l1);
		t1.setFont(font);
		t1.setForeground(Color.gray);
		t1.setPreferredSize(new Dimension(100, 40));
		t1.setEditable(false);
		south.add(t1);
		button1.setPreferredSize(new Dimension(100, 40));
		button1.setFont(font);
		south.add(button1);
		button2.setPreferredSize(new Dimension(100, 40));
		button2.setFont(font);
		south.add(button2);
		button3.setPreferredSize(new Dimension(200, 40));
		button3.setFont(font);
		south.add(button3);
		button4.setPreferredSize(new Dimension(100, 40));
		button4.setFont(font);
		south.add(button4);
		button5.setPreferredSize(new Dimension(100, 40));
		button5.setFont(font);
		south.add(button5);
		l2.setPreferredSize(new Dimension(75, 40));
		l2.setFont(font2);
		south.add(l2);
		t2.setPreferredSize(new Dimension(100, 40));
		t2.setFont(font);
		t2.setForeground(Color.gray);
		t2.setEditable(false);
		south.add(t2);
		setTitle("������Ϸ");
		setSize(1200,700);
		add(area1,BorderLayout.CENTER);
		add(south,BorderLayout.SOUTH);
		setVisible(true);		        
     		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args){
		new RedFlag();
	}
}