import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
class SqlResult{
	String sqlFeedBack = null;
    	Object[][] valueLines = null;
   	public SqlResult() { }
    	public SqlResult(String sqlFeedBack, Object[][] valueLines) {
        		this.sqlFeedBack = sqlFeedBack;
        		this.valueLines = valueLines;
    	}
	public String dePart(String s){
		if(String.valueOf(s.charAt(0)).equals("'")&&String.valueOf(s.charAt(s.length()-1)).equals("'")){
			s=s.substring(1,s.length()-1);
		}
		return s;
	}
	public String toString() {
		for(int i=0;i<valueLines.length;i++){
			for(int j=0;j<valueLines[i].length-1;j++){
				sqlFeedBack=sqlFeedBack+dePart(String.valueOf(valueLines[i][j]))+", ";
			}
			sqlFeedBack=sqlFeedBack+dePart(String.valueOf(valueLines[i][valueLines[i].length-1]))+"\n";
		}
		return sqlFeedBack;
    	}
}

public class TableManager{
	public static ArrayList<String> tablenames=new ArrayList<String>();
	public static String db;
	public static String getType(String s){//�õ�����
		String type;
		if(s.toLowerCase().equals("int")){
			type="int";
		}else if(s.toLowerCase().equals("decimal")){
			type="double";
		}else{
			type="String";
		}
		return type;
	}
	public static int isCorrect(String type,String in){//�ж����������Ƿ����ת��ΪҪ������ͣ�String���������ж��Ƿ񱻵����Ű���
		if(type.equals("int")){
			try{
				Integer.valueOf(in);
				return 1;
			}catch(Exception e){//���������ת������Ჶ���쳣���򷵻ز���ת���ı�־
				return 0;
			}
		}else if(type.equals("double")){
			try{
				Double.valueOf(in);
				return 1;
			}catch(Exception e){//���������ת������Ჶ���쳣���򷵻ز���ת���ı�־
				return 0;
			}
		}else{
			if(!String.valueOf(in.charAt(0)).equals("'")||!String.valueOf(in.charAt(in.length()-1)).equals("'")){//String���������ж��Ƿ񱻵����Ű���
				return 2;
			}else{
				return 1;
			}
		}
	}
	public static int compare(String v1,String cmp,String v2,String type){//�򵥵ıȽ�
		int result=0;
		if(type.equals("int")){//���ж�����
			int value1=Integer.valueOf(v1);//ת��Ϊ��Ӧ����
			int value2=Integer.valueOf(v2);
			if(cmp.equals(">")){
				if(value1>value2){
					result=1;
				}
			}
			if(cmp.equals("<")){
				if(value1<value2){
					result=1;
				}
			}
			if(cmp.equals(">=")){
				if(value1>=value2){
					result=1;
				}
			}
			if(cmp.equals("<=")){
				if(value1<=value2){
					result=1;
				}
			}
			if(cmp.equals("=")){
				if(value1==value2){
					result=1;
				}
			}
			if(cmp.equals("<>")){
				if(value1!=value2){
					result=1;
				}
			}
		}else if(type.equals("double")){
			double value1=Double.valueOf(v1);
			double value2=Double.valueOf(v2);
			if(cmp.equals(">")){
				if(value1>value2){
					result=1;
				}
			}
			if(cmp.equals("<")){
				if(value1<value2){
					result=1;
				}
			}
			if(cmp.equals(">=")){
				if(value1>=value2){
					result=1;
				}
			}
			if(cmp.equals("<=")){
				if(value1<=value2){
					result=1;
				}
			}
			if(cmp.equals("=")){
				if(value1==value2){
					result=1;
				}
			}
			if(cmp.equals("<>")){
				if(value1!=value2){
					result=1;
				}
			}
		}else if(type.equals("String")){
			if(cmp.equals(">")){
				if(v1.toLowerCase().compareTo(v2.toLowerCase())>0){
					result=1;
				}
			}
			if(cmp.equals("<")){
				if(v1.toLowerCase().compareTo(v2.toLowerCase())<0){
					result=1;
				}
			}
			if(cmp.equals(">=")){
				if(v1.toLowerCase().compareTo(v2.toLowerCase())>=0){
					result=1;
				}
			}
			if(cmp.equals("<=")){
				if(v1.toLowerCase().compareTo(v2.toLowerCase())<=0){
					result=1;
				}
			}
			if(cmp.equals("=")){
				if(v1.toLowerCase().equals(v2.toLowerCase())){
					result=1;
				}
			} 
			if(cmp.equals("<>")){
				if(!v1.toLowerCase().equals(v2.toLowerCase())){
					result=1;
				}
			}
		}
		return result;
	}
	public static Object[][] sortedDisplay(Object[][] objects,Object[][] byObjects,ArrayList<Integer> flagAscend,String[] type){//����������ڲ���
		class LineAndBy implements Comparable<LineAndBy>{//ʵ��Comparable�ӿ�
			Object[] line;
			Object[] byObject;
			int bycount;
			LineAndBy(Object[] line,Object[] byObject,int bycount){//���캯��
				this.line=line;
				this.byObject=byObject;
				this.bycount=bycount;
			}
			public int compareTo(LineAndBy that){
				if(type[0].equals("int")){
					Integer thisInteger=(Integer)this.byObject[0];
                   				Integer thatInteger=(Integer)that.byObject[0];
                   				int compareResult = thisInteger.compareTo(thatInteger);
					if(flagAscend.get(0)==0){
                        					compareResult=-compareResult;
					}
					int c=1;
					while(compareResult==0&&c<bycount){
						if(type[c].equals("int")){
							thisInteger=(Integer)this.byObject[c];
                   						thatInteger=(Integer)that.byObject[c];
                   						compareResult = thisInteger.compareTo(thatInteger);
						}else if(type[c].equals("double")){
							Double thisDouble=(Double)this.byObject[c];
                   						Double thatDouble=(Double)that.byObject[c];
                   						compareResult = thisDouble.compareTo(thatDouble);
						}else{
							String thisString = (String) this.byObject[c];
                    						String thatString = (String) that.byObject[c];
                    						compareResult = thisString.compareTo(thatString);
						}
						if(flagAscend.get(c)==0){
                        						compareResult=-compareResult;
						}
						c++;
					}
					return compareResult;
				}else if(type[0].equals("double")){
					Double thisDouble=(Double)this.byObject[0];
                   				Double thatDouble=(Double)that.byObject[0];
                   				int compareResult = thisDouble.compareTo(thatDouble);
					if(flagAscend.get(0)==0){
                        					compareResult=-compareResult;
					}
					int c=1;
					while(compareResult==0&&c<bycount){
						if(type[c].equals("int")){
							Integer thisInteger=(Integer)this.byObject[c];
                   						Integer thatInteger=(Integer)that.byObject[c];
                   						compareResult = thisInteger.compareTo(thatInteger);
						}else if(type[c].equals("double")){
							thisDouble=(Double)this.byObject[c];
                   						thatDouble=(Double)that.byObject[c];
                   						compareResult = thisDouble.compareTo(thatDouble);
						}else{
							String thisString = (String) this.byObject[c];
                    						String thatString = (String) that.byObject[c];
                    						compareResult = thisString.compareTo(thatString);
						}
						if(flagAscend.get(c)==0){
                        						compareResult=-compareResult;
						}
						c++;
					}
                       				return compareResult;
				}else{
					String thisString = (String) this.byObject[0];
                    				String thatString = (String) that.byObject[0];
                    				int compareResult = thisString.compareTo(thatString);
					if(flagAscend.get(0)==0){
                        					compareResult=-compareResult;
					}
					int c=1;
					while(compareResult==0&&c<bycount){
						if(type[c].equals("int")){
							Integer thisInteger=(Integer)this.byObject[c];
                   						Integer thatInteger=(Integer)that.byObject[c];
                   						compareResult = thisInteger.compareTo(thatInteger);
						}else if(type[c].equals("double")){
							Double thisDouble=(Double)this.byObject[c];
                   						Double thatDouble=(Double)that.byObject[c];
                   						compareResult = thisDouble.compareTo(thatDouble);
						}else{
							thisString = (String) this.byObject[c];
                    						thatString = (String) that.byObject[c];
                    						compareResult = thisString.compareTo(thatString);
						}
						if(flagAscend.get(c)==0){
                        						compareResult=-compareResult;
						}
						c++;
					}
                        				return compareResult;
				}
			}
		}
		int amount=objects.length;
        		int columns=byObjects[0].length;
        		LineAndBy[] lineAndBys = new LineAndBy[amount];
       		for (int i=0; i<amount; i++)
            			lineAndBys[i] = new LineAndBy(objects[i], byObjects[i],columns);
       		Arrays.sort(lineAndBys);
        		Object[][] objectsToDisplay = new Object[amount][];
        		for (int i=0; i<amount; i++)
            			objectsToDisplay[i]=lineAndBys[i].line;
        		return objectsToDisplay;
	}
	public static void operate(String sql)throws IOException{
		String[] tmp=sql.split("\\s+");//����Զ���ո�Ϊ�ָ���ַ���
		int a=tmp.length;
		while(tmp[0].equals("")){//����û��������ǰ�������˿ո���ֳ����������һ��Ϊ�գ�Ϊ����������������뱣֤����һ������ؼ���
			for(int i=0;i<a-1;i++){
				tmp[i]=tmp[i+1];
			}
			tmp[a-1]="";
			a--;
		}
		//���ݵ�һ������Ĵʻ��ж��û�����
		if(tmp[0].toLowerCase().contains("show")){//��ʾ��
			ArrayList<String> namelist=new ArrayList<String>();
			RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","rw");//���ṹ�ļ�
			byte[] bs=new byte[32];
			for(int i=0;i<raf.length()/129;i++){
				raf.read(bs);//����
				String n=new String(bs,StandardCharsets.UTF_8).trim();
				if(namelist.size()==0){//��һ����ȡ�ı�����һ�������֮ǰ�ı����ظ�
					namelist.add(n);
				}else if(!n.equals(namelist.get(namelist.size()-1))){//�����ȡ�ı�������һ����ͬ��������������б���
					namelist.add(n);
				}
				raf.read(bs);//����
				raf.read(bs);//������
				raf.read(bs);//���
				raf.read();//���з�
			}
			raf.close();
			System.out.println("���ݱ�");
			for(int i=0;i<namelist.size();i++){
				System.out.println(namelist.get(i));
			}
			System.out.println("����"+String.valueOf(namelist.size())+"�����ݱ�");
		}else if(tmp[0].toLowerCase().contains("create")){//������
			int i;
			for(i=0;i<sql.length();i++){//��ǰ����ҵ���һ��ǰ����
				if(String.valueOf(sql.charAt(i)).equals("(")){
					break;
				}
			}
			int k;
			for(k=sql.length()-1;k>i;k--){//�Ӻ���ǰ��ȡ���һ������������λ��
				if(String.valueOf(sql.charAt(k)).equals(")")){
					break;
				}
			}
			if(i==sql.length()||k==i||k==i+1){//���û��ʶ��ǰ���Ż�����ţ���������û������
				System.out.println("��δ�������ݱ��������Լ��������� �� ���Ķ���δ������һ��������");
			}else{
				String findname=sql.substring(0,i);//��ȡǰ����֮ǰ���ַ�������ȡ����������ֵ��ַ���
				String[] t=findname.split("\\s+");//����table�ͱ���֮������пո������Զ���ո�Ϊ�ָ�
				int j;
				for(j=0;j<t.length;j++){
					if(t[j].toLowerCase().contains("table")){
						break;
					}
				}
				String name=t[j+1].toLowerCase();//��ȡ�������
				int u;
				for(u=0;u<tablenames.size();u++){
					if(name.toLowerCase().equals(tablenames.get(u).toLowerCase())){
						break;
					}
				}
				if(!name.equals("")&&u==tablenames.size()){//���б����б��в������������ı���
					ArrayList<String> cnames=new ArrayList<String>();
					String findtable=sql.substring(i+1,k);//��ȡ�����в������ַ���
					String[] t2=findtable.split(",");//ÿ���е���ز�����Ϊһ��
					int f1=0;
					for(k=0;k<t2.length;k++){//����ÿһ���е���ز���
						String[] c=t2[k].split("\\s+");//�������������ͷֿ�
						int b=c.length;
						while(c[0].equals("")){//�������ǰ����ո���Ҫ������ǰ��
							for(int h=0;h<b-1;h++){
								c[h]=c[h+1];
							}
							c[b-1]="";
							b--;	
						}
						String cname=c[0];//��ȡ����
						int l=0;
						while(l<cnames.size()){
							if(cname.equals(cnames.get(l))){//�ж������Ƿ�����ظ������,��������ʾ������������
								System.out.println("��"+String.valueOf(k+1)+"�е�������"+"��"+String.valueOf(l+1)+"�е������ظ�!");
								f1=1;
								break;
							}else{
								l++;
							}
						}
						String ctype=c[1].toLowerCase();//��ȡ�е�����
						if(!ctype.equals("int")&&!ctype.equals("decimal")&&!ctype.contains("varchar")){//�ж��е������Ƿ����
							System.out.println("������ĵ�"+String.valueOf(k+1)+"���е��������ʹ��������������������int��decimal��varchar(�ַ���)");
							f1=1;
						}
						if(ctype.contains("varchar")){
							int m;
							for(m=0;m<ctype.length();m++){
								if(String.valueOf(ctype.charAt(m)).equals("(")){
									break;
								}
							}
							int n;
							for(n=ctype.length()-1;n>m;n--){
								if(String.valueOf(ctype.charAt(n)).equals(")")){
									break;
								}
							}
							if(m==ctype.length()||n==m){//�ж��ַ��������Ƿ������Ű���
								System.out.println("������ĵ�"+String.valueOf(k+1)+"�е�varchar���͵����ݣ��ַ������ȸ�ʽ���ڴ���Ӧ�������Ű���!");
								f1=1;
							}else{//�жϸ�ʽ�Ƿ����
								int wi;
								try{
									wi=Integer.valueOf(ctype.substring(m+1,n));
									if(wi<0){
										System.out.println("������ĵ�"+String.valueOf(k+1)+"�е�varchar���͵����ݣ��ַ������ȴ��ڴ��󣬳��ȱ���Ϊ�Ǹ���!");
										f1=1;
									}
								}catch(Exception e){
									System.out.println("������ĵ�"+String.valueOf(k+1)+"�е�varchar���͵����ݣ��ַ������ȴ��ڴ������ṩ�ĳ��Ȳ���ת��������!");
									f1=1;
								}
							}
						}
					}
					if(f1==0){//����ʽ��ȷ
						tablenames.add(name);//������б�����Ӵ˱�
						String fname=name+".midb";//���ݱ��ļ���
						File datatable=new File(db+"/"+fname);
						datatable.createNewFile();//������Ӧ�ļ�
						RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","rw");
						raf.seek(raf.length());//��λ���ļ�ĩβλ��
						for(k=0;k<t2.length;k++){
							String[] c=t2[k].split("\\s+");//�������������ͷֿ�
							int b=c.length;
							while(c[0].equals("")){//�������ǰ����ո���Ҫ������ǰ��
								for(int h=0;h<b-1;h++){
									c[h]=c[h+1];
								}
								c[b-1]="";
								b--;	
							}
							String cname=c[0];//��ȡ����
							String ctype=c[1];//��ȡ�е�����
							int width=-1;
							byte[] data=name.getBytes(StandardCharsets.UTF_8);
							data=Arrays.copyOf(data,32);
							raf.write(data);//д�����
							data=cname.getBytes(StandardCharsets.UTF_8);
							data=Arrays.copyOf(data,32);
							raf.write(data);//д������
							if(ctype.equals("int")||ctype.equals("decimal")){
								data=ctype.getBytes(StandardCharsets.UTF_8);
								data=Arrays.copyOf(data,32);
								raf.write(data);//д��������
								width=1;//���Ϊ1
							}else if(ctype.contains("varchar")){//���Ϊ�ַ�������
								String tctype="varchar";
								data=tctype.getBytes(StandardCharsets.UTF_8);
								data=Arrays.copyOf(data,32);
								raf.write(data);
								int m;
								for(m=0;m<ctype.length();m++){
									if(String.valueOf(ctype.charAt(m)).equals("(")){
										break;
									}
								}
								int n;
								for(n=ctype.length()-1;n>m;n--){
									if(String.valueOf(ctype.charAt(n)).equals(")")){
										break;
									}
								}
								width=Integer.valueOf(ctype.substring(m+1,n));//��ȡ�����е�ֵ
							}
							data=String.valueOf(width).getBytes(StandardCharsets.UTF_8);
							data=Arrays.copyOf(data,32);
							raf.write(data);
							raf.writeBytes("\n");
						}
						raf.close();
						System.out.println("�������ݱ�"+name+"�ɹ���");
					}
				}else if(name.equals("")){
					System.out.println("���ݱ�������Ϊ��");
				}else{//���б����а����������б�
					System.out.println("ͬ�����ݱ��Ѵ��ڣ�");
				}
			}
		}else if(tmp[0].toLowerCase().contains("desc")){//��ʾ�ṹ
			sql=sql.toLowerCase();
			sql=sql.replaceAll(" ","");//ȥ���ո�
			String table=sql.replaceFirst("desc","");//ȥ����һ��desc
			RandomAccessFile r=new RandomAccessFile("TableInfo.ti","r");//����ṹ�ļ�
			byte[] bs=new byte[32];
			int flag=0;
			for(int i=0;i<r.length()/129;i++){
				r.read(bs);
				String tablename=new String(bs,StandardCharsets.UTF_8).trim();
				r.read(bs);
				String cname=new String(bs,StandardCharsets.UTF_8).trim();
				r.read(bs);
				String ctype=new String(bs,StandardCharsets.UTF_8).trim();
				r.read(bs);
				String width=new String(bs,StandardCharsets.UTF_8).trim();
				if(tablename.toLowerCase().equals(table.toLowerCase())){
					if(flag==0){//��һ���ҵ���¼
						System.out.println("������"+", "+"������"+", "+"�п��");
					}
					System.out.println(cname+", "+ctype+", "+width);
					flag=1;//�ҵ���¼���־Ϊ1
				}
				r.read();
			}
			if(flag==0){//û���ҵ�Ŀ�����ݱ�ļ�¼
				System.out.println("���ݱ�"+table+"������");
			}
			r.close();
		}else if(tmp[0].toLowerCase().contains("drop")){//ɾ�����ݱ�
			sql=sql.toLowerCase();
			String table=sql.replaceAll(" ","").replaceFirst("droptable","");//ȥ�����пո��ȥ��droptable
			String ftablename=table+".midb";//���ݱ��ļ���
			int u;
			for(u=0;u<tablenames.size();u++){
				if(table.toLowerCase().equals(tablenames.get(u).toLowerCase())){
					break;
				}
			}
			if(u<tablenames.size()){//�ҵ����ļ�
				tablenames.remove(table);//���б����Ƴ������ݱ���
				File ftable=new File(db+"/"+ftablename);
				ftable.delete();//ɾ�����ļ�
				RandomAccessFile r=new RandomAccessFile("TableInfo.ti","r");//���ṹ�ļ�
				RandomAccessFile wtmp=new RandomAccessFile("Tmp.ti","rw");//д��ʱ�ļ�
				byte[] bs=new byte[32];
				int count=0;
				for(int i=0;i<r.length()/129;i++){//�����ṹ�ļ������������Ҫɾ���ı�����ͬ��д����ʱ�ļ�
					r.read(bs);
					String n=new String(bs,StandardCharsets.UTF_8).trim();
					if(!n.toLowerCase().equals(table.toLowerCase())){
						byte[] tmpdata=new byte[32];
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//����
						r.read(bs);
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//����
						r.read(bs);
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//������
						r.read(bs);
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//���
						r.read();
						wtmp.writeBytes("\n");
					}else{
						count++;
						for(int j=0;j<3;j++){
							r.read(bs);
						}
						r.read();//��ȡ���з�
					}
				}
				r.close();
				wtmp.close();
				File file=new File("TableInfo.ti");
				file.delete();//ɾ��ԭ�ļ����������ɾ������������ݲ��ܸ���ԭ�ļ�����ɵĴ���
				RandomAccessFile dr=new RandomAccessFile("TableInfo.ti","rw");//д�ṹ�ļ�
				RandomAccessFile dtmp=new RandomAccessFile("Tmp.ti","r");//����ʱ�ļ�
				byte[] tmpdata=new byte[32];
				for(int i=0;i<dtmp.length()/129;i++){//����ʱ�ļ�ȫ��д��ṹ�ļ�
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//����
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//����
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//������
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//���
					dtmp.read();
					dr.writeBytes("\n");
				}
				dr.close();
				dtmp.close();
				File tmpfile=new File("Tmp.ti");
				tmpfile.delete();//ɾ����ʱ�ļ�
				System.out.println("ɾ�����ݱ�"+table+"�ɹ�");
			}else{//δ�ҵ����ļ�
				System.out.println("���ݱ�"+table+"������");
			}
		}else if(tmp[0].toLowerCase().contains("insert")){//�����ݱ��в���
			String[] t1=sql.split("\\s+");//��Ϊinto�����ݱ�֮��һ�����ڿո������Զ���ո�Ϊ�ָ�
			int z;
			for(z=0;z<t1.length;z++){
				if(t1[z].toLowerCase().contains("into")){
					break;
				}
			}
			String tablename="";
			if(z+1<t1.length){
				tablename=t1[z+1].toLowerCase();
			}
			int u;
			for(u=0;u<tablenames.size();u++){
				if(tablename.toLowerCase().equals(tablenames.get(u).toLowerCase())){
					break;
				}
			}
			if(u<tablenames.size()){//��ѯ�������ݱ�
				RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","r");
				byte[] bs=new byte[32];
				ArrayList<String> cname=new ArrayList<String>();
				ArrayList<String> ctype=new ArrayList<String>();
				ArrayList<String> cwidth=new ArrayList<String>();
				//System.out.println(raf.length());
				for(int i=0;i<raf.length()/129;i++){//��ȡ�����б��������б�����б�
					raf.read(bs);
					String n=new String(bs,StandardCharsets.UTF_8).trim();
					if(tablename.toLowerCase().equals(n.toLowerCase())){
						raf.read(bs);
						String cn=new String(bs,StandardCharsets.UTF_8).trim();
						cname.add(cn);
						raf.read(bs);
						String ct=new String(bs,StandardCharsets.UTF_8).trim();
						ctype.add(ct);
						raf.read(bs);
						String cw=new String(bs,StandardCharsets.UTF_8).trim();
						cwidth.add(cw);
						raf.read();//���з�
					}else{
						raf.read(bs);
						raf.read(bs);
						raf.read(bs);
						raf.read();
					}
				} 
				raf.close();
				int i;
				for(i=0;i<sql.length();i++){
					if(String.valueOf(sql.charAt(i)).equals("(")){
						break;
					}
				}
				int j;
				for(j=sql.length()-1;j>i;j--){
					if(String.valueOf(sql.charAt(j)).equals(")")){
						break;
					}
				}
				if(i==sql.length()||j==i||j==i+1){
					System.out.println("������Ĳ���ֵ��ʽ����δ�����Ű��� �� �������޲�����");
				}else{
					String values=sql.substring(i+1,j);
					if(values.contains(",")){//�д��ڵ���2�����������
						//System.out.println(raf.length());
						String[] vs=values.split(",");//��,�ָ��ȡ��������
						if(vs.length!=cname.size()){//�жϲ��������Ƿ���ñ����������
							System.out.println("������Ĳ��������빹��"+tablename+"���ݱ��м�¼�������������"+"����Ҫ����"+String.valueOf(cname.size())+"������");
						}else{
							int flag=0;
							for(int k=0;k<vs.length;k++){//�ж������ֵ
								String type=getType(ctype.get(k));
								vs[k]=vs[k].trim();//ȥ��ǰ��Ŀո�
								if(isCorrect(type,vs[k])==1){
									if(type.equals("String")){
										if(vs[k].length()-2>Integer.valueOf(cwidth.get(k))*3){
											System.out.println("���ṩ�ĵ�"+String.valueOf(k+1)+"���ַ������ȣ�����"+tablename+"�е�"+String.valueOf(k+1)+"��Ĺ涨�ַ�����!");
											flag=1;
										}
									}
								}else if(isCorrect(type,vs[k])==2){
									System.out.println("���ṩ�ĵ�"+String.valueOf(k+1)+"���ַ�����ʽ����Ӧ���õ����Ű���");
									flag=1;
								}else{
									String h;
									if(type.equals("int")){
										h="����";
									}else{
										h="С��";
									}
									System.out.println("���ṩ�ĵ�"+String.valueOf(k+1)+"��ֵ����ת��Ϊ"+h);
									flag=1;
								}
							}
							if(flag==0){//ȷ�����������д���ļ�
								String tfn=tablename+".midb";
								RandomAccessFile tf=new RandomAccessFile(db+"/"+tfn,"rw");
								tf.seek(tf.length());
								for(int k=0;k<vs.length;k++){
									String type=getType(ctype.get(k));
									//System.out.println(type);
									//System.out.println(vs[k]);
									byte[] bs1=vs[k].getBytes(StandardCharsets.UTF_8);
									if(type.equals("int")){
										bs1=Arrays.copyOf(bs1,4);
										tf.write(bs1);
									}else if(type.equals("double")){
										bs1=Arrays.copyOf(bs1,8);
										tf.write(bs1);
									}else if(type.equals("String")){//������ַ������ͣ��ֽ���Ϊ��ȳ�3
										int b=Integer.valueOf(cwidth.get(k))*3+2;
										//System.out.println(b);
										bs1=Arrays.copyOf(bs1,b);
										tf.write(bs1);
									}
								}
								tf.writeBytes("\n");
								tf.close();
								System.out.println("�����ݱ�"+tablename+"д��һ����¼�ɹ�");
							}
						}
					}else{//ֻ��һ�����������
						if(cname.size()!=1){
							System.out.println("������Ĳ��������빹��"+tablename+"���ݱ��м�¼�������������"+"����Ҫ����"+String.valueOf(cname.size())+"������");
						}else{
							//System.out.println(values);
							String type=getType(ctype.get(0));
							values=values.trim();//ȥ����β�ո�
							int flag=0;
							if(isCorrect(type,values)==1){
								if(type.equals("String")){
									if(values.length()-2>Integer.valueOf(cwidth.get(0))*3){
										System.out.println("���ṩ�ĵ�1���ַ������ȣ�����"+tablename+"�е�1��Ĺ涨�ַ�����!");
										flag=1;
									}
								}
							}else if(isCorrect(type,values)==2){
								System.out.println("���ṩ�ĵ�1���ַ�����ʽ����Ӧ���õ����Ű���");
								flag=1;
							}else{
								String h;
								if(type.equals("int")){
									h="����";
								}else{
									h="С��";
								}
								System.out.println("���ṩ�ĵ�1��ֵ����ת��Ϊ"+h);
								flag=1;
							}
							if(flag==0){
								String tfn=tablename+".midb";
								RandomAccessFile tf=new RandomAccessFile(db+"/"+tfn,"rw");
								tf.seek(tf.length());
								byte[] bs1=values.getBytes(StandardCharsets.UTF_8);
								if(type.equals("int")){
									bs1=Arrays.copyOf(bs1,4);
									tf.write(bs1);
								}else if(type.equals("double")){
									bs1=Arrays.copyOf(bs1,8);
									tf.write(bs1);
								}else if(type.equals("String")){
									int b=Integer.valueOf(cwidth.get(0))*3+2;
									//System.out.println(b);
									//System.out.println(values);
									bs1=Arrays.copyOf(bs1,b);
									tf.write(bs1);
								}
								tf.writeBytes("\n");
								tf.close();
								System.out.println("�����ݱ�"+tablename+"д��һ����¼�ɹ�");
							}
						}
					}
				}
			}else{//δ��ѯ�������ݱ�
				System.out.println("���ݱ�"+tablename+"�����ڣ�");
			}
		}else if(tmp[0].toLowerCase().contains("update")){//�������ݱ��е�����
			String[] s1=sql.split("\\s+");
			int u;
			for(u=0;u<s1.length;u++){
				if(s1[u].toLowerCase().equals("where")){
					sql.replace(s1[u],"where");
				}
			}
			for(u=0;u<s1.length;u++){
				if(s1[u].toLowerCase().equals("set")){
					sql.replace(s1[u],"set");
					break;
				}
			}
			String tablename="";
			if(u>0){
				tablename=s1[u-1].toLowerCase();
			}
			for(u=0;u<tablenames.size();u++){
				if(tablename.toLowerCase().equals(tablenames.get(u).toLowerCase())){
					break;
				}
			}
			if(u<tablenames.size()){//�ҵ����ݱ�
				RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","r");
				byte[] bs=new byte[32];
				ArrayList<String> cname=new ArrayList<String>();
				ArrayList<String> ctype=new ArrayList<String>();
				ArrayList<String> cwidth=new ArrayList<String>();
				for(int i=0;i<raf.length()/129;i++){//��ȡ�����б��������б�����б�
					raf.read(bs);
					String n=new String(bs,StandardCharsets.UTF_8).trim();
					if(tablename.toLowerCase().equals(n.toLowerCase())){
						raf.read(bs);
						String cn=new String(bs,StandardCharsets.UTF_8).trim();
						cname.add(cn);
						raf.read(bs);
						String ct=new String(bs,StandardCharsets.UTF_8).trim();
						ctype.add(ct);
						raf.read(bs);
						String cw=new String(bs,StandardCharsets.UTF_8).trim();
						cwidth.add(cw);
						raf.read();//���з�
					}else{
						raf.read(bs);
						raf.read(bs);
						raf.read(bs);
						raf.read();
					}
				}
				raf.close();
				int rowlen=0; 
				for(int i=0;i<ctype.size();i++){
					String type=getType(ctype.get(i));
					if(type.equals("int")){
						rowlen+=4;
					}else if(type.equals("double")){
						rowlen+=8;
					}else{
						rowlen+=Integer.valueOf(cwidth.get(i))*3+2;
					}
				}
				rowlen+=1;//��ȡ���ݱ��ļ���ÿһ�еĳ���
				String[] keys=sql.split("set");
				String key=keys[1];//�õ�set��������
				key=key.replace(">\\s+=",">=");
				key=key.replace("<\\s+=","<=");
				key=key.replace("<\\s+>","<>");//ȥ����������֮��Ŀո�
				if(key.contains("where")){
					String[] sites=key.split("where");
					String[] site0=sites[0].split(",");
					ArrayList<String> changenames=new ArrayList<String>();
					ArrayList<String> changevalues=new ArrayList<String>();
					for(int v=0;v<site0.length;v++){
						String changet=site0[v]; 
						String[] change=changet.split("=");
						changenames.add(change[0].trim());
						changevalues.add(change[1].trim());
					}
					String[] site1=sites[1].split(",");
					ArrayList<String> reqnames=new ArrayList<String>();
					ArrayList<String> cmps=new ArrayList<String>();
					ArrayList<String> reqvalues=new ArrayList<String>();
					for(int v=0;v<site1.length;v++){//��ȡ����
						String request=site1[v];
						if(request.contains(">=")){
							String[] re=request.split(">=");
							reqnames.add(re[0].trim());
							cmps.add(">=");
							reqvalues.add(re[1].trim());
						}else if(request.contains("<=")){
							String[] re=request.split("<=");
							reqnames.add(re[0].trim());
							cmps.add("<=");
							reqvalues.add(re[1].trim());
						}else if(request.contains("<>")){
							String[] re=request.split("<>");
							reqnames.add(re[0].trim());
							cmps.add("<>");
							reqvalues.add(re[1].trim());
						}else if(request.contains(">")){
							String[] re=request.split(">");
							reqnames.add(re[0].trim());
							cmps.add(">");
							reqvalues.add(re[1].trim());
						}else if(request.contains("<")){
							String[] re=request.split("<");
							reqnames.add(re[0].trim());
							cmps.add("<");
							reqvalues.add(re[1].trim());
						}else if(request.contains("=")){
							String[] re=request.split("=");
							reqnames.add(re[0].trim());
							cmps.add("=");
							reqvalues.add(re[1].trim());
						}
					}
					int finalFlag=0;
					for(int g=0;g<changenames.size();g++){
						int f1=0;
						int i;
						for(i=0;i<cname.size();i++){
							if(changenames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){//�ж������Ƿ����
								f1=1;
								if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==1){//�ж��Ƿ���ϸ�ʽ
									f1=2;
								}
								break;
							}
						}
						if(f1==0){
							System.out.println("�������Ҫ���µĵ�"+String.valueOf(g+1)+"�������Ʋ�������"+tablename+"��");
							finalFlag=1;
							break;
						}
						if(f1==1){
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==0){
								if(getType(ctype.get(i)).equals("int")){
									System.out.println("�������Ҫ�����еĵ�"+String.valueOf(g+1)+"���еĸ���ֵ��ʽ���󣬲���ת��Ϊ������");
								}else{
									System.out.println("�������Ҫ�����еĵ�"+String.valueOf(g+1)+"���еĸ���ֵ��ʽ���󣬲���ת��ΪС����");
								}
							}
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==2){
								System.out.println("�������Ҫ�����еĵ�"+String.valueOf(g+1)+"���еĸ���ֵ��ʽ�����ַ���δ�������Ű�����");
							}
							finalFlag=1;
							break;
						}
					}
					for(int g=0;g<reqnames.size();g++){
						int f1=0;
						int i;
						for(i=0;i<cname.size();i++){
							if(reqnames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){
								f1=1;
								if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==1){
									f1=2;
								}
								break;
							}
						}
						if(f1==0){
							System.out.println("������������еĵ�"+String.valueOf(g+1)+"�������Ʋ�������"+tablename+"��");
							finalFlag=1;
							break;
						}
						if(f1==1){
							if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==0){
								if(getType(ctype.get(i)).equals("int")){
									System.out.println("������������еĵ�"+String.valueOf(g+1)+"���е�����ֵ��ʽ���󣬲���ת��Ϊ������");
								}else{
									System.out.println("������������еĵ�"+String.valueOf(g+1)+"���е�����ֵ��ʽ���󣬲���ת��ΪС����");
								}
							}
							if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==2){
								System.out.println("������������еĵ�"+String.valueOf(g+1)+"���е�����ֵ��ʽ�����ַ���δ�������Ű�����");
							}
							finalFlag=1;
							break;
						}
					}
					if(finalFlag==0){
						String rname=tablename+".midb";
						RandomAccessFile r=new RandomAccessFile(db+"/"+rname,"r");
						ArrayList<Integer> reqnamesites=new ArrayList<Integer>();
						for(int g=0;g<reqnames.size();g++){
							for(int i=0;i<cname.size();i++){
								if(reqnames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){
									int reqnamesite=i;
									reqnamesites.add(reqnamesite);
								}
							}
						}
						ArrayList<Integer> changenamesites=new ArrayList<Integer>();
						for(int g=0;g<changenames.size();g++){
							for(int i=0;i<cname.size();i++){
								if(changenames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){
									int changenamesite=i;
									changenamesites.add(changenamesite);
								}
							}
						}
						ArrayList<Integer> issatisfy=new ArrayList<Integer>();
						int count=0;
						for(int i=0;i<(int)r.length()/rowlen;i++){
							int j=0;
							int boolflag=0;
							while(j<cname.size()){
								String type=getType(ctype.get(j));
								byte[] rbs;
								if(type.equals("int")){
									rbs=new byte[4];
									r.read(rbs);
								}else if(type.equals("double")){
									rbs=new byte[8];
									r.read(rbs);
								}else{
									rbs=new byte[Integer.valueOf(cwidth.get(j))*3+2];
									r.read(rbs);
								}
								for(int g=0;g<reqnamesites.size();g++){
									if(j==reqnamesites.get(g)){
										String ct=new String(rbs,StandardCharsets.UTF_8).trim();
										if(compare(ct,cmps.get(g),reqvalues.get(g),type)==1){
											boolflag+=1;
										}
									}
								}
								j++;
							}
							if(boolflag==reqnamesites.size()){
								issatisfy.add(1);
								count++;
							}else{
								issatisfy.add(0);
							}
							r.read();//�����з�
						}
						r.close();
						RandomAccessFile w=new RandomAccessFile(db+"/"+rname,"rw");
						for(int i=0;i<(int)w.length()/rowlen;i++){
							int j=0;
							while(j<cname.size()){
								int flagin=0;
								String type=getType(ctype.get(j));
								for(int g=0;g<changenamesites.size();g++){
									byte[] data=changevalues.get(g).getBytes(StandardCharsets.UTF_8);
									if(j==changenamesites.get(g)&&issatisfy.get(i)==1){
										flagin=1;
										if(type.equals("int")){
											data=Arrays.copyOf(data,4);
											w.write(data);
										}
										if(type.equals("double")){
											data=Arrays.copyOf(data,8);
											w.write(data);
										}
										if(type.equals("String")){
											data=Arrays.copyOf(data,Integer.valueOf(cwidth.get(j))*3+2);
											w.write(data);
										}
									}
								}
								if(flagin==0){
									if(type.equals("int")){
										byte[] wbs=new byte[4];
										w.read(wbs);
									}
									if(type.equals("double")){
										byte[] wbs=new byte[8];
										w.read(wbs);
									}
									if(type.equals("String")){
										byte[] wbs=new byte[Integer.valueOf(cwidth.get(j))*3+2];
										w.read(wbs);
									}	
								}
								j++;
							}
							w.read();
						}
						w.close();
						System.out.println("��������"+String.valueOf(count)+"����¼");
					}
				}else{//���������
					String[] site0=key.split(",");
					ArrayList<String> changenames=new ArrayList<String>();
					ArrayList<String> changevalues=new ArrayList<String>();
					for(int v=0;v<site0.length;v++){
						String changet=site0[v]; 
						String[] change=changet.split("=");
						changenames.add(change[0].trim());
						changevalues.add(change[1].trim());
					}
					int finalFlag=0;
					for(int g=0;g<changenames.size();g++){
						int f1=0;
						int i;
						for(i=0;i<cname.size();i++){
							if(changenames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){
								f1=1;
								if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==1){
									f1=2;
								}
								break;//��������
							}
						}
						if(f1==0){
							System.out.println("�������Ҫ���µĵ�"+String.valueOf(g+1)+"�������Ʋ�������"+tablename+"��");
							finalFlag=1;
							break;
						}
						if(f1==1){
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==0){
								if(getType(ctype.get(i)).equals("int")){
									System.out.println("�������Ҫ�����еĵ�"+String.valueOf(g+1)+"���еĸ���ֵ��ʽ���󣬲���ת��Ϊ������");
								}else{
									System.out.println("�������Ҫ�����еĵ�"+String.valueOf(g+1)+"���еĸ���ֵ��ʽ���󣬲���ת��ΪС����");
								}
							}
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==2){
								System.out.println("�������Ҫ�����еĵ�"+String.valueOf(g+1)+"���еĸ���ֵ��ʽ�����ַ���δ�������Ű�����");
							}
							finalFlag=1;
							break;
						}
					}
					if(finalFlag==0){
						ArrayList<Integer> changenamesites=new ArrayList<Integer>();
						for(int g=0;g<changenames.size();g++){
							for(int i=0;i<cname.size();i++){
								if(changenames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){
									int changenamesite=i;
									changenamesites.add(changenamesite);
									break;
								}
							}
						}
						String rname=tablename+".midb";
						RandomAccessFile w=new RandomAccessFile(db+"/"+rname,"rw");
						int count=0;
						for(int i=0;i<(int)w.length()/rowlen;i++){
							int j=0;
							while(j<cname.size()){
								int flagin=0;
								String type=getType(ctype.get(j));
								for(int g=0;g<changenamesites.size();g++){
									byte[] data=changevalues.get(g).getBytes(StandardCharsets.UTF_8);
									if(j==changenamesites.get(g)){
										flagin=1;
										if(type.equals("int")){
											data=Arrays.copyOf(data,4);
											w.write(data);
										}
										if(type.equals("double")){
											data=Arrays.copyOf(data,8);
											w.write(data);
										}
										if(type.equals("String")){
											data=Arrays.copyOf(data,Integer.valueOf(cwidth.get(j))*3+2);
											w.write(data);
										}
									}
								}
								if(flagin==0){
									if(type.equals("int")){
										byte[] bs1=new byte[4];
										w.read(bs1);
									}
									if(type.equals("double")){
										byte[] bs1=new byte[8];
										w.read(bs1);
									}
									if(type.equals("String")){
										byte[] bs1=new byte[Integer.valueOf(cwidth.get(j))*3+2];
										w.read(bs1);
									}	
								}
								j++;
							}
							w.read();
							count++;
						}
						w.close();
						System.out.println("��������"+String.valueOf(count)+"����¼");
					}
				}
			}else{//δ�ҵ����ݱ�
					System.out.println("���ݱ�"+tablename+"�����ڣ�");
			}
		}else if(tmp[0].toLowerCase().contains("select")){//ѡ�����ݱ��е��������
			sql=sql.toLowerCase();
			int z;
			for(z=0;z<tmp.length;z++){
				if(tmp[z].toLowerCase().contains("from")){
					break;
				}
			}
			String tablename="";
			if(z+1<tmp.length){
				tablename=tmp[z+1].toLowerCase();
			}
			int u;
			for(u=0;u<tablenames.size();u++){
				if(tablename.toLowerCase().equals(tablenames.get(u).toLowerCase())){
					break;
				}
			}
			if(u<tablenames.size()){//�ҵ����ݱ�
				String[] t1=sql.split("from");
				String t2=t1[0];
				String[] reqs=t2.split("select");
				int b=reqs.length;
				while(reqs[0].equals("")){
					for(int i=0;i<b-1;i++){
						reqs[i]=reqs[i+1];
					}
					reqs[b-1]="";
					b--;
				}
				//System.out.println(reqs[0]);
				RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","r");
				byte[] bs=new byte[32];
				ArrayList<String> cname=new ArrayList<String>();
				ArrayList<String> ctype=new ArrayList<String>();
				ArrayList<String> cwidth=new ArrayList<String>();
				for(int i=0;i<raf.length()/129;i++){//��ȡ�����б��������б�����б�
					raf.read(bs);
					String n=new String(bs,StandardCharsets.UTF_8).trim();
					if(tablename.toLowerCase().equals(n.toLowerCase())){
						raf.read(bs);
						String cn=new String(bs,StandardCharsets.UTF_8).trim();
						cname.add(cn);
						raf.read(bs);
						String ct=new String(bs,StandardCharsets.UTF_8).trim();
						ctype.add(ct);
						raf.read(bs);
						String cw=new String(bs,StandardCharsets.UTF_8).trim();
						cwidth.add(cw);
						raf.read();//���з�
					}else{
						raf.read(bs);
						raf.read(bs);
						raf.read(bs);
						raf.read();
					}
				}
				raf.close();
				String[] requests=reqs[0].split(",");
				int flag1=0;
				int boolflag1=1;
				if(!reqs[0].replaceAll(" ","").equals("*")){
					for(int i=0;i<requests.length;i++){
						flag1=0;
						requests[i]=requests[i].replaceAll(" ","");
						for(int j=0;j<cname.size();j++){
							if(requests[i].toLowerCase().equals(cname.get(j).toLowerCase())){
								flag1=1;
							}
						}
						if(flag1==0){
							System.out.println("��Ҫѡ��ĵ�"+String.valueOf(i+1)+"�в����������ݱ�"+tablename+"�У�");
							boolflag1=0;
							break;
						}
					}
				}
				if(boolflag1==1){
					ArrayList<Integer> reqsite=new ArrayList<Integer>();
					if(reqs[0].replaceAll(" ","").equals("*")){
						for(int j=0;j<cname.size();j++){
							reqsite.add(j);
						}
					}else{
						for(int i=0;i<requests.length;i++){
							//System.out.println(requests[i]);
							requests[i]=requests[i].replace(" ","");
							for(int j=0;j<cname.size();j++){
								if(requests[i].toLowerCase().equals(cname.get(j).toLowerCase())){
									reqsite.add(j);
								}
							}
						}
					}//��ȡѡ�����
					for(int i=0;i<reqsite.size()-1;i++){
						System.out.print(cname.get(reqsite.get(i))+", ");
					}
					System.out.println(cname.get(reqsite.get(reqsite.size()-1)));
					int rowlen=0; 	
					for(int i=0;i<ctype.size();i++){
						String type=getType(ctype.get(i));
						if(type.equals("int")){
							rowlen+=4;
						}else if(type.equals("double")){
							rowlen+=8;
						}else{
							rowlen+=Integer.valueOf(cwidth.get(i))*3+2;
						}
					}
					rowlen+=1;
					String rname=tablename+".midb";
					RandomAccessFile r=new RandomAccessFile(db+"/"+rname,"r");
					Object[][] table=new Object[(int)r.length()/rowlen][cname.size()];
					for(int i=0;i<(int)r.length()/rowlen;i++){
						int j=0;
						while(j<cname.size()){
							String type=getType(ctype.get(j));
							byte[] rbs;
							if(type.equals("int")){
								rbs=new byte[4];
								r.read(rbs);
								String o=new String(rbs,StandardCharsets.UTF_8).trim();
								table[i][j]=Integer.valueOf(o);
							}
							if(type.equals("double")){
								rbs=new byte[8];
								r.read(rbs);
								String o=new String(rbs,StandardCharsets.UTF_8).trim();
								table[i][j]=Double.valueOf(o);
							}
							if(type.equals("String")){
								rbs=new byte[Integer.valueOf(cwidth.get(j))*3+2];
								//rbs=new byte[100];
								r.read(rbs);
								String o=new String(rbs,StandardCharsets.UTF_8).trim();
								//System.out.println(o);
								table[i][j]=o;
							}
							j++;
						}
						r.read();//�����з�
					}
					if(!sql.contains("where")&&!sql.contains("order")){
						Object[][] result=new Object[(int)r.length()/rowlen][reqsite.size()];
						for(int i=0;i<(int)r.length()/rowlen;i++){
							for(int j=0;j<reqsite.size();j++){
								result[i][j]=table[i][reqsite.get(j)];
							}
						}
						SqlResult sr=new SqlResult("",result);
						System.out.println(sr.toString());
						System.out.println("����"+String.valueOf((int)r.length()/rowlen)+"����¼");
					}else if(sql.contains("where")&&!sql.contains("order")){
						String[] t3=sql.split("where");
						String tmplimits=t3[1];
						tmplimits=tmplimits.replace(">\\s+=",">=");
						tmplimits=tmplimits.replace("<\\s+=","<=");
						tmplimits=tmplimits.replace("<\\s+>","<>");
						String[] limits=tmplimits.split(",");
						ArrayList<Integer> lcname=new ArrayList<Integer>();
						ArrayList<String> lcmp=new ArrayList<String>();
						ArrayList<String> lvalue=new ArrayList<String>();
						int boolflag2=1;
						for(int i=0;i<limits.length;i++){
							int flag2=0;
							String[] seq=new String[3];
							String request=limits[i];
							if(request.contains(">=")){
								String[] seqt=request.split(">=");
								seq[0]=seqt[0].trim();
								seq[1]=">=";
								seq[2]=seqt[1].trim();
							}else if(request.contains("<=")){
								String[] seqt=request.split("<=");
								seq[0]=seqt[0].trim();
								seq[1]="<=";
								seq[2]=seqt[1].trim();
							}else if(request.contains("<>")){
								String[] seqt=request.split("<>");
								seq[0]=seqt[0].trim();
								seq[1]="<>";
								seq[2]=seqt[1].trim();
							}else if(request.contains(">")){
								String[] seqt=request.split(">");
								seq[0]=seqt[0].trim();
								seq[1]=">";
								seq[2]=seqt[1].trim();
							}else if(request.contains("<")){
								String[] seqt=request.split("<");
								seq[0]=seqt[0];
								seq[1]="<";
								seq[2]=seqt[1];
							}else if(request.contains("=")){
								String[] seqt=request.split("=");
								seq[0]=seqt[0].trim();
								seq[1]="=";
								seq[2]=seqt[1].trim();
							}
							int c=seq.length;
							int j;
							for(j=0;j<cname.size();j++){
								if(seq[0].toLowerCase().equals(cname.get(j).toLowerCase())){
									flag2=1;
									if(isCorrect(getType(ctype.get(j)),seq[2])==1){
										lcname.add(j);
										lcmp.add(seq[1]);
										lvalue.add(seq[2]);
										flag2=2;
									}
									break;
								}
							}
							if(flag2==0){
								System.out.println("������������еĵ�"+String.valueOf(i+1)+"�в����������ݱ�"+tablename+"��");
								boolflag2=0;
								break;
							}
							if(flag2==1){
								if(isCorrect(getType(ctype.get(j)),seq[2])==0){
									if(getType(ctype.get(j)).equals("int")){
										System.out.println("������������еĵ�"+String.valueOf(i+1)+"�е�����ֵ��ʽ����,����ת��Ϊ������");
									}else if(getType(ctype.get(j)).equals("double")){
										System.out.println("������������еĵ�"+String.valueOf(i+1)+"�е�����ֵ��ʽ����,����ת��ΪС����");
									}
								}else if(isCorrect(getType(ctype.get(j)),seq[2])==2){
									System.out.println("������������еĵ�"+String.valueOf(i+1)+"�е�����ֵ��ʽ����,�ַ���δ�������Ű�����");
								} 
								boolflag2=0;
								break;
							}
						} 
						if(boolflag2==1){
							ArrayList<Integer> issatisfy=new ArrayList<Integer>();
							for(int i=0;i<(int)r.length()/rowlen;i++){
								int j=0;
								while(j<lcname.size()){
									if(compare(String.valueOf(table[i][lcname.get(j)]),lcmp.get(j),lvalue.get(j),getType(ctype.get(lcname.get(j))))!=1){
										break;
									}
									j++;
								}
								if(j==lcname.size()){
									issatisfy.add(1);
								}else{
									issatisfy.add(0);
								}
							}
							int d=0;
							for(int i=0;i<issatisfy.size();i++){
								if(issatisfy.get(i)==1){
									d++;
								}
							}
							Object[][] result=new Object[d][reqsite.size()];
							int e=0;
							for(int i=0;i<(int)r.length()/rowlen;i++){
								if(issatisfy.get(i)==1){
									for(int j=0;j<reqsite.size();j++){
										result[e][j]=table[i][reqsite.get(j)];
									}
									e++;
								}
							}
							SqlResult sr=new SqlResult("",result);
							System.out.println(sr.toString());
							System.out.println("����"+String.valueOf(e)+"����¼");
						}
					}else if(!sql.contains("where")&&sql.contains("order")){
						String[] sctmp=sql.split("by");
						String sortcname=sctmp[1];
						String[] sortcnames=sortcname.split(",");
						int flago=0;
						for(int v=0;v<sortcnames.length;v++){
							//System.out.println(sortcnames[v]);
							String stmp=sortcnames[v].replaceAll("asc","").replaceAll("desc","").trim();
							int site=-1;
							for(int j=0;j<cname.size();j++){
								if(stmp.toLowerCase().equals(cname.get(j).toLowerCase())){
									site=j;
								}
							}
							if(site==-1){
								System.out.println("������ĵ�"+String.valueOf(v+1)+"�������׼����������ݱ�"+tablename+"��");
								flago=1;
							}
						}
						if(flago==0){
							ArrayList<Integer> sites=new ArrayList<Integer>();
							ArrayList<Integer> iss=new ArrayList<Integer>();
							for(int v=0;v<sortcnames.length;v++){
								if(sortcnames[v].contains("desc")){
									iss.add(0);
								}else{
									iss.add(1);
								}
								//System.out.println(sortcnames[v]);
								sortcnames[v]=sortcnames[v].replaceAll("asc","").replaceAll("desc","").trim();
								for(int j=0;j<cname.size();j++){
									if(sortcnames[v].toLowerCase().equals(cname.get(j).toLowerCase())){
										sites.add(j);
									}
								}
							}
							Object[][] byObjects=new Object[(int)r.length()/rowlen][sites.size()];
							for(int i=0;i<(int)r.length()/rowlen;i++){
								for(int v=0;v<sites.size();v++){
									byObjects[i][v]=table[i][sites.get(v)];
								}
							}
							Object[][] retmp=new Object[(int)r.length()/rowlen][reqsite.size()];
							for(int i=0;i<(int)r.length()/rowlen;i++){
								for(int j=0;j<reqsite.size();j++){
									retmp[i][j]=table[i][reqsite.get(j)];
								}
							}
							String[] types=new String[sites.size()];
							for(int i=0;i<sites.size();i++){	
								types[i]=getType(ctype.get(sites.get(i)));
							}
							Object[][] result=sortedDisplay(retmp,byObjects,iss,types);
							SqlResult sr=new SqlResult("",result);
							System.out.println(sr.toString());
							System.out.println("����"+String.valueOf((int)r.length()/rowlen)+"����¼");
						}
					}else{//����������
						String[] t4=sql.split("order");
						String[] t3=t4[0].split("where");
						String tmplimits=t3[1];
						tmplimits=tmplimits.replace(">\\s+=",">=");
						tmplimits=tmplimits.replace("<\\s+=","<=");
						tmplimits=tmplimits.replace("<\\s+>","<>");
						String[] limits=tmplimits.split(",");
						ArrayList<Integer> lcname=new ArrayList<Integer>();
						ArrayList<String> lcmp=new ArrayList<String>();
						ArrayList<String> lvalue=new ArrayList<String>();
						int boolflag2=1;
						for(int i=0;i<limits.length;i++){
							int flag2=0;
							String request=limits[i];
							String[] seq=new String[3];
							if(request.contains(">=")){
								String[] seqt=request.split(">=");
								seq[0]=seqt[0].trim();
								seq[1]=">=";
								seq[2]=seqt[1].trim();
							}else if(request.contains("<=")){
								String[] seqt=request.split("<=");
								seq[0]=seqt[0].trim();
								seq[1]="<=";
								seq[2]=seqt[1].trim();
							}else if(request.contains("<>")){
								String[] seqt=request.split("<>");
								seq[0]=seqt[0].trim();
								seq[1]="<>";
								seq[2]=seqt[1].trim();
							}else if(request.contains(">")){
								String[] seqt=request.split(">");
								seq[0]=seqt[0].trim();
								seq[1]=">";
								seq[2]=seqt[1].trim();
							}else if(request.contains("<")){
								String[] seqt=request.split("<");
								seq[0]=seqt[0].trim();
								seq[1]="<";
								seq[2]=seqt[1].trim();
							}else if(request.contains("=")){
								String[] seqt=request.split("=");
								seq[0]=seqt[0].trim();
								seq[1]="=";
								seq[2]=seqt[1].trim();
							}
							int c=seq.length;
							int j;
							for(j=0;j<cname.size();j++){
								if(seq[0].toLowerCase().equals(cname.get(j).toLowerCase())){
									flag2=1;
									if(isCorrect(getType(ctype.get(j)),seq[2])==1){
										lcname.add(j);
										lcmp.add(seq[1]);
										lvalue.add(seq[2]);
										flag2=2;
									}
									break;
								}
							}
							if(flag2==0){
								System.out.println("������������еĵ�"+String.valueOf(i+1)+"�в����������ݱ�"+tablename+"��");
								boolflag2=0;
								break;
							}
							if(flag2==1){
								if(isCorrect(getType(ctype.get(j)),seq[2])==0){
									if(getType(ctype.get(j)).equals("int")){
										System.out.println("������������еĵ�"+String.valueOf(i+1)+"�е�����ֵ��ʽ����,����ת��Ϊ������");
									}else if(getType(ctype.get(j)).equals("double")){
										System.out.println("������������еĵ�"+String.valueOf(i+1)+"�е�����ֵ��ʽ����,����ת��ΪС����");
									}
								}else if(isCorrect(getType(ctype.get(j)),seq[2])==2){
									System.out.println("������������еĵ�"+String.valueOf(i+1)+"�е�����ֵ��ʽ����,�ַ���δ�������Ű�����");
								} 
								boolflag2=0;
								break;
							}
						} 
						if(boolflag2==1){
							ArrayList<Integer> issatisfy=new ArrayList<Integer>();
							for(int i=0;i<(int)r.length()/rowlen;i++){
								int j=0;
								while(j<lcname.size()){
									if(compare(String.valueOf(table[i][lcname.get(j)]),lcmp.get(j),lvalue.get(j),getType(ctype.get(lcname.get(j))))!=1){
										break;
									}
									j++;
								}
								if(j==lcname.size()){
									issatisfy.add(1);
								}else{
									issatisfy.add(0);
								}
							}
							int d=0;
							for(int i=0;i<issatisfy.size();i++){
								if(issatisfy.get(i)==1){
									d++;
								}
							}
							Object[][] result0=new Object[d][reqsite.size()];
							int e=0;
							for(int i=0;i<(int)r.length()/rowlen;i++){
								if(issatisfy.get(i)==1){
									for(int j=0;j<reqsite.size();j++){
										result0[e][j]=table[i][reqsite.get(j)];
									}
									e++;
								}
							}
							String[] sctmp=sql.split("by");
							String sortcname=sctmp[1];
							String[] sortcnames=sortcname.split(",");
							int flago=0;
							for(int v=0;v<sortcnames.length;v++){
								String stmp=sortcnames[v].replaceAll("asc","").replaceAll("desc","").trim();
								int site=-1;
								for(int j=0;j<cname.size();j++){
									if(stmp.toLowerCase().equals(cname.get(j).toLowerCase())){
										site=j;
									}
								}
								if(site==-1){
									System.out.println("������ĵ�"+String.valueOf(v+1)+"�������׼����������ݱ�"+tablename+"��");
									flago=1;
								}
							}
							if(flago==0){
								ArrayList<Integer> sites=new ArrayList<Integer>();
								ArrayList<Integer> iss=new ArrayList<Integer>();
								for(int v=0;v<sortcnames.length;v++){
									if(sortcnames[v].contains("desc")){
										iss.add(0);
									}else{
										iss.add(1);
									}
									//System.out.println(iss.get(v));
									sortcnames[v]=sortcnames[v].replaceAll("asc","").replaceAll("desc","").trim();
									for(int j=0;j<cname.size();j++){
										if(sortcnames[v].toLowerCase().equals(cname.get(j).toLowerCase())){
											sites.add(j);
										}
									}
								}
								Object[][] byObjects=new Object[d][sites.size()];
								int c=0;
								for(int i=0;i<(int)r.length()/rowlen;i++){
									if(issatisfy.get(i)==1){
										for(int v=0;v<sites.size();v++){
											byObjects[c][v]=table[i][sites.get(v)];
										}
										c++;
									}
								}
								String[] types=new String[sites.size()];
								for(int i=0;i<sites.size();i++){	
									types[i]=getType(ctype.get(sites.get(i)));
								}
								Object[][] result=sortedDisplay(result0,byObjects,iss,types);
								SqlResult sr=new SqlResult("",result);
								System.out.println(sr.toString());
								System.out.println("����"+String.valueOf(d)+"����¼");
							}
						}
					}
					r.close();
				}
				
			}else{
					System.out.println("���ݱ�"+tablename+"�����ڣ�");
			}
		}else if(tmp[0].toLowerCase().contains("delete")){//ɾ�����ݱ��еļ�¼
			sql=sql.toLowerCase();
			String[] s1=sql.split("where");
			String s2=s1[0].replaceAll(" ","");
			String tablename=s2.replaceFirst("deletefrom","").toLowerCase();//��ȡ����
			int u;
			for(u=0;u<tablenames.size();u++){
				if(tablename.toLowerCase().equals(tablenames.get(u).toLowerCase())){
					break;
				}
			}
			if(u<tablenames.size()){//�ҵ���
				String s3="";
				if(sql.contains("where")){
					s3=s1[1];
				}else{
					s3=s1[0];
				}
				s3=s3.replace(">\\s+=",">=");//ȥ����������֮��Ŀո�
				s3=s3.replace("<\\s+=","<=");
				s3=s3.replace("<\\s+>","<>");
				ArrayList<String> reqnames=new ArrayList<String>();//��������
				ArrayList<String> cmps=new ArrayList<String>();//�ȽϷ���
				ArrayList<String> reqvalues=new ArrayList<String>();//�Ƚ�ֵ
				String[] reqs=s3.split(",");
				for(int v=0;v<reqs.length;v++){//��ȡ����
					String request=reqs[v];
					if(request.contains(">=")){
						String[] re=request.split(">=");
						reqnames.add(re[0].trim());
						cmps.add(">=");
						reqvalues.add(re[1].trim());
					}else if(request.contains("<=")){
						String[] re=request.split("<=");
						reqnames.add(re[0].trim());
						cmps.add("<=");
						reqvalues.add(re[1].trim());
					}else if(request.contains("<>")){
						String[] re=request.split("<>");
						reqnames.add(re[0].trim());
						cmps.add("<>");
						reqvalues.add(re[1].trim());
					}else if(request.contains(">")){
						String[] re=request.split(">");
						reqnames.add(re[0].trim());
						cmps.add(">");
						reqvalues.add(re[1].trim());
					}else if(request.contains("<")){
						String[] re=request.split("<");
						reqnames.add(re[0].trim());
						cmps.add("<");
						reqvalues.add(re[1].trim());
					}else if(request.contains("=")){
						String[] re=request.split("=");
						reqnames.add(re[0].trim());
						cmps.add("=");
						reqvalues.add(re[1].trim());
					}
				}
				RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","r");
				byte[] bs=new byte[32];
				ArrayList<String> cname=new ArrayList<String>();
				ArrayList<String> ctype=new ArrayList<String>();
				ArrayList<String> cwidth=new ArrayList<String>();
				for(int i=0;i<raf.length()/129;i++){//��ȡ�����б��������б�����б�
					raf.read(bs);
					String n=new String(bs,StandardCharsets.UTF_8).trim();
					if(tablename.toLowerCase().equals(n.toLowerCase())){
						raf.read(bs);
						String cn=new String(bs,StandardCharsets.UTF_8).trim();
						cname.add(cn);
						raf.read(bs);
						String ct=new String(bs,StandardCharsets.UTF_8).trim();
						ctype.add(ct);
						raf.read(bs);
						String cw=new String(bs,StandardCharsets.UTF_8).trim();
						cwidth.add(cw);
						raf.read();//���з�
					}else{
						raf.read(bs);
						raf.read(bs);
						raf.read(bs);
						raf.read();
					}
				}
				raf.close();
				int finalFlag=0;
				for(int g=0;g<reqnames.size();g++){
					int f1=0;
					int i;
					for(i=0;i<cname.size();i++){
						if(reqnames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){//�ж������Ƿ����
							f1=1;
							if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==1){//�жϸ�ʽ�Ƿ���ȷ
								f1=2;
							}
							break;
						}
					}
					if(f1==0){
						//System.out.println(reqnames.get(g));
						System.out.println("������������еĵ�"+String.valueOf(g+1)+"�������Ʋ�������"+tablename+"��");
						finalFlag=1;
						break;
					}
					if(f1==1){
						//System.out.println("������������еĵ�"+String.valueOf(g+1)+"���е�����ֵ��ʽ����");
						if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==0){
							if(getType(ctype.get(i)).equals("int")){
								System.out.println("������������еĵ�"+String.valueOf(g+1)+"���е�����ֵ��ʽ���󣬲���ת��Ϊ������");
							}else{
								System.out.println("������������еĵ�"+String.valueOf(g+1)+"���е�����ֵ��ʽ���󣬲���ת��ΪС����");
							}
						}
						if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==2){
							System.out.println("������������еĵ�"+String.valueOf(g+1)+"���е�����ֵ��ʽ�����ַ���δ�������Ű�����");
						}
						finalFlag=1;
						break;
					}
				}
				if(finalFlag==0){
					int rowlen=0; 
					for(int i=0;i<ctype.size();i++){
						String type=getType(ctype.get(i));
						if(type.equals("int")){
							rowlen+=4;
						}else if(type.equals("double")){
							rowlen+=8;
						}else{
							rowlen+=Integer.valueOf(cwidth.get(i))*3+2;
						}
					}
					rowlen+=1;//��ȡÿ�е��ֽ���
					ArrayList<Integer> reqnamesites=new ArrayList<Integer>();
					for(int g=0;g<reqnames.size();g++){//��ȡ����������Ӧ���±��б�
						for(int i=0;i<cname.size();i++){
							if(reqnames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){
								int reqnamesite=i;
								reqnamesites.add(i);
							}
						}
					}
					String rname=tablename+".midb";
					RandomAccessFile r=new RandomAccessFile(db+"/"+rname,"r");//��ȡ���ݱ�
					ArrayList<Integer> issatisfy=new ArrayList<Integer>();
					int count=0;
					for(int i=0;i<(int)r.length()/rowlen;i++){
						int j=0;
						int boolflag=0;
						while(j<cname.size()){
							String type=getType(ctype.get(j));
							byte[] rbs;
							if(type.equals("int")){
								rbs=new byte[4];
								r.read(rbs);
							}else if(type.equals("double")){
								rbs=new byte[8];
								r.read(rbs);
							}else{
								rbs=new byte[Integer.valueOf(cwidth.get(j))*3+2];
								r.read(rbs);
							}//�������Ͱ��ֽڶ�ȡ
							for(int g=0;g<reqnamesites.size();g++){//�����������±��б�
								if(j==reqnamesites.get(g)){//�����Ҫ���жϵ���
									String ct=new String(rbs,StandardCharsets.UTF_8).trim();
									if(compare(ct,cmps.get(g),reqvalues.get(g),type)==1){//��ǰ����������
										boolflag+=1;
									}
								}
							}
							j++;
						}
						if(boolflag==reqnamesites.size()){//����Ҫ����ж�������������ü�¼��������
							issatisfy.add(1);
							count++;
						}else{
							issatisfy.add(0);
						}
						r.read();//�����з�
					}
					r.close();
					RandomAccessFile r1=new RandomAccessFile(db+"/"+rname,"r");
					RandomAccessFile wtmp=new RandomAccessFile("Tmp.midb","rw");
					for(int i=0;i<(int)r1.length()/rowlen;i++){
						int j=0;
						while(j<cname.size()){
							String type=getType(ctype.get(j));
							byte[] wbs;
							if(type.equals("int")){
								wbs=new byte[4];
								r1.read(wbs);
							}else if(type.equals("double")){
								wbs=new byte[8];
								r1.read(wbs);
							}else{
								wbs=new byte[Integer.valueOf(cwidth.get(j))*3+2];
								r1.read(wbs);
							}//�������Ͱ��ֽڶ�ȡ
							if(issatisfy.get(i)!=1){
								wtmp.write(wbs);
							}
							j++;
						}
						r1.read();//�����з�
						if(issatisfy.get(i)!=1){
							wtmp.writeBytes("\n");
						}
					}
					r1.close();
					wtmp.close();
					File file=new File(db+"/"+rname);
					file.delete();//ɾ��ԭ�ļ����������ɾ������������ݲ��ܸ���ԭ�ļ�����ɵĴ���
					RandomAccessFile w=new RandomAccessFile(db+"/"+rname,"rw");
					RandomAccessFile rtmp=new RandomAccessFile("Tmp.midb","r");
					for(int i=0;i<(int)rtmp.length()/rowlen;i++){
						int j=0;
						while(j<cname.size()){
							String type=getType(ctype.get(j));
							byte[] wbs;
							if(type.equals("int")){
								wbs=new byte[4];
								rtmp.read(wbs);
							}else if(type.equals("double")){
								wbs=new byte[8];
								rtmp.read(wbs);
							}else{
								wbs=new byte[Integer.valueOf(cwidth.get(j))*3+2];
								rtmp.read(wbs);
							}//�������Ͱ��ֽڶ�ȡ
							w.write(wbs);
							j++;
						}
						rtmp.read();
						w.writeBytes("\n");
					}
					w.close();
					rtmp.close();
					File dwtmp=new File("Tmp.midb");
					dwtmp.delete();
					System.out.println("��ɾ����"+String.valueOf(count)+"����¼");
				}
			}else{//δ�ҵ���
				System.out.println("���ݱ�"+tablename+"�����ڣ�");
			}
		}else{
			System.out.println("����ʶ���ָ�����");
		}
		
	}
	public static void main(String [] args)throws IOException{
		System.out.println("��ӭʹ���������ݿ����ϵͳ����ʹ��help�����ѯ���в�������");
		Properties properties = new Properties();
		try {
            			properties.load(new FileInputStream("Config.properties"));
            			String dbHomePath = properties.getProperty("dbHome");
			File tmpFile=new File(dbHomePath);
			if(!tmpFile.exists()){//�����Ŀ¼�����ڣ��򴴽�Ŀ¼
				tmpFile.mkdirs();
			}
           			TableManager.db=dbHomePath;
			File[] result=tmpFile.listFiles();
			for(int i=0;i<result.length;i++){//��ʼ��tablenames�б���ԭ�����ݵ����ݱ�����ӵ��б���
				File fs=result[i];
				if(fs.isFile()){//�ж��ļ��Ƿ����
					if(fs.getName().contains(".midb")){
						TableManager.tablenames.add(fs.getName().replaceAll(".midb",""));
					}
				}
			}
        		} catch (IOException ioe) {//�쳣����
        		}
		while(true){
			System.out.println(" ");
			System.out.print("miDB>");//��ʾ
			InputStreamReader is=new InputStreamReader(System.in,"GBK");
			BufferedReader br=new BufferedReader(is);
			String req=br.readLine();//��ȡ����
			if(req.replaceAll(" ","").toLowerCase().equals("quit")){//��������
				break;
			}else if(req.toLowerCase().replaceAll(" ","").equals("help")){//helpָ���ʾָ������
				System.out.println("quit ---- �˳��������ݿ����ϵͳ��");
				System.out.println("help ---- ��ʾ���е�ָ�");
				System.out.println("show tables -- ��ʾĿǰ�������ݱ�");
				System.out.println("desc XXX -- ��ʾ���ݱ�XXX�еı�ṹ��");
				System.out.println("create table XXX(columnA varchar(10), columnB int, columnC decimal) ---- ����һ��3�е�����ΪXXX�ı�������ֱ�ΪcolumnA��columnB��columnC�������ͷֱ�Ϊ10�����ڵ��ַ�����������С����");
				System.out.println("drop table XXX -- ɾ�����XXX��");
				System.out.println("select colX, colY,colX from XXX where colZ > 1.5 order by colZ desc ---- �����ݱ�XXX��ѡȡ3�У�colX��colY��colX��ÿһ����¼��������colZ��ֵ����1.5 ����ʾʱ����colZ��һ�еĽ������С�");
				System.out.println("select * from XXX where colA <> '������Ϣ'  ---- �����ݱ�XXX��ѡȡ���е��У�����¼Ҫ������colA���Ǳ�����Ϣ��");
				System.out.println("insert into XXX values('������Ϣ', 15, 25.5) ---- �����ݱ���׷��һ����¼�������е�ֵ�ֱ�Ϊ������Ϣ��15��25.5��");
				System.out.println("delete from XXX where colB = 10 ---- �ѱ��XXX��colB�е�ֵ��10�ļ�¼ȫ��ɾ����");
				System.out.println("update XXX set colD = '�������ѧ�뼼��' where colA = '������Ϣ' ---- �����ݱ�XXX�У�����ЩcolA�Ǳ�����Ϣ�ļ�¼�е�colD��ȫ����дΪ�������ѧ�뼼����");
				System.out.println("ע�⣺����ָ����ִ�Сд���������ݱ�Student�����ݱ�studentһ�£�helpָ����HELPָ�������ͬ");
				System.out.println("���ڴ������ݱ�ʱ������ͬ�ᵼ����ز������ң����Դ������ݱ�ʱ������������ͬ");
			}else{
				operate(req);//������ָ����д���
			}
		}
	}
}

