package messenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messenger extends Thread {
    public static final int PORT_NUM = 8001;
    public static final int SO_TIMEOUT = 20000;
    public static final boolean TCP_NO_DELAY = true;
    
    private String toSend; //buffer for outgoing messsages on socket
    private String toReceive;
    
    private Socket conn;
    private ServerSocket ss;
    private String userName;
    private PrintWriter pw;
    private BufferedReader br;
        
    public Messenger(String peerAddr, String userName) {
        System.out.println("Messenger initializing...");
        this.userName = userName;
        connect(peerAddr);
        try {
            pw = new PrintWriter(conn.getOutputStream());
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String receive() {
        return toReceive;
    }
    
    public void send(String msg, String name) {
        toSend = name + ": " + msg;
    }
    
    //Returns an array of strings denoting all reachable hosts in the local area
    //network. A host is reachable iff [INetAddress].isReachable() returns true.
    public static String[] findHosts() {
        ArrayList<String> hosts = new ArrayList<>();
        ArrayList<HostsDetector> detectors = new ArrayList<>();
        
        Thread[] threads = new Thread[4*Runtime.getRuntime().availableProcessors()];
        
        //for-loop assigns each thread a range on which to search - from (i*threads.length) to (i+1)*threads.length-1)
        for (int i = 0; i < threads.length; i++) {
            detectors.add(i, new HostsDetector(i*threads.length, (i+1)*threads.length-1));
            threads[i] = new Thread(detectors.get(i));
            threads[i].start();
        }
        
        
        for (int i = 0; i < threads.length; i++) {
            
            while(threads[i].isAlive()) {
                try {
                    Thread.sleep(HostsDetector.TIMEOUT);
                } catch (InterruptedException ex) {
                    break;
                }
            }
            
            String[] results = detectors.get(i).getReachableHosts();
            hosts.addAll(Arrays.asList(results));
        }
        
        String[] allHosts = new String[hosts.size()];
        for (int i = 0; i < allHosts.length; i++) {
            allHosts[i] = hosts.get(i);
        }
        return allHosts;
    }
    
    //directly connects this object's Socket object to a peer.
    private void connect(String peerAddr) {
        ss = null;
        
        try {
            if (peerAddr.compareTo("localhost") == 0 || 
                    peerAddr.compareTo(InetAddress.getLocalHost().getHostAddress()) < 0) {
                System.out.println("Connecting (client-side) ...");
                while (!Thread.interrupted()) {
                    try {
                        conn = new Socket(InetAddress.getByName(peerAddr), PORT_NUM);
                        break;
                    } catch(IOException ex) {
                        if (conn != null)
                            conn.close();
                    }
                }
            }
            else {
                System.out.println("Connecting (server-side) ...");
                ss = new ServerSocket(PORT_NUM); //arg 0 signifies that port# is automatically assigned
                ss.setReuseAddress(true);
                ss.setSoTimeout(SO_TIMEOUT);
                conn = ss.accept();
            }
            conn.setTcpNoDelay(TCP_NO_DELAY);
            System.out.println("Connected!");
            
        } catch (IOException ex) {
            try {
                if (ss != null) ss.close();
                if (conn != null) conn.close();
            } catch (IOException ex1) {
                Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex1);
                System.exit(1);
            }
            Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    private void writeSocket(String out) {
        if (out == null || out.compareTo("") == 0)
            return;
        pw.println(out);
        pw.flush();
    }
    
    //returns empty string if nothing was read, else returns a non-empty, 
    //non-null string
    private String readSocket() {
        String in = "";
        try {
            while (br.ready()) {
                String line = br.readLine();
                in = in.concat(line).concat("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return in;
    }
    
    private void closeAll() {
        try {
            pw.close();
            br.close();
            conn.close();
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    @Override
    public void run() {
        System.out.println("Starting thread for " + userName + ".");
        try {
            while(!Thread.interrupted()) {
                String in = readSocket();
                if (in != null && in.compareTo("") != 0) {
                    toReceive = in;
                    synchronized(this) {
                        notify(); //notify Window_chat object that input is available
                    }
                }
                if (toSend != null && !toSend.isEmpty()) {
                    writeSocket(toSend);
                    toSend = "";
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeAll();
        }
    }
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        Window_connect wConn = new Window_connect();
        wConn.setVisible(true);
        try {
            synchronized(wConn) {
                wConn.wait();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Messenger.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        if (wConn == null) //if Window_connect window was closed early
            return;
        
        String userName = wConn.getUserName();
        Messenger mes = wConn.getMessenger();
        wConn.dispose();
        
        Window_chat wChat = new Window_chat(mes, userName);
        wChat.setVisible(true);
        Thread t = new Thread(mes);
        t.start();
    }
}