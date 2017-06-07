package com.wyd.BigData.bean;

import java.util.Date;

public class DeviceInfo 
    implements java.io.Serializable {
    private static final long serialVersionUID = 253741164215691002L;
    private int id;
        private int               serviceId;
        private int               channelId;
        private String            deviceMac;
        private Date              createTime;
        private String            deviceName;// 设备名称
        private String            systemName;// 系统名称
        private String            systemVersion;// 系统版本号
        private String            appVersion;// app版本号
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getServiceId() {
            return serviceId;
        }
        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }
        public int getChannelId() {
            return channelId;
        }
        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }
        public String getDeviceMac() {
            return deviceMac;
        }
        public void setDeviceMac(String deviceMac) {
            this.deviceMac = deviceMac;
        }
        public Date getCreateTime() {
            return createTime;
        }
        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
        public String getDeviceName() {
            return deviceName;
        }
        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }
        public String getSystemName() {
            return systemName;
        }
        public void setSystemName(String systemName) {
            this.systemName = systemName;
        }
        public String getSystemVersion() {
            return systemVersion;
        }
        public void setSystemVersion(String systemVersion) {
            this.systemVersion = systemVersion;
        }
        public String getAppVersion() {
            return appVersion;
        }
        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }
        
}
