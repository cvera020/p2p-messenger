package messenger;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PipedInputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Window_chat extends javax.swing.JFrame {
    private final Messenger mes;
    private final String userName;
    Thread chatThread;

    private class ChatThread implements Runnable {
        public static final int BUFFER_SIZE = 1024;
        
        @Override
        public void run() {
            while(!Thread.interrupted()) {
                try {
                    synchronized(mes) {
                        mes.wait();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Window_chat.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
                jTextArea_chat.append(mes.receive());
            }
        }
    }
    
    public Window_chat(Messenger m, String name) {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mes = m;
        userName = name;
        
        jTextArea_chat.setRows(1024);
        chatThread = new Thread(new ChatThread());
        chatThread.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_chat = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_send = new javax.swing.JTextArea();
        jButton_send = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea_chat.setEditable(false);
        jTextArea_chat.setColumns(20);
        jTextArea_chat.setRows(5);
        jScrollPane1.setViewportView(jTextArea_chat);

        jTextArea_send.setColumns(20);
        jTextArea_send.setRows(5);
        jTextArea_send.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea_sendKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea_send);

        jButton_send.setText("Send");
        jButton_send.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_sendMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_send, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_send))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_sendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_sendMouseClicked
        //evt == null is checked so that container function can be called explicitly
        if ( ( evt == null || evt.getButton() == MouseEvent.BUTTON1 ) 
                && jTextArea_send.getText().trim().isEmpty() == false) {
            String text = jTextArea_send.getText().trim();
            mes.send(text, userName);
            jTextArea_chat.append(userName + ": " + text + "\n");
            jTextArea_send.setText("");
        }
    }//GEN-LAST:event_jButton_sendMouseClicked

    private void jTextArea_sendKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea_sendKeyTyped
        if (evt.getKeyChar() == '\n') 
            jButton_sendMouseClicked(null);
    }//GEN-LAST:event_jTextArea_sendKeyTyped

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_send;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea_chat;
    private javax.swing.JTextArea jTextArea_send;
    // End of variables declaration//GEN-END:variables

}
