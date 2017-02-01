import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class weatherforecast 
{
	 public static void main(String[] args)
	 {
		 Scanner input=new Scanner(System.in);
		 System.out.println("Enter the lattitude:");
		 double lat=input.nextDouble();
		 System.out.println("Enter the longitude:");
		 double lon=input.nextDouble();
		 input.close();
		 String url="http://api.met.no/weatherapi/locationforecast/1.9/?lat="
		 +lat+"&lon="+lon;
//		 System.out.println(url);
		 if(checkResponCode(url))
		 {
		 try {
			 DocumentBuilderFactory factory =
		     DocumentBuilderFactory.newInstance();
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 Document doc = builder.parse(url);
		
			 doc.getDocumentElement().normalize();    
	         Node rootnote=doc.getDocumentElement();
	         Element now=(Element)rootnote;
	         //System.out.println("Root element :" +rootnote.getNodeName());
	       
	         Calendar begintime=TransferDate(now.getAttribute("created"));
	         begintime.add(Calendar.DATE, 1);
	         begintime.set(Calendar.HOUR_OF_DAY, 0);
	         begintime.set(Calendar.MINUTE, 0);
	         begintime.set(Calendar.SECOND, 0);
	         //printCalendar(begintime);
	         Calendar endtime=begintime;
	         endtime.add(Calendar.DATE, 7);
	         //printCalendar(endtime);
	         NodeList tList = doc.getElementsByTagName("temperature");
	         NodeList wList = doc.getElementsByTagName("windSpeed");
	         NodeList cList = doc.getElementsByTagName("cloudiness");
//	         System.out.println(tList.getLength());
//	         System.out.println(wList.getLength());
//	         System.out.println(cList.getLength());
	         
	         for (int temp = 0; temp < tList.getLength(); temp++) 
	         {
	            Node tempNode = tList.item(temp);
	            Element element=(Element)tempNode; 
	            Node pNode=tempNode.getParentNode().getParentNode();
	            Element time=(Element)pNode;
	            
	            if(!withinoneweek(time.getAttribute("from"), endtime))
	            {
	            	break;
	            }
	            
	            System.out.println("Time:"+time.getAttribute("from"));
	            TransferDate(time.getAttribute("from"));
	            System.out.println("【Temperature】"+element.getAttribute("value")+" "+element.getAttribute("unit")); 
	            
	            tempNode = wList.item(temp);
	            element=(Element)tempNode;
	            System.out.println("【Windspeed】mps:"+element.getAttribute("mps")+" beaufort:"+element.getAttribute("beaufort")+" name:"+element.getAttribute("name"));      
	       
	            tempNode= cList.item(temp);
	            element=(Element)tempNode; 
	            System.out.println("【Cloudiness】percent:"+element.getAttribute("percent"));      
	            System.out.println();
	         } 
			} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		 }
		 else{
			 System.out.println("The lattitude should be within (-90,90],and the longtitude should be within (-180,180).");
		 }
	
	}
	 
	 private static Calendar TransferDate(String s)
	 { 
		 Calendar cal=Calendar.getInstance();  
		 try  
		 {  
			 DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");  
			 Date date = sdf.parse(s);  
			 cal.setTime(date);  		 
		 }  
		 catch (ParseException e)  
		 {  
		     System.out.println(e.getMessage());  
		 } 	 
		 return cal;	 
	 }
 
	 private static void printCalendar(Calendar cal)
	 {
		 Date date=cal.getTime();  
		 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		 System.out.println(df.format(date)); 
	 }
	 
	 private static boolean withinoneweek(String s,Calendar cal)
	 {
		 boolean b=false;
		 if(TransferDate(s).before(cal))
		 {
			 b=true;
		 }
		 return b;
	 }
	 
	 private static boolean checkResponCode(String url)
	 {
		 boolean success=true;
		 try {
			URL u = new URL(url);
			HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();  
			 uConnection.connect();  
			 String code=uConnection.getResponseCode()+" ";
			 if(code.startsWith("4"))
			 {
				 success=false;;  
			 }
		 } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}  
		 return success;	 
	 }
	 
}
