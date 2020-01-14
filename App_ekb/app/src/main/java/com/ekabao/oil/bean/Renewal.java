package com.ekabao.oil.bean;

/**
 * Created by Administrator on 2018/5/29.
 *
 * 启动页面的 版本更新
 *
 */

public class Renewal {

    /**
     * isMaintenance :
     * sysAppRenewal : {"addTime":1526538187000,"addUser":1,"containers":2,"content":"最新版本已经发布，请及时更新！","id":1,"isForceUpdate":0,"releaseVersion":"1.0.8","remark":"","status":1,"updateTime":1526610628000,"updateUser":1,"version":"1.0.0"}
     */
    private String maxVersion;
    private String isMaintenance;
    private SysAppRenewalBean sysAppRenewal;

    public String getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
    }
    public String getIsMaintenance() {
        return isMaintenance;
    }

    public void setIsMaintenance(String isMaintenance) {
        this.isMaintenance = isMaintenance;
    }

    public SysAppRenewalBean getSysAppRenewal() {
        return sysAppRenewal;
    }

    public void setSysAppRenewal(SysAppRenewalBean sysAppRenewal) {
        this.sysAppRenewal = sysAppRenewal;
    }

    public static class SysAppRenewalBean {
        /**
         * addTime : 1526538187000
         * addUser : 1
         * containers : 2
         * content : 最新版本已经发布，请及时更新！
         * id : 1
         * isForceUpdate : 0
         * releaseVersion : 1.0.8
         * remark :
         * status : 1
         * updateTime : 1526610628000
         * updateUser : 1
         * version : 1.0.0
         */

        private long addTime;
        private int addUser;
        private int containers;
        private String content;
        private int id;
        private String isForceUpdate;// int
        private String releaseVersion;
        private String remark;
        private int status;
        private long updateTime;
        private int updateUser;
        private String version;

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public int getAddUser() {
            return addUser;
        }

        public void setAddUser(int addUser) {
            this.addUser = addUser;
        }

        public int getContainers() {
            return containers;
        }

        public void setContainers(int containers) {
            this.containers = containers;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIsForceUpdate() {
            return isForceUpdate;
        }

        public void setIsForceUpdate(String isForceUpdate) {
            this.isForceUpdate = isForceUpdate;
        }

        public String getReleaseVersion() {
            return releaseVersion;
        }

        public void setReleaseVersion(String releaseVersion) {
            this.releaseVersion = releaseVersion;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(int updateUser) {
            this.updateUser = updateUser;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
