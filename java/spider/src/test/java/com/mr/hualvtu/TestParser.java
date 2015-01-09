package com.mr.hualvtu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.mr.hualvtu.entity.Country;
import com.mr.hualvtu.entity.Dest;
import com.mr.hualvtu.entity.Land;

/**
 * Created by Administrator on 2015/1/8.
 */
public class TestParser {

    static Pattern land_p = Pattern.compile("<h3 style=\"border-bottom: 1px solid #ccc; padding-bottom: 5px;\">(.+)</h3>");
    static Pattern country_p = Pattern.compile("<div cid=\"(\\d+)\"><strong>(.+)：</strong></div>");
    static Pattern dest_p = Pattern.compile("<a href=\"http://www.hualvtu.com/dest/(\\d+)\" target=\"_blank\">(.+)</a>");

    public static void main(String[] args) throws ParserException {
        Parser parser = new Parser();
        parser.setResource("src/main/html/dests.html");
        parser.setEncoding("UTF-8");
        NodeList list = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class='container entry'] div[class='row']"));

        int land_id = 0;
        String s;
        Matcher matcher;
        for (int i = 0; i < 2; i ++) {
            Node node = list.elementAt(i);
            NodeList l = new NodeList();
            node.collectInto(l, new CssSelectorNodeFilter("div[class='span24']"));
            if (l.size() > 0) {
                //land
                s = ((CompositeTag) l.elementAt(0)).getStringText();
                matcher = land_p.matcher(s.trim());
                if (matcher.find()) {
                    Land land = new Land();
                    land.setId(++land_id);
                    land.setName(matcher.group(1));
                    System.out.print(land.getName());
                }
                continue;
            }
            node.collectInto(l, new CssSelectorNodeFilter("div[class='span2']"));
            if (l.size() > 0) {
                //country
                s = ((CompositeTag) l.elementAt(0)).getStringText();
                int country_id = 0;
                matcher = country_p.matcher(s.trim());
                if (matcher.find()) {
                    Country country = new Country();
                    country_id = Integer.parseInt(matcher.group(1));
                    country.setId(country_id);
                    country.setName(matcher.group(2));
                    country.setLandId(land_id);
                }
                //dests
                l = new NodeList();
                node.collectInto(l, new TagNameFilter("li"));
                for (int j = 0; j < l.size(); j++) {
                    s = ((CompositeTag) l.elementAt(j)).getStringText();
                    matcher = dest_p.matcher(s.trim());
                    if (matcher.find()) {
                        Dest dest = new Dest();
                        dest.setId(Integer.parseInt(matcher.group(1)));
                        dest.setName(matcher.group(2));
                        dest.setCountryId(country_id);
                    }
                }
            }
        }
    }
}
