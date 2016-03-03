package Down;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DonwThread implements Runnable{
	
	String name;
	String url;
	int bytesum = 0;
	int byteread = 0;
	String path;
	
	
	
	public DonwThread(String name, String url,String path) {
		super();
		this.path=path;
		this.name = name;
		this.url = url;
	}
	@Override
	public void run() {
		try {
			URL url = new URL("http://www.imooc.com/course/ajaxmediainfo/?mid="+this.url+"&mode=flash");
			URLConnection uc = url.openConnection();
			uc.connect();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String a;
			String b = "";
			while ((a = bufferedReader.readLine()) != null) {
				b += a;
			}
			JsonElement e = new JsonParser().parse(b);
			e = e.getAsJsonObject().getAsJsonObject("data").get("result").getAsJsonObject().getAsJsonArray("mpath");
			URL urls = new URL(e.getAsJsonArray().get(0).toString().replace("\"", ""));
			URLConnection conn = urls.openConnection();
			InputStream inStream = conn.getInputStream();
			int size=conn.getContentLength()/1000000;
			FileOutputStream fs = new FileOutputStream(Down.Base_Path+"\\"+path+"\\"+name.substring(0, name.indexOf('('))+".mp4");
			byte[] buffer = new byte[1204];
			System.out.println(name+":"+size+"M   已经下载");
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();
			bufferedReader.close();
			System.err.println(name+"========下载完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
