package wanghaisheng.com.xiaoya.api.converter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import wanghaisheng.com.xiaoya.api.jianshu.JianshuContent;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContentResult;
import wanghaisheng.com.xiaoya.utils.StringUtils;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuContentParser {
    /**
     * 根据传入的html解析生成JianhuContentResult
     * @param htmlStr
     * @return
     */
    public static JianshuContentResult parseGroup(String htmlStr) {
        JianshuContentResult result = new JianshuContentResult();
        List<JianshuContent> list = new ArrayList<>();
        Document doc = Jsoup.parse(htmlStr);
        Element nextPageElem = doc.select(".ladda-button ").get(0);
        String nextPageURL = nextPageElem.attr("data-url");
//        LogUtils.d("parser nextPageUrl..."+nextPageURL);

        Elements listElements = doc.select("ul.article-list .have-img");
        int length = listElements.size();
        for(int i=0; i<length; i++) {
            JianshuContent content = new JianshuContent();

            Element element = listElements.get(i);

            Element timeElem = element.select(".list-top .time").first();//时间node
            String date = timeElem.attr("data-shared-at");
            content.setDate(date);

            Element titleElem = element.select("h4.title a").get(0);
            String title = titleElem.text();
            content.setTitle(title);
            String url = titleElem.attr("href");
            content.setUrl(url);

            Element readElem = element.select("div.list-footer a").get(0);
            content.setReadCount(StringUtils.getNumbers(readElem.text()));

            Element imgElem = element.select("a.wrap-img img").get(0);
            String imgUrl = imgElem.attr("src");
            content.setImgUrl(imgUrl);

            list.add(content);
        }

        result.setContents(list);
        result.setNextUrl(nextPageURL);

        return result;
    }

    public static String getJianshuDetailContent(String html) {
        Document doc = Jsoup.parse(html);

        Element content = doc.select(".show-content").get(0);

        return content.outerHtml();
    }
}
