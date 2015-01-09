package com.mr.hualvtu.spider.parser;

import com.mr.hualvtu.common.BaseParser;
import com.mr.hualvtu.entity.Country;
import com.mr.hualvtu.entity.Dest;
import com.mr.hualvtu.entity.Land;
import com.mr.hualvtu.service.CountryService;
import com.mr.hualvtu.service.DestService;
import com.mr.hualvtu.service.LandService;
import org.htmlparser.Node;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/1/8.
 */
@Component("destParser")
public class CountryParser extends BaseParser {

    @Autowired
    private CountryService countryService;
    @Autowired
    private LandService landService;
    @Autowired
    private DestService destService;

    static Pattern land_p = Pattern.compile("<h3 style=\"border-bottom: 1px solid #ccc; padding-bottom: 5px;\">(.+)</h3>");
    static Pattern country_p = Pattern.compile("<div cid=\"(\\d+)\"><strong>(.+)：</strong></div>");
    static Pattern dest_p = Pattern.compile("<a href=\"http://www.hualvtu.com/dest/(\\d+)\" target=\"_blank\">(.+)</a>");

    public void parse(String content) throws ParserException {
        parser.setInputHTML(content);
        parser.setEncoding(this.encoding);
        NodeList list = parser.extractAllNodesThatMatch(new CssSelectorNodeFilter("div[class='container entry'] div[class='row']"));
        int land_id = 0;
        String text;
        Matcher matcher;
        List<Land> land_list = new ArrayList<Land>();
        List<Country> country_list = new ArrayList<Country>();
        List<Dest> dest_list = new ArrayList<Dest>();
        for (int i = 0; i < list.size(); i ++) {
            Node node = list.elementAt(i);
            NodeList l = new NodeList();
            node.collectInto(l, new CssSelectorNodeFilter("div[class='span24']"));
            if (l.size() > 0) {
                //land
                text = ((CompositeTag) l.elementAt(0)).getStringText();
                matcher = land_p.matcher(text.trim());
                if (matcher.find()) {
                    Land land = new Land();
                    land.setId(++land_id);
                    land.setName(matcher.group(1));
                    land_list.add(land);
                }
                continue;
            }
            node.collectInto(l, new CssSelectorNodeFilter("div[class='span2']"));
            if (l.size() > 0) {
                //country
                text = ((CompositeTag) l.elementAt(0)).getStringText();
                int country_id = 0;
                matcher = country_p.matcher(text.trim());
                if (matcher.find()) {
                    Country country = new Country();
                    country_id = Integer.parseInt(matcher.group(1));
                    country.setId(country_id);
                    country.setName(matcher.group(2));
                    country.setLandId(land_id);
                    country_list.add(country);
                }
                //dests
                l = new NodeList();
                node.collectInto(l, new TagNameFilter("li"));
                for (int j = 0; j < l.size(); j++) {
                    text = ((CompositeTag) l.elementAt(j)).getStringText();
                    matcher = dest_p.matcher(text.trim());
                    if (matcher.find()) {
                        Dest dest = new Dest();
                        dest.setId(Integer.parseInt(matcher.group(1)));
                        dest.setName(matcher.group(2));
                        dest.setCountryId(country_id);
                        dest_list.add(dest);
                    }
                }
            }
        }
        landService.batchInsert(land_list);
        countryService.batchInsert(country_list);
        destService.batchInsert(dest_list);
    }
}
