package com.ekabao.oil.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 */

public class Activity {

    /**
     * Page : {"pageInfo":{"limit":10,"offset":0},"pageOn":1,"pageSize":10,"rows":[{"activityDate":"201805","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-04/null/20180427ab9aa782-1015-4db4-95d9-ddb157bc5630.png","appUrl":"http://192.168.1.2/qmyhinvite","id":7,"isTop":1,"pcUrl":"/qmyhinvite","status":1,"title":"五一集结号","type":1,"updateTime":1524792987000,"updateUser":"1","pcPic":"http://192.168.1.2/upload/productPic/2018-04/5/20180408cc943e19-6616-4ed3-b781-1235462598d5.png"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-04/5/20180408163c2ef1-501f-4f50-b190-e5186a159387.png","appUrl":"http://www.baidu.com","id":5,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-04/5/20180408cc943e19-6616-4ed3-b781-1235462598d5.png","pcUrl":"http://www.baidu.com","status":1,"title":"红包免费领","type":1,"updateTime":1524040013000,"updateUser":"1"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327afe4e9cd-bbb9-4c24-99fa-6729536d391e.jpg","appUrl":"http://192.168.1.12:8088/redpacketCollar","id":4,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327677202d4-a66b-4104-b990-758bca294950.jpg","pcUrl":"https://www.baidu.com","status":1,"title":"活动列表-002","type":1,"updateTime":1523433876000,"updateUser":"1"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-03/null/201803277725db90-e6ad-48af-bf5f-c3d4dbbec3aa.jpg","appUrl":"https://www.baidu.com","id":1,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327fa9347cd-b3c3-47be-9d79-852db9ccae0f.jpg","pcUrl":"https://www.baidu.com","status":1,"title":"三月活动","type":1,"updateTime":1524040023000,"updateUser":"1"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327d3d94338-51fe-4d28-8393-d7ca7fb59a1b.jpg","appUrl":"https://www.baidu.com","id":3,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-03/null/201803276b799798-2070-4234-8081-4de1fd8e299c.jpg","pcUrl":"https://www.baidu.com","status":2,"title":"活动列表-001","type":1,"updateTime":1522230117000,"updateUser":"1"}],"total":5,"totalPage":1}
     * btnUrl : http://192.168.1.110/btn11.png
     */

    private PageBean Page;
    private String btnUrl;

    public PageBean getPage() {
        return Page;
    }

    public void setPage(PageBean Page) {
        this.Page = Page;
    }

    public String getBtnUrl() {
        return btnUrl;
    }

    public void setBtnUrl(String btnUrl) {
        this.btnUrl = btnUrl;
    }

    public static class PageBean {
        /**
         * pageInfo : {"limit":10,"offset":0}
         * pageOn : 1
         * pageSize : 10
         * rows : [{"activityDate":"201805","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-04/null/20180427ab9aa782-1015-4db4-95d9-ddb157bc5630.png","appUrl":"http://192.168.1.2/qmyhinvite","id":7,"isTop":1,"pcUrl":"/qmyhinvite","status":1,"title":"五一集结号","type":1,"updateTime":1524792987000,"updateUser":"1"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-04/5/20180408163c2ef1-501f-4f50-b190-e5186a159387.png","appUrl":"http://www.baidu.com","id":5,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-04/5/20180408cc943e19-6616-4ed3-b781-1235462598d5.png","pcUrl":"http://www.baidu.com","status":1,"title":"红包免费领","type":1,"updateTime":1524040013000,"updateUser":"1"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327afe4e9cd-bbb9-4c24-99fa-6729536d391e.jpg","appUrl":"http://192.168.1.12:8088/redpacketCollar","id":4,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327677202d4-a66b-4104-b990-758bca294950.jpg","pcUrl":"https://www.baidu.com","status":1,"title":"活动列表-002","type":1,"updateTime":1523433876000,"updateUser":"1"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-03/null/201803277725db90-e6ad-48af-bf5f-c3d4dbbec3aa.jpg","appUrl":"https://www.baidu.com","id":1,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327fa9347cd-b3c3-47be-9d79-852db9ccae0f.jpg","pcUrl":"https://www.baidu.com","status":1,"title":"三月活动","type":1,"updateTime":1524040023000,"updateUser":"1"},{"activityDate":"2018-04","addTime":2018,"addUser":1,"appPic":"http://192.168.1.2/upload/productPic/2018-03/null/20180327d3d94338-51fe-4d28-8393-d7ca7fb59a1b.jpg","appUrl":"https://www.baidu.com","id":3,"isTop":0,"pcPic":"http://192.168.1.2/upload/productPic/2018-03/null/201803276b799798-2070-4234-8081-4de1fd8e299c.jpg","pcUrl":"https://www.baidu.com","status":2,"title":"活动列表-001","type":1,"updateTime":1522230117000,"updateUser":"1"}]
         * total : 5
         * totalPage : 1
         */

        private PageInfoBean pageInfo;
        private int pageOn;
        private int pageSize;
        private int total;
        private int totalPage;
        private List<RowsBean> rows;

        public PageInfoBean getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfoBean pageInfo) {
            this.pageInfo = pageInfo;
        }

        public int getPageOn() {
            return pageOn;
        }

        public void setPageOn(int pageOn) {
            this.pageOn = pageOn;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class PageInfoBean {
            /**
             * limit : 10
             * offset : 0
             */

            private int limit;
            private int offset;

            public int getLimit() {
                return limit;
            }

            public void setLimit(int limit) {
                this.limit = limit;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }
        }

        public static class RowsBean {
            /**
             * activityDate : 201805
             * addTime : 2018
             * addUser : 1
             * appPic : http://192.168.1.2/upload/productPic/2018-04/null/20180427ab9aa782-1015-4db4-95d9-ddb157bc5630.png
             * appUrl : http://192.168.1.2/qmyhinvite
             * id : 7
             * isTop : 1
             * pcUrl : /qmyhinvite
             * status : 1
             * title : 五一集结号
             * type : 1
             * updateTime : 1524792987000
             * updateUser : 1
             * pcPic : http://192.168.1.2/upload/productPic/2018-04/5/20180408cc943e19-6616-4ed3-b781-1235462598d5.png
             */

            private String activityDate;
            private int addTime;
            private int addUser;
            private String appPic;
            private String appUrl;
            private int id;
            private int isTop;
            private String pcUrl;
            private int status;
            private String title;
            private int type;
            private long updateTime;
            private String updateUser;
            private String pcPic;

            public String getActivityDate() {
                return activityDate;
            }

            public void setActivityDate(String activityDate) {
                this.activityDate = activityDate;
            }

            public int getAddTime() {
                return addTime;
            }

            public void setAddTime(int addTime) {
                this.addTime = addTime;
            }

            public int getAddUser() {
                return addUser;
            }

            public void setAddUser(int addUser) {
                this.addUser = addUser;
            }

            public String getAppPic() {
                return appPic;
            }

            public void setAppPic(String appPic) {
                this.appPic = appPic;
            }

            public String getAppUrl() {
                return appUrl;
            }

            public void setAppUrl(String appUrl) {
                this.appUrl = appUrl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsTop() {
                return isTop;
            }

            public void setIsTop(int isTop) {
                this.isTop = isTop;
            }

            public String getPcUrl() {
                return pcUrl;
            }

            public void setPcUrl(String pcUrl) {
                this.pcUrl = pcUrl;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public String getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(String updateUser) {
                this.updateUser = updateUser;
            }

            public String getPcPic() {
                return pcPic;
            }

            public void setPcPic(String pcPic) {
                this.pcPic = pcPic;
            }
        }
    }
}
