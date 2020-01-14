package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_akzj
 *
 * @time 2019/3/28 17:00
 * Created by lj on 2019/3/28 17:00.
 */

public class MallCategory {


    /**
     * bannerUrl :
     * iconUrl : https://akzj-oss.oss-cn-hangzhou.aliyuncs.com/20190328/150612327da0c0.png
     * id : 1019002
     * imgUrl :
     * isShow : true
     * level : L1
     * name : 家居百货
     * parentId : 0
     * showIndex : true
     * type : 0
     * wapBannerUrl :
     */

    private String bannerUrl;
    private String iconUrl;
    private int id;
    private String imgUrl;
    private boolean isShow;
    private String level;
    private String name;
    private int parentId;
    private boolean showIndex;
    private int type;
    private String wapBannerUrl;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isIsShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isShowIndex() {
        return showIndex;
    }

    public void setShowIndex(boolean showIndex) {
        this.showIndex = showIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWapBannerUrl() {
        return wapBannerUrl;
    }

    public void setWapBannerUrl(String wapBannerUrl) {
        this.wapBannerUrl = wapBannerUrl;
    }
}
