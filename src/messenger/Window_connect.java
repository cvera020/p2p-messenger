package messenger;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Window_connect extends javax.swing.JFrame {
    public Messenger m = null;
    public String userName = null;
    
    public Window_connect() {
        initComponents();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Messenger getMessenger() {
        return m;
    }
    
    public String getUserName() {
        return userName;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton_connect = new javax.swing.JButton();
        jTextField_sn = new javax.swing.JTextField();
        jTextField_ip = new javax.swing.JTextField();
        jButton_findHosts = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton_connect.setText("Connect");
        jButton_connect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_connectMouseClicked(evt);
            }
        });
        jButton_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_connectActionPerformed(evt);
            }
        });

        jTextField_sn.setText("name");
        jTextField_sn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_snKeyTyped(evt);
            }
        });

        jTextField_ip.setText("ip");
        jTextField_ip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_ipKeyTyped(evt);
            }
        });

        jButton_findHosts.setText("Find Hosts");
        jButton_findHosts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_findHostsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField_sn, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                            .addComponent(jTextField_ip))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton_connect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(jButton_findHosts)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField_sn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_connect)
                    .addComponent(jButton_findHosts))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_connectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_connectMouseClicked
        String recIP = this.jTextField_ip.getText();
        userName = this.jTextField_sn.getText();
        
        if ( recIP.isEmpty() || userName.isEmpty())
            return;
        else
            m = new Messenger(recIP, userName);
        
        if (m != null) {
            this.jButton_connect.setEnabled(false);
            synchronized(this) { this.notify(); }
        }
    }//GEN-LAST:event_jButton_connectMouseClicked

    private void jTextField_ipKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ipKeyTyped
        if (evt.getKeyChar() == '\n') 
            jButton_connectMouseClicked(null);
    }//GEN-LAST:event_jTextField_ipKeyTyped

    private void jTextField_snKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_snKeyTyped
        if (evt.getKeyChar() == '\n') 
            jButton_connectMouseClicked(null);
    }//GEN-LAST:event_jTextField_snKeyTyped

    private void jButton_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_connectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_connectActionPerformed

    private void jButton_findHostsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_findHostsActionPerformed
        ArrayList<String> hosts = new HostsDetector().getReachableHosts();
        Object[] options = new Object[hosts.size()];
        for (int i = 0; i < hosts.size(); i++)
            options[i] = hosts.get(i);
        String s = (String)JOptionPane.showInputDialog(
                    this,
                    "Select a host to connect to: ",
                    "Available hosts",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    "...");
        //If a string was returned, say so.
        if (s != null) {
            jTextField_ip.setText(s);
        }
    }//GEN-LAST:event_jButton_findHostsActionPerformed

    

    public void start() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window_connect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window_connect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window_connect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window_connect.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window_connect().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_connect;
    private javax.swing.JButton jButton_findHosts;
    private javax.swing.JTextField jTextField_ip;
    private javax.swing.JTextField jTextField_sn;
    // End of variables declaration//GEN-END:variables
}
