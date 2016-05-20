package messenger;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;


//This class is used to detect hosts in the LAN whose addresses are
//of the form 192.168.1.x
public class HostsDetector implements Runnable{
    private int from;
    private int upTo;
    private ArrayList<String> reachableIPs;
    
    public static final String IP_PREFIX = "192.168.1.";
    public static final int OCTET_MAX_VAL = 255;
    public static final int TIMEOUT = 200;
    
    public String[] getReachableHosts() {
        String[] hosts = new String[reachableIPs.size()];
        return reachableIPs.toArray(hosts);
    }
    
    public HostsDetector(int from, int upTo) {
        if (from < 0) 
            this.from = 0;
        else 
            this.from = from;
        if (upTo > OCTET_MAX_VAL) 
            this.from = OCTET_MAX_VAL;
        else 
            this.upTo = upTo;
        
        reachableIPs = new ArrayList<>();
    }
    
    @Override
    public void run() {
        for (int i = from; i <= upTo && !Thread.interrupted(); i++) {
            try {
                if (Inet4Address.getByName(IP_PREFIX + i).isReachable(TIMEOUT))
                    reachableIPs.add(IP_PREFIX + i);
            } catch (IOException ex) {
                //if exception was thrown, then the host is not reachable
            }
        }
    }
}
