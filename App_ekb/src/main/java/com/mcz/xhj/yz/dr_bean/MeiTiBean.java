package com.mcz.xhj.yz.dr_bean;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：
 * 创建人：shuc
 * 创建时间：2017/2/22 10:29
 * 修改人：DELL
 * 修改时间：2017/2/22 10:29
 * 修改备注：
 */
public class MeiTiBean {

    private String artiId;  //链接标识号
    private String title;  //标题
    private long createTime; //创建时间
    private String litpic;//缩略图链接
    private String proId; //栏目

    public MeiTiBean() {
        super();
    }

    public MeiTiBean(String artiId,String title,long createTime,String litpic,String proId){
        super();
        this.artiId = artiId;
        this.title = title;
        this.createTime = createTime;
        this.litpic = litpic;
        this.proId = proId;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getArtiId() {
        return artiId;
    }

    public void setArtiId(String artiId) {
        this.artiId = artiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
