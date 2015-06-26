package cn.edu.dlut.tiyuguan.internet;

import java.io.IOException;
import java.util.LinkedHashMap;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.edu.dlut.tiyuguan.global.Img;

public class LoadImgSrcThread  implements Runnable{

	Handler parent=null;
	public LoadImgSrcThread(Handler parent) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		ParseHtmlFromTYGSite parsehtml = null;
			try {
				parsehtml = new ParseHtmlFromTYGSite("http://tyg.dlut.edu.cn/", 5);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                return;
			}
				LinkedHashMap<String, String> newsLink = parsehtml.getNewsLinks();
				Img.img = parsehtml.getNewsImgSrcByLink(newsLink);
				if(Img.img != null){
					Message msg=new Message();
					msg.what = 0x1238;
					msg.obj = "图片加载成功了哦";
					Looper.prepare();
					parent.sendMessage(msg);
					Looper.loop();
				}

		 
	}
}
