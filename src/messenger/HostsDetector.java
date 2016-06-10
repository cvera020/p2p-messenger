package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//***********************************************************************
// This class is used to detect hosts in the LAN whose addresses are of
// the form 192.168.1.x. 
//***********************************************************************
public class HostsDetector {
    private ArrayList<String> reachableIPs;
    private Detector[] detectors;

    public static final String IP_PREFIX = "192.168.1."; //will never include 4th octet. Should end in "."

    //getNumAddr() relies on IP_PREFIX constant to end in "." (a dot). This means that
    //IP_PREFIX should never include the final octet of an IP address.
    //returns the number of IP addresses to check
    public static int getNumAddresses() {
        double numAddr = 4 - (IP_PREFIX.length() - IP_PREFIX.replace(".", "").length());
        return (int) Math.pow(2, 8 * numAddr);
    }
    
    public synchronized void addReachableIP(String ip) {
        reachableIPs.add(ip);
    }
    
    public static int TIMEOUT_WAIT_ON_THREAD = 200;
    public ArrayList<String> getReachableHosts() {
        reachableIPs = new ArrayList();
        detectors = new Detector[Runtime.getRuntime().availableProcessors() * 16];
        int numAddr = getNumAddresses();
        
        //instantiate each object in detectors (Detector[]) array, assign a range of IPs
        //for each to check, and create a new thread for each Detector
        for (int i = 0; i < detectors.length; i++) {
            int lowRange = (numAddr/detectors.length)*i;
            int highRange = (numAddr/detectors.length)*(i+1) - 1;
            detectors[i] = new Detector(lowRange, highRange, this);
            new Thread(detectors[i]).start();
        }
        
        //wait for all Detector threads to finish their tasks
        for (int i = 0; i < detectors.length; ) {
            if (detectors[i].isFinished) {
                i++;
            } else {
                try {
                    Thread.sleep(TIMEOUT_WAIT_ON_THREAD);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HostsDetector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return reachableIPs;
    }
    
    

    /***********************************************************************
     * The Detector object runs a single thread to check if a given range of
     * IP addresses is reachable. Many of these objects may be used by the
     * HostsDetector container to run a multi-threaded search on which IPs are
     * reachable.
     * 
     ***********************************************************************/
    private class Detector implements Runnable {
        HostsDetector hd; //used for adding reachable IP addresses to reachableIPs (ArrayList)
        int rangeLow;
        int rangeHigh;
        boolean isFinished;

        public Detector(int low, int high, HostsDetector hd) {
            if (low < 0)
                rangeLow = 0;
            else
                rangeLow = low;
            if (rangeHigh > getNumAddresses())
                rangeHigh = getNumAddresses();
            else
                rangeHigh = high;
            isFinished = false;
            
            this.hd = hd;
        }

        public boolean isReachable(String addr) throws IOException {
            Process p = Runtime.getRuntime().exec("ping -n 1 " + addr);
            try {
                p.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(HostsDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("bytes=")) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public void run() {
            for (int i = rangeLow; i <= rangeHigh; i++) {
                try {
                    if (isReachable(IP_PREFIX + i)) {
                        hd.addReachableIP(IP_PREFIX + i);
                    }
                } catch (UnknownHostException ex) {
                    //then the IP address is not reachable
                } catch (IOException ex) {
                    //then the IP address is not reachable
                }
            }
            isFinished = true;
        }
    }
}
