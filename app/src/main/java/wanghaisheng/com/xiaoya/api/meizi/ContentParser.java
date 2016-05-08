package wanghaisheng.com.xiaoya.api.meizi;

import com.apkfuns.logutils.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import wanghaisheng.com.xiaoya.beans.GroupResult;
import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.db.Group;

/**
 * Created by Mr_Wrong on 15/9/14.
 * 解析具体大图页面
 */
public class ContentParser {

    public static Content ParserContent(String html) {
        Content content = new Content();
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("img[src~=(?i)\\.(png|jpe?g)]");

        Element element = links.get(1).getElementsByTag("img").first();

        content.setUrl(element.attr("src"));
        content.setTitle(element.attr("alt"));
        return content;
    }

    public static List<Group> parseGroups(String html) {
        List<Group> list = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("ul#pins a:has(img)");//选择id为pins的ul元素下的a元素，而且该a元素下有img标签

        Element imgElement;
        Element aElement;
        for(int i=0; i<links.size(); i++) {
            aElement = links.get(i);
            imgElement = aElement.child(0);

            Group bean = new Group();
            bean.setOrder(i);
            bean.setTitle(imgElement.attr("alt").toString());
            bean.setImageurl(imgElement.attr("data-original"));

            //bean.setType(type);
            bean.setHeight(354);//element.attr("height")
            bean.setWidth(236);
            if (aElement != null) {
                bean.setUrl(aElement.attr("href"));
            }
            bean.setGroupid(url2groupid(bean.getUrl()));//首页的这个是从大到小排序的 可以当做排序依据
            list.add(bean);
        }

//        LogUtils.d("print group");
//        LogUtils.d(list);

        return list;
    }

    /**
     * 解析html得到图片个数和第一张图片的url
     * @param html
     * @return
     */
    public static GroupResult getCount(String html) {
        Document doc = Jsoup.parse(html);
        Elements pages = doc.select("div.pagenavi span");
        int pageSize = 0;
        try {
            pageSize = Integer.parseInt(pages.get(pages.size()-2).text());
        }catch (NumberFormatException e) {
            LogUtils.d("ContentParser...NumberFormatException");
        }

        Element imgElem = doc.select("div.main-image img").get(0);
        String imgUrl = imgElem.attr("src");

        return new GroupResult(pageSize,imgUrl);

//        return Integer.parseInt(stringBuffer.toString().substring(1));
    }

    private static String url2groupid(String url) {
        return url.split("/")[3];
    }

}
