
import java.util.ArrayList;
import javax.swing.JLabel;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Sina
 */
public class Login extends javax.swing.JFrame {
    
    private InsuranceCompany company;
    private final String fileName = "company.ser";
    
    /**
     * Creates new form Login
     */
    public Login(InsuranceCompany company) {
        this.company = company;
        initComponents();
        setTitle("Login UI");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usersListDialog = new javax.swing.JDialog();
        jButton1 = new javax.swing.JButton();
        usernameField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        loadMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        javax.swing.GroupLayout usersListDialogLayout = new javax.swing.GroupLayout(usersListDialog.getContentPane());
        usersListDialog.getContentPane().setLayout(usersListDialogLayout);
        usersListDialogLayout.setHorizontalGroup(
            usersListDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        usersListDialogLayout.setVerticalGroup(
            usersListDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        usernameField.setToolTipText("Username");

        passwordField.setToolTipText("Password");
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        jMenu2.setText("File");

        saveMenuItem.setText("Save");
        saveMenuItem.setToolTipText("");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(saveMenuItem);

        loadMenuItem.setText("Load");
        loadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(loadMenuItem);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("List of Users");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(155, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            if (company.validateAdmin(usernameField.getText(), passwordField.getText())) {
                new AdminUI(company, this).setVisible(true);
            } else {
                User user = company.validateUser(Integer.parseInt(usernameField.getText()), passwordField.getText());
                if (user == null) throw new Exception();
                ArrayList<String> cities = company.populateDistinctCityNames(company.getAdminUsername(), company.getAdminPassword());
                new UserUI(user, cities, company.getFlatRate(), this).setVisible(true);
            }
            setVisible(false);
            passwordField.setText("");
        } catch (Exception e) {
            System.out.println(e);
            usernameField.setText("");
            passwordField.setText("");
            JOptionPane.showMessageDialog(rootPane, "Username or Password incorrect.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        // TODO add your handling code here:
        boolean succeed = company.save(company.getAdminUsername(), company.getAdminPassword(), fileName);
        JOptionPane.showMessageDialog(
            rootPane,
            "Saving data was " + (succeed ? "" : "un") + "successful!",
            "Saving Data",
            succeed ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        // TODO add your handling code here:
        String list = "";
        for (User user : company.getUsers().values()) {
            list += "Username: " + user.getUserID() + "     Password: " + user.getPassword();
            list += "\n";
        }
        JOptionPane.showMessageDialog(rootPane, list, "List of all Users", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenu3MouseClicked

    private void loadMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMenuItemActionPerformed
        // TODO add your handling code here:
        boolean succeed = company.load(company.getAdminUsername(), company.getAdminPassword(), fileName);
        JOptionPane.showMessageDialog(
            rootPane,
            "Loading data was " + (succeed ? "" : "un") + "successful!",
            "Loading Data",
            succeed ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
    }//GEN-LAST:event_loadMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem loadMenuItem;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JTextField usernameField;
    private javax.swing.JDialog usersListDialog;
    // End of variables declaration//GEN-END:variables
}
