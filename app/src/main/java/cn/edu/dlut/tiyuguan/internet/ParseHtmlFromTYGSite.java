package cn.edu.dlut.tiyuguan.internet;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @author asus
 * 
 * 
 *
 */
public class ParseHtmlFromTYGSite{
     String URL;
	 Document doc;
	 int num=5;//抓取的数量
	/**
	 * @param URL传入的URL
	 * @throws java.io.IOException
	 * 
	 */
	public ParseHtmlFromTYGSite(String URL,int n) throws IOException {
		// TODO Auto-generated constructor stub
		this.URL=URL;
		this.doc=Jsoup.connect(URL).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(9000).get();
		//Jsoup.
		this.num=n;

	}
    /**
     * @return LinkedHashMap<String,String>所有的新闻动态的文章-地址hash集合
     */
    /*获取前n条新闻的链接*/
    public LinkedHashMap<String,String> getNewsLinks(){
    	LinkedHashMap<String,String> news=new LinkedHashMap<>();
    	if(doc!=null){
    		Elements links=doc.getElementsByClass("c63065");
    		if(links!=null){
    			//System.out.println(links.size());
    		}
    			int i=0;
    			for(Element link:links){
    				if(++i>this.num) break;
    				String newslink=link.attr("href");
    				String newstitle=link.text();
    				newslink=URL+newslink;
    				news.put(newstitle, newslink);
    			}
    		
    	}
    	return news;
    }
    /*根据新闻的链接抓取对应链接中的第一张图片的地址*/
    public   LinkedHashMap<String, String> getNewsImgSrcByLink( LinkedHashMap<String, String> link) {
    	 LinkedHashMap<String, String> imgsrc=new LinkedHashMap<>();
 		 Iterator iter = link.entrySet().iterator();
    	 String src = "http://www.dlut.edu.cn/xiaohua.jpg";//初始值的默认
    	 String title="";
    	 Element div=null;
    	 Elements imgs=null;
    	 Document doc=null;
    	 Connection con=null;
    	 while(iter.hasNext()){
         	@SuppressWarnings("unchecked")
		   Map.Entry<String,String> entry=(Map.Entry<String,String>)iter.next();
         	String url=entry.getValue();
         	System.out.println(url);
         	try {
                con = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(3000);
         		
         		 if(con != null)
         		      doc = con.get();
         		 else
         			 System.out.println("t连接为空");
				if(doc == null){
					System.out.println("doc为空");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

         	if(doc == null){
				System.out.println("doc为空");
			}
         	else{
         		div = doc.getElementById("vsb_content_2");
             	title = (doc.getElementsByTag("title")).text();
         	}
         	
         	if(div == null)
         		{
         		  System.out.println("div为空");
         		}
         	else{
         		 imgs = div.getElementsByTag("img");
         	}

         	if(imgs != null){
         	for(Element img:imgs){
         		src = img.attr("src");
         		src = "http://tyg.dlut.edu.cn/"+src.substring(6);
         		break;//只需要第一个img
         	}
         	}
         	imgsrc.put(title, src);
         }
    	 
    	 return imgsrc;
    	
    }
	
	
}
