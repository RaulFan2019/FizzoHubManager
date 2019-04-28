package cn.fizzo.hub.manager.entity.net;

public class GetConsoleInfoRE {


    /**
     * console : {"id":482,"registertime":"2017-12-29 14:08:42","serialno":"210ba2005b188d803e18c8cfc61200e2","name":"c61200e2",
     * "provider":1,"appname":"FizzoHub","packagename":"com.fizzo.fizzohub","apkurl":"www.123yd.cn/android-release/FizzoFitnessHub_20180608.apk","
     * latest_versioncode":329,"latest_versioninfo":"1.修正PK过程中的卡路里计算"}
     */

    public ConsoleBean console;

    public ConsoleBean getConsole() {
        return console;
    }

    public void setConsole(ConsoleBean console) {
        this.console = console;
    }

    public static class ConsoleBean {
        /**
         * id : 482
         * registertime : 2017-12-29 14:08:42
         * serialno : 210ba2005b188d803e18c8cfc61200e2
         * name : c61200e2
         * provider : 1
         * appname : FizzoHub
         * packagename : com.fizzo.fizzohub
         * apkurl : www.123yd.cn/android-release/FizzoFitnessHub_20180608.apk
         * latest_versioncode : 329
         * latest_versioninfo : 1.修正PK过程中的卡路里计算
         */

        public int id;
        public String registertime;
        public String serialno;
        public String name;
        public int provider;
        public String appname;
        public String packagename;
        public String classname;
        public String apkurl;
        public int latest_versioncode;
        public String latest_versioninfo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getRegistertime() {
            return registertime;
        }

        public void setRegistertime(String registertime) {
            this.registertime = registertime;
        }

        public String getSerialno() {
            return serialno;
        }

        public void setSerialno(String serialno) {
            this.serialno = serialno;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProvider() {
            return provider;
        }

        public void setProvider(int provider) {
            this.provider = provider;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getPackagename() {
            return packagename;
        }

        public void setPackagename(String packagename) {
            this.packagename = packagename;
        }

        public String getApkurl() {
            return apkurl;
        }

        public void setApkurl(String apkurl) {
            this.apkurl = apkurl;
        }

        public int getLatest_versioncode() {
            return latest_versioncode;
        }

        public void setLatest_versioncode(int latest_versioncode) {
            this.latest_versioncode = latest_versioncode;
        }

        public String getLatest_versioninfo() {
            return latest_versioninfo;
        }

        public void setLatest_versioninfo(String latest_versioninfo) {
            this.latest_versioninfo = latest_versioninfo;
        }
    }
}
