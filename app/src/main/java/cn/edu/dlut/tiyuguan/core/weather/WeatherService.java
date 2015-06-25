package cn.edu.dlut.tiyuguan.core.weather;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *根据api获取本地天气信息
 * @author asus
 *
 */
public class WeatherService {

	public static String getHtml(String path) {
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();//ͨ����������ȡhtml����
			byte[] data = readInputStream(inStream);//�õ�html�Ķ���������
			String html = new String(data, "utf-8");
			return html;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}
