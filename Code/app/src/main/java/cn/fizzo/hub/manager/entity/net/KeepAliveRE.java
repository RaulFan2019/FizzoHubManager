package cn.fizzo.hub.manager.entity.net;

public class KeepAliveRE {


    /**
     * keeper : {"id":1,"commandid":0,"linuxcommand":""}
     */

    public KeeperBean keeper;

    public KeeperBean getKeeper() {
        return keeper;
    }

    public void setKeeper(KeeperBean keeper) {
        this.keeper = keeper;
    }

    public static class KeeperBean {
        /**
         * id : 1
         * commandid : 0
         * linuxcommand : 
         */

        public int id;
        public int commandid;
        public String linuxcommand;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCommandid() {
            return commandid;
        }

        public void setCommandid(int commandid) {
            this.commandid = commandid;
        }

        public String getLinuxcommand() {
            return linuxcommand;
        }

        public void setLinuxcommand(String linuxcommand) {
            this.linuxcommand = linuxcommand;
        }
    }
}
