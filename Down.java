package Down;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Down {
	public static String Base_Path = "R:\\Java_download\\";

	public static void main(String[] args) throws Exception {
		ArrayList<info> arrayList;
		if (args.length > 1) {
			Base_Path = args[0];
			arrayList = init(args[1]);
		} else {
			arrayList = init(args[0]);
		}
		File f = new File("R:\\Java_download");
		if (!f.exists())
			f.mkdirs();
		ExecutorService executor = Executors.newFixedThreadPool(11);
		arrayList.forEach(i -> executor.execute(new Thread(new DonwThread(i.name, i.url, i.path))));

	}

	private static ArrayList<info> init(String url) throws Exception {
		ArrayList<info> arrayList = new ArrayList<>();
		Document d = Jsoup.connect(url.replace("view", "learn")).get();
		Elements el = d.select(".video li");
		for (Element eee : el) {
			arrayList.add(new info(eee.select("a").text(), eee.select("a").attr("href"), d.select(".hd").text()));
		}
		return arrayList;

	}

}

class info {
	String url;
	String name;
	String path;
	public info(String name, String url, String path) {
		this.url = url.replace("/video/", "");
		this.path = path;
		this.name = name;
		File f = new File(Down.Base_Path + "\\" + path);
		if (!f.exists())
			f.mkdirs();
	}
}