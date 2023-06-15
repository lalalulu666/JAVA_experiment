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
	static File f=new File("p.xml");//�����ļ�����
	public Color getColor(String c,int r){//ͨ��RGB����ɫ���ƣ���ȡ��ɫ
		c=c.replaceAll(" ","");//ȥ������Ŀո�
		if(c.contains(",")){//���ΪRGB��ʽ
			int[] num=new int[3];
			String[] array=c.split(",");//���ַ���ͨ�����ŷָ�
			for(int i=0;i<array.length;i++){
				num[i]=Integer.valueOf(array[i]);
			}
			Color col=new Color(num[0],num[1],num[2]);
			return col;
		}else{//���Ϊ��ɫ����
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
	public  Double[] getNum(String s){//���ַ���ת��Ϊ����
		Double[] num=new Double[100];
		s=s.replaceAll(" ","");
		String[] array=s.split(",");
		for(int i=0;i<array.length;i++){
			num[i]=Double.valueOf(array[i]);
		}
		return num;
	}
	private class MyCanvas extends Canvas{//�̳�Canvas��
		public void paint(Graphics g){
			Graphics2D g2=(Graphics2D)g;//���ʶ���
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//ȥ�����
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);//ȥ�����
			g2.clearRect(0, 0, (int)getBounds().getWidth(), (int)getBounds().getHeight());//����
			try{
				if(f.exists()){//����ļ�����
   	 			SAXReader reader=new SAXReader();//����xml��ȡ����
    				Document document=reader.read(new FileInputStream("p.xml"));//��ȡҪ������xml�ļ�
    				Element root=document.getRootElement();//��ȡ���ڵ�
				//����
    				Element bg=root.element("bg");
				String color1=bg.elementText("col");
				this.setBackground(getColor(color1,1));//���ñ���ɫ
				String xRange=bg.elementText("xRange");
				String yRange=bg.elementText("yRange");
				Double a=getBounds().getWidth();//��ȡ��ǰ������С
				Double b=getBounds().getHeight();
				Double[] tmp1=getNum(xRange);
				Double[] tmp2=getNum(yRange);
				Double ta=tmp1[1]-tmp1[0];//��ȡĿ�껭����С
				Double tb=tmp2[1]-tmp2[0];
				Double extenda=a/ta;//��ȡ����������Ҫ��չ�ı���������ת����ʽ��������:(x-tmp1[0])*extenda,������:b-(y-tmp2[0])*extendb
				Double extendb=b/tb;
				//����
				//��
				List<Element> point=root.elements("points");
				for(int i=0;i<point.size();i++){//���������б�
					Element p=point.get(i);
					String pad=p.elementText("pad").replaceAll(" ","");
					String color=p.elementText("col");
					g2.setColor(getColor(color,0));//���û�����ɫ
					String width=p.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//����Ĭ�ϻ��ʴ�ϸ
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//���û��ʴ�ϸ
					}
					String radius=p.elementText("radius").replaceAll(" ","");
					Double r=Double.valueOf(radius);//��ȡ��İ뾶
					String list=p.elementText("list");
					String[] plist=list.split("\n");//��ȡ����б�
					for(int j=1;j<plist.length;j++){//��������б�
						Double[] d1=getNum(plist[j].replaceAll(" ",""));
						Ellipse2D ellipse=new Ellipse2D.Double((d1[0]-tmp1[0])*extenda-r,b-(d1[1]-tmp2[0])*extendb-r,2*r,2*r);//�Ի�Բ�ķ�ʽ����
						g2.draw(ellipse);
						if(pad.equals("true")){//���pad����Ϊ�棬��Ҫ��ʵ�ĵ㣬��Ҫ����Բ
							g2.fill(ellipse);
						}
					}
				}
				//��
				//����
				List<Element> lines=root.elements("lines");
				for(int i=0;i<lines.size();i++){
					Element l=lines.get(i);
					String color=l.elementText("col");
					g2.setColor(getColor(color,0));//���û�����ɫ
					String width=l.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//����Ĭ�ϻ��ʴ�ϸ
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//���û��ʴ�ϸ
					}
					String list1=l.elementText("list").replaceAll(" ","");
					String[] slist=list1.split("\n");//��ȡ����б�
					for(int j=1;j<slist.length-1;j++){
						Double[] d1=getNum(slist[j].replaceAll(" ",""));//��ȡ��ǰ��
						Double[] d2=getNum(slist[j+1].replaceAll(" ",""));//��ȡ��һ����
						Line2D l1=new Line2D.Double((d1[0]-tmp1[0])*extenda,b-(d1[1]-tmp2[0])*extendb,(d2[0]-tmp1[0])*extenda,b-(d2[1]-tmp2[0])*extendb);//���ӵ�ǰ�����һ����
						g2.draw(l1);//����
					}
				}
				//����
				//ֱ��
				List<Element> line=root.elements("line");
				for(int i=0;i<line.size();i++){
					Element l=line.get(i);
					String color=l.elementText("col").replaceAll(" ","");
					g2.setColor(getColor(color,0));//���û�����ɫ
					String width=l.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//����Ĭ�ϻ��ʴ�ϸ
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//���û��ʴ�ϸ
					}
					String point1=l.elementText("point").replaceAll(" ","");
					Double[] n=getNum(point1);//��ȡ�ؾ���
					String slope=l.elementText("slope").replaceAll(" ","");
					if(slope.equals("inf")){//ƽ����y���ֱ��
						Line2D line1=new Line2D.Double((n[0]-tmp1[0])*extenda,b,(n[0]-tmp1[0])*extenda,0);//��������ؾ����������ȣ�������Ϊ0��b
						g2.draw(line1);//����
					}else{
						Double dtmp=(-1)*Double.valueOf(slope);//б��
						if(dtmp==0){//���б��Ϊ0
							Line2D line1=new Line2D.Double(0,b-(n[1]-tmp2[0])*extendb,a,b-(n[1]-tmp2[0])*extendb);//��������ؾ������������
							g2.draw(line1);//����
						}else{
							Double c=b-(n[1]-tmp2[0])*extendb-dtmp*(n[0]-tmp1[0])*extenda;//ƫ����
							Line2D line1=new Line2D.Double((-c)/dtmp,0,(b-c)/dtmp,b);//���������0��b�����������
							g2.draw(line1);//����
						}
					}
				}
				//ֱ��
				//����
				List<Element> curve=root.elements("curve");
				for(int i=0;i<curve.size();i++){
					Element c=curve.get(i);
					String color=c.elementText("col").replaceAll(" ","");
					g2.setColor(getColor(color,0));//���û�����ɫ
					String width=c.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//����Ĭ�ϻ��ʴ�ϸ
					if(!width.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(width));
					g2.setStroke(stroke1);//���û��ʴ�ϸ
					}
					String range=c.elementText("range").replaceAll(" ","");
					Double[] num=getNum(range);//�����ϵ�����귶Χ
					String amount=c.elementText("amount").replaceAll(" ","");
					Double am=500.0;//Ĭ���Ա����ָ�
					if(!amount.equals("")){
						am=Double.valueOf(amount);//�Ա����ָ�
					}
					String function=c.elementText("function").replaceAll(" ","");
					 try {
					ScriptEngineManager manager=new ScriptEngineManager();
					ScriptEngine engine=manager.getEngineByName("js");//����Manager��engine����������ߺ���
					for(double j=num[0];j<=num[1];j+=(num[1]-num[0])/am){//������Χ�ں�����
					engine.put("x",j);//����������뷽��
					Object r1=engine.eval(function);//��ý��
					Line2D l=new Line2D.Double((j-tmp1[0])*extenda,b-((double)r1-tmp2[0])*extendb,(j-tmp1[0])*extenda,b-((double)r1-tmp2[0])*extendb);//���������
					g2.draw(l);//����
					}
					}catch (final ScriptException fe) { fe.printStackTrace(); }
				}
				//����
				//���κ���Բ
				List<Element> shape=root.elements("shape");
				for(int i=0;i<shape.size();i++){
					Element s=shape.get(i);
					String color=s.elementText("col");
					g2.setColor(getColor(color,0));//���û�����ɫ
					String wid=s.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//����Ĭ�ϻ��ʴ�ϸ
					if(!wid.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(wid));
					g2.setStroke(stroke1);//���û��ʴ�ϸ
					}
					String type=s.elementText("type").replaceAll(" ","");
					String pad=s.elementText("pad").replaceAll(" ","");
					String center=s.elementText("center").replaceAll(" ","");
					String width=s.elementText("width").replaceAll(" ","");
					String height=s.elementText("height").replaceAll(" ","");
					if(type.equals("oval")){//Ŀ��Ϊ������Բ
						Double[] n=getNum(center);//�õ�ͼ�����ĵ㣬ע�����ͼ��ʱҪ����ת��Ϊͼ�����Ͻǵĵ�
						Ellipse2D ellipse=new Ellipse2D.Double((n[0]-Double.valueOf(width)/2-tmp1[0])*extenda,b-(n[1]+Double.valueOf(height)/2-tmp2[0])*extendb,Double.valueOf(width)*extenda,Double.valueOf(height)*extendb);
						g2.draw(ellipse);//������Բ
						if(pad.equals("true")){
							g2.fill(ellipse);//���Ŀ����ʵ�ĵģ���Ҫ����
						}
					}
					if(type.equals("rect")){//Ŀ��Ϊ���ƾ���
						Double[] n=getNum(center);//�õ�ͼ�����ĵ㣬ע�����ͼ��ʱҪ����ת��Ϊͼ�����Ͻǵĵ�
						Rectangle2D rec=new Rectangle2D.Double((n[0]-Double.valueOf(width)/2-tmp1[0])*extenda,b-(n[1]+Double.valueOf(height)/2-tmp2[0])*extendb,Double.valueOf(width)*extenda,Double.valueOf(height)*extendb);
						g2.draw(rec);//���ƾ���
						if(pad.equals("true")){
							g2.fill(rec);//���Ŀ����ʵ�ĵģ���Ҫ����
						}
					}
				}
				//���κ���Բ
				//������
				List<Element> scale=root.elements("scale");
				for(int i=0;i<scale.size();i++){
					Element s=scale.get(i);
					String color=s.elementText("col");
					g2.setColor(getColor(color,0));//���û�����ɫ
					String wid=s.elementText("wid").replaceAll(" ","");
					Stroke stroke=new BasicStroke(1);
					g2.setStroke(stroke);//����Ĭ�ϻ��ʴ�ϸ
					if(!wid.equals("")){
					Stroke stroke1=new BasicStroke(Float.valueOf(wid));
					g2.setStroke(stroke1);//���û��ʴ�ϸ
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
						k=k*10f;//Ϊ���걣������׼��
					}
					Font font=new Font("����",Font.BOLD,12);//������������
					g2.setFont(font);
					if(direction.equals("x")){//�������Ŀ��Ϊx��
						g2.drawString(String.valueOf(Math.round(Float.valueOf(from)*k)/k),(float)((Double.valueOf(from)-tmp1[0])*extenda-7),(float)(b-(Float.valueOf(pos)-tmp2[0])*extendb+10));//����ԭ������
						for(int j=0;j<am;j++){
							Line2D line1=new Line2D.Double((Double.valueOf(from)+Double.valueOf(step)*j-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb,(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb);//����
							g2.draw(line1);
							g2.drawString(String.valueOf(Math.round((Float.valueOf(from)+Float.valueOf(step)*(j+1))*k)/k),(float)((Float.valueOf(from)+Float.valueOf(step)*(j+1)-tmp1[0])*extenda-7),(float)(b-(Float.valueOf(pos)-tmp2[0])*extendb)+10);//��������
							Line2D line2=new Line2D.Double((Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb,(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp1[0])*extenda,b-(Double.valueOf(pos)-tmp2[0])*extendb-10);
							g2.draw(line2);//���̶�
						}
					}else if(direction.equals("y")){//�������Ŀ��Ϊy��
						g2.drawString(String.valueOf(Math.round(Float.valueOf(from)*k)/k),(float)((Double.valueOf(pos)-tmp1[0])*extenda-20),(float)(b-(Float.valueOf(from)-tmp2[0])*extendb-7));//����ԭ������
						for(int j=0;j<am;j++){
							Line2D line1=new Line2D.Double((Double.valueOf(pos)-tmp1[0])*extenda,b-(Double.valueOf(from)+Double.valueOf(step)*j-tmp2[0])*extendb,(Double.valueOf(pos)-tmp1[0])*extenda,b-(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp2[0])*extendb);
							g2.draw(line1);//����
							g2.drawString(String.valueOf(Math.round((Float.valueOf(from)+Float.valueOf(step)*(j+1))*k)/k),(float)((Float.valueOf(pos)-tmp1[0])*extenda-22),(float)(b-(Float.valueOf(from)+Float.valueOf(step)*(j+1)-tmp2[0])*extendb)-1);//��������
							Line2D line2=new Line2D.Double((Double.valueOf(pos)-tmp1[0])*extenda,b-(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp2[0])*extendb,(Double.valueOf(pos)-tmp1[0])*extenda+10,b-(Double.valueOf(from)+Double.valueOf(step)*(j+1)-tmp2[0])*extendb);
							g2.draw(line2);//���̶�
						}
					}
				}
				//������
				}
			}catch(IOException ioe2){//�쳣����
				ioe2.printStackTrace();
			}catch(DocumentException de){//�쳣����
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
		+"\n</paintings>";//��Ҫ���õ�xml�ļ�д���ַ���
		
	JSplitPane splitPane;//��������
	JButton button1=new JButton("��ͼ");
	JButton button2=new JButton("��������");
	MyCanvas area1=new MyCanvas();//������������
	JTextArea area2=new JTextArea();//�����ı�������
	ConfigurableDrawing(){
		setTitle("�����û�ͼ����");
		setSize(1200,700);
		button1.addActionListener(new ActionListener(){//���¼�
			public void actionPerformed(ActionEvent e){
				String s2=area2.getText();//��ȡ�ı������ַ���
				try{
					File f=new File("p.xml");
					FileWriter w=new FileWriter(f);
					w.flush();//����ļ��е��ı�
					w.write(s2);//���ļ���д���ȡ�����ַ���
					w.close();//�ر��ļ�
				}catch(IOException ioe){//�쳣����
					ioe.printStackTrace();
				}
				area1.repaint();
			}
		});
		button2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				area2.setText(s);//����������д���ı�����
			}
		});
		JPanel north=new JPanel();//�����м���������
		north.setLayout(new GridLayout(1,2));//���ò���
		north.add(button1);
		north.add(button2);//��Ӱ�ť
		JPanel south=new JPanel();//�����м���������
		south.setLayout(new BorderLayout());//���ò���
		south.add(north,BorderLayout.NORTH);
		south.add(new JScrollPane(area2),BorderLayout.CENTER);
		splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,area1,south);
		splitPane.setDividerLocation(1100);//���ó�ʼ�����ָ���λ��
		add(splitPane,BorderLayout.CENTER);
		setVisible(true);//���ô��ڿɼ�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ð��رռ����˳�����
	}
	public static void main(String[] args){
		new ConfigurableDrawing();
		f.delete();
	}
}