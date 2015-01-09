package com.mr.hualvtu;

import com.mr.hualvtu.spider.parser.CountryParser;
import com.mr.hualvtu.util.HtmlUtil;
import org.htmlparser.util.ParserException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


/**
 * Created by Administrator on 2015/1/7.
 */
public class Main {

    public static void main(String[] args) throws IOException, ParserException {
    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        CountryParser p = (CountryParser)context.getBean("destParser");
        p.parse(HtmlUtil.get("http://www.hualvtu.com/entry2.action", "www.hualvtu.com", "UTF-8"));

//        http://www.hualvtu.com/viewDest2.action?did=QUEUE_NEW_16274#anchor

//        AlbumService albumService = (AlbumService)context.getBean("albumService");
//        Album album = new Album();
//        album.setId(1);
//        album.setUserId(2);
//        album.setDestId(3);
//        albumService.insert(album);
        context.close();
    }
}
