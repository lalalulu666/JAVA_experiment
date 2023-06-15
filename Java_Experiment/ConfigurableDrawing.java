import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.script.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

public class ConfigurableDrawing extends JFrame{
	static File f=new File("p.xml");//创建文件对象
	public Color getColor(String c,int r){//通过RGB或颜色名称，获取颜色
		c=c.replaceAll(" ","");//去掉多余的空格
		if(c.contains(",")){//如果为RGB格式
			int[] num=new int[3];
			String[] array=c.split(",");//将字符串通过逗号分隔
			for(int i=0;i<array.length;i++){
				num[i]=Integer.valueOf(array[i]);
			}
			Color col=new Color(num[0],num[1],num[2]);
			return col;
		}else{//如果为颜色名称
			if(c.equals("black")) return Color.BLACK;
			if(c.equals("blue")) return Color.BLUE;
			if(c.equals("cyan")) return Color.CYAN;
			if(c.equals("gray")) return Color.GRAY;
			if(c.equals("green")) return Color.GREEN;
			if(c.equals("orange")) return Color.ORANGE;
			if(c.equals("pink")) return Color.PINK;
			if(c.equals("red")) return Color.RED;
			if(c.equals("white")) return Color.WHITE;
			if(c.equals("yellow")) return Color.YELLOW;
			if(r==1){
				return Color.WHITE;
			}else{
				return Color.BLACK;
			}
		}
	}
	public  Double[] getNum(String s){//将字符串转换为数组
		Double[] num=new Double[100];
		s=s.replaceAll(" ","");
		String[] array=s.split(",");
		for(int i=0;i<array.length;i++){
			num[i]=Double.valueOf(array[i]);
		}
		return num;
	}
	private class MyCanvas extends Canvas{//继承Canvas类
		public void paint(Graphics g){
			Graphics2D g2=(Graphics2D)g;//画笔对象
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//去除锯齿
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//去除锯齿
			g2.clearRect(0, 0, (int)getBounds().getWidth(), (int)getBounds().getHeight());//清屏
			try{
				if(f.exists()){//如果文件存在
   	 			SAXReader reader=new SAXReader();//创建xml读取对象
    				Document document=reader.read(new FileInputStream("p.xml"));//读取要解析的xml文件
    				Element root=document.getRootElement();//获取根节点
				//画布
    				Element bg=root.element("bg");
				String color1=bg.elementText("col");
				this.setBackground(getColor(color1,1));//设置背景色
				String xRange=bg.elementText("xRange");
				String yRange=bg.elementText("yRange");
				Double a=getBounds().getWidth();//获取当前画布大小
				Double b=getBounds().getHeight();
				Double[] tmp1=getNum(xRange);
				Double[] tmp2=getNum(yRange);
				Double ta=tmp1[1]-tmp1[0];//获取目标画布大小
				Double tb=tmp2[1]-tmp2[0];
				Double extenda=a/ta;//获取横纵坐标需要扩展的倍数，坐标转换方式：横坐标:(x-tmp1[0])*extenda,纵坐标:b-(y-tmp2[0])*extendb
				Double extendb=b/tb;
				//画布
				//点
				List<Element> point=root.elements("points");
				for(int i=0;i<point.size();i++){//遍历点组列表
					Element p=point.get(i);
					String pad=p.elementText("pad").replaceAll(" ","");
					String color=p.elementText("col");
					g2.setColor(getColor(color,0));//设置画笔颜色
					String width=p.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//设置默认画笔粗细
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//设置画笔粗细
					}
					String radius=p.elementText("radius").replaceAll(" ","");
					Double r=Double.valueOf(radius);//获取点的半径
					String list=p.elementText("list");
					String[] plist=list.split("\n");//获取点的列表
					for(int j=1;j<plist.length;j++){//遍历点的列表
						Double[] d1=getNum(plist[j].replaceAll(" ",""));
						Ellipse2D ellipse=new Ellipse2D.Double((d1[0]-tmp1[0])*extenda-r,b-(d1[1]-tmp2[0])*extendb-r,2*r,2*r);//以画圆的方式画点
						g2.draw(ellipse);
						if(pad.equals("true")){//如果pad属性为真，则要画实心点，就要填满圆
							g2.fill(ellipse);
						}
					}
				}
				//点
				//连线
				List<Element> lines=root.elements("lines");
				for(int i=0;i<lines.size();i++){
					Element l=lines.get(i);
					String color=l.elementText("col");
					g2.setColor(getColor(color,0));//设置画笔颜色
					String width=l.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//设置默认画笔粗细
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//设置画笔粗细
					}
					String list1=l.elementText("list").replaceAll(" ","");
					String[] slist=list1.split("\n");//获取点的列表
					for(int j=1;j<slist.length-1;j++){
						Double[] d1=getNum(slist[j].replaceAll(" ",""));//获取当前点
						Double[] d2=getNum(slist[j+1].replaceAll(" ",""));//获取下一个点
						Line2D l1=new Line2D.Double((d1[0]-tmp1[0])*extenda,b-(d1[1]-tmp2[0])*extendb,(d2[0]-tmp1[0])*extenda,b-(d2[1]-tmp2[0])*extendb);//连接当前点和下一个点
						g2.draw(l1);//画线
					}
				}
				//连线
				//直线
				List<Element> line=root.elements("line");
				for(int i=0;i<line.size();i++){
					Element l=line.get(i);
					String color=l.elementText("col").replaceAll(" ","");
					g2.setColor(getColor(color,0));//设置画笔颜色
					String width=l.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//设置默认画笔粗细
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//设置画笔粗细
					}
					String point1=l.elementText("point").replaceAll(" ","");
					Double[] n=getNum(point1);//获取必经点
					String slope=l.elementText("slope").replaceAll(" ","");
					if(slope.equals("inf")){//平行于y轴的直线
						Line2D line1=new Line2D.Double((n[0]-tmp1[0])*extenda,b,(n[0]-tmp1[0])*extenda,0);//横坐标与必经点横坐标相等，纵坐标为0和b
						g2.draw(line1);//画线
					}else{
						Double dtmp=(-1)*Double.valueOf(slope);//斜率
						if(dtmp==0){//如果斜率为0
							Line2D line1=new Line2D.Double(0,b-(n[1]-tmp2[0])*extendb,a,b-(n[1]-tmp2[0])*extendb);//纵坐标与必经点纵坐标相等
							g2.draw(line1);//画线
						}else{
							Double c=b-(n[1]-tmp2[0])*extendb-dtmp*(n[0]-tmp1[0])*extenda;//偏移量
							Line2D line1=new Line2D.Double((-c)/dtmp,0,(b-c)/dtmp,b);//纵坐标给定0和b，求出横坐标
							g2.draw(line1);//画线
						}
					}
				}
				//直线
				//曲线
				List<Element> curve=root.elements("curve");
				for(int i=0;i<curve.size();i++){
					Element c=curve.get(i);
					String color=c.elementText("col").replaceAll(" ","");
					g2.setColor(getColor(color,0));//设置画笔颜色
					String width=c.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//设置默认画笔粗细
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//设置画笔粗细
					}
					String range=c.elementText("range").replaceAll(" ","");
					Double[] num=getNum(range);//曲线上点横坐标范围
					String amount=c.elementText("amount").replaceAll(" ","");
					Double am=500.0;//默认自变量分割
					if(!amount.equals("")){
						am=Double.valueOf(amount);//自变量分割
					}
					String function=c.elementText("function").replaceAll(" ","");
					 try {
					ScriptEngineManager manager=new ScriptEngineManager();
					ScriptEngine engine=manager.getEngineByName("js");//创建Manager和engine对象解析曲线函数
					for(double j=num[0];j<=num[1];j+=(num[1]-num[0])/am){//遍历范围内横坐标
					engine.put("x",j);//将横坐标带入方程
					Object r1=engine.eval(function);//获得结果
					Line2D l=new Line2D.Double((j-tmp1[0])*extenda,b-((double)r1-tmp2[0])*extendb,(j-tmp1[0])*extenda,b-((double)r1-tmp2[0])*extendb);//创建点对象
					g2.draw(l);//画点
					}
					}catch (final ScriptException fe) { fe.printStackTrace(); }
				}
				//曲线
				//矩形和椭圆
				List<Element> shape=root.elements("shape");
				for(int i=0;i<shape.size();i++){
					Element s=shape.get(i);
					String color=s.elementText("col");
					g2.setColor(getColor(color,0));//设置画笔颜色
					String wid=s.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//设置默认画笔粗细
					if(!wid.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(wid));
					g2.setStroke(stroke1);//设置画笔粗细
					}
					String type=s.elementText("type").replaceAll(" ","");
					String pad=s.elementText("pad").replaceAll(" ","");
					String center=s.elementText("center").replaceAll(" ","");
					String width=s.elementText("width").replaceAll(" ","");
					String height=s.elementText("height").replaceAll(" ","");
					if(type.equals("oval")){//目标为绘制椭圆
						Double[] n=getNum(center);//得到图形中心点，注意绘制图形时要将其转换为图形左上角的点
						Ellipse2D ellipse=new Ellipse2D.Double((n[0]-Double.valueOf(width)/2-tmp1[0])*extenda,b-(n[1]+Double.valueOf(height)/2-tmp2[0])*extendb,Double.valueOf(width)*extenda,Double.valueOf(height)*extendb);
						g2.draw(ellipse);//绘制椭圆
						if(pad.equals("true")){
							g2.fill(ellipse);//如果目标是实心的，还要填满
						}
					}
					if(type.equals("rect")){//目标为绘制矩形
						Double[] n=getNum(center);//得到图形中心点，注意绘制图形时要将其转换为图形左上角的点
						Rectangle2D rec=new Rectangle2D.Double((n[0]-Double.valueOf(width)/2-tmp1[0])*extenda,b-(n[1]+Double.valueOf(height)/2-tmp2[0])*extendb,Double.valueOf(width)*extenda,Double.valueOf(height)*extendb);
						g2.draw(rec);//绘制矩形
						if(pad.equals("true")){
							g2.fill(rec);//如果目标是实心的，还要填满
						}
					}
				}
				//矩形和椭圆
				//坐标轴
				List<Element> scale=root.elements("scale");
				for(int i=0;i<scale.size();i++){
					Element s=scale.get(i);
					String color=s.elementText("col");
					g2.setColor(getColor(color,0));//设置画笔颜色
					String wid=s.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//设置默认画笔粗细
					if(!wid.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(wid));
					g2.setStroke(stroke1);//设置画笔粗细
					}
					String direction=s.elementText("direction").replaceAll(" ","");
					String pos=s.elementText("pos").replaceAll(" ","");
					String from=s.elementText("from").replaceAll(" ","");
					String step=s.elementText("step").replaceAll(" ","");
					String amount=s.elementText("amount").replaceAll(" ","");
					int am=10;
					if(!amount.equals("")){
						am=Integer.valueOf(amount);
					}
					String precision=s.elementText("precision").replaceAll(" ","");
					int h=Integer.valueOf(precision);
					float k=1;
					for(int m=0;m<h;m++){
						k=k*10f;//为坐标保留精度准备
					}
					Font font=new Font("宋体",Font.BOLD,12);//设置文字字体
					g2.setFont(font);
					if(direction.equals("x")){//如果绘制目标为x轴
						g2.drawString(String.valueOf(Math.round(Float.valueOf(from)*k)/k),(float)((Double.valueOf(from)-tmp1[0])*extenda-7),(float)(b-(Float.valueOf(pos)-tmp2[0])*extendb+10));//绘制原点坐标
						for(int j=0;j<am;j++){
							Line2D line1=new Line2D.Double((Double.valueOf(from)+Double.valueOf(step)*j-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb,(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb);//画线
							g2.draw(line1);
							g2.drawString(String.valueOf(Math.round((Float.valueOf(from)+Float.valueOf(step)*(j+1))*k)/k),(float)((Float.valueOf(from)+Float.valueOf(step)*(j+1)-tmp1[0])*extenda-7),(float)(b-(Float.valueOf(pos)-tmp2[0])*extendb)+10);//绘制坐标
							Line2D line2=new Line2D.Double((Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb,(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb-10);
							g2.draw(line2);//画刻度
						}
					}else if(direction.equals("y")){//如果绘制目标为y轴
						g2.drawString(String.valueOf(Math.round(Float.valueOf(from)*k)/k),(float)((Double.valueOf(pos)-tmp1[0])*extenda-20),(float)(b-(Float.valueOf(from)-tmp2[0])*extendb-7));//绘制原点坐标
						for(int j=0;j<am;j++){
							Line2D line1=new Line2D.Double((Double.valueOf(pos)-tmp1[0])*extenda,b-(Double.valueOf(from)+Double.valueOf(step)*j-tmp2[0])*extendb,(Double.valueOf(pos)-tmp1[0])*extenda,b-(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp2[0])*extendb);
							g2.draw(line1);//画线
							g2.drawString(String.valueOf(Math.round((Float.valueOf(from)+Float.valueOf(step)*(j+1))*k)/k),(float)((Float.valueOf(pos)-tmp1[0])*extenda-22),(float)(b-(Float.valueOf(from)+Float.valueOf(step)*(j+1)-tmp2[0])*extendb)-1);//绘制坐标
							Line2D line2=new Line2D.Double((Double.valueOf(pos)-tmp1[0])*extenda,b-(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp2[0])*extendb,(Double.valueOf(pos)-tmp1[0])*extenda+10,b-(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp2[0])*extendb);
							g2.draw(line2);//画刻度
						}
					}
				}
				//坐标轴
				}
			}catch(IOException ioe2){//异常处理
				ioe2.printStackTrace();
			}catch(DocumentException de){//异常处理
				de.printStackTrace();
			}
		}
		
	}
	String s="<paintings>\n"
		+"<bg>\n"+"<col> 255,255,255 </col>\n"+"<xRange> -0.1, 1.1 </xRange>\n"+"<yRange> -0.1, 1.1 </yRange>\n"+"</bg>\n"
		+"\n<points>\n"+"<pad> true </pad>\n"+"<col> red </col>\n"+"<wid> 2 </wid>\n"+"<radius> 6 </radius>\n"
		+"<list>\n"+"0.2, 0.2\n"+"0.2, 0.5\n"+"0.2, 0.8\n"+"0.5, 0.8\n"+"0.8, 0.8\n"+"0.8, 0.5\n"+"0.8, 0.2\n"+"0.5, 0.2\n"+"</list>\n"+"</points>\n"
		+"\n<lines>\n"+"<col> orange </col>\n"+"<wid> 3 </wid>\n"+"<list>\n"+"0.2, 0.2\n"+"0.2, 0.5\n"+"0.2, 0.8\n"+"0.5, 0.8\n"+"0.8, 0.8\n"
		+"0.8, 0.5\n"+"0.8, 0.2\n"+"0.5, 0.2\n"+"0.5, 0.5\n"+"</list>\n"+"</lines>\n"
		+"\n<line>\n"+"<col>  </col>\n"+"<wid> 1 </wid>\n"+"<point> 0.3, 0.3 </point>\n"+"<slope> inf </slope>\n"+"</line>\n"
		+"\n<curve>\n"+"<col> 0,0,255 </col>\n"+"<wid> 3 </wid>\n"+"<range> 0, 1 </range>\n"+"<amount> 500 </amount>\n"+"<function> Math.pow(x,2)-0.05 </function>\n"+"</curve>\n"
		+"\n<shape>\n"+"<col> 180,180,180 </col>\n"+"<wid> 1 </wid>\n"+"<type> oval </type>\n"+"<!-- type includes oval & rect -->\n"	+"<pad> true </pad>\n"
		+"<center> 0.6, 0.8 </center>\n"+"<width> 0.3 </width>\n"+"<height> 0.2 </height>\n"+"</shape>\n"
		+"\n<shape>\n"+"<col> 180,180,180 </col>\n"+"<wid> 5 </wid>\n"+"<type> rect </type>\n"+"<!-- type includes oval & rect -->\n"+"<pad> false </pad>\n"+"<center> 0.3, 0.4 </center>\n"
		+"<width> 0.3 </width>\n"+"<height> 0.2 </height>\n"+"</shape>\n"
		+"\n<scale>\n"+"<col> black </col>\n"+"<wid> 1 </wid>\n"+"<direction> x </direction>\n"+"<pos> 0.0 </pos>\n"+"<from> 0 </from>\n"
		+"<step> 0.1 </step>\n"+"<amount> 10 </amount>\n"+"<precision> 1 </precision>\n"+"</scale>\n"
		+"\n<scale>\n"+"<col> black </col>\n"+"<wid> 1 </wid>\n"+"<direction> y </direction>\n"+"<pos> 0 </pos>\n"+"<from> 0 </from>\n"
		+"<step> 0.1 </step>\n"+"<amount> 10 </amount>\n"+"<precision> 1 </precision>\n"+"</scale>\n"
		+"\n</paintings>";//将要配置的xml文件写成字符串
		
	JSplitPane splitPane;//容器对象
	JButton button1=new JButton("绘图");
	JButton button2=new JButton("配置样例");
	MyCanvas area1=new MyCanvas();//创建画布对象
	JTextArea area2=new JTextArea();//创建文本区对象
	ConfigurableDrawing(){
		setTitle("可配置绘图工具");
		setSize(1200,700);
		button1.addActionListener(new ActionListener(){//绑定事件
			public void actionPerformed(ActionEvent e){
				String s2=area2.getText();//获取文本区的字符串
				try{
					File f=new File("p.xml");
					FileWriter w=new FileWriter(f);
					w.flush();//清除文件中的文本
					w.write(s2);//向文件中写入获取到的字符串
					w.close();//关闭文件
				}catch(IOException ioe){//异常处理
					ioe.printStackTrace();
				}
				area1.repaint();
			}
		});
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				area2.setText(s);//将配置样例写入文本区中
			}
		});
		JPanel north=new JPanel();//创建中间容器对象
		north.setLayout(new GridLayout(1,2));//设置布局
		north.add(button1);
		north.add(button2);//添加按钮
		JPanel south=new JPanel();//创建中间容器对象
		south.setLayout(new BorderLayout());//设置布局
		south.add(north,BorderLayout.NORTH);
		south.add(new JScrollPane(area2),BorderLayout.CENTER);
		splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,area1,south);
		splitPane.setDividerLocation(1100);//设置初始容器分割线位置
		add(splitPane,BorderLayout.CENTER);
		setVisible(true);//设置窗口可见
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置按关闭键后退出程序
	}
	public static void main(String[] args){
		new ConfigurableDrawing();
		f.delete();
	}
}