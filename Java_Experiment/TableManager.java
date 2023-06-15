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
	public static String getType(String s){//得到类型
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
	public static int isCorrect(String type,String in){//判断输入数据是否可以转换为要求的类型，String类型数据判断是否被单引号包裹
		if(type.equals("int")){
			try{
				Integer.valueOf(in);
				return 1;
			}catch(Exception e){//如果不可以转换，则会捕获异常，则返回不能转换的标志
				return 0;
			}
		}else if(type.equals("double")){
			try{
				Double.valueOf(in);
				return 1;
			}catch(Exception e){//如果不可以转换，则会捕获异常，则返回不能转换的标志
				return 0;
			}
		}else{
			if(!String.valueOf(in.charAt(0)).equals("'")||!String.valueOf(in.charAt(in.length()-1)).equals("'")){//String类型数据判断是否被单引号包裹
				return 2;
			}else{
				return 1;
			}
		}
	}
	public static int compare(String v1,String cmp,String v2,String type){//简单的比较
		int result=0;
		if(type.equals("int")){//先判断类型
			int value1=Integer.valueOf(v1);//转换为对应类型
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
	public static Object[][] sortedDisplay(Object[][] objects,Object[][] byObjects,ArrayList<Integer> flagAscend,String[] type){//用于排序的内部类
		class LineAndBy implements Comparable<LineAndBy>{//实现Comparable接口
			Object[] line;
			Object[] byObject;
			int bycount;
			LineAndBy(Object[] line,Object[] byObject,int bycount){//构造函数
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
		String[] tmp=sql.split("\\s+");//拆分以多个空格为分割的字符串
		int a=tmp.length;
		while(tmp[0].equals("")){//如果用户在输入的前面输入了空格，则分出来的数组第一项为空，为避免这种情况，必须保证，第一项包含关键字
			for(int i=0;i<a-1;i++){
				tmp[i]=tmp[i+1];
			}
			tmp[a-1]="";
			a--;
		}
		//根据第一个输入的词汇判断用户需求
		if(tmp[0].toLowerCase().contains("show")){//显示表
			ArrayList<String> namelist=new ArrayList<String>();
			RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","rw");//读结构文件
			byte[] bs=new byte[32];
			for(int i=0;i<raf.length()/129;i++){
				raf.read(bs);//表名
				String n=new String(bs,StandardCharsets.UTF_8).trim();
				if(namelist.size()==0){//第一个读取的表名，一定不会和之前的表名重复
					namelist.add(n);
				}else if(!n.equals(namelist.get(namelist.size()-1))){//如果读取的表名与上一个不同，则添加至表名列表中
					namelist.add(n);
				}
				raf.read(bs);//列名
				raf.read(bs);//列类型
				raf.read(bs);//宽度
				raf.read();//换行符
			}
			raf.close();
			System.out.println("数据表");
			for(int i=0;i<namelist.size();i++){
				System.out.println(namelist.get(i));
			}
			System.out.println("共有"+String.valueOf(namelist.size())+"个数据表");
		}else if(tmp[0].toLowerCase().contains("create")){//创建表
			int i;
			for(i=0;i<sql.length();i++){//从前向后找到第一个前括号
				if(String.valueOf(sql.charAt(i)).equals("(")){
					break;
				}
			}
			int k;
			for(k=sql.length()-1;k>i;k--){//从后向前获取最后一个后括号所在位置
				if(String.valueOf(sql.charAt(k)).equals(")")){
					break;
				}
			}
			if(i==sql.length()||k==i||k==i+1){//如果没有识别到前括号或后括号，或括号中没有内容
				System.out.println("您未定义数据表中列名以及数据类型 或 您的定义未包含在一对括号中");
			}else{
				String findname=sql.substring(0,i);//截取前括号之前的字符串，获取包含表的名字的字符串
				String[] t=findname.split("\\s+");//由于table和表名之间必须有空格，所以以多个空格为分割
				int j;
				for(j=0;j<t.length;j++){
					if(t[j].toLowerCase().contains("table")){
						break;
					}
				}
				String name=t[j+1].toLowerCase();//获取表的名字
				int u;
				for(u=0;u<tablenames.size();u++){
					if(name.toLowerCase().equals(tablenames.get(u).toLowerCase())){
						break;
					}
				}
				if(!name.equals("")&&u==tablenames.size()){//已有表名列表中不包含待创建的表名
					ArrayList<String> cnames=new ArrayList<String>();
					String findtable=sql.substring(i+1,k);//获取包含列参数的字符串
					String[] t2=findtable.split(",");//每个列的相关参数分为一组
					int f1=0;
					for(k=0;k<t2.length;k++){//遍历每一个列的相关参数
						String[] c=t2[k].split("\\s+");//将列名与列类型分开
						int b=c.length;
						while(c[0].equals("")){//如果列名前输入空格，则要将数组前移
							for(int h=0;h<b-1;h++){
								c[h]=c[h+1];
							}
							c[b-1]="";
							b--;	
						}
						String cname=c[0];//获取列名
						int l=0;
						while(l<cnames.size()){
							if(cname.equals(cnames.get(l))){//判断列名是否存在重复的情况,若存在提示重新输入列名
								System.out.println("第"+String.valueOf(k+1)+"列的列名与"+"第"+String.valueOf(l+1)+"列的列名重复!");
								f1=1;
								break;
							}else{
								l++;
							}
						}
						String ctype=c[1].toLowerCase();//获取列的类型
						if(!ctype.equals("int")&&!ctype.equals("decimal")&&!ctype.contains("varchar")){//判断列的类型是否错误
							System.out.println("您输入的第"+String.valueOf(k+1)+"个列的数据类型错误，您可以输入的类型有int、decimal、varchar(字符数)");
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
							if(m==ctype.length()||n==m){//判断字符串长度是否用括号包裹
								System.out.println("您输入的第"+String.valueOf(k+1)+"列的varchar类型的数据，字符串长度格式存在错误，应该用括号包裹!");
								f1=1;
							}else{//判断格式是否错误
								int wi;
								try{
									wi=Integer.valueOf(ctype.substring(m+1,n));
									if(wi<0){
										System.out.println("您输入的第"+String.valueOf(k+1)+"列的varchar类型的数据，字符串长度存在错误，长度必须为非负数!");
										f1=1;
									}
								}catch(Exception e){
									System.out.println("您输入的第"+String.valueOf(k+1)+"列的varchar类型的数据，字符串长度存在错误，您提供的长度不能转换成整数!");
									f1=1;
								}
							}
						}
					}
					if(f1==0){//语句格式正确
						tablenames.add(name);//向表名列表中添加此表
						String fname=name+".midb";//数据表文件名
						File datatable=new File(db+"/"+fname);
						datatable.createNewFile();//创建对应文件
						RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","rw");
						raf.seek(raf.length());//定位到文件末尾位置
						for(k=0;k<t2.length;k++){
							String[] c=t2[k].split("\\s+");//将列名与列类型分开
							int b=c.length;
							while(c[0].equals("")){//如果列名前输入空格，则要将数组前移
								for(int h=0;h<b-1;h++){
									c[h]=c[h+1];
								}
								c[b-1]="";
								b--;	
							}
							String cname=c[0];//获取列名
							String ctype=c[1];//获取列的类型
							int width=-1;
							byte[] data=name.getBytes(StandardCharsets.UTF_8);
							data=Arrays.copyOf(data,32);
							raf.write(data);//写入表名
							data=cname.getBytes(StandardCharsets.UTF_8);
							data=Arrays.copyOf(data,32);
							raf.write(data);//写入列名
							if(ctype.equals("int")||ctype.equals("decimal")){
								data=ctype.getBytes(StandardCharsets.UTF_8);
								data=Arrays.copyOf(data,32);
								raf.write(data);//写入类型名
								width=1;//宽度为1
							}else if(ctype.contains("varchar")){//如果为字符串类型
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
								width=Integer.valueOf(ctype.substring(m+1,n));//获取括号中的值
							}
							data=String.valueOf(width).getBytes(StandardCharsets.UTF_8);
							data=Arrays.copyOf(data,32);
							raf.write(data);
							raf.writeBytes("\n");
						}
						raf.close();
						System.out.println("创建数据表"+name+"成功！");
					}
				}else if(name.equals("")){
					System.out.println("数据表名不能为空");
				}else{//已有表名中包含待创建列表
					System.out.println("同名数据表已存在！");
				}
			}
		}else if(tmp[0].toLowerCase().contains("desc")){//显示结构
			sql=sql.toLowerCase();
			sql=sql.replaceAll(" ","");//去除空格
			String table=sql.replaceFirst("desc","");//去除第一个desc
			RandomAccessFile r=new RandomAccessFile("TableInfo.ti","r");//读表结构文件
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
					if(flag==0){//第一次找到记录
						System.out.println("列名称"+", "+"列类型"+", "+"列宽度");
					}
					System.out.println(cname+", "+ctype+", "+width);
					flag=1;//找到记录后标志为1
				}
				r.read();
			}
			if(flag==0){//没有找到目标数据表的记录
				System.out.println("数据表"+table+"不存在");
			}
			r.close();
		}else if(tmp[0].toLowerCase().contains("drop")){//删除数据表
			sql=sql.toLowerCase();
			String table=sql.replaceAll(" ","").replaceFirst("droptable","");//去掉所有空格后去掉droptable
			String ftablename=table+".midb";//数据表文件名
			int u;
			for(u=0;u<tablenames.size();u++){
				if(table.toLowerCase().equals(tablenames.get(u).toLowerCase())){
					break;
				}
			}
			if(u<tablenames.size()){//找到该文件
				tablenames.remove(table);//从列表中移除该数据表名
				File ftable=new File(db+"/"+ftablename);
				ftable.delete();//删除该文件
				RandomAccessFile r=new RandomAccessFile("TableInfo.ti","r");//读结构文件
				RandomAccessFile wtmp=new RandomAccessFile("Tmp.ti","rw");//写临时文件
				byte[] bs=new byte[32];
				int count=0;
				for(int i=0;i<r.length()/129;i++){//遍历结构文件，如果表名与要删除的表名不同则写入临时文件
					r.read(bs);
					String n=new String(bs,StandardCharsets.UTF_8).trim();
					if(!n.toLowerCase().equals(table.toLowerCase())){
						byte[] tmpdata=new byte[32];
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//表名
						r.read(bs);
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//列名
						r.read(bs);
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//列类型
						r.read(bs);
						tmpdata=Arrays.copyOf(bs,32);
						wtmp.write(tmpdata);//宽度
						r.read();
						wtmp.writeBytes("\n");
					}else{
						count++;
						for(int j=0;j<3;j++){
							r.read(bs);
						}
						r.read();//读取换行符
					}
				}
				r.close();
				wtmp.close();
				File file=new File("TableInfo.ti");
				file.delete();//删除原文件，避免进行删除操作后的数据不能覆盖原文件而造成的错误
				RandomAccessFile dr=new RandomAccessFile("TableInfo.ti","rw");//写结构文件
				RandomAccessFile dtmp=new RandomAccessFile("Tmp.ti","r");//读临时文件
				byte[] tmpdata=new byte[32];
				for(int i=0;i<dtmp.length()/129;i++){//将临时文件全部写入结构文件
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//表名
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//列名
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//列类型
					dtmp.read(bs);
					tmpdata=Arrays.copyOf(bs,32);
					dr.write(tmpdata);//宽度
					dtmp.read();
					dr.writeBytes("\n");
				}
				dr.close();
				dtmp.close();
				File tmpfile=new File("Tmp.ti");
				tmpfile.delete();//删除临时文件
				System.out.println("删除数据表"+table+"成功");
			}else{//未找到该文件
				System.out.println("数据表"+table+"不存在");
			}
		}else if(tmp[0].toLowerCase().contains("insert")){//向数据表中插入
			String[] t1=sql.split("\\s+");//因为into和数据表之间一定存在空格，所以以多个空格为分割
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
			if(u<tablenames.size()){//查询到该数据表
				RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","r");
				byte[] bs=new byte[32];
				ArrayList<String> cname=new ArrayList<String>();
				ArrayList<String> ctype=new ArrayList<String>();
				ArrayList<String> cwidth=new ArrayList<String>();
				//System.out.println(raf.length());
				for(int i=0;i<raf.length()/129;i++){//获取列名列表、列类型列表、宽度列表
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
						raf.read();//换行符
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
					System.out.println("您输入的参数值格式错误，未用括号包裹 或 括号中无参数！");
				}else{
					String values=sql.substring(i+1,j);
					if(values.contains(",")){//有大于等于2个参数的情况
						//System.out.println(raf.length());
						String[] vs=values.split(",");//以,分割获取各个参数
						if(vs.length!=cname.size()){//判断参数数量是否与该表列数量相等
							System.out.println("您输入的参数数量与构建"+tablename+"数据表中记录所需参数不符，"+"您需要输入"+String.valueOf(cname.size())+"个参数");
						}else{
							int flag=0;
							for(int k=0;k<vs.length;k++){//判断输入的值
								String type=getType(ctype.get(k));
								vs[k]=vs[k].trim();//去掉前后的空格
								if(isCorrect(type,vs[k])==1){
									if(type.equals("String")){
										if(vs[k].length()-2>Integer.valueOf(cwidth.get(k))*3){
											System.out.println("您提供的第"+String.valueOf(k+1)+"个字符串长度，超过"+tablename+"中第"+String.valueOf(k+1)+"项的规定字符长度!");
											flag=1;
										}
									}
								}else if(isCorrect(type,vs[k])==2){
									System.out.println("您提供的第"+String.valueOf(k+1)+"个字符串格式错误，应该用单引号包裹");
									flag=1;
								}else{
									String h;
									if(type.equals("int")){
										h="整数";
									}else{
										h="小数";
									}
									System.out.println("您提供的第"+String.valueOf(k+1)+"个值不能转换为"+h);
									flag=1;
								}
							}
							if(flag==0){//确认输入无误后写入文件
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
									}else if(type.equals("String")){//如果是字符串类型，字节数为宽度乘3
										int b=Integer.valueOf(cwidth.get(k))*3+2;
										//System.out.println(b);
										bs1=Arrays.copyOf(bs1,b);
										tf.write(bs1);
									}
								}
								tf.writeBytes("\n");
								tf.close();
								System.out.println("向数据表"+tablename+"写入一条记录成功");
							}
						}
					}else{//只有一个参数的情况
						if(cname.size()!=1){
							System.out.println("您输入的参数数量与构建"+tablename+"数据表中记录所需参数不符，"+"您需要输入"+String.valueOf(cname.size())+"个参数");
						}else{
							//System.out.println(values);
							String type=getType(ctype.get(0));
							values=values.trim();//去掉首尾空格
							int flag=0;
							if(isCorrect(type,values)==1){
								if(type.equals("String")){
									if(values.length()-2>Integer.valueOf(cwidth.get(0))*3){
										System.out.println("您提供的第1个字符串长度，超过"+tablename+"中第1项的规定字符长度!");
										flag=1;
									}
								}
							}else if(isCorrect(type,values)==2){
								System.out.println("您提供的第1个字符串格式错误，应该用单引号包裹");
								flag=1;
							}else{
								String h;
								if(type.equals("int")){
									h="整数";
								}else{
									h="小数";
								}
								System.out.println("您提供的第1个值不能转换为"+h);
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
								System.out.println("向数据表"+tablename+"写入一条记录成功");
							}
						}
					}
				}
			}else{//未查询到该数据表
				System.out.println("数据表"+tablename+"不存在！");
			}
		}else if(tmp[0].toLowerCase().contains("update")){//更新数据表中的内容
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
			if(u<tablenames.size()){//找到数据表
				RandomAccessFile raf=new RandomAccessFile("TableInfo.ti","r");
				byte[] bs=new byte[32];
				ArrayList<String> cname=new ArrayList<String>();
				ArrayList<String> ctype=new ArrayList<String>();
				ArrayList<String> cwidth=new ArrayList<String>();
				for(int i=0;i<raf.length()/129;i++){//获取列名列表、列类型列表、宽度列表
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
						raf.read();//换行符
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
				rowlen+=1;//获取数据表文件中每一行的长度
				String[] keys=sql.split("set");
				String key=keys[1];//得到set后面的语句
				key=key.replace(">\\s+=",">=");
				key=key.replace("<\\s+=","<=");
				key=key.replace("<\\s+>","<>");//去除两个符号之间的空格
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
					for(int v=0;v<site1.length;v++){//获取条件
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
							if(changenames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){//判断列名是否存在
								f1=1;
								if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==1){//判断是否符合格式
									f1=2;
								}
								break;
							}
						}
						if(f1==0){
							System.out.println("您输入的要更新的第"+String.valueOf(g+1)+"个列名称不存在于"+tablename+"中");
							finalFlag=1;
							break;
						}
						if(f1==1){
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==0){
								if(getType(ctype.get(i)).equals("int")){
									System.out.println("您输入的要更新中的第"+String.valueOf(g+1)+"个列的更新值格式错误，不能转换为整数！");
								}else{
									System.out.println("您输入的要更新中的第"+String.valueOf(g+1)+"个列的更新值格式错误，不能转换为小数！");
								}
							}
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==2){
								System.out.println("您输入的要更新中的第"+String.valueOf(g+1)+"个列的更新值格式错误，字符串未被单引号包裹！");
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
							System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列名称不存在于"+tablename+"中");
							finalFlag=1;
							break;
						}
						if(f1==1){
							if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==0){
								if(getType(ctype.get(i)).equals("int")){
									System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列的条件值格式错误，不能转换为整数！");
								}else{
									System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列的条件值格式错误，不能转换为小数！");
								}
							}
							if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==2){
								System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列的条件值格式错误，字符串未被单引号包裹！");
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
							r.read();//读换行符
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
						System.out.println("共更新了"+String.valueOf(count)+"条记录");
					}
				}else{//无条件情况
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
								break;//满足条件
							}
						}
						if(f1==0){
							System.out.println("您输入的要更新的第"+String.valueOf(g+1)+"个列名称不存在于"+tablename+"中");
							finalFlag=1;
							break;
						}
						if(f1==1){
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==0){
								if(getType(ctype.get(i)).equals("int")){
									System.out.println("您输入的要更新中的第"+String.valueOf(g+1)+"个列的更新值格式错误，不能转换为整数！");
								}else{
									System.out.println("您输入的要更新中的第"+String.valueOf(g+1)+"个列的更新值格式错误，不能转换为小数！");
								}
							}
							if(isCorrect(getType(ctype.get(i)),changevalues.get(g))==2){
								System.out.println("您输入的要更新中的第"+String.valueOf(g+1)+"个列的更新值格式错误，字符串未被单引号包裹！");
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
						System.out.println("共更新了"+String.valueOf(count)+"条记录");
					}
				}
			}else{//未找到数据表
					System.out.println("数据表"+tablename+"不存在！");
			}
		}else if(tmp[0].toLowerCase().contains("select")){//选择数据表中的内容输出
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
			if(u<tablenames.size()){//找到数据表
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
				for(int i=0;i<raf.length()/129;i++){//获取列名列表、列类型列表、宽度列表
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
						raf.read();//换行符
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
							System.out.println("您要选择的第"+String.valueOf(i+1)+"列不存在于数据表"+tablename+"中！");
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
					}//获取选择的列
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
						r.read();//读换行符
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
						System.out.println("共有"+String.valueOf((int)r.length()/rowlen)+"条记录");
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
								System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列不存在于数据表"+tablename+"中");
								boolflag2=0;
								break;
							}
							if(flag2==1){
								if(isCorrect(getType(ctype.get(j)),seq[2])==0){
									if(getType(ctype.get(j)).equals("int")){
										System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列的条件值格式错误,不能转换为整数！");
									}else if(getType(ctype.get(j)).equals("double")){
										System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列的条件值格式错误,不能转换为小数！");
									}
								}else if(isCorrect(getType(ctype.get(j)),seq[2])==2){
									System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列的条件值格式错误,字符串未被单引号包裹！");
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
							System.out.println("共有"+String.valueOf(e)+"条记录");
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
								System.out.println("您输入的第"+String.valueOf(v+1)+"个排序标准项不存在于数据表"+tablename+"中");
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
							System.out.println("共有"+String.valueOf((int)r.length()/rowlen)+"条记录");
						}
					}else{//两个都包含
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
								System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列不存在于数据表"+tablename+"中");
								boolflag2=0;
								break;
							}
							if(flag2==1){
								if(isCorrect(getType(ctype.get(j)),seq[2])==0){
									if(getType(ctype.get(j)).equals("int")){
										System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列的条件值格式错误,不能转换为整数！");
									}else if(getType(ctype.get(j)).equals("double")){
										System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列的条件值格式错误,不能转换为小数！");
									}
								}else if(isCorrect(getType(ctype.get(j)),seq[2])==2){
									System.out.println("您输入的条件中的第"+String.valueOf(i+1)+"列的条件值格式错误,字符串未被单引号包裹！");
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
									System.out.println("您输入的第"+String.valueOf(v+1)+"个排序标准项不存在于数据表"+tablename+"中");
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
								System.out.println("共有"+String.valueOf(d)+"条记录");
							}
						}
					}
					r.close();
				}
				
			}else{
					System.out.println("数据表"+tablename+"不存在！");
			}
		}else if(tmp[0].toLowerCase().contains("delete")){//删除数据表中的记录
			sql=sql.toLowerCase();
			String[] s1=sql.split("where");
			String s2=s1[0].replaceAll(" ","");
			String tablename=s2.replaceFirst("deletefrom","").toLowerCase();//获取表名
			int u;
			for(u=0;u<tablenames.size();u++){
				if(tablename.toLowerCase().equals(tablenames.get(u).toLowerCase())){
					break;
				}
			}
			if(u<tablenames.size()){//找到表
				String s3="";
				if(sql.contains("where")){
					s3=s1[1];
				}else{
					s3=s1[0];
				}
				s3=s3.replace(">\\s+=",">=");//去掉两个符号之间的空格
				s3=s3.replace("<\\s+=","<=");
				s3=s3.replace("<\\s+>","<>");
				ArrayList<String> reqnames=new ArrayList<String>();//条件列名
				ArrayList<String> cmps=new ArrayList<String>();//比较符号
				ArrayList<String> reqvalues=new ArrayList<String>();//比较值
				String[] reqs=s3.split(",");
				for(int v=0;v<reqs.length;v++){//获取条件
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
				for(int i=0;i<raf.length()/129;i++){//获取列名列表、列类型列表、宽度列表
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
						raf.read();//换行符
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
						if(reqnames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){//判断列名是否存在
							f1=1;
							if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==1){//判断格式是否正确
								f1=2;
							}
							break;
						}
					}
					if(f1==0){
						//System.out.println(reqnames.get(g));
						System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列名称不存在于"+tablename+"中");
						finalFlag=1;
						break;
					}
					if(f1==1){
						//System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列的条件值格式错误！");
						if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==0){
							if(getType(ctype.get(i)).equals("int")){
								System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列的条件值格式错误，不能转换为整数！");
							}else{
								System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列的条件值格式错误，不能转换为小数！");
							}
						}
						if(isCorrect(getType(ctype.get(i)),reqvalues.get(g))==2){
							System.out.println("您输入的条件中的第"+String.valueOf(g+1)+"个列的条件值格式错误，字符串未被单引号包裹！");
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
					rowlen+=1;//获取每行的字节数
					ArrayList<Integer> reqnamesites=new ArrayList<Integer>();
					for(int g=0;g<reqnames.size();g++){//获取条件列名对应的下标列表
						for(int i=0;i<cname.size();i++){
							if(reqnames.get(g).toLowerCase().equals(cname.get(i).toLowerCase())){
								int reqnamesite=i;
								reqnamesites.add(i);
							}
						}
					}
					String rname=tablename+".midb";
					RandomAccessFile r=new RandomAccessFile(db+"/"+rname,"r");//读取数据表
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
							}//根据类型按字节读取
							for(int g=0;g<reqnamesites.size();g++){//遍历条件列下标列表
								if(j==reqnamesites.get(g)){//如果是要求判断的列
									String ct=new String(rbs,StandardCharsets.UTF_8).trim();
									if(compare(ct,cmps.get(g),reqvalues.get(g),type)==1){//当前列满足条件
										boolflag+=1;
									}
								}
							}
							j++;
						}
						if(boolflag==reqnamesites.size()){//所有要求的列都满足条件，则该记录满足条件
							issatisfy.add(1);
							count++;
						}else{
							issatisfy.add(0);
						}
						r.read();//读换行符
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
							}//根据类型按字节读取
							if(issatisfy.get(i)!=1){
								wtmp.write(wbs);
							}
							j++;
						}
						r1.read();//读换行符
						if(issatisfy.get(i)!=1){
							wtmp.writeBytes("\n");
						}
					}
					r1.close();
					wtmp.close();
					File file=new File(db+"/"+rname);
					file.delete();//删除原文件，避免进行删除操作后的数据不能覆盖原文件而造成的错误
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
							}//根据类型按字节读取
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
					System.out.println("共删除了"+String.valueOf(count)+"条记录");
				}
			}else{//未找到表
				System.out.println("数据表"+tablename+"不存在！");
			}
		}else{
			System.out.println("不能识别该指令，请检查");
		}
		
	}
	public static void main(String [] args)throws IOException{
		System.out.println("欢迎使用迷你数据库管理系统！请使用help命令查询所有操作介绍");
		Properties properties = new Properties();
		try {
            			properties.load(new FileInputStream("Config.properties"));
            			String dbHomePath = properties.getProperty("dbHome");
			File tmpFile=new File(dbHomePath);
			if(!tmpFile.exists()){//如果该目录不存在，则创建目录
				tmpFile.mkdirs();
			}
           			TableManager.db=dbHomePath;
			File[] result=tmpFile.listFiles();
			for(int i=0;i<result.length;i++){//初始化tablenames列表，将原有数据的数据表名添加到列表中
				File fs=result[i];
				if(fs.isFile()){//判断文件是否存在
					if(fs.getName().contains(".midb")){
						TableManager.tablenames.add(fs.getName().replaceAll(".midb",""));
					}
				}
			}
        		} catch (IOException ioe) {//异常处理
        		}
		while(true){
			System.out.println(" ");
			System.out.print("miDB>");//提示
			InputStreamReader is=new InputStreamReader(System.in,"GBK");
			BufferedReader br=new BufferedReader(is);
			String req=br.readLine();//获取输入
			if(req.replaceAll(" ","").toLowerCase().equals("quit")){//结束运行
				break;
			}else if(req.toLowerCase().replaceAll(" ","").equals("help")){//help指令，显示指令内容
				System.out.println("quit ---- 退出迷你数据库管理系统。");
				System.out.println("help ---- 显示所有的指令。");
				System.out.println("show tables -- 显示目前所有数据表。");
				System.out.println("desc XXX -- 显示数据表XXX中的表结构。");
				System.out.println("create table XXX(columnA varchar(10), columnB int, columnC decimal) ---- 创建一个3列的名称为XXX的表格，列名分别为columnA、columnB、columnC，其类型分别为10个以内的字符、整型数和小数。");
				System.out.println("drop table XXX -- 删除表格XXX。");
				System.out.println("select colX, colY,colX from XXX where colZ > 1.5 order by colZ desc ---- 从数据表XXX中选取3列，colX，colY，colX，每一个记录必须满足colZ的值大于1.5 且显示时按照colZ这一列的降序排列。");
				System.out.println("select * from XXX where colA <> '北林信息'  ---- 从数据表XXX中选取所有的列，但记录要满足列colA不是北林信息。");
				System.out.println("insert into XXX values('北林信息', 15, 25.5) ---- 向数据表中追加一条记录，各个列的值分别为北林信息、15、25.5。");
				System.out.println("delete from XXX where colB = 10 ---- 把表格XXX中colB列的值是10的记录全部删除。");
				System.out.println("update XXX set colD = '计算机科学与技术' where colA = '北林信息' ---- 在数据表XXX中，把那些colA是北林信息的记录中的colD列全部改写为计算机科学与技术。");
				System.out.println("注意：输入指令不区分大小写，例如数据表Student与数据表student一致，help指令与HELP指令操作相同");
				System.out.println("由于创建数据表时列名相同会导致相关操作混乱，所以创建数据表时不允许列名相同");
			}else{
				operate(req);//对输入指令进行处理
			}
		}
	}
}

