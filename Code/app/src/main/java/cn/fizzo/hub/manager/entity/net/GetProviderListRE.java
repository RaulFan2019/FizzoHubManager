package cn.fizzo.hub.manager.entity.net;

import java.util.List;

public class GetProviderListRE {


    public List<ProvidersBean> providers;

    public List<ProvidersBean> getProviders() {
        return providers;
    }

    public void setProviders(List<ProvidersBean> providers) {
        this.providers = providers;
    }

    public static class ProvidersBean {
        /**
         * provider : 1
         * appname : FizzoHub
         * packagename : com.fizzo.fizzohub
         * classname : cn.fizzo.hub.fitness.ui.activity.main.WelcomeActivity
         * description : 健身房标准版HUB
         */

        public int provider;
        public String appname;
        public String packagename;
        public String classname;
        public String description;

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

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
