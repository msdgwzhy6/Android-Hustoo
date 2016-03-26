package com.scb.administrator.a.entity;

/**
 * Created by Administrator on 2015/9/23 0023.
 */
public class Book {

    private String link;
    private String  name ;
    private String pic;


    public Book( String name, String pic,String link) {
        this.link = link;
        this.name = name;
        this.pic = pic;
    }



    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
