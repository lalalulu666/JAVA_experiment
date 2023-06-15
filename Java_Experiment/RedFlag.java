import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.script.*;
import java.util.*;


class LeftBall extends Thread{//继承Thread类实现从窗口左面出现的导弹的绘制、移动
	JPanel LEFTPANEL;//从窗口左面出现的导弹的面板
	public volatile boolean exit=false;//用于结束线程
	private final Object lock = new Object();//用于暂停线程
	private boolean pause=false;//用于暂停线程
	int x;//导弹的横坐标
	static int x_increase=5;//导弹移动的增量
	int maxY;//y的最大取值
	int y;//导弹的纵坐标
	//Image offScreenImage;
	LeftBall(JPanel j){//构造函数
		LEFTPANEL=j;
		x=0;
		maxY=(int)LEFTPANEL.getBounds().getHeight()-40;//计算画导弹时Y轴的最大坐标
		Random rand=new Random();
		y=rand.nextInt(maxY-60)+40;//在0~maxY-19范围内随机生成y坐标
		//offScreenImage=LEFTPANEL.createImage((int)LEFTPANEL.getBounds().getWidth(),(int)LEFTPANEL.getBounds().getHeight());
	}
	void pauseThread(){
        		pause=true;
	}
    	void resumeThread(){//唤醒线程
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause() {//阻塞线程
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){  //继承Thread类实现的方法
		Graphics g= LEFTPANEL.getGraphics();//获得图形
		while(!exit) {//设置标志，来退出程序
			while(pause){
				onPause();
			}
			//循环移动导弹
			g.setColor(Color.WHITE);//将上一次画的导弹擦掉
			g.fillOval(x,y,40,40);//填充外接指定矩形框的圆
			//g.drawImage(offScreenImage,0,0,null);
			x=x+x_increase;//导弹每次X轴的位置
			if(x>=(int)LEFTPANEL.getBounds().getWidth()-40){
				exit=true;//碰到边界
			}else{
				g.setColor(Color.BLUE);//设置导弹的颜色
				g.fillOval(x,y,40,40);//填充外接指定矩形框的圆
				//g.drawImage(offScreenImage,0,0,null);
			}
			while(pause){
				onPause();
			}
			try {
				Thread.sleep(100);//休眠一段时间
			}catch(Exception e) {
				//捕获异常
			}
			if(exit){//用于拦截器与导弹碰撞后，导弹的擦除
				g.setColor(Color.WHITE);//设置导弹的颜色
				g.fillOval(x,y,40,40);//填充外接指定矩形框的圆
				//g.drawImage(offScreenImage,0,0,null);
			}
		}
		
	}
}

class RightBall extends Thread{//继承Thread类实现从窗口右面出现的导弹的绘制、移动
	JPanel RIGHTPANEL;//从窗口右面出现的导弹的面板
	public volatile boolean exit=false;//用于结束线程
	private final Object lock = new Object();//用于暂停线程
	private boolean pause=false;//用于暂停线程
	int x;//导弹的横坐标
	int maxY;//计算画导弹时Y轴的最大坐标
	int y;//导弹的纵坐标
	static int x_increase=-5;//导弹移动的增量
	RightBall(JPanel j){//构造函数
		RIGHTPANEL=j;
		maxY=(int)RIGHTPANEL.getBounds().getHeight()-40;//计算画导弹时Y轴的最大坐标
		x=(int)RIGHTPANEL.getBounds().getWidth();
		Random rand=new Random();
		y=rand.nextInt(maxY-60)+40;//随机产生纵坐标值
	}
	void pauseThread(){
        		pause=true;
	}
    	void resumeThread(){//唤醒线程
        		pause=false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){//阻塞线程
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){  //继承Thread类实现的方法
		Graphics g= RIGHTPANEL.getGraphics();//获得图形
		while(!exit) {//标志，用于退出线程
			while(pause){//用于暂停
				onPause();
			}
			//循环移动导弹
			g.setColor(Color.WHITE);//将上一次画的导弹擦掉
			g.fillOval(x,y,40,40);//填充外接指定矩形框的圆
			x=x+x_increase;//导弹每次X轴的位置
			if(x<=0){//判断是否碰触边缘
				exit=true;
			}else{
				g.setColor(Color.BLUE);//设置导弹的颜色
				g.fillOval(x,y,40,40);//填充外接指定矩形框的圆
			}
			while(pause){
				onPause();
			}
			try {
				Thread.sleep(100);//休眠一段时间
			}catch(Exception e) {
				//捕获异常
			}
			if(exit){//用于拦截器和导弹碰撞后。导弹的擦除
				g.setColor(Color.WHITE);//设置导弹的颜色
				g.fillOval(x,y,40,40);//填充外接指定矩形框的圆
			}
		}
		
	}
}

class YBall extends Thread{//继承Thread类实现拦截器的绘制和移动
	JPanel YPANEL;//从窗口右面出现的拦截器的面板
	public volatile boolean exit=false;//标志，用于结束线程
	private final Object lock = new Object();//用于暂停线程
	private boolean pause=false;//标志，用于暂停线程
	static int y_increase=-10;//拦截器移动的增量
	int x;//拦截器横坐标
	int maxY;//计算画拦截器时Y轴的最大坐标
	int y;//拦截器纵坐标
	YBall(JPanel j){//构造函数
		YPANEL=j;
		x=(int)(YPANEL.getBounds().getWidth()/2.0)-2;
		maxY=(int)YPANEL.getBounds().getHeight();
		y=maxY;
	}
	void pauseThread(){//用于暂停线程
        		pause=true;
	}
    	void resumeThread(){//用于唤醒线程
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){//用于暂停线程
        		synchronized(lock){
           			try{
                			lock.wait();
            			}catch (InterruptedException e3){
                			e3.printStackTrace();
            			}
        		}
    	}
	public void run(){  //继承Thread类实现的方法
		Graphics g=YPANEL.getGraphics();//获得图形
		while(!exit) {
			while(pause){//用于暂停线程
				onPause();
			}
			//循环移动拦截器
			g.setColor(Color.WHITE);//将上一次画的拦截器擦掉
			g.fillOval(x,y,30,30);//填充外接指定矩形框的圆
			y=y+y_increase;//拦截器每次y轴的位置
			if(y<=0){//判断是否碰触边界
				exit=true;
			}else{
				g.setColor(Color.RED);//设置拦截器的颜色
				g.fillOval(x,y,30,30);//填充外接指定矩形框的圆
			}
			try {
				Thread.sleep(100);//休眠一段时间
			}catch(Exception e) {
				//捕获异常
			}
			if(exit){
				g.setColor(Color.WHITE);//设置拦截器的颜色
				g.fillOval(x,y,30,30);//填充外接指定矩形框的圆
			}
		}
		
	}
}

class BallRun extends Thread{//两种导弹线程的创建
	JPanel area1;
	private final Object lock = new Object();//用于暂停线程
	boolean pause=false;//用于暂停线程
	static ArrayList<RightBall> rlist=new ArrayList<RightBall>();//从右出现的导弹线程列表
	static ArrayList<LeftBall> llist=new ArrayList<LeftBall>();//从左出现的导弹线程列表
	BallRun(JPanel j){
		area1=j;
	}
	void pauseThread(){//用于暂停线程
        		pause=true;
	}
    	void resumeThread(){//唤醒线程
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
	void onPause(){//阻塞线程
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
		while(pause){//暂停线程
			for(int i=0;i<rlist.size();i++){//注意此时要遍历列表，将每个导弹线程都暂停
				rlist.get(i).pauseThread();
			}
			for(int i=0;i<llist.size();i++){//注意此时要遍历列表，将每个导弹线程都暂停
				llist.get(i).pauseThread();
			}
			onPause();
		}
		int z=random.nextInt(2);//产生一个随机数，使导弹随机从两边进入，随机数取值范围为1,0
		if(z==1){//为1，则产生从右出现的导弹
			RightBall r=new RightBall(area1);//实例化右边的导弹对象
			rlist.add(r);
			r.start();//启动线程
			try{
				r.sleep(2000);
			}catch(Exception e1){
			}
		}else {//为0，则产生从左出现的导弹
			LeftBall ball=new LeftBall(area1);//实例化左边的导弹对象
			llist.add(ball);
			ball.start();//启动线程
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

class TimeCount extends Thread{//计时器线程
	private final Object lock = new Object();//用于暂停线程
	boolean pause=false;//用于暂停线程
	JTextField tf;
	long start;//开始时间
	long current;//当前时间
	long time;//用时
	TimeCount(long s,JTextField t1){//构造函数
		tf=t1;
		start=s;//第一次按下开始按钮时，将当前时间传入，作为开始时间
	}
	void pauseThread(){
        		pause=true;
	}
    	void resumeThread(){//唤醒线程
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){
        		synchronized(lock){//阻塞线程
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
				start+=System.currentTimeMillis()-current;//暂停时，更改start,保证time在线程唤醒时能够继续暂停之前的时间进行计时
			}
			//System.out.println(start);
			tf.setText(String.valueOf(time));//将时间输入到文本框中
			try{
				sleep(1000);
			}catch(Exception e2){
			}
		}
	}
}

class ImpactCount extends Thread{//记录碰撞次数线程
	private final Object lock = new Object();//用于暂停线程
	boolean pause=false;//标志，用于暂停线程
	boolean exit=false;//标志，用于结束线程
	ArrayList<RightBall> rlist=new ArrayList<RightBall>();//从右出现的导弹的列表
	ArrayList<LeftBall> llist=new ArrayList<LeftBall>();//从左出现的导弹的列表
	ArrayList<YBall> ylist=new ArrayList<YBall>();//拦截器列表
	JTextField tf;//文本框对象
	JButton button;//按钮对象
	static int count;//次数
	int sum;//已发射的拦截器总数
	ImpactCount(JTextField t2,ArrayList<YBall> ylist,int j,JButton button3){//构造函数
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
    	void resumeThread(){//唤醒线程
        		pause =false;
        		synchronized(lock){
           	 		lock.notify();
        		}
    	}
	void onPause(){//阻塞线程
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
			if(ylist.size()==10){//当前有10个正在移动的拦截器时，不可再发射拦截器
				button.setEnabled(false);
			}
			if(ylist.size()<10){//当前少于10个正在移动的拦截器时，可再发射拦截器
				button.setEnabled(true);
			}
			for(int i=0;i<ylist.size();i++){//当碰触边界时，线程从列表中移除
				if(ylist.get(i).y<=0){
					ylist.remove(i);
				}
			}
			for(int i=0;i<ylist.size();i++){//遍历拦截器列表与导弹的列表，判断二者是否碰撞
				for(int j=0;j<rlist.size();j++){
					if(((ylist.get(i).x+15)-(rlist.get(j).x+20))*((ylist.get(i).x+15)-(rlist.get(j).x+20))+((ylist.get(i).y+15)-(rlist.get(j).y+20))*((ylist.get(i).y+15)-(rlist.get(j).y+20))<=1600){
						ylist.get(i).exit=true;//结束当前拦截器线程
						rlist.get(j).exit=true;//结束当前导弹的线程
						ylist.remove(i);//列表中移除
						rlist.remove(j);
						count++;//碰撞次数增加
						tf.setText(String.valueOf(count)+"/"+String.valueOf(sum));//将碰撞次数和当前拦截器总数输出到文本框中
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

public class RedFlag extends JFrame{//主窗口
	int b=1;
	private class MyCanvas extends Canvas{
		public void paint(Graphics g){
			Graphics2D g2=(Graphics2D)g;
			setBackground(Color.WHITE);//设置背景
		}
	} 
	JLabel l1=new JLabel("计时(秒):");
	JTextField t1=new JTextField("0");
	JButton button1=new JButton("开始");
	JButton button2=new JButton("加速");
	JButton button3=new JButton("拦截");
	JButton button4=new JButton("减速");
	JButton button5=new JButton("停止");
	JLabel l2=new JLabel("计数(次):");
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
		button1.addActionListener(new ActionListener(){//开始
			public void actionPerformed(ActionEvent e){
				if(!b.pause){//非停止后再次按下开始
					long s=System.currentTimeMillis();
					b.start();
					t=new TimeCount(s,t1);
					t.start();
					button5.setEnabled(true);
					button1.setEnabled(false);
				}else{//停止后再次按下开始
					b.resumeThread();
					for(int i=0;i<ylist.size();i++){
						ylist.get(i).resumeThread();//唤醒所有拦截器线程
					}
					t.resumeThread();
					button5.setEnabled(true);
					button1.setEnabled(false);
					button3.setEnabled(true);
				}
			}
		});
		button2.addActionListener(new ActionListener(){//加速
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
		button3.addActionListener(new ActionListener(){//拦截
			public void actionPerformed(ActionEvent e){
				j++;
				if(j>1){//避免冲突，当有新的记录次数的线程产生时，将上一个记录线程结束
					p.exit=true;
				}
				YBall yb=new YBall(area1);//实例化拦截器对象
				ylist.add(yb);
				yb.start();//启动线程
				p=new ImpactCount(t2,ylist,j,button3);//记录碰撞次数的线程
				p.start();
			}
		});
		button4.addActionListener(new ActionListener(){//减速
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
		button5.addActionListener(new ActionListener(){//停止
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
		Font font=new Font("宋体",Font.BOLD,20);
		Font font2=new Font("宋体",Font.BOLD,15);
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
		setTitle("红旗游戏");
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