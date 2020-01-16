package com.ekabao.oil.bean;

/**
 * Created by Administrator on 2018/5/29.
 */

public class SysBanner {

    /**
     * sysBanner : {"code":"8","id":5,"imgUrl":"http://192.168.1.2/upload/banner/2018-03/201803268d8cb79a-3cbf-41ca-b17a-1ac2aeb36d2b.jpg","location":"http://www.baidu.com","minVersion":"0.0.0","remark":"","title":"app启动页"}
     */

    private SysBannerBean sysBanner;

    public SysBannerBean getSysBanner() {
        return sysBanner;
    }

    public void setSysBanner(SysBannerBean sysBanner) {
        this.sysBanner = sysBanner;
    }

    public static class SysBannerBean {
        /**
         * code : 8
         * id : 5
         * imgUrl : http://192.168.1.2/upload/banner/2018-03/201803268d8cb79a-3cbf-41ca-b17a-1ac2aeb36d2b.jpg
         * location : http://www.baidu.com
         * minVersion : 0.0.0
         * remark :
         * title : app启动页
         */

        private String code;
        private int id;
        private String imgUrl;
        private String location;
        private String minVersion;
        private String remark;
        private String title;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(String minVersion) {
            this.minVersion = minVersion;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
