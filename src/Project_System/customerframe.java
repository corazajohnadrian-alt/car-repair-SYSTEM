/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project_System;

/**
 *
 * @author Adrian
 */
import static Project_System.addcar.file;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class customerframe extends javax.swing.JFrame {

    
    static String username = "adri"; // FOR CHATT

            File carpic;
            private JFXPanel fxPanel;
private MediaPlayer mediaPlayer;
    private Timer refreshTimer; //FOR CHATT
    static String carrepairr = "src\\CAR REPAIRS.csv"; //THIS IS FOR THE CAR REPAIRS
    static String problemsfile = "src\\problems.csv"; // PROBLEMS FOR EACH CAR SAVED IN CSV
    static String updatestatusCSV = "src\\updatestatus.csv";
    private int currentGrandTotal = 0; // tracks total for cash calculation
    private int    currentSameCustomerId = 1; // tracks which car slot (1,2,3)
    private String currentPaymentMethod  = "CASH"; // tracks payment method
    private int    currentIdreq      = -1;
    private String currentCar        = "";
    private int    currentLabourTotal = 0;
    private int    currentPartsTotal  = 0;
    private List<String>   currentIssues    = new ArrayList<>();
    private List<String[]> currentPartsData = new ArrayList<>();
    private int repairmanidissue = 0;
    private int currentServiceSlot = 1; // 1, 2, or 3 — which car button was clicked
    private String assignedMechanic = ""; // mechanic assigned to this customer
    private String selectedTimeSlot = "";

    
        // Messenger fields (mirrored from mechanicframe — mechanic is contact, customer is self)
    private java.util.Map<String, java.util.List<String[]>> conversations = new java.util.LinkedHashMap<>();
    private java.util.Map<String, String>                   displayNames   = new java.util.LinkedHashMap<>();
    private java.util.Set<String>                           readConversations = new java.util.HashSet<>();
    private String                                          activeChatUser = null;
    static final String                                     MESSAGES_CSV   = "src\\MESSAGES.csv";


    
    static int problemnum =0;

       
     //VARIABLES 
    int numlayercreatereq = 0;
    int numlayerservicestat = 0;
    JPanel[] layercreatereq;
    JPanel [] layerserv;
    JPanel [] paymentlayer;
    JButton[] buttons; //problems
    boolean clickbut = false; //problems
    private List<String> selectedProblems = new ArrayList<>();
    private String selectedFilePath = "";
    private String autoAssignedMechanic = ""; // repairman picked at submit time
    private static final String[] ALL_MECHANICS = {"SEV", "Sherwin", "Edmer"};

    
    public customerframe(String username) {
        initComponents();
        
        
        // System name for frame
        this.setTitle("FIXO: Framework for Interactive X-auto Operations");
        try {
            // for logo in frame
            ImageIcon logo = new ImageIcon(getClass().getResource("/Project_System/resources/logo.png"));
            this.setIconImage(logo.getImage());
        } catch (Exception e) {
            System.out.println("Logo could not be loaded: " + e.getMessage());
        }
        
        jProgressBar1.setUI(new Project_System.Design.ModernProgressBarUI());
        jProgressBar1.setForeground(new Color(0, 102, 115)); // Your FIXO Teal
        jProgressBar1.setBorderPainted(false);
        pack();
        loadDashboard();    
        problemscsv();
        this.username = username;
        buttongroup();

            // Time slot toggle tracking + disable past slots
        button1.addActionListener(evt -> { selectedTimeSlot = button1.isSelected() ? "7 AM - 9 AM"  : ""; });
        button2.addActionListener(evt -> { selectedTimeSlot = button2.isSelected() ? "9 AM - 12 PM" : ""; });
        button3.addActionListener(evt -> { selectedTimeSlot = button3.isSelected() ? "1 PM - 3 PM"  : ""; });
        button4.addActionListener(evt -> { selectedTimeSlot = button4.isSelected() ? "3 PM - 5 PM"  : ""; });

        datee.addPropertyChangeListener("date", evt -> checkAndDisableTimeSlots());
        
        loadAssignedMechanic(); // ← sets assignedMechanic + jLabel5 text
        assignlabel();
        visibles();
        
        Platform.setImplicitExit(false);
        fxPanel = new JFXPanel();
        videopanel.setLayout(new java.awt.BorderLayout());
        videopanel.add(fxPanel, java.awt.BorderLayout.CENTER);

        startRefreshing(); // ADD THIS
        this.setLocationRelativeTo(null);//CENTER form in the screen
        setSize(1280, 724);
        startRefreshingbutton(); // ADD THIS
                initChatHeadListener(); // wire up jTable1 click → open chat
               


        layercreatereq = new JPanel[]{vehicles, issue, Photos, schedule, SUMMARY};
        layerserv = new JPanel[]{servpanel1, servpanel2};
        paymentlayer = new JPanel[]{paymentlayer1, paymentlayer2};
        
       // ── Wire radio buttons into one group ──
        buttonGroup1.add(CASH);
        CASH.setSelected(true);          // default = CASH
        cash.setEnabled(true);           // cash field enabled by default

        // ── Live balance calculation ──
        cash.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                if (!currentPaymentMethod.equals("CASH")) return;
                try {
                    int entered = Integer.parseInt(cash.getText().trim().replace(",", ""));
                    int change = entered - currentGrandTotal;
                    balance.setText(change >= 0 ? "₱" + String.format("%,d", change) : "Insufficient");
                } catch (NumberFormatException e) {
                    balance.setText("₱0");
                }
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
        

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel2 = new Project_System.Design.GradientPanel();
        Dashboard = new javax.swing.JButton();
        CreateR = new javax.swing.JButton();
        ServiceStatus = new javax.swing.JButton();
        Messages = new javax.swing.JButton();
        Payment = new javax.swing.JButton();
        History = new javax.swing.JButton();
        jLabel23 = new Project_System.Design.RoundedLogo();
        jLabel24 = new javax.swing.JLabel();
        jButton11 = new Project_System.Design.ModernButton();
        USER = new javax.swing.JTabbedPane();
        DASHBOARD = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new Project_System.Design.RoundedPanel();
        jLabel21 = new javax.swing.JLabel();
        numofcarinprogress = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        numofcarinreadytopickup = new javax.swing.JLabel();
        jPanel7 = new Project_System.Design.RoundedPanel();
        carpic2 = new javax.swing.JButton();
        carpic3 = new javax.swing.JButton();
        carpic1 = new javax.swing.JButton();
        carmodelandplatenum = new javax.swing.JLabel();
        carmodelandplatenum1 = new javax.swing.JLabel();
        carmodelandplatenum2 = new javax.swing.JLabel();
        progress = new javax.swing.JLabel();
        progress3 = new javax.swing.JLabel();
        progress2 = new javax.swing.JLabel();
        serviceupdate = new Project_System.Design.ModernButton();
        jButton15 = new Project_System.Design.ModernButton();
        jButton16 = new Project_System.Design.ModernButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        notifarea = new Project_System.Design.ShadowTextArea();
        jLabel29 = new javax.swing.JLabel();
        createrq = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel3 = new Project_System.Design.RoundedPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        createreqpanel = new javax.swing.JLayeredPane();
        vehicles = new Project_System.Design.RoundedPanel();
        addcar1 = new javax.swing.JButton();
        addcar2 = new javax.swing.JButton();
        addcar3 = new javax.swing.JButton();
        edit1 = new Project_System.Design.ModernButton();
        edits3 = new Project_System.Design.ModernButton();
        edit2 = new Project_System.Design.ModernButton();
        issue = new Project_System.Design.RoundedPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtareaissues = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        issuess = new javax.swing.JTable();
        idontknow = new javax.swing.JButton();
        others = new javax.swing.JButton();
        Photos = new Project_System.Design.RoundedPanel();
        videopanel = new javax.swing.JPanel();
        image = new javax.swing.JButton();
        schedule = new javax.swing.JPanel();
        datee = new com.toedter.calendar.JDateChooser();
        timeslot = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        button1 = new javax.swing.JToggleButton();
        button2 = new javax.swing.JToggleButton();
        button4 = new javax.swing.JToggleButton();
        button3 = new javax.swing.JToggleButton();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        label3 = new javax.swing.JLabel();
        SUMMARY = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        summarypanel = new javax.swing.JTextArea();
        sumpic = new javax.swing.JLabel();
        ADDCAR = new Project_System.Design.ModernButton();
        back = new Project_System.Design.ModernButton();
        next = new Project_System.Design.ModernButton();
        USERNAME = new javax.swing.JLabel();
        servicestatus = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        back2 = new Project_System.Design.ModernButton();
        servicestatpanel = new javax.swing.JLayeredPane();
        servpanel1 = new Project_System.Design.RoundedPanel();
        servicecar1 = new javax.swing.JButton();
        servicecar2 = new javax.swing.JButton();
        servicecar3 = new javax.swing.JButton();
        edit3 = new Project_System.Design.ModernButton();
        edit4 = new Project_System.Design.ModernButton();
        edit5 = new Project_System.Design.ModernButton();
        servpanel2 = new Project_System.Design.RoundedPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jButton8 = new Project_System.Design.ModernButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        updatestatus = new Project_System.Design.ShadowTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        materialarea = new javax.swing.JTextArea();
        toppanel = new javax.swing.JLayeredPane();
        SERVPANEL1 = new Project_System.Design.RoundedPanel();
        jLabel18 = new javax.swing.JLabel();
        SERVPANEL2 = new Project_System.Design.RoundedPanel();
        servpanel = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        message = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        display2 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jButton7 = new Project_System.Design.ModernButton();
        text2message = new Project_System.Design.RoundedTextField();
        SENDBUTTONMESSAGE = new Project_System.Design.ModernButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        payment = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        paymentlayerpanel = new javax.swing.JLayeredPane();
        paymentlayer1 = new Project_System.Design.RoundedPanel();
        paymentcar1 = new Project_System.Design.ModernButton();
        paymentcar3 = new Project_System.Design.ModernButton();
        paymentcar2 = new Project_System.Design.ModernButton();
        paymentlayer2 = new Project_System.Design.RoundedPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        recieptarea = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        paymentareatext = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        partslist = new Project_System.Design.ModernTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cash = new javax.swing.JTextField();
        totalpayment = new javax.swing.JLabel();
        balance = new javax.swing.JLabel();
        paybutton = new Project_System.Design.ModernButton();
        jButton9 = new Project_System.Design.ModernButton();
        CASH = new javax.swing.JRadioButton();
        jLabel27 = new javax.swing.JLabel();
        history = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        historytable = new Project_System.Design.ModernTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setOpaque(false);

        Dashboard.setBackground(new java.awt.Color(255, 255, 255));
        Dashboard.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        Dashboard.setForeground(new java.awt.Color(0, 153, 153));
        Dashboard.setText("Dashboard");
        Dashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardActionPerformed(evt);
            }
        });

        CreateR.setBackground(new java.awt.Color(255, 255, 255));
        CreateR.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        CreateR.setForeground(new java.awt.Color(0, 153, 153));
        CreateR.setText("Create Request");
        CreateR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateRActionPerformed(evt);
            }
        });

        ServiceStatus.setBackground(new java.awt.Color(255, 255, 255));
        ServiceStatus.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        ServiceStatus.setForeground(new java.awt.Color(0, 153, 153));
        ServiceStatus.setText("Service Status");
        ServiceStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServiceStatusActionPerformed(evt);
            }
        });

        Messages.setBackground(new java.awt.Color(255, 255, 255));
        Messages.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        Messages.setForeground(new java.awt.Color(0, 153, 153));
        Messages.setText("Messages");
        Messages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MessagesActionPerformed(evt);
            }
        });

        Payment.setBackground(new java.awt.Color(255, 255, 255));
        Payment.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        Payment.setForeground(new java.awt.Color(0, 153, 153));
        Payment.setText("Payment");
        Payment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PaymentActionPerformed(evt);
            }
        });

        History.setBackground(new java.awt.Color(255, 255, 255));
        History.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        History.setForeground(new java.awt.Color(0, 153, 153));
        History.setText("History");
        History.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryActionPerformed(evt);
            }
        });

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Project_System/resources/logo.png"))); // NOI18N
        jLabel23.setMaximumSize(new java.awt.Dimension(100, 100));
        jLabel23.setMinimumSize(new java.awt.Dimension(100, 100));
        jLabel23.setPreferredSize(new java.awt.Dimension(100, 100));

        jLabel24.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 28)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("FIXO");
        jLabel24.setMaximumSize(new java.awt.Dimension(100, 100));
        jLabel24.setMinimumSize(new java.awt.Dimension(100, 100));
        jLabel24.setPreferredSize(new java.awt.Dimension(100, 100));

        jButton11.setBackground(new java.awt.Color(0, 153, 153));
        jButton11.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Log Out");
        jButton11.setBorder(null);
        jButton11.setMinimumSize(new java.awt.Dimension(65, 25));
        jButton11.setPreferredSize(new java.awt.Dimension(65, 25));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(History, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(Payment, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(Messages, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(ServiceStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(CreateR, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(Dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CreateR, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ServiceStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Messages, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Payment, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(History, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 720));

        DASHBOARD.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DASHBOARD");

        jPanel8.setBackground(new java.awt.Color(239, 239, 239));
        jPanel8.setForeground(new java.awt.Color(153, 153, 153));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel21.setText("IN PROGRESS");

        numofcarinprogress.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        numofcarinprogress.setText("jLabel23");

        jLabel22.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel22.setText("READY FOR PICKUP");

        numofcarinreadytopickup.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        numofcarinreadytopickup.setText("jLabel23");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(numofcarinprogress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numofcarinreadytopickup)
                    .addComponent(jLabel22))
                .addGap(164, 164, 164))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numofcarinprogress)
                    .addComponent(numofcarinreadytopickup))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(java.awt.Color.white);
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        carpic2.setText("jButton10");
        carpic2.setMaximumSize(new java.awt.Dimension(80, 80));
        carpic2.setMinimumSize(new java.awt.Dimension(80, 80));
        carpic2.setPreferredSize(new java.awt.Dimension(80, 80));

        carpic3.setText("jButton10");
        carpic3.setMaximumSize(new java.awt.Dimension(80, 80));
        carpic3.setMinimumSize(new java.awt.Dimension(80, 80));
        carpic3.setPreferredSize(new java.awt.Dimension(80, 80));

        carpic1.setText("jButton10");
        carpic1.setMaximumSize(new java.awt.Dimension(80, 80));
        carpic1.setMinimumSize(new java.awt.Dimension(80, 80));
        carpic1.setPreferredSize(new java.awt.Dimension(80, 80));

        carmodelandplatenum.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        carmodelandplatenum.setText("carmodelandplatenum");

        carmodelandplatenum1.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        carmodelandplatenum1.setText("carmodelandplatenum");

        carmodelandplatenum2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        carmodelandplatenum2.setText("carmodelandplatenum");

        progress.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        progress.setText("progress and time");

        progress3.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        progress3.setText("progress and time");

        progress2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        progress2.setText("progress and time");

        serviceupdate.setBackground(new java.awt.Color(0, 153, 153));
        serviceupdate.setForeground(new java.awt.Color(255, 255, 255));
        serviceupdate.setText("VIEW SERVICE UPDATE");
        serviceupdate.setBorder(null);
        serviceupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceupdateActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(0, 153, 153));
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("VIEW SERVICE UPDATE");
        jButton15.setBorder(null);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(0, 153, 153));
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("VIEW SERVICE UPDATE");
        jButton16.setBorder(null);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(carpic1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(carpic2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(carpic3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(carmodelandplatenum)
                    .addComponent(progress)
                    .addComponent(carmodelandplatenum1)
                    .addComponent(progress2)
                    .addComponent(carmodelandplatenum2)
                    .addComponent(progress3))
                .addGap(38, 38, 38)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(serviceupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(carpic1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(carmodelandplatenum)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progress))
                    .addComponent(serviceupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(carpic2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(carmodelandplatenum1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progress2))
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(carmodelandplatenum2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progress3))
                    .addComponent(carpic3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(104, Short.MAX_VALUE))
        );

        jScrollPane12.setBorder(null);

        notifarea.setColumns(20);
        notifarea.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        notifarea.setRows(5);
        notifarea.setBorder(null);
        jScrollPane12.setViewportView(notifarea);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane12)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jLabel29.setBackground(new java.awt.Color(255, 255, 255));
        jLabel29.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Username");
        jLabel29.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel29.setPreferredSize(new java.awt.Dimension(84, 25));

        javax.swing.GroupLayout DASHBOARDLayout = new javax.swing.GroupLayout(DASHBOARD);
        DASHBOARD.setLayout(DASHBOARDLayout);
        DASHBOARDLayout.setHorizontalGroup(
            DASHBOARDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(DASHBOARDLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        DASHBOARDLayout.setVerticalGroup(
            DASHBOARDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DASHBOARDLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(DASHBOARDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        USER.addTab("tab1", DASHBOARD);

        createrq.setBackground(new java.awt.Color(0, 0, 0));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CREATE REQUEST");

        jPanel10.setBackground(new java.awt.Color(239, 239, 239));
        jPanel10.setForeground(new java.awt.Color(153, 153, 153));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jProgressBar1.setBorderPainted(false);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel13.setText("SCHEDULE");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel14.setText("SUMMARY");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel11.setText("VEHICLE");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel12.setText("PICTURE");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel16.setText("ISSUE");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(104, 104, 104)
                        .addComponent(jLabel16)
                        .addGap(100, 100, 100)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addGap(90, 90, 90)
                        .addComponent(jLabel14))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 853, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 15, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        createreqpanel.setLayout(new java.awt.CardLayout());

        vehicles.setBackground(new java.awt.Color(255, 255, 255));

        addcar1.setBackground(new java.awt.Color(255, 255, 255));
        addcar1.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        addcar1.setText("CAR MODEL");
        addcar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addcar1.setBorderPainted(false);
        addcar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addcar1.setMargin(new java.awt.Insets(10, 20, 10, 20));
        addcar1.setMaximumSize(new java.awt.Dimension(480, 100));
        addcar1.setMinimumSize(new java.awt.Dimension(480, 100));
        addcar1.setPreferredSize(new java.awt.Dimension(480, 100));
        addcar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addcar1ActionPerformed(evt);
            }
        });

        addcar2.setBackground(new java.awt.Color(255, 255, 255));
        addcar2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        addcar2.setText("+");
        addcar2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addcar2.setBorderPainted(false);
        addcar2.setMaximumSize(new java.awt.Dimension(480, 100));
        addcar2.setMinimumSize(new java.awt.Dimension(480, 100));
        addcar2.setPreferredSize(new java.awt.Dimension(480, 100));
        addcar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addcar2ActionPerformed(evt);
            }
        });

        addcar3.setBackground(new java.awt.Color(255, 255, 255));
        addcar3.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        addcar3.setText("+");
        addcar3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addcar3.setBorderPainted(false);
        addcar3.setMaximumSize(new java.awt.Dimension(480, 100));
        addcar3.setMinimumSize(new java.awt.Dimension(480, 100));
        addcar3.setPreferredSize(new java.awt.Dimension(480, 100));
        addcar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addcar3ActionPerformed(evt);
            }
        });

        edit1.setBackground(new java.awt.Color(0, 153, 153));
        edit1.setForeground(new java.awt.Color(255, 255, 255));
        edit1.setText("Edit");
        edit1.setBorder(null);
        edit1.setBorderPainted(false);
        edit1.setMaximumSize(new java.awt.Dimension(75, 75));
        edit1.setMinimumSize(new java.awt.Dimension(75, 75));
        edit1.setPreferredSize(new java.awt.Dimension(75, 75));
        edit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit1ActionPerformed(evt);
            }
        });

        edits3.setBackground(new java.awt.Color(0, 153, 153));
        edits3.setForeground(new java.awt.Color(255, 255, 255));
        edits3.setText("Edit");
        edits3.setBorder(null);
        edits3.setBorderPainted(false);
        edits3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edits3ActionPerformed(evt);
            }
        });

        edit2.setBackground(new java.awt.Color(0, 153, 153));
        edit2.setForeground(new java.awt.Color(255, 255, 255));
        edit2.setText("Edit");
        edit2.setBorder(null);
        edit2.setBorderPainted(false);
        edit2.setMaximumSize(new java.awt.Dimension(75, 75));
        edit2.setMinimumSize(new java.awt.Dimension(75, 75));
        edit2.setPreferredSize(new java.awt.Dimension(75, 75));
        edit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout vehiclesLayout = new javax.swing.GroupLayout(vehicles);
        vehicles.setLayout(vehiclesLayout);
        vehiclesLayout.setHorizontalGroup(
            vehiclesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclesLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(vehiclesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addcar1, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                    .addComponent(addcar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addcar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(vehiclesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edit2, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(edits3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edit1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        vehiclesLayout.setVerticalGroup(
            vehiclesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclesLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(vehiclesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addcar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edit1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(vehiclesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addcar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edit2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(vehiclesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addcar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edits3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        createreqpanel.add(vehicles, "card2");

        issue.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtareaissues.setColumns(20);
        txtareaissues.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtareaissues.setRows(5);
        jScrollPane2.setViewportView(txtareaissues);

        issuess.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISSUES"
            }
        ));
        jScrollPane3.setViewportView(issuess);

        idontknow.setText("I DONT KNOW");
        idontknow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idontknowActionPerformed(evt);
            }
        });

        others.setText("OTHERS");
        others.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                othersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout issueLayout = new javax.swing.GroupLayout(issue);
        issue.setLayout(issueLayout);
        issueLayout.setHorizontalGroup(
            issueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(issueLayout.createSequentialGroup()
                .addGroup(issueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(issueLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(issueLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(idontknow, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(others, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        issueLayout.setVerticalGroup(
            issueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(issueLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(issueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(issueLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(issueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(idontknow)
                            .addComponent(others))))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        createreqpanel.add(issue, "card3");

        Photos.setBackground(new java.awt.Color(255, 255, 255));

        videopanel.setBackground(new java.awt.Color(204, 255, 255));

        javax.swing.GroupLayout videopanelLayout = new javax.swing.GroupLayout(videopanel);
        videopanel.setLayout(videopanelLayout);
        videopanelLayout.setHorizontalGroup(
            videopanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 421, Short.MAX_VALUE)
        );
        videopanelLayout.setVerticalGroup(
            videopanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        image.setText("INSERT VIDEO OR PHOTOS");
        image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PhotosLayout = new javax.swing.GroupLayout(Photos);
        Photos.setLayout(PhotosLayout);
        PhotosLayout.setHorizontalGroup(
            PhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PhotosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(videopanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PhotosLayout.setVerticalGroup(
            PhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PhotosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(PhotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(videopanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        createreqpanel.add(Photos, "card3");

        schedule.setBackground(new java.awt.Color(204, 204, 204));

        datee.setDateFormatString("MMMMM dd, yyyy");
        datee.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        datee.setPreferredSize(new java.awt.Dimension(200, 22));
        datee.setMinSelectableDate(new java.util.Date());

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setText("TIMESLOT");

        button1.setText("7 AM - 9 AM");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button2.setText("9 AM - 12 PM");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        button4.setText("3 PM - 5 PM");

        button3.setText("1 PM - 3 PM");

        label1.setText("jLabel9");

        label2.setText("jLabel10");

        label4.setText("jLabel10");

        label3.setText("jLabel9");

        javax.swing.GroupLayout timeslotLayout = new javax.swing.GroupLayout(timeslot);
        timeslot.setLayout(timeslotLayout);
        timeslotLayout.setHorizontalGroup(
            timeslotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, timeslotLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(320, 320, 320))
            .addGroup(timeslotLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addGroup(timeslotLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(label1)
                .addGap(150, 150, 150)
                .addComponent(label2)
                .addGap(148, 148, 148)
                .addComponent(label3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label4)
                .addGap(85, 85, 85))
        );
        timeslotLayout.setVerticalGroup(
            timeslotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(timeslotLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(timeslotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(timeslotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(timeslotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(label3)
                        .addComponent(label4)
                        .addComponent(label2))
                    .addComponent(label1))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout scheduleLayout = new javax.swing.GroupLayout(schedule);
        schedule.setLayout(scheduleLayout);
        scheduleLayout.setHorizontalGroup(
            scheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(scheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeslot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(datee, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        scheduleLayout.setVerticalGroup(
            scheduleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datee, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(timeslot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        createreqpanel.add(schedule, "card3");

        SUMMARY.setBackground(new java.awt.Color(204, 204, 204));

        summarypanel.setColumns(20);
        summarypanel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        summarypanel.setRows(5);
        summarypanel.setText("\n\n");
        summarypanel.setBorder(null);
        jScrollPane4.setViewportView(summarypanel);

        sumpic.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout SUMMARYLayout = new javax.swing.GroupLayout(SUMMARY);
        SUMMARY.setLayout(SUMMARYLayout);
        SUMMARYLayout.setHorizontalGroup(
            SUMMARYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SUMMARYLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sumpic, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SUMMARYLayout.setVerticalGroup(
            SUMMARYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SUMMARYLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SUMMARYLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SUMMARYLayout.createSequentialGroup()
                        .addComponent(sumpic, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 29, Short.MAX_VALUE))
                    .addGroup(SUMMARYLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        createreqpanel.add(SUMMARY, "card3");

        ADDCAR.setBackground(new java.awt.Color(0, 153, 153));
        ADDCAR.setForeground(new java.awt.Color(255, 255, 255));
        ADDCAR.setText("ADD NEW CAR");
        ADDCAR.setBorder(null);
        ADDCAR.setBorderPainted(false);
        ADDCAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ADDCARActionPerformed(evt);
            }
        });

        back.setBackground(new java.awt.Color(0, 153, 153));
        back.setForeground(new java.awt.Color(255, 255, 255));
        back.setText("BACK");
        back.setBorder(null);
        back.setBorderPainted(false);
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        next.setBackground(new java.awt.Color(0, 153, 153));
        next.setForeground(new java.awt.Color(255, 255, 255));
        next.setText("NEXT");
        next.setBorder(null);
        next.setBorderPainted(false);
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(ADDCAR, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addContainerGap(15, Short.MAX_VALUE)
                    .addComponent(createreqpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 907, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(23, 23, 23)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 398, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ADDCAR, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(next, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(131, 131, 131)
                    .addComponent(createreqpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(120, Short.MAX_VALUE)))
        );

        USERNAME.setBackground(new java.awt.Color(255, 255, 255));
        USERNAME.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        USERNAME.setForeground(new java.awt.Color(255, 255, 255));
        USERNAME.setText("Username");
        USERNAME.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        USERNAME.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        USERNAME.setPreferredSize(new java.awt.Dimension(84, 25));

        javax.swing.GroupLayout createrqLayout = new javax.swing.GroupLayout(createrq);
        createrq.setLayout(createrqLayout);
        createrqLayout.setHorizontalGroup(
            createrqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createrqLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(USERNAME, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        createrqLayout.setVerticalGroup(
            createrqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createrqLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(createrqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(USERNAME, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        USER.addTab("tab1", createrq);

        servicestatus.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SERVICE STATUS");

        jPanel4.setBackground(new java.awt.Color(239, 239, 239));

        back2.setBackground(new java.awt.Color(0, 153, 153));
        back2.setForeground(new java.awt.Color(255, 255, 255));
        back2.setText("BACK");
        back2.setBorder(null);
        back2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        back2.setDefaultCapable(false);
        back2.setPreferredSize(new java.awt.Dimension(100, 25));
        back2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                back2ActionPerformed(evt);
            }
        });

        servicestatpanel.setLayout(new java.awt.CardLayout());

        servpanel1.setBackground(new java.awt.Color(255, 255, 255));
        servpanel1.setPreferredSize(new java.awt.Dimension(910, 414));

        servicecar1.setBackground(new java.awt.Color(255, 255, 255));
        servicecar1.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        servicecar1.setText("CAR MODEL");
        servicecar1.setPreferredSize(new java.awt.Dimension(129, 100));
        servicecar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servicecar1ActionPerformed(evt);
            }
        });

        servicecar2.setBackground(new java.awt.Color(255, 255, 255));
        servicecar2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        servicecar2.setText("CAR MODEL");
        servicecar2.setPreferredSize(new java.awt.Dimension(129, 100));
        servicecar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servicecar2ActionPerformed(evt);
            }
        });

        servicecar3.setBackground(new java.awt.Color(255, 255, 255));
        servicecar3.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        servicecar3.setText("CAR MODEL");
        servicecar3.setPreferredSize(new java.awt.Dimension(129, 100));
        servicecar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servicecar3ActionPerformed(evt);
            }
        });

        edit3.setBackground(new java.awt.Color(0, 153, 153));
        edit3.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        edit3.setForeground(new java.awt.Color(255, 255, 255));
        edit3.setText("Edit");
        edit3.setBorder(null);
        edit3.setMaximumSize(new java.awt.Dimension(80, 80));
        edit3.setMinimumSize(new java.awt.Dimension(80, 80));
        edit3.setPreferredSize(new java.awt.Dimension(80, 80));
        edit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit3ActionPerformed(evt);
            }
        });

        edit4.setBackground(new java.awt.Color(0, 153, 153));
        edit4.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        edit4.setForeground(new java.awt.Color(255, 255, 255));
        edit4.setText("Edit");
        edit4.setBorder(null);
        edit4.setPreferredSize(new java.awt.Dimension(80, 80));
        edit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit4ActionPerformed(evt);
            }
        });

        edit5.setBackground(new java.awt.Color(0, 153, 153));
        edit5.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        edit5.setForeground(new java.awt.Color(255, 255, 255));
        edit5.setText("Edit");
        edit5.setBorder(null);
        edit5.setPreferredSize(new java.awt.Dimension(80, 80));
        edit5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout servpanel1Layout = new javax.swing.GroupLayout(servpanel1);
        servpanel1.setLayout(servpanel1Layout);
        servpanel1Layout.setHorizontalGroup(
            servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servpanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(servpanel1Layout.createSequentialGroup()
                        .addGroup(servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(servicecar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(servicecar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(servpanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(edit4, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                            .addGroup(servpanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edit5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(23, 23, 23))
                    .addGroup(servpanel1Layout.createSequentialGroup()
                        .addComponent(servicecar1, javax.swing.GroupLayout.PREFERRED_SIZE, 751, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(edit3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 23, Short.MAX_VALUE))))
        );
        servpanel1Layout.setVerticalGroup(
            servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servpanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(servicecar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edit3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(servicecar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edit4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(servpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(servicecar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edit5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        servicestatpanel.add(servpanel1, "card2");

        servpanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel15.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel15.setText("Timeline");

        jLabel25.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel25.setText("Material Cost");

        jButton8.setBackground(new java.awt.Color(0, 153, 153));
        jButton8.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("MESSAGE THE MECHANIC FOR MORE INFO");
        jButton8.setBorder(null);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        updatestatus.setColumns(20);
        updatestatus.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        updatestatus.setRows(5);
        jScrollPane5.setViewportView(updatestatus);

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        materialarea.setColumns(20);
        materialarea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        materialarea.setRows(5);
        materialarea.setText("MATERIAL COST");
        jScrollPane7.setViewportView(materialarea);

        javax.swing.GroupLayout servpanel2Layout = new javax.swing.GroupLayout(servpanel2);
        servpanel2.setLayout(servpanel2Layout);
        servpanel2Layout.setHorizontalGroup(
            servpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servpanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(servpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(servpanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(servpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(servpanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                        .addGap(12, 12, 12))
                    .addGroup(servpanel2Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        servpanel2Layout.setVerticalGroup(
            servpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, servpanel2Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(servpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(servpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(servpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        servicestatpanel.add(servpanel2, "card3");

        toppanel.setPreferredSize(new java.awt.Dimension(910, 75));
        toppanel.setLayout(new java.awt.CardLayout());

        SERVPANEL1.setBackground(new java.awt.Color(255, 255, 255));
        SERVPANEL1.setPreferredSize(new java.awt.Dimension(910, 75));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("ACTIVE REPAIR");

        javax.swing.GroupLayout SERVPANEL1Layout = new javax.swing.GroupLayout(SERVPANEL1);
        SERVPANEL1.setLayout(SERVPANEL1Layout);
        SERVPANEL1Layout.setHorizontalGroup(
            SERVPANEL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SERVPANEL1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(698, Short.MAX_VALUE))
        );
        SERVPANEL1Layout.setVerticalGroup(
            SERVPANEL1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SERVPANEL1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        toppanel.add(SERVPANEL1, "card3");

        SERVPANEL2.setBackground(new java.awt.Color(255, 255, 255));
        SERVPANEL2.setPreferredSize(new java.awt.Dimension(900, 101));

        servpanel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        servpanel.setText("jLabel19");

        javax.swing.GroupLayout SERVPANEL2Layout = new javax.swing.GroupLayout(SERVPANEL2);
        SERVPANEL2.setLayout(SERVPANEL2Layout);
        SERVPANEL2Layout.setHorizontalGroup(
            SERVPANEL2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SERVPANEL2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(servpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(293, Short.MAX_VALUE))
        );
        SERVPANEL2Layout.setVerticalGroup(
            SERVPANEL2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SERVPANEL2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(servpanel, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addContainerGap())
        );

        toppanel.add(SERVPANEL2, "card3");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(back2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(toppanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(servicestatpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(31, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(back2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toppanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(527, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(153, 153, 153)
                    .addComponent(servicestatpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(82, Short.MAX_VALUE)))
        );

        back2.getAccessibleContext().setAccessibleDescription("");

        jLabel32.setBackground(new java.awt.Color(255, 255, 255));
        jLabel32.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Username");
        jLabel32.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel32.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel32.setPreferredSize(new java.awt.Dimension(84, 25));

        javax.swing.GroupLayout servicestatusLayout = new javax.swing.GroupLayout(servicestatus);
        servicestatus.setLayout(servicestatusLayout);
        servicestatusLayout.setHorizontalGroup(
            servicestatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servicestatusLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addGroup(servicestatusLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        servicestatusLayout.setVerticalGroup(
            servicestatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(servicestatusLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(servicestatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        USER.addTab("tab1", servicestatus);

        message.setBackground(new java.awt.Color(0, 0, 0));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("MESSAGE");

        jPanel5.setBackground(new java.awt.Color(239, 239, 239));

        display2.setColumns(20);
        display2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        display2.setRows(5);
        jScrollPane1.setViewportView(display2);

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel5.setText("MECHANIC");

        jButton7.setBackground(new java.awt.Color(0, 153, 153));
        jButton7.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("+");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        text2message.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                text2messageMouseClicked(evt);
            }
        });
        text2message.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text2messageActionPerformed(evt);
            }
        });

        SENDBUTTONMESSAGE.setBackground(new java.awt.Color(0, 153, 153));
        SENDBUTTONMESSAGE.setForeground(new java.awt.Color(255, 255, 255));
        SENDBUTTONMESSAGE.setText("SEND");
        SENDBUTTONMESSAGE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SENDBUTTONMESSAGEActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "MESSAGES"
            }
        ));
        jTable1.setRowHeight(100);
        jScrollPane14.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(text2message, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SENDBUTTONMESSAGE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(text2message, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SENDBUTTONMESSAGE, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane14))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Username");
        jLabel28.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel28.setPreferredSize(new java.awt.Dimension(84, 25));

        javax.swing.GroupLayout messageLayout = new javax.swing.GroupLayout(message);
        message.setLayout(messageLayout);
        messageLayout.setHorizontalGroup(
            messageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messageLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        messageLayout.setVerticalGroup(
            messageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messageLayout.createSequentialGroup()
                .addGroup(messageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(messageLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, messageLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)))
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        USER.addTab("tab1", message);

        payment.setBackground(new java.awt.Color(0, 0, 0));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("PAYMENT");

        jPanel6.setBackground(new java.awt.Color(239, 239, 239));

        paymentlayerpanel.setLayout(new java.awt.CardLayout());

        paymentlayer1.setBackground(new java.awt.Color(255, 255, 255));
        paymentlayer1.setOpaque(false);

        paymentcar1.setBackground(new java.awt.Color(0, 153, 153));
        paymentcar1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        paymentcar1.setForeground(new java.awt.Color(255, 255, 255));
        paymentcar1.setText("CAR MODEL");
        paymentcar1.setBorder(null);
        paymentcar1.setPreferredSize(new java.awt.Dimension(850, 100));
        paymentcar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentcar1ActionPerformed(evt);
            }
        });

        paymentcar3.setBackground(new java.awt.Color(0, 153, 153));
        paymentcar3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        paymentcar3.setForeground(new java.awt.Color(255, 255, 255));
        paymentcar3.setText("CAR MODEL");
        paymentcar3.setBorder(null);
        paymentcar3.setPreferredSize(new java.awt.Dimension(850, 100));
        paymentcar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentcar3ActionPerformed(evt);
            }
        });

        paymentcar2.setBackground(new java.awt.Color(0, 153, 153));
        paymentcar2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        paymentcar2.setForeground(new java.awt.Color(255, 255, 255));
        paymentcar2.setText("CAR MODEL");
        paymentcar2.setBorder(null);
        paymentcar2.setPreferredSize(new java.awt.Dimension(850, 100));
        paymentcar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentcar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paymentlayer1Layout = new javax.swing.GroupLayout(paymentlayer1);
        paymentlayer1.setLayout(paymentlayer1Layout);
        paymentlayer1Layout.setHorizontalGroup(
            paymentlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentlayer1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(paymentlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paymentcar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paymentcar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paymentcar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        paymentlayer1Layout.setVerticalGroup(
            paymentlayer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentlayer1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(paymentcar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(paymentcar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(paymentcar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(185, Short.MAX_VALUE))
        );

        paymentlayerpanel.add(paymentlayer1, "card2");

        paymentlayer2.setBackground(new java.awt.Color(255, 255, 255));

        recieptarea.setEditable(false);
        recieptarea.setColumns(20);
        recieptarea.setRows(5);
        jScrollPane8.setViewportView(recieptarea);

        paymentareatext.setColumns(20);
        paymentareatext.setRows(5);
        jScrollPane9.setViewportView(paymentareatext);

        partslist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Category", "Parts", "QTY"
            }
        ));
        jScrollPane10.setViewportView(partslist);

        jLabel17.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jLabel17.setText("TOTAL : ");

        jLabel19.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jLabel19.setText("CASH            :");

        jLabel20.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jLabel20.setText("BALANCE      :");

        cash.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N

        totalpayment.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        totalpayment.setText("0");

        balance.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        balance.setText("0");

        paybutton.setBackground(new java.awt.Color(0, 153, 153));
        paybutton.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        paybutton.setForeground(new java.awt.Color(255, 255, 255));
        paybutton.setText("PAY");
        paybutton.setBorder(null);
        paybutton.setPreferredSize(new java.awt.Dimension(85, 25));
        paybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paybuttonActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 153, 153));
        jButton9.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("BACK");
        jButton9.setBorder(null);
        jButton9.setPreferredSize(new java.awt.Dimension(85, 25));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        CASH.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        CASH.setText("CASH");
        CASH.setBorder(null);
        CASH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CASHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paymentlayer2Layout = new javax.swing.GroupLayout(paymentlayer2);
        paymentlayer2.setLayout(paymentlayer2Layout);
        paymentlayer2Layout.setHorizontalGroup(
            paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentlayer2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CASH))
                    .addGroup(paymentlayer2Layout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paybutton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paymentlayer2Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(paymentlayer2Layout.createSequentialGroup()
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(totalpayment, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cash, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        paymentlayer2Layout.setVerticalGroup(
            paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentlayer2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paymentlayer2Layout.createSequentialGroup()
                        .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paymentlayer2Layout.createSequentialGroup()
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(CASH)
                                    .addComponent(totalpayment)
                                    .addComponent(cash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel19)))
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paymentlayer2Layout.createSequentialGroup()
                                .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20)
                                    .addComponent(balance))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(paymentlayer2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(paymentlayer2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(paybutton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        paymentlayerpanel.add(paymentlayer2, "card3");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(paymentlayerpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(paymentlayerpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Username");
        jLabel27.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout paymentLayout = new javax.swing.GroupLayout(payment);
        payment.setLayout(paymentLayout);
        paymentLayout.setHorizontalGroup(
            paymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(paymentLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        paymentLayout.setVerticalGroup(
            paymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(paymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(14, 14, 14)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        USER.addTab("tab1", payment);

        history.setBackground(new java.awt.Color(0, 0, 0));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("HISTORY");

        jLabel26.setBackground(new java.awt.Color(255, 255, 255));
        jLabel26.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Username");
        jLabel26.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel26.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jPanel9.setBackground(new java.awt.Color(239, 239, 239));

        historytable.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        historytable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "DATE", "PLATENUMBER", "MECHANIC ASSIGNED", "MODEL", "TOTAL SPENT", "PAYMENT METHOD", "PAID/NOT PAID"
            }
        ));
        historytable.setGridColor(new java.awt.Color(204, 204, 204));
        historytable.setRowHeight(25);
        jScrollPane11.setViewportView(historytable);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout historyLayout = new javax.swing.GroupLayout(history);
        history.setLayout(historyLayout);
        historyLayout.setHorizontalGroup(
            historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
            .addGroup(historyLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        historyLayout.setVerticalGroup(
            historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyLayout.createSequentialGroup()
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historyLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        USER.addTab("tab1", history);

        getContentPane().add(USER, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, -26, 950, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    
    
    private void checkAndDisableTimeSlots() {
        java.util.Date chosen = datee.getDate();

        // Reset button labels first
        button1.setText("7 AM - 9 AM");
        button2.setText("9 AM - 12 PM");
        button3.setText("1 PM - 3 PM");
        button4.setText("3 PM - 5 PM");

        if (chosen == null) {
            button1.setEnabled(true);
            button2.setEnabled(true);
            button3.setEnabled(true);
            button4.setEnabled(true);
            return;
        }

        java.util.Calendar selected = java.util.Calendar.getInstance();
        selected.setTime(chosen);
        java.util.Calendar today = java.util.Calendar.getInstance();

        boolean isToday =
            selected.get(java.util.Calendar.YEAR)        == today.get(java.util.Calendar.YEAR) &&
            selected.get(java.util.Calendar.DAY_OF_YEAR) == today.get(java.util.Calendar.DAY_OF_YEAR);

        int hour = today.get(java.util.Calendar.HOUR_OF_DAY);

        // Format chosen date to match how pictures.csv stores it  e.g. "May/08/2026"
        String dateKey = new java.text.SimpleDateFormat("MMM/dd/yyyy").format(chosen);

        // Load all mechanics
        int totalMechanics = ALL_MECHANICS.length;


        // Slot definitions: label, past-cutoff hour
        String[] slotLabels  = {"7 AM - 9 AM", "9 AM - 12 PM", "1 PM - 3 PM", "3 PM - 5 PM"};
        int[]    pastCutoffs = {9,              12,              15,             17};
        javax.swing.JLabel[] labels = {label1, label2, label3, label4};

        for (int s = 0; s < 4; s++) {
            String slotLabel = slotLabels[s];
            javax.swing.JToggleButton[] slotBtns = {button1, button2, button3, button4};

            int busyCount = countBusyMechanics(dateKey, slotLabel);
            int freeCount = totalMechanics - busyCount;
            if (freeCount < 0) freeCount = 0;

            boolean pastTime = isToday && hour >= pastCutoffs[s];
            boolean canBook  = (totalMechanics == 0 || freeCount > 0) && !pastTime;

            // ✅ Disable/enable the actual toggle button
            slotBtns[s].setEnabled(canBook);
            if (!canBook && slotBtns[s].isSelected()) {
                slotBtns[s].setSelected(false);
                selectedTimeSlot = "";
            }

            // ✅ Always set label text regardless of pastTime
            if (pastTime) {
                labels[s].setText("(past)");
                labels[s].setForeground(java.awt.Color.GRAY);
            } else if (totalMechanics == 0) {
                labels[s].setText(slotLabels[s]);
                labels[s].setForeground(java.awt.Color.DARK_GRAY);
            } else if (freeCount > 0) {
                labels[s].setText("(" + freeCount + " mechanic"
                    + (freeCount > 1 ? "s" : "") + " free)");
                labels[s].setForeground(new java.awt.Color(0, 153, 0));
            } else {
                labels[s].setText("(fully booked)");
                labels[s].setForeground(java.awt.Color.RED);
            }
        }
    }
    
    private java.util.List<String> loadAllMechanics() {
        java.util.List<String> list = new java.util.ArrayList<>();
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader("src\\users.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                // cols: username(0), password(1), role(2)
                if (c.length >= 3 && c[2].trim().equalsIgnoreCase("mechanic")) {
                    list.add(c[0].trim());
                }
            }
        } catch (Exception e) { /* ignore if absent */ }
        return list;
    }
    
    private int countBusyMechanics(String dateKey, String slotLabel) {
        java.util.Set<String> busy = new java.util.HashSet<>();
        java.io.File file = new java.io.File("src\\pictures.csv");
        if (!file.exists()) return 0;
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 7) continue;
                String mechanic = c[3].trim();
                String rowDate  = c[5].trim();
                String rowSlot  = c[6].trim();
                if (rowDate.equalsIgnoreCase(dateKey) &&
                    rowSlot.equalsIgnoreCase(slotLabel) &&
                    !mechanic.isEmpty() && !mechanic.equalsIgnoreCase("N/A")) {
                    busy.add(mechanic.toLowerCase());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return busy.size();
    }
   
    public void buttongroup(){
        buttonGroup2.add(button1);
        buttonGroup2.add(button2);
        buttonGroup2.add(button3);
        buttonGroup2.add(button4);
    }
    
    private void refreshMessages() {
        java.util.Map<String, java.util.List<String[]>> fresh      = new java.util.LinkedHashMap<>();
        java.util.Map<String, String>                   freshNames = new java.util.LinkedHashMap<>();

        // 1. Load unique mechanics assigned to THIS customer from problems.csv
        // cols: repairmanissuenumber(0), issuenumber(1), username(2), car(3),
        //       problems(4), date(5), idreq(6), repairman(7)
        try (BufferedReader br = new BufferedReader(new FileReader("src\\problems.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (first) { first = false; continue; }

                String[] cols = line.split(",", -1);
                if (cols.length < 8) continue;

                String rowCustomer = cols[2].trim();
                String mechanic    = cols[7].trim();

                if (!rowCustomer.equalsIgnoreCase(username)) continue;
                if (mechanic.isEmpty()) continue;

                String key = mechanic.toLowerCase();
                freshNames.putIfAbsent(key, mechanic);
                fresh.computeIfAbsent(key, k -> new java.util.ArrayList<>());
            }
        } catch (IOException e) { e.printStackTrace(); }

        // 2. Load messages from MESSAGES.csv — attach to each mechanic contact
        // cols: customer(0), mechanic(1), sender(2), message(3)
        try (BufferedReader br = new BufferedReader(new FileReader("src\\MESSAGES.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (first) { first = false; continue; }

                String[] cols = line.split(",", 4);
                if (cols.length < 4) continue;

                String rowCustomer = cols[0].trim();
                String mechanic    = cols[1].trim();
                String sender      = cols[2].trim();
                String message     = cols[3].trim();

                if (!rowCustomer.equalsIgnoreCase(username)) continue;

                String key = mechanic.toLowerCase();
                if (fresh.containsKey(key)) {
                    fresh.get(key).add(new String[]{sender, message});
                }
            }
        } catch (IOException e) { /* file may not exist yet */ }

        // Mark unread if new messages arrived
        for (String key : fresh.keySet()) {
            int newCount = fresh.get(key).size();
            int oldCount = conversations.containsKey(key) ? conversations.get(key).size() : 0;
            if (newCount > oldCount) readConversations.remove(key);
        }

        conversations = fresh;
        displayNames  = freshNames;
        updateChatHeadTable();
        if (activeChatUser != null) loadChatMessages(activeChatUser);
    }

    private void updateChatHeadTable() {
        DefaultTableModel headModel = new DefaultTableModel(new String[]{"MESSAGES"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        for (java.util.Map.Entry<String, java.util.List<String[]>> entry : conversations.entrySet()) {
            String key                   = entry.getKey();
            java.util.List<String[]> msgs = entry.getValue();
            String lastMsg = msgs.isEmpty() ? "No messages yet" : msgs.get(msgs.size() - 1)[1];
            boolean unread = !readConversations.contains(key);

            String displayName = displayNames.getOrDefault(key,
                key.substring(0, 1).toUpperCase() + key.substring(1));

            String preview = lastMsg.length() > 40 ? lastMsg.substring(0, 40) + "…" : lastMsg;

            headModel.addRow(new Object[]{ new String[]{
                (unread ? "🔵 " : "    ") + displayName,
                preview
            }});
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            jTable1.setModel(headModel);
            jTable1.setRowHeight(60);

            jTable1.getColumnModel().getColumn(0).setCellRenderer(
                (table, value, isSelected, hasFocus, row, col) -> {
                    javax.swing.JPanel panel = new javax.swing.JPanel();
                    panel.setLayout(new java.awt.GridLayout(2, 1));
                    panel.setBackground(isSelected
                        ? table.getSelectionBackground()
                        : table.getBackground());

                    String[] lines = (String[]) value;

                    javax.swing.JLabel nameLine = new javax.swing.JLabel(lines[0]);
                    nameLine.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
                    nameLine.setForeground(isSelected
                        ? table.getSelectionForeground()
                        : table.getForeground());

                    javax.swing.JLabel previewLine = new javax.swing.JLabel(lines[1]);
                    previewLine.setFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
                    previewLine.setForeground(java.awt.Color.GRAY);

                    panel.add(nameLine);
                    panel.add(previewLine);
                    return panel;
                }
            );
        });
    }

    private void loadChatMessages(String mechanicKey) {
        java.util.List<String[]> msgs = conversations.getOrDefault(mechanicKey, new java.util.ArrayList<>());

        StringBuilder sb = new StringBuilder();
        for (String[] row : msgs) {
            String sender  = row[0];
            String message = row[1];
            String label   = sender.equalsIgnoreCase(username) ? "You" : sender;
            sb.append(label).append(": ").append(message).append("\n\n");
        }

        String displayName = displayNames.getOrDefault(mechanicKey,
            mechanicKey.substring(0, 1).toUpperCase() + mechanicKey.substring(1));

        javax.swing.SwingUtilities.invokeLater(() -> {
            display2.setText(sb.toString());
            display2.setCaretPosition(display2.getDocument().getLength());
        });

        readConversations.add(mechanicKey);
        updateChatHeadTable();
    }

    private void initChatHeadListener() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.rowAtPoint(evt.getPoint());
                if (row < 0) return;

                Object val = jTable1.getValueAt(row, 0);
                if (!(val instanceof String[])) return;

                String nameLine    = ((String[]) val)[0];
                String displayName = nameLine.replaceAll("^[🔵\\s]+", "").trim();
                String matchedKey  = displayName.toLowerCase();

                if (conversations.containsKey(matchedKey)) {
                    activeChatUser = matchedKey;
                    loadChatMessages(matchedKey);
                }
            }
        });
    }

    private void loadMessageTable() {
        // legacy stub — now handled by refreshMessages() + initChatHeadListener()
        refreshMessages();
    }

    void openConversation(String customer, String repairman) {
        // legacy — click on chat head instead; kept for ButtonEditor compatibility
        String key = repairman.toLowerCase();
        if (conversations.containsKey(key)) {
            activeChatUser = key;
            loadChatMessages(key);
        }
    }
    
    
    
    public void assignlabel(){
        jLabel29.setText(username);
        USERNAME.setText(username);
        jLabel32.setText(username);
        jLabel28.setText(username);
        jLabel27.setText(username);
        jLabel26.setText(username);

        
}

    
    
    public void visibles(){
        back2.setVisible(false);
             System.out.println(numlayercreatereq);
            if (numlayercreatereq == 0) {
                next.setVisible(false);
                back.setVisible(false);
            } 
            else {
                next.setVisible(true);
                back.setVisible(true);
            }

    }
    
    private void refreshTimeSlots() {
     java.util.Date picked = datee.getDate();
     if (picked == null) return;
     String dateStr = new java.text.SimpleDateFormat("MMM/dd/yyyy").format(picked);

    javax.swing.JToggleButton[] slotBtns = {button1, button2, button3, button4};
    String[] slotNames = {
        "7:00 AM - 9:00 AM",
        "9:00 AM - 12:00 PM",
        "1:00 PM - 3:00 PM",
        "3:00 PM - 6:00 PM"
    };

    for (int i = 0; i < slotBtns.length; i++) {
        final String slotName = slotNames[i];
        slotBtns[i].addActionListener(e -> {
            // Deselect all others
            for (javax.swing.JToggleButton sb : slotBtns) {
                if (sb != e.getSource()) sb.setSelected(false);
            }
            // If this one is now selected, store the slot
            if (((javax.swing.JToggleButton) e.getSource()).isSelected()) {
                selectedTimeSlot = slotName;
            } else {
                selectedTimeSlot = ""; // user untoggled
            }
        });
    }
    }

 /**
  * Reads pictures.csv and returns which mechanics are already booked
  * for the given date + time slot.
  */
    private java.util.List<String> getBookedMechanics(String date, String timeslot) {
        java.util.List<String> booked = new java.util.ArrayList<>();
        java.io.File file = new java.io.File("src\\pictures.csv");
        if (!file.exists()) return booked;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                String[] c = line.split(",", -1);
                // id(0), repairmanissuenumber(1), customer(2), mechanic(3), pathfile(4), date(5), time(6)
                if (c.length >= 7
                    && c[5].trim().equalsIgnoreCase(date)
                    && c[6].trim().equalsIgnoreCase(timeslot)
                    && !c[3].trim().isEmpty()) {
                    booked.add(c[3].trim());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return booked;
    }
    
    private void loadAssignedMechanic() {
            // Read problems.csv — col[2]=username, col[7]=mechanic
            String problemsPath = "src\\problems.csv";
            try (BufferedReader br = new BufferedReader(new FileReader(problemsPath))) {
                String line; boolean first = true;
                while ((line = br.readLine()) != null) {
                    if (first) { first = false; continue; }
                    String[] cols = line.split(",", -1);
                    if (cols.length >= 8 &&
                        cols[2].trim().equalsIgnoreCase(username) &&
                        !cols[7].trim().isEmpty()) {
                        assignedMechanic = cols[7].trim();
                        jLabel5.setText(assignedMechanic); // show mechanic name in label
                        break;
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    private void loadChatMessages() {
        if (assignedMechanic.isEmpty()) return;

        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("src\\MESSAGES.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (first) { first = false; continue; } // skip header

                // cols: customer(0), mechanic(1), sender(2), message(3)
                String[] cols = line.split(",", 4);
                if (cols.length < 4) continue;

                String rowCustomer = cols[0].trim();
                String rowMechanic = cols[1].trim();
                String sender      = cols[2].trim();
                String message     = cols[3].trim();

                // Only show THIS customer's conversation with THEIR mechanic
                if (rowCustomer.equalsIgnoreCase(username) &&
                    rowMechanic.equalsIgnoreCase(assignedMechanic)) {

                    String label = sender.equalsIgnoreCase(username) ? "You" : assignedMechanic;
                    sb.append(label).append(": ").append(message).append("\n\n");
                }
            }
        } catch (Exception e) { /* file may not exist yet */ }

        String text = sb.toString();
        if (!text.equals(display2.getText())) {
            display2.setText(text);
            display2.setCaretPosition(display2.getDocument().getLength());
        }
    }
    
       private void startRefreshing() {
    refreshTimer = new Timer(2000, e -> {
        loadChatMessages();
        loadmaterialsrequestparts();
        // ✅ Auto-refresh the status textarea for whichever car is currently open
        
    });
    refreshTimer.start();
}   
    private void refreshIssueTextArea() {
        txtareaissues.setText("");
        int i = 1;
        for (String p : selectedProblems) {
            txtareaissues.append(i + ". " + p + "\n");
            i++;
        }
    }
    public void problemscsv(){
        List<String> issueNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\issues.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] parts = line.split(",", -1);
                if (parts.length >= 3) {
                    String name = parts[2].trim();
                    if (!name.isEmpty()) issueNames.add(name);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(customerframe.class.getName()).log(Level.SEVERE, null, ex);
        }

        buttons = new JButton[issueNames.size()];
        Map<JButton, Boolean> state = new HashMap<>();
        DefaultTableModel model = (DefaultTableModel) issuess.getModel();
        model.setRowCount(0);

        for (int idx = 0; idx < issueNames.size(); idx++) {
            String issueName = issueNames.get(idx);
            JButton btn = new JButton(issueName);
            btn.setBackground(new Color(0, 153, 153));
            btn.setForeground(Color.WHITE);
            btn.setFont(new java.awt.Font("Bahnschrift", 0, 13));
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            buttons[idx] = btn;
            state.put(btn, false);
            model.addRow(new Object[]{btn});
        }

        issuess.getColumn("ISSUES").setCellRenderer(new IssueButtonRenderer(state));
        issuess.getColumn("ISSUES").setCellEditor(new IssueButtonEditor(state));
        issuess.setRowHeight(32);
    }
    public void loadDashboard() {
        String customer = customerframe.username;

        // Component arrays — slot 1,2,3 mapped to index 0,1,2
        javax.swing.JButton[]  carpics  = {carpic1, carpic2, carpic3};
        javax.swing.JLabel[]   carinfos = {carmodelandplatenum, carmodelandplatenum1, carmodelandplatenum2};
        javax.swing.JLabel[]   progLabels = {progress, progress2, progress3};

        // Reset all slots
        for (int i = 0; i < 3; i++) {
            carpics[i].setIcon(null);
            carpics[i].setText("-");
            carinfos[i].setText("-");
            progLabels[i].setText("-");
        }

        // ── 1. Load car info from CAR REPAIRS.csv ──
        // customerid(0), ownername(1), plateNum(2), model(3), brand(4),
        // year(5), color(6), mileage(7), link(8)
        java.util.Map<Integer, String[]> carMap = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(carrepairr))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 9) continue;
                if (!c[1].trim().equalsIgnoreCase(customer)) continue;
                int slot = Integer.parseInt(c[0].trim()); // customerid = slot
                carMap.put(slot, new String[]{
                    c[2].trim(), // plateNum
                    c[4].trim(), // model
                    c[8].trim()  // image path
                });
            }
        } catch (Exception e) { e.printStackTrace(); }

        
        // ── 3. Check reciept.csv for paid slots ──
        // id(0), samecustomerid(1), customer(2), DATE(3), payment(4), totalcost(5)
        java.util.Set<Integer> paidSlots = new java.util.HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\reciept.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                String[] c = line.split(",", 7);
                if (c.length < 3) continue;
                if (!c[2].trim().equalsIgnoreCase(customer)) continue;
                try { paidSlots.add(Integer.parseInt(c[1].trim())); }
                catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}

        // ── 4. Get latest status per idcustomer from updatestatus.csv ──
        java.util.Map<Integer, String> slotStatus = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(updatestatusCSV))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                String[] c = line.split(",", 6);
                if (c.length < 6) continue;
                if (!c[1].trim().equalsIgnoreCase(customer)) continue;
                // col[5]=idcustomer, col[3]=status — last row per idcustomer wins
                try { slotStatus.put(Integer.parseInt(c[5].trim()), c[3].trim()); }
                catch (Exception ignored) {}
            }
        } catch (Exception e) { e.printStackTrace(); }

        // ── 5. Populate dashboard per slot ──
        int inProgress = 0;
        int readyPickup = 0;

        for (int slot = 1; slot <= 3; slot++) {
            int idx = slot - 1;
            if (!carMap.containsKey(slot)) continue;

            String[] car     = carMap.get(slot);
            String plateNum  = car[0];
            String model     = car[1];
            String imagePath = car[2];

            carinfos[idx].setText(model + " | " + plateNum);

            if (!imagePath.isEmpty()) {
                java.io.File imgFile = new java.io.File(imagePath);
                if (imgFile.exists()) {
                    try {
                        java.awt.image.BufferedImage bimg = javax.imageio.ImageIO.read(imgFile);
                        if (bimg != null) {
                            java.awt.Image thumb = bimg.getScaledInstance(119, 88, java.awt.Image.SCALE_SMOOTH);
                            carpics[idx].setIcon(new javax.swing.ImageIcon(thumb));
                            carpics[idx].setText("");
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }

            boolean paid   = paidSlots.contains(slot);
            String status  = slotStatus.getOrDefault(slot, "WAITING");

            if (paid) {
                progLabels[idx].setText("PAID & RELEASED");
                progLabels[idx].setForeground(new java.awt.Color(0, 153, 0));
                                readyPickup++;

            } else if (status.equalsIgnoreCase("READY FOR PICKUP")) {
                progLabels[idx].setText("READY FOR PICKUP");
                progLabels[idx].setForeground(new java.awt.Color(255, 140, 0));
                readyPickup++;
            } else if (status.equalsIgnoreCase("WAITING")) {
                progLabels[idx].setText("WAITING");
                progLabels[idx].setForeground(java.awt.Color.GRAY);
                inProgress++;
            } else {
                progLabels[idx].setText("Diagnosing " + status);
                progLabels[idx].setForeground(new java.awt.Color(0, 102, 204));
                inProgress++;
            }
        }

        numofcarinprogress.setText(String.valueOf(inProgress));
        numofcarinreadytopickup.setText(String.valueOf(readyPickup));
        
        // ── 6. Append latest status of each car to notifarea ──
        StringBuilder notif = new StringBuilder();
        for (int slot = 1; slot <= 3; slot++) {
            if (!carMap.containsKey(slot)) continue;

            String[] car    = carMap.get(slot);
            String model    = car[1];
            String plateNum = car[0];
            String status   = slotStatus.getOrDefault(slot, "WAITING");

            // Get the latest timestamp for this slot from updatestatus.csv
            String latestTime = "N/A";
            try (BufferedReader br = new BufferedReader(new FileReader(updatestatusCSV))) {
                String line; boolean first = true;
                while ((line = br.readLine()) != null) {
                    if (first) { first = false; continue; }
                    if (line.trim().isEmpty()) continue;
                    String[] c = line.split(",", 6);
                    if (c.length < 6) continue;
                    if (!c[1].trim().equalsIgnoreCase(customer)) continue;
                    if (Integer.parseInt(c[5].trim()) == slot) {
                        latestTime = c[4].trim(); // last matching row wins
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }

            // Look up mechanic for this specific slot via idreq
            String slotMechanic = "Not yet assigned";
            try (BufferedReader brm = new BufferedReader(new FileReader("src\\problems.csv"))) {
                String mline; boolean mfirst = true;
                while ((mline = brm.readLine()) != null) {
                    if (mfirst) { mfirst = false; continue; }
                    String[] mc = mline.split(",", -1);
                    if (mc.length >= 8 &&
                        mc[2].trim().equalsIgnoreCase(customer) &&
                        Integer.parseInt(mc[6].trim()) == slot &&
                        !mc[7].trim().isEmpty()) {
                        slotMechanic = mc[7].trim();
                        break;
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }

            notif.append("Car ").append(slot).append(" — ").append(model)
            .append(" | ").append(plateNum)
            .append("\n  Customer : ").append(customer)
            .append("\n  Mechanic : ").append(slotMechanic)
            .append("\n  Status   : ").append(status)
            .append("\n  Updated  : ").append(latestTime)
            .append("\n\n");
               }

        notifarea.append(notif.toString());
        notifarea.setCaretPosition(notifarea.getDocument().getLength());
    }
    
    public void loadmaterialsrequestparts() {
        String csvPath = "src\\cartrequest.csv";
        java.io.File file = new java.io.File(csvPath);

        if (!file.exists()) {
            materialarea.setText("No parts requested yet.");
            return;
        }

        // ── Step 1: get the mechanic for the current service slot from problems.csv ──
        String targetMechanic = "";
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader("src\\problems.csv"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] cols = line.split(",", -1);
                if (cols.length < 8) continue;
               int idreq = Integer.parseInt(cols[6].trim());
                String uname = cols[2].trim();
                if (idreq == currentServiceSlot && uname.equalsIgnoreCase(username)) {
                    targetMechanic = cols[7].trim();
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (targetMechanic.isEmpty()) {
            materialarea.setText("No parts requested yet.");
            return;
        }

        // ── Step 2: read cartrequest.csv filtered by customer + mechanic ──
        StringBuilder sb = new StringBuilder();
        double grandTotal = 0;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (line.toLowerCase().startsWith("orderid")) continue;
                if (line.startsWith("---") || line.contains("GRAND TOTAL")) continue;

                String[] cols = line.split(",", -1);
                if (cols.length < 8) continue;

                String cust = cols[6].trim();   // col 6 = customer
                String mech = cols[7].trim();   // col 7 = mechanic

                if (cust.equalsIgnoreCase(username) && mech.equalsIgnoreCase(targetMechanic)) {
                    String part     = cols[2].trim();
                    String qty      = cols[3].trim();
                    String price    = cols[4].trim();
                    String total    = cols[5].trim();
                    try { grandTotal += Double.parseDouble(total); } catch (NumberFormatException ignored) {}
                    sb.append(String.format("  %-20s x%-3s  ₱%s%n", part, qty, total));
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            materialarea.setText("Error loading materials.");
            return;
        }

        if (sb.length() == 0) {
            materialarea.setText("No parts requested for this service.");
            return;
        }

        sb.insert(0, "══════════════════════════\n  MATERIAL COST\n══════════════════════════\n");
        sb.append("──────────────────────────\n");
        sb.append(String.format("  TOTAL:              ₱%.0f%n", grandTotal));
        sb.append("══════════════════════════\n");

        materialarea.setText(sb.toString());
        materialarea.setCaretPosition(0);
    }
    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        USER.setSelectedIndex(0);

    }//GEN-LAST:event_DashboardActionPerformed

    private void CreateRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateRActionPerformed
       USER.setSelectedIndex(1);

    }//GEN-LAST:event_CreateRActionPerformed

    private void ServiceStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServiceStatusActionPerformed
        USER.setSelectedIndex(2);

    }//GEN-LAST:event_ServiceStatusActionPerformed

    private void PaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PaymentActionPerformed
        USER.setSelectedIndex(4);

    }//GEN-LAST:event_PaymentActionPerformed

    private void MessagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MessagesActionPerformed
        USER.setSelectedIndex(3);
        loadAssignedMechanic();   // make sure mechanic is loaded first
            loadMessageTable();  
    }//GEN-LAST:event_MessagesActionPerformed

    private void HistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoryActionPerformed
        USER.setSelectedIndex(5);
            loadHistoryTable();

    }//GEN-LAST:event_HistoryActionPerformed
    public void loadHistoryTable() {
        String customer = customerframe.username;

        String[] columns = {"DATE", "PLATENUMBER", "MECHANIC ASSIGNED",
                            "MODEL", "TOTAL SPENT", "PAYMENT METHOD", "PAID/NOT PAID"};

        java.util.List<Object[]> rows = new java.util.ArrayList<>();

        // ── 1. Read all car slots for this customer from CAR REPAIRS ──
        // carrepair: customerid(0), ownername(1), plateNum(2), model(3), brand(4),
        //            year(5), color(6), mileage(7), link(8), notes(9)
        java.util.Map<String, String[]> carMap = new java.util.HashMap<>();
        // key = customerid, value = [plateNum, model]
        try (BufferedReader br = new BufferedReader(new FileReader(carrepairr))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 4) continue;
                if (!c[1].trim().equalsIgnoreCase(customer)) continue;
                carMap.put(c[0].trim(), new String[]{c[2].trim(), c[3].trim()});
            }
        } catch (Exception e) { e.printStackTrace(); }

        // ── 2. Read problems.csv to get repairmanissuenumber→idreq + date + mechanic ──
        // cols: repairmanissuenumber(0), issuenumber(1), username(2), car(3),
        //       problems(4), date(5), idreq(6), repairman(7)
        java.util.Map<String, String[]> requestMap = new java.util.HashMap<>();
        // key = repairmanissuenumber, value = [date, repairman, idreq]
        try (BufferedReader br = new BufferedReader(new FileReader(problemsfile))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 8) continue;
                if (!c[2].trim().equalsIgnoreCase(customer)) continue;
                String key = c[0].trim(); // repairmanissuenumber
                if (!requestMap.containsKey(key))
                    requestMap.put(key, new String[]{c[5].trim(), c[7].trim(), c[6].trim()});
            }
        } catch (Exception e) { e.printStackTrace(); }

        // ── 3. Read reciept.csv to check paid status ──
        // cols: id(0), samecustomerid(1), customer(2), DATE(3),
        //       payment(4), totalcost(5), reciept(6)
        // key = samecustomerid, value = [totalcost, paymentmethod]
        java.util.Map<String, String[]> paidMap = new java.util.HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src\\reciept.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                String[] c = line.split(",", 7);
                if (c.length < 6) continue;
                if (!c[2].trim().equalsIgnoreCase(customer)) continue;
                paidMap.put(c[1].trim(), new String[]{c[5].trim(), c[4].trim()});
            }
        } catch (Exception ignored) {}

        // ── 4. Build one row per car slot ──
        // repairmanissuenumber 1,2,3 maps to customerid 1,2,3
        for (java.util.Map.Entry<String, String[]> entry : requestMap.entrySet()) {
            String rissue    = entry.getKey();              // "1","2","3"
            String[] reqInfo = entry.getValue();            // [date, repairman, idreq]
            String date      = reqInfo[0];
            String mechanic  = reqInfo[1];

            // car slot = same number as repairmanissuenumber
            String[] carInfo = carMap.getOrDefault(rissue, new String[]{"N/A", "N/A"});
            String plateNum  = carInfo[0];
            String model     = carInfo[1];

            // Check if paid
            boolean paid = paidMap.containsKey(rissue);
            String totalSpent   = paid ? "₱" + paidMap.get(rissue)[0] : "₱0";
            String payMethod    = paid ? paidMap.get(rissue)[1]        : "N/A";
            String paidStatus   = paid ? "PAID"                        : "NOT PAID";

            rows.add(new Object[]{date, plateNum, mechanic, model,
                                  totalSpent, payMethod, paidStatus});
        }

        // ── 5. Set table model ──
        Object[][] data = rows.toArray(new Object[0][]);
        historytable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });

        // Color PAID green, NOT PAID red
        historytable.setDefaultRenderer(Object.class,
            new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public java.awt.Component getTableCellRendererComponent(
                        javax.swing.JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int col) {
                    super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);
                    String paidVal = String.valueOf(
                        table.getValueAt(row, 6)); // col 6 = PAID/NOT PAID
                    if (!isSelected) {
                        setBackground(paidVal.equals("PAID")
                            ? new java.awt.Color(198, 239, 206)
                            : new java.awt.Color(255, 199, 206));
                    }
                    return this;
                }
            });

        // ── 6. Auto-save to history.csv ──
        saveHistoryToCSV(rows, columns);
    }

private void saveHistoryToCSV(java.util.List<Object[]> rows, String[] columns) {
    java.io.File file = new java.io.File("src\\history.csv");
    try (java.io.PrintWriter pw = new java.io.PrintWriter(
            new java.io.FileWriter(file, false))) { // overwrite each time

        pw.println(String.join(",", columns));
        for (Object[] row : rows) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(row[i] != null ? row[i].toString() : "");
            }
            pw.println(sb.toString());
        }
    } catch (Exception e) { e.printStackTrace(); }
}
    
    
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void SENDBUTTONMESSAGEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SENDBUTTONMESSAGEActionPerformed
          String message = text2message.getText().trim();
            if (message.isEmpty()) return;

            // Use active chat mechanic if selected, otherwise fall back to assignedMechanic
            String targetMechanic = (activeChatUser != null)
                ? displayNames.getOrDefault(activeChatUser, activeChatUser)
                : assignedMechanic;

            if (targetMechanic.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No mechanic assigned yet.");
                return;
            }

            java.io.File file = new java.io.File(MESSAGES_CSV);
            boolean isNew = !file.exists() || file.length() == 0;

            try (java.io.PrintWriter pw = new java.io.PrintWriter(
                    new java.io.FileWriter(file, true))) {
                if (isNew) pw.println("customer,mechanic,sender,message");
                // customer, mechanic, sender (= customer here), message
                pw.printf("%s,%s,%s,%s%n", username, targetMechanic, username, message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            text2message.setText("");
            refreshMessages(); // show immediately
 
    }//GEN-LAST:event_SENDBUTTONMESSAGEActionPerformed

    private void text2messageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text2messageActionPerformed

    }//GEN-LAST:event_text2messageActionPerformed

    private void text2messageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_text2messageMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_text2messageMouseClicked
   public void saveAllToCSV(int repairmanidissue) {
    java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("MMM/dd/yyyy");
    String dateStr = (datee.getDate() != null)
        ? fmt.format(datee.getDate())
        : fmt.format(new java.util.Date()); 
 
    // ── Lookup car model ──────────────────────────────────────────
    String carName = "N/A";
    try (java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.FileReader(carrepairr))) {
        String line; boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) { first = false; continue; }
            String[] c = line.split(",", -1);
            if (c.length < 4) continue;
            if (c[0].trim().equals(String.valueOf(repairmanidissue)) &&
                c[1].trim().equalsIgnoreCase(username)) {
                carName = c[3].trim();
                break;
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
 
    System.out.println("Car for slot " + repairmanidissue + ": " + carName);
 
    // ── Generate idreq ────────────────────────────────────────────
    int idreq = 1;
    java.io.File f = new java.io.File(problemsfile);
    if (f.exists()) {
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(f))) {
            String line, last = null;
            while ((line = br.readLine()) != null)
                if (!line.trim().isEmpty()) last = line;
            if (last != null && !last.startsWith("repairman")) {
                try { idreq = Integer.parseInt(last.split(",")[1].trim()) + 1; }
                catch (Exception ignored) {}
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
 
    System.out.println("Generated idreq: " + idreq);
 
    // ── AUTO-ASSIGN MECHANIC ──────────────────────────────────────
    String mechanic = autoAssignMechanic();
 
        if (mechanic.equals("PENDING")) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "All mechanics are currently busy at your chosen time slot.\n"
              + "Your request is in the queue — a mechanic will be\n"
              + "assigned as soon as one becomes available.");
            mechanic = "";
        } else if (!mechanic.isEmpty()) {
            assignedMechanic = mechanic;
            jLabel5.setText(assignedMechanic);
        }
        System.out.println("Mechanic to assign: " + mechanic);
 
        // ── Save each problem row via problembuttons ──────────────────
        for (int i = 0; i < selectedProblems.size(); i++) {
            problembuttons.savetocsvcustomer(
                repairmanidissue,
                i + 1,
                username,
                carName,
                selectedProblems.get(i),
                dateStr,
                idreq
            );
        }

        // ── Auto-assign mechanic and save to pictures.csv ──
    if (mechanic.isEmpty() && datee.getDate() != null && !selectedTimeSlot.isEmpty()) {
        dateStr = new java.text.SimpleDateFormat("MMM/dd/yyyy").format(datee.getDate());

        java.util.List<String> booked = getBookedMechanics(dateStr, selectedTimeSlot);
        java.util.List<String> free   = new java.util.ArrayList<>();
        for (String m : ALL_MECHANICS) {
            if (!booked.contains(m)) free.add(m);
        }

        if (!free.isEmpty()) {
            autoAssignedMechanic = free.get(new java.util.Random().nextInt(free.size()));
            System.out.println("Auto-assigned: " + autoAssignedMechanic);

            saveToPicturesCSV(repairmanidissue, dateStr, selectedTimeSlot, autoAssignedMechanic);
            updateMechanicInProblemsCSV(repairmanidissue, autoAssignedMechanic);

            assignedMechanic = autoAssignedMechanic;
            jLabel5.setText(assignedMechanic);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No mechanics available for that slot. Please go back and pick another.");
        }
    }
 
    // ── Patch repairman column in problems.csv ────────────────────
    // problembuttons.savetocsvcustomer writes col[7] as empty or "N/A".
    // We rewrite the CSV and fill in col[7] for rows that match this
    // repairmanidissue + username + idreq.
    if (!mechanic.isEmpty()) {
        patchRepairmanInCSV(repairmanidissue, idreq, mechanic);
        savePictureToCSV(repairmanidissue, idreq, mechanic);
    }
}
    private void saveToPicturesCSV(int repairmanIssueNum, String date, String timeslot, String mechanic) {
    java.io.File file = new java.io.File("src\\pictures.csv");
    boolean isNew = !file.exists() || file.length() == 0;

    int nextId = 1;
    if (!isNew) {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line, last = null;
            while ((line = br.readLine()) != null)
                if (!line.trim().isEmpty()) last = line;
            if (last != null && !last.startsWith("id")) {
                try { nextId = Integer.parseInt(last.split(",")[0].trim()) + 1; }
                catch (Exception ignored) {}
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(file, true))) {
        if (isNew) pw.println("id,repairmanissuenumber,customer,mechanic,pathfile,date,time");
        pw.printf("%d,%d,%s,%s,%s,%s,%s%n",
            nextId, repairmanIssueNum, username, mechanic, "", date, timeslot);
        System.out.println("Saved to pictures.csv → " + mechanic + " | " + date + " | " + timeslot);
    } catch (Exception e) { e.printStackTrace(); }
}

    private void updateMechanicInProblemsCSV(int repairmanIssueNum, String mechanic) {
        java.io.File file = new java.io.File(problemsfile);
        if (!file.exists()) return;

        java.util.List<String> lines = new java.util.ArrayList<>();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (Exception e) { e.printStackTrace(); return; }

        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(file, false))) {
            for (String line : lines) {
                String[] c = line.split(",", -1);

                // Skip header
                if (c[0].equalsIgnoreCase("repairmanissuenumber")) {
                    pw.println(line);
                    continue;
                }

                // ✅ If row is missing repairman column, pad it to 8
                if (c.length < 8) {
                    c = java.util.Arrays.copyOf(c, 8);
                    c[7] = ""; // blank by default
                }

                // ✅ Now fill in the mechanic for the matching row
                if (c[0].trim().equals(String.valueOf(repairmanIssueNum))
                    && c[2].trim().equalsIgnoreCase(username)) {
                    c[7] = mechanic;
                }

                pw.println(String.join(",", c));
            }
            System.out.println("Updated problems.csv → repairman set to " + mechanic);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void patchRepairmanInCSV(int repairmanidissue, int idreq, String mechanic) {
    java.io.File file = new java.io.File(problemsfile);
    if (!file.exists()) return;
 
    java.util.List<String> lines = new java.util.ArrayList<>();
    try (java.io.BufferedReader br = new java.io.BufferedReader(
            new java.io.FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null)
            lines.add(line);
    } catch (Exception e) { e.printStackTrace(); return; }
 
    try (java.io.PrintWriter pw = new java.io.PrintWriter(
            new java.io.FileWriter(file, false))) { // overwrite
        for (String line : lines) {
            String[] c = line.split(",", -1);
            // Check if this row matches our new submission
            if (c.length >= 8
                    && !c[0].trim().equalsIgnoreCase("repairmanissuenumber") // skip header
                    && c[0].trim().equals(String.valueOf(repairmanidissue))
                    && c[2].trim().equalsIgnoreCase(username)
                    && c[6].trim().equals(String.valueOf(idreq))
                    && c[7].trim().isEmpty()) {           // only blank ones
                c[7] = mechanic;
                pw.println(String.join(",", c));
            } else {
                pw.println(line);
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
 
    System.out.println("Patched repairman '" + mechanic
                     + "' into problems.csv for idreq=" + idreq);
}
    
   
    
    public int getNextIdReq() {
        int lastId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(problemsfile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");

                if (row.length < 7) continue;
                if (row[0].equalsIgnoreCase("repairmanissuenumber")) continue;

                int currentId = Integer.parseInt(row[6]);
                if (currentId > lastId) {
                    lastId = currentId;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastId + 1;
    }
    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        // 🔥 IF already at last panel → SUBMIT
        

    // ✅ SUBMIT — only runs when user clicks SUBMIT button
    if (numlayercreatereq == layercreatereq.length - 1) {

        // 1. Save to CSV
        saveAllToCSV(repairmanidissue);

        // 2. Show confirmation dialog AFTER saving
        JOptionPane.showMessageDialog(null, "YOU HAVE SUBMITTED YOUR REQUEST");

        // 3. Reset everything back to first panel
        numlayercreatereq = 0;
        next.setText("NEXT");
        jProgressBar1.setValue(0);

        createreqpanel.removeAll();
        createreqpanel.add(layercreatereq[0]);
        createreqpanel.repaint();
        createreqpanel.revalidate();
        visibles();

        return; // stop here, don't fall through
    }
    
    if (numlayercreatereq == 3) {
    if (datee.getDate() == null) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a date first.");
        return;
    }
            if (selectedTimeSlot.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please select a time slot.");
                return;
            }
        }

    // ── Progress bar update ──────────────────────────────────
    int current = jProgressBar1.getValue();
    switch (current) {
        case 0:  current = 25;  break;
        case 25: current = 50;  break;
        case 50: current = 75; break;
        case 75: current = 100; break;

        default: break;
    }
    jProgressBar1.setValue(current);

    // ── Go to next panel ─────────────────────────────────────
    numlayercreatereq++;

    if (numlayercreatereq >= layercreatereq.length) {
        numlayercreatereq = 0;
    }

    // ── If we just arrived at the LAST panel → show summary ──
    if (numlayercreatereq == layercreatereq.length - 1) {
        next.setText("SUBMIT");

        // Read car info
        String carName  = "N/A";
        String plateNum = "N/A";
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(carrepairr))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 4) continue;
                if (c[0].trim().equals(String.valueOf(repairmanidissue)) &&
                    c[1].trim().equalsIgnoreCase(username)) {
                    plateNum = c[2].trim();
                    carName  = c[3].trim();
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Read selected date
        String selectedDate = "No date selected";
        if (datee.getDate() != null) {
            selectedDate = new java.text.SimpleDateFormat("MMM dd, yyyy")
                               .format(datee.getDate());
        }

        // Build summary text
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════\n");
        sb.append("  CAR SLOT  : ").append(repairmanidissue).append("\n");
        sb.append("  MODEL     : ").append(carName).append("\n");
        sb.append("  PLATE #   : ").append(plateNum).append("\n");
        sb.append("  DATE      : ").append(selectedDate).append("\n");
        sb.append("  TIME SLOT : ").append(selectedTimeSlot).append("\n"); // ADD THIS
        sb.append("══════════════════════════════\n");
        sb.append("  ISSUES REPORTED:\n");
        sb.append("──────────────────────────────\n");

        if (selectedProblems.isEmpty()) {
            sb.append("  (no issues selected)\n");
        } else {
            int i = 1;
            for (String problem : selectedProblems) {
                sb.append("  ").append(i).append(". ").append(problem).append("\n");
                i++;
            }
        }
        sb.append("══════════════════════════════\n");

        summarypanel.setText(sb.toString());
        summarypanel.setCaretPosition(0);

    } else {
        next.setText("NEXT");
    }

    // ── Swap the visible panel ────────────────────────────────
    createreqpanel.removeAll();
    createreqpanel.add(layercreatereq[numlayercreatereq]);
    createreqpanel.repaint();
    createreqpanel.revalidate();
    visibles();


    }//GEN-LAST:event_nextActionPerformed

    
    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
                

        int current = jProgressBar1.getValue();

        switch (current) {
            case 25:
                current = 0;
                break;
            case 50:
                current = 25;
                break;
            case 75:
                current = 50;
                break;
                case 100:
                current = 75;
                break;
            default:
                break;
        }

        jProgressBar1.setValue(current);
        
        numlayercreatereq--; // go back

        if (numlayercreatereq < 0) {
            numlayercreatereq = 0; // stop at first panel
        }
        
        if (numlayercreatereq == layercreatereq.length - 1) {
            next.setText("SUBMIT");
        } else {
            next.setText("NEXT");
        }
        
        createreqpanel.removeAll();
        createreqpanel.add(layercreatereq[numlayercreatereq]);
        createreqpanel.repaint();
        createreqpanel.revalidate();
        visibles();
    }//GEN-LAST:event_backActionPerformed

    private void ADDCARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ADDCARActionPerformed
        addcar ac = new addcar(username, "save", 1);
        ac.setVisible(true);
        ac.pack();
        ac.setLocationRelativeTo(null);
    }//GEN-LAST:event_ADDCARActionPerformed
     
    private void startRefreshingbutton() {
        refreshTimer = new javax.swing.Timer(1000, e -> {
        try (BufferedReader reader = new BufferedReader(new FileReader(carrepairr))) {
                String line;

                // Reset all to "+"
                addcar1.setText("+"); addcar1.setIcon(null);
                addcar2.setText("+"); addcar2.setIcon(null);
                addcar3.setText("+"); addcar3.setIcon(null);
                servicecar1.setIcon(null);
                servicecar2.setIcon(null);
                servicecar3.setIcon(null);

                while ((line = reader.readLine()) != null) {
                    String[] row = line.split(",", -1);
                    if (row.length < 9) continue;
                    if (row[0].equalsIgnoreCase("customerid")) continue; // skip header

                    int cid = Integer.parseInt(row[0].trim());   // col[0] = customerid
                    String owner = row[1].trim();                // col[1] = ownername

                    if (!owner.equalsIgnoreCase(username)) continue;

                    String plateNum  = row[2].trim();
                    String model     = row[3].trim();
                    String imagePath = row[8].trim();

                    String label = "MODEL: " + model + ", PLATE: " + plateNum;

                    // Pick which button slot based on customerid
                    javax.swing.JButton carBtn  = null;
                    javax.swing.JButton servBtn = null;

                    if (cid == 1) { carBtn = addcar1; servBtn = servicecar1; }
                    else if (cid == 2) { carBtn = addcar2; servBtn = servicecar2; }
                    else if (cid == 3) { carBtn = addcar3; servBtn = servicecar3; }

                    if (carBtn == null) continue;

                    carBtn.setText(label);
                    carBtn.setHorizontalAlignment(SwingConstants.LEFT);
                    carBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
                    carBtn.setIconTextGap(80);

                    if (!imagePath.isEmpty()) {
                            java.io.File imgFile = new java.io.File(imagePath);
                            if (imgFile.exists()) {
                                try {
                                    // Read into BufferedImage first — avoids ClassCastException
                                    java.awt.image.BufferedImage bimg = javax.imageio.ImageIO.read(imgFile);
                                    if (bimg != null) {
                                        // Thumbnail for car button
                                        Image thumb = bimg.getScaledInstance(150, 110, Image.SCALE_SMOOTH);
                                        carBtn.setIcon(new ImageIcon(thumb));
                                        carBtn.setHorizontalAlignment(SwingConstants.LEFT);
                                        carBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
                                        carBtn.setIconTextGap(80);

                                        // Thumbnail for service button
                                        servBtn.setIcon(new ImageIcon(thumb));
                                        servBtn.setHorizontalAlignment(SwingConstants.LEFT);
                                        servBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
                                        servBtn.setIconTextGap(80);
                                        servBtn.setText(label);

                                        // Big pic for sumpic (car 1 only)
                                        if (cid == 1) {
                                            Image big = bimg.getScaledInstance(290, 326, Image.SCALE_SMOOTH);
                                            sumpic.setIcon(new ImageIcon(big));
                                        }
                                    }
                                } catch (Exception imgEx) {
                                    imgEx.printStackTrace();
                                }
                            }
                        
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        refreshTimer.start();
    }   
    public void loadRecieptOnOpen() {
        String customer = customerframe.username;
        java.io.File file = new java.io.File("src\\reciept.csv");
        if (!file.exists()) { recieptarea.setText("No receipts found."); return; }

        StringBuilder display = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new java.io.InputStreamReader(
                    new java.io.FileInputStream(file), "UTF-8"))) {

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 7);
                if (parts.length < 7) continue;

                String rowCustomer = parts[2].trim();
                String rowReceipt  = parts[6].trim()
                                        .replaceAll("^\"|\"$", "")
                                        .replace("\"\"", "\"");

                if (!rowCustomer.equalsIgnoreCase(customer)) continue;

                display.append(rowReceipt).append("\n");
            }

        } catch (Exception e) { e.printStackTrace(); }

        if (display.length() == 0) {
            recieptarea.setText("No receipts found for your account.");
        } else {
            recieptarea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
            recieptarea.setText(display.toString().trim());
            recieptarea.setCaretPosition(0);
        }
    }

    public void loadStatusFromCSV(int carButtonSlot) {
    String customer = customerframe.username;
    File file = new File(updatestatusCSV);
    if (!file.exists()) {
        updatestatus.setText("No status updates found for this car.");
        return;
    }

    // ── STEP 1: Resolve the actual repairmanissuenumber from problems.csv ──
    // carButtonSlot (1,2,3) is the customer's car button — but idcustomer in
    // updatestatus.csv stores repairmanissuenumber, which may differ.
    // Match: customer + carButtonSlot (col 0 = repairmanissuenumber... wait)
    // Actually problems.csv col[0]=repairmanissuenumber, col[2]=username, col[6]=idreq
    // The mechanic writes targetCarSlot = repairmanissuenumber into idcustomer.
    // We need to find which repairmanissuenumber belongs to this customer's carButtonSlot.
    // Since addcar slot (customerid in CAR REPAIRS) maps 1:1 to repairmanissuenumber,
    // we look up problems.csv for this customer and find rows where col[0]=carButtonSlot.
    // But from the CSV: idcustomer=2 for fhel's slot 1 button — meaning the mechanic
    // assigned repairmanissuenumber=2. So we must read it from problems.csv.

    int repairmanIssueNum = carButtonSlot; // fallback default

    try (BufferedReader br = new BufferedReader(new FileReader("src\\problems.csv"))) {
        String line; boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) { first = false; continue; }
            String[] c = line.split(",", -1);
            if (c.length < 8) continue;
            // cols: repairmanissuenumber(0), issuenumber(1), username(2), car(3),
            //       problems(4), date(5), idreq(6), repairman(7)
            if (c[2].trim().equalsIgnoreCase(customer) &&
                c[0].trim().equals(String.valueOf(carButtonSlot))) {
                repairmanIssueNum = Integer.parseInt(c[0].trim());
                break; // take the first matching row
            }
        }
    } catch (Exception e) { e.printStackTrace(); }

    // ── STEP 2: Read updatestatus.csv filtering by customer + repairmanIssueNum ──
    StringBuilder sb = new StringBuilder();
    boolean hasReadyForPickup = false;
    String readyTimestamp = "";

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line; boolean firstRow = true;
        while ((line = br.readLine()) != null) {
            if (firstRow) { firstRow = false; continue; }
            if (line.trim().isEmpty()) continue;

            String[] cols = line.split(",", -1);
            // cols: id(0), customer(1), repairman(2), diagnoses(3), customersame(4), idcustomer(5)
            if (cols.length < 6) continue;
            if (!cols[1].trim().equalsIgnoreCase(customer)) continue;
            if (!cols[5].trim().equals(String.valueOf(repairmanIssueNum))) continue;

            String status    = cols[3].trim();
            String timestamp = cols[4].trim();

            sb.append("[").append(status).append("]\n")
              .append(timestamp).append("\n\n");

            if (status.equalsIgnoreCase("READY FOR PICKUP")) {
                hasReadyForPickup = true;
                readyTimestamp = timestamp; // keep updating — last one wins
            }
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }

    // ── STEP 3: Set textarea ──
    if (sb.length() > 0) {
        updatestatus.setText(sb.toString().trim());
        updatestatus.setCaretPosition(updatestatus.getDocument().getLength());
    } else {
        updatestatus.setText("No status updates found for this car.");
    }

    // ── STEP 4: Show popup ONCE if READY FOR PICKUP is present ──
    if (hasReadyForPickup) {
        JOptionPane.showMessageDialog(null,
            "Your car is READY FOR PICKUP!\nPlease proceed to payment before vehicle release.\nTime: " + readyTimestamp);
    }
}
    private String autoAssignMechanic() {
 
        // ── Step A: Get all mechanics ──
        java.util.List<String> mechanicList = loadAllMechanics();

        // Fallback: collect from problems.csv if users.csv gave nothing
        if (mechanicList.isEmpty()) {
            java.util.Set<String> seen = new java.util.LinkedHashSet<>();
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader(problemsfile))) {
                String line; boolean first = true;
                while ((line = br.readLine()) != null) {
                    if (first) { first = false; continue; }
                    String[] c = line.split(",", -1);
                    if (c.length >= 8 && !c[7].trim().isEmpty())
                        seen.add(c[7].trim());
                }
            } catch (Exception ignored) {}
            mechanicList.addAll(seen);
        }

        if (mechanicList.isEmpty()) return "";

        // ── Step B: Booking date + time slot chosen by customer ──
        String bookingDate = "";
        if (datee.getDate() != null) {
            bookingDate = new java.text.SimpleDateFormat("MMM/dd/yyyy")
                              .format(datee.getDate());
        }
        String bookingSlot = selectedTimeSlot; // e.g. "7 AM - 9 AM"

        // ── Step C: Find mechanics busy at that date+slot from pictures.csv ──
        java.util.Set<String> busyAtSlot = new java.util.HashSet<>();
        if (!bookingDate.isEmpty() && !bookingSlot.isEmpty()) {
            java.io.File file = new java.io.File("src\\pictures.csv");
            if (file.exists()) {
                try (java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.FileReader(file))) {
                    String line; boolean first = true;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty()) continue;
                        if (first) { first = false; continue; }
                        String[] c = line.split(",", -1);
                        if (c.length < 7) continue;
                        String rowMechanic = c[3].trim();
                        String rowDate     = c[5].trim();
                        String rowSlot     = c[6].trim();
                        if (rowDate.equalsIgnoreCase(bookingDate) &&
                            rowSlot.equalsIgnoreCase(bookingSlot) &&
                            !rowMechanic.isEmpty() && !rowMechanic.equalsIgnoreCase("N/A")) {
                            busyAtSlot.add(rowMechanic.toLowerCase());
                        }
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }

        // ── Step D: Count active jobs per mechanic from problems.csv ──
        java.util.Map<String, Integer> jobCount = new java.util.LinkedHashMap<>();
        for (String m : mechanicList) jobCount.put(m, 0);
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(problemsfile))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 8) continue;
                String mech = c[7].trim();
                if (!mech.isEmpty() && jobCount.containsKey(mech))
                    jobCount.put(mech, jobCount.get(mech) + 1);
            }
        } catch (Exception e) { e.printStackTrace(); }

        // ── Step E: Try SEV first ──
        final String DEFAULT_MECHANIC = "SEV"; // ← change to your default mechanic's username
        String chosen = "";

        if (mechanicList.contains(DEFAULT_MECHANIC) &&
            !busyAtSlot.contains(DEFAULT_MECHANIC.toLowerCase())) {
            chosen = DEFAULT_MECHANIC;
            System.out.println("-> SEV is free — assigned to SEV");
        } else {
            System.out.println("  SEV is busy at " + bookingDate + " " + bookingSlot
                             + " — finding another mechanic...");
            // Pick the free mechanic with the fewest jobs
            int lowestCount = Integer.MAX_VALUE;
            for (String m : mechanicList) {
                if (busyAtSlot.contains(m.toLowerCase())) {
                    System.out.println("  " + m + " BUSY — skipped");
                    continue;
                }
                int cnt = jobCount.getOrDefault(m, 0);
                System.out.println("  " + m + " free, active jobs: " + cnt);
                if (cnt < lowestCount) {
                    lowestCount = cnt;
                    chosen = m;
                }
            }
        }

        // ── Step F: All busy → Pending ──
        if (chosen.isEmpty()) {
            System.out.println("-> ALL MECHANICS BUSY — Pending");
            return "PENDING";
        }

        System.out.println("-> Auto-assigned: " + chosen
                         + " (" + bookingDate + " | " + bookingSlot + ")");
        return chosen;
    }
    
    
    private void handleAddCar(javax.swing.JButton btn, int customerid) {
    // repairmanissuenumber = customerid directly (addcar1→1, addcar2→2, addcar3→3)
        int repairmanIssueNum = customerid;

        boolean carExists = false;
        try (BufferedReader br = new BufferedReader(new FileReader(carrepairr))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",", -1);
                if (row.length < 2) continue;
                if (row[0].equalsIgnoreCase("customerid")) continue;
                if (row[0].trim().equals(String.valueOf(customerid)) &&
                    row[1].trim().equalsIgnoreCase(username)) {
                    carExists = true;
                    break;
                }
            }
        } catch (Exception e) {}

        if (!carExists) {
            addcar ac = new addcar(username, "save", customerid);
            ac.setVisible(true);
            ac.pack();
            ac.setLocationRelativeTo(null);
        } else {
            repairmanidissue = repairmanIssueNum; // addcar1→1, addcar2→2, addcar3→3

            int current = jProgressBar1.getValue();
            jProgressBar1.setValue(current + 25);

            numlayercreatereq++;
            if (numlayercreatereq >= layercreatereq.length) numlayercreatereq = 0;

            next.setText(numlayercreatereq == layercreatereq.length - 1 ? "SUBMIT" : "NEXT");

            createreqpanel.removeAll();
            createreqpanel.add(layercreatereq[numlayercreatereq]);
            createreqpanel.repaint();
            createreqpanel.revalidate();

            visibles();
        }
}
    private void servicecar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servicecar1ActionPerformed
    handleServiceCar(1);

         currentServiceSlot = 1;

    back2.setVisible(true);

    toppanel.removeAll();
    toppanel.add(SERVPANEL2);
    toppanel.repaint();
    toppanel.revalidate();

    servicestatpanel.removeAll();
    servicestatpanel.add(layerserv[1]);
    servicestatpanel.repaint();
    servicestatpanel.revalidate();

    }//GEN-LAST:event_servicecar1ActionPerformed

    private void edit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edit3ActionPerformed

    private void back2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_back2ActionPerformed
            toppanel.removeAll();
            toppanel.add(SERVPANEL1);
            toppanel.repaint();
            toppanel.revalidate();

            servicestatpanel.removeAll();
            servicestatpanel.add(layerserv[0]);
            servicestatpanel.repaint();
            servicestatpanel.revalidate();
            
            back2.setVisible(false);
    }//GEN-LAST:event_back2ActionPerformed

    private void servicecar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servicecar2ActionPerformed
    handleServiceCar(2);

        currentServiceSlot = 2;
    loadStatusFromCSV(2);

    back2.setVisible(true);

    toppanel.removeAll();
    toppanel.add(SERVPANEL2);
    toppanel.repaint();
    toppanel.revalidate();

    servicestatpanel.removeAll();
    servicestatpanel.add(layerserv[1]);
    servicestatpanel.repaint();
    servicestatpanel.revalidate();
    }//GEN-LAST:event_servicecar2ActionPerformed

    private void edit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edit4ActionPerformed

    private void servicecar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servicecar3ActionPerformed
handleServiceCar(3);

        currentServiceSlot = 3;
    loadStatusFromCSV(3);

    back2.setVisible(true);

    toppanel.removeAll();
    toppanel.add(SERVPANEL2);
    toppanel.repaint();
    toppanel.revalidate();

    servicestatpanel.removeAll();
    servicestatpanel.add(layerserv[1]);
    servicestatpanel.repaint();
    servicestatpanel.revalidate();
    }//GEN-LAST:event_servicecar3ActionPerformed
    
    private void handleServiceCar(int customersame) {
    currentServiceSlot = customersame;

    // ── Get car info for this slot ──
    String carName  = "N/A";
    String plateNum = "N/A";
    try (BufferedReader br = new BufferedReader(new FileReader(carrepairr))) {
        String line; boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) { first = false; continue; }
            String[] c = line.split(",", -1);
            if (c.length < 4) continue;
            if (c[0].trim().equals(String.valueOf(customersame)) &&
                c[1].trim().equalsIgnoreCase(username)) {
                plateNum = c[2].trim(); // col 2 = plate
                carName  = c[3].trim(); // col 3 = model
                break;
            }
        }
    } catch (Exception e) { e.printStackTrace(); }

        // ── Look up the mechanic assigned to this specific slot ──
            String slotMechanic = "Not assigned";
            try (BufferedReader br2 = new BufferedReader(new FileReader("src\\problems.csv"))) {
                String line2; boolean first2 = true;
                while ((line2 = br2.readLine()) != null) {
                    if (first2) { first2 = false; continue; }
                    String[] c2 = line2.split(",", -1);
                    if (c2.length >= 8 &&
                        Integer.parseInt(c2[6].trim()) == customersame &&
                        c2[2].trim().equalsIgnoreCase(username) &&
                        !c2[7].trim().isEmpty()) {
                        slotMechanic = c2[7].trim();
                        break;
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }

            // ── Update SERVPANEL2 label with car info + mechanic ──
            servpanel.setText("  Car " + customersame + "  |  " + carName
                + "  |  " + plateNum
                + "  |  Mechanic: " + slotMechanic);

    // ── Load status and materials ──
    loadStatusFromCSV(customersame);
    loadmaterialsrequestparts();

    // ── Switch panels ──
    back2.setVisible(true);

    toppanel.removeAll();
    toppanel.add(SERVPANEL2);
    toppanel.repaint();
    toppanel.revalidate();

    servicestatpanel.removeAll();
    servicestatpanel.add(layerserv[1]);
    servicestatpanel.repaint();
    servicestatpanel.revalidate();
}
    private void edit5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edit5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        USER.setSelectedIndex(3);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void paymentcar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentcar1ActionPerformed
     if (!isReadyForPickup(1)) {
        JOptionPane.showMessageDialog(null, "Car 1 is not ready for pickup yet.\nPlease wait for the mechanic to finish.");
        return;
    }
    currentSameCustomerId = 1;
    loadPaymentForRequest(1);
    paymentlayerpanel.removeAll();
    paymentlayerpanel.add(paymentlayer[1]);
    paymentlayerpanel.repaint();
    paymentlayerpanel.revalidate();
    loadRecieptOnOpen();  // ← MOVE TO AFTER panel switch


    }//GEN-LAST:event_paymentcar1ActionPerformed

    private void paybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paybuttonActionPerformed
        if (currentPaymentMethod.equals("GCASH")) {
        bill(0, currentGrandTotal); // cashAmount ignored for GCASH
    } else {
        try {
            int cash1 = Integer.parseInt(cash.getText().trim().replace(",", ""));
            bill(cash1, currentGrandTotal);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid cash amount.");
        }
    }
    }//GEN-LAST:event_paybuttonActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        paymentlayerpanel.removeAll();
        paymentlayerpanel.add(paymentlayer[0]);
        paymentlayerpanel.repaint();
        paymentlayerpanel.revalidate();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void paymentcar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentcar2ActionPerformed
     if (!isReadyForPickup(2)) {
        JOptionPane.showMessageDialog(null, "Car 2 is not ready for pickup yet.\nPlease wait for the mechanic to finish.");
        return;
    }
    currentSameCustomerId = 2;
        loadRecieptOnOpen();  // ← ADD THIS

    loadPaymentForRequest(2);
    paymentlayerpanel.removeAll();
    paymentlayerpanel.add(paymentlayer[1]);
    paymentlayerpanel.repaint();
    paymentlayerpanel.revalidate();
    }//GEN-LAST:event_paymentcar2ActionPerformed

    private void paymentcar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentcar3ActionPerformed
        if (!isReadyForPickup(3)) {
        JOptionPane.showMessageDialog(null, "Car 3 is not ready for pickup yet.\nPlease wait for the mechanic to finish.");
        return;
    }
    currentSameCustomerId = 3;
    loadPaymentForRequest(3);
        loadRecieptOnOpen();  // ← ADD THIS

    paymentlayerpanel.removeAll();
    paymentlayerpanel.add(paymentlayer[1]);
    paymentlayerpanel.repaint();
    paymentlayerpanel.revalidate();
    }//GEN-LAST:event_paymentcar3ActionPerformed

    private void serviceupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceupdateActionPerformed
                USER.setSelectedIndex(2);

    }//GEN-LAST:event_serviceupdateActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
                USER.setSelectedIndex(2);

    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        USER.setSelectedIndex(2);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void edit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit2ActionPerformed
        addcar ac = new addcar(username, "edit", 2);
        ac.setVisible(true);
        ac.pack();
        ac.setLocationRelativeTo(null);
    }//GEN-LAST:event_edit2ActionPerformed

    private void edits3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edits3ActionPerformed
        addcar ac = new addcar(username, "edit", 3);
        ac.setVisible(true);
        ac.pack();
        ac.setLocationRelativeTo(null);
    }//GEN-LAST:event_edits3ActionPerformed

    private void edit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit1ActionPerformed
        addcar ac = new addcar(username, "edit", 1);
        ac.setVisible(true); ac.pack(); ac.setLocationRelativeTo(null);

    }//GEN-LAST:event_edit1ActionPerformed

    private void addcar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addcar3ActionPerformed
        handleAddCar(addcar3, 3);

    }//GEN-LAST:event_addcar3ActionPerformed

    private void addcar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addcar2ActionPerformed
        handleAddCar(addcar2, 2);

    }//GEN-LAST:event_addcar2ActionPerformed

    private void addcar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addcar1ActionPerformed
        handleAddCar(addcar1, 1);

    }//GEN-LAST:event_addcar1ActionPerformed

    private void CASHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CASHActionPerformed
        currentPaymentMethod = "CASH";
    cash.setEnabled(true);
    cash.setText("");
    balance.setText("₱0");
    }//GEN-LAST:event_CASHActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        if (refreshTimer != null && refreshTimer.isRunning()) {
        refreshTimer.stop();
        }

        // Open the Login window
        LOGIN loginWindow = new LOGIN();
        loginWindow.setVisible(true);

        // Close this frame
        this.dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void imageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageActionPerformed
            if (evt.getSource() == image) {

            JFileChooser fileChooser = new JFileChooser();
            

                    fileChooser.setCurrentDirectory(
                    new File("C:/Users/Adrian/Videos/pictures or videos car")
                );
                fileChooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                    "Images & Videos",
                    "jpg", "jpeg", "png", "mp4"
                )
            );

            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {

                File selectedFile = fileChooser.getSelectedFile();

                String path = selectedFile.getAbsolutePath().toLowerCase();

                 selectedFilePath = selectedFile.getAbsolutePath(); 
                // IMAGE
                if (path.endsWith(".jpg") ||
                    path.endsWith(".jpeg") ||
                    path.endsWith(".png")) {

                    ImageIcon icon = new ImageIcon(path);

                    Image img = icon.getImage().getScaledInstance(
                        452,
                        194,
                        Image.SCALE_SMOOTH
                    );

                    image.setText("");
                    image.setIcon(new ImageIcon(img));
                }

                // VIDEO
                else if (path.endsWith(".mp4")) {

                    image.setIcon(null);

                    Platform.runLater(() -> {

                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                        }

                        Media media = new Media(
                            selectedFile.toURI().toString()
                        );

                        mediaPlayer = new MediaPlayer(media);

                        MediaView mediaView = new MediaView(mediaPlayer);

                        mediaView.setFitWidth(445);
                        mediaView.setFitHeight(298);

                        StackPane root = new StackPane(mediaView);

                        Scene scene = new Scene(root, 445, 298);

                        fxPanel.setScene(scene);

                        mediaPlayer.play();
                    });
                }
            }
        }
    }//GEN-LAST:event_imageActionPerformed

    private void savePictureToCSV(int repairmanidissue, int idreq, String mechanic) {
        if (selectedFilePath.isEmpty()) return; // nothing was uploaded — skip
 
        String picturesPath = "src\\pictures.csv";
        java.io.File file   = new java.io.File(picturesPath);
        boolean isNew       = !file.exists() || file.length() == 0;
 
        // Get next id
        int nextId = 1;
        if (!isNew) {
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader(file))) {
                String line, last = null;
                while ((line = br.readLine()) != null)
                    if (!line.trim().isEmpty()) last = line;
                if (last != null && !last.toLowerCase().startsWith("id")) {
                    try { nextId = Integer.parseInt(last.split(",")[0].trim()) + 1; }
                    catch (Exception ignored) {}
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
 
        // Date and time
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String dateStr = now.format(java.time.format.DateTimeFormatter.ofPattern("MMM/dd/yyyy"));
        String timeStr = selectedTimeSlot.isEmpty()
                ? now.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"))
                : selectedTimeSlot; 
        
        try (java.io.PrintWriter pw = new java.io.PrintWriter(
                new java.io.FileWriter(file, true))) { // append
 
            if (isNew) pw.println("id,repairmanissuenumber,customer,mechanic,pathfile,date,time");
 
            // id, repairmanissuenumber, customer, mechanic, pathfile, date, time
            pw.printf("%d,%d,%s,%s,%s,%s,%s%n",
                nextId,
                repairmanidissue,
                username,
                mechanic.isEmpty() ? "N/A" : mechanic,
                selectedFilePath,
                dateStr,
                timeStr
            );
 
            System.out.println("Saved picture path to pictures.csv: " + selectedFilePath);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // Reset after saving so it doesn't carry over to the next request
        selectedFilePath = "";
    }
    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button2ActionPerformed

    private void idontknowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idontknowActionPerformed
            String problem = "I DONT KNOW";
        if (selectedProblems.contains(problem)) {
            selectedProblems.remove(problem);
            idontknow.setBackground(new Color(200, 200, 200));
            idontknow.setForeground(java.awt.Color.DARK_GRAY);
        } else {
            selectedProblems.add(problem);
            idontknow.setBackground(new Color(0, 153, 153));
            idontknow.setForeground(java.awt.Color.WHITE);
        }
        refreshIssueTextArea();
    }//GEN-LAST:event_idontknowActionPerformed

    private void othersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_othersActionPerformed
        String problem = "OTHERS";
    if (selectedProblems.contains(problem)) {
        selectedProblems.remove(problem);
        others.setBackground(new Color(200, 200, 200));
        others.setForeground(java.awt.Color.DARK_GRAY);
    } else {
        selectedProblems.add(problem);
        others.setBackground(new Color(0, 153, 153));
        others.setForeground(java.awt.Color.WHITE);
    }
    refreshIssueTextArea();
    }//GEN-LAST:event_othersActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button1ActionPerformed
    public boolean isReadyForPickup(int carSlot) {
    String customer = customerframe.username;

    // Step 1: Resolve repairmanIssueNum from problems.csv
        int repairmanIssueNum = carSlot; // fallback
        try (BufferedReader br = new BufferedReader(new FileReader("src\\problems.csv"))) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] c = line.split(",", -1);
                if (c.length < 8) continue;
                if (c[2].trim().equalsIgnoreCase(customer) &&
                    c[0].trim().equals(String.valueOf(carSlot))) {   // ← add this check
                    repairmanIssueNum = Integer.parseInt(c[0].trim());
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

    // Step 2: Check updatestatus.csv using the resolved repairmanIssueNum
    java.io.File file = new java.io.File("src\\updatestatus.csv");
    if (!file.exists()) return false;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line; boolean first = true;
        while ((line = br.readLine()) != null) {
            if (first) { first = false; continue; }
            String[] cols = line.split(",", -1);
            if (cols.length < 6) continue;
            if (cols[1].trim().equalsIgnoreCase(customer) &&
                cols[5].trim().equals(String.valueOf(repairmanIssueNum)) &&
                cols[3].trim().equalsIgnoreCase("READY FOR PICKUP")) {
                return true;
            }
        }
    } catch (Exception e) { e.printStackTrace(); }

    return false;
}

       public void loadPaymentForRequest(int repairmanIssueNum) {
        String customer = customerframe.username;

        // ── 1. Load all problems for this customer + repairmanissuenumber ──
        String problemsPath = "src\\problems.csv";
        List<String> issues = new ArrayList<>();
        int idreqTarget = -1;
        String requestMechanic = "";


        try (BufferedReader br = new BufferedReader(new FileReader(problemsPath))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                String[] cols = line.split(",", -1);
                if (cols.length < 7) continue;

                int idreq = Integer.parseInt(cols[6].trim());
                String uname  = cols[2].trim();

                if (idreq == repairmanIssueNum && uname.equalsIgnoreCase(customer)) {
                    issues.add(cols[4].trim());
                    idreqTarget = Integer.parseInt(cols[6].trim());
                    currentCar = cols[3].trim();
                    if (requestMechanic.isEmpty() && cols.length >= 8) {
                        requestMechanic = cols[7].trim();
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (issues.isEmpty()) {
            paymentareatext.setText("No records found for this request.");
            partslist.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{},
                new String[]{"Part", "Price"}));
            totalpayment.setText("₱0");
            return;
        }

        // ── 2. Load labour fees from labourfee.csv ──
        String labourPath = "src\\labourfee.csv";
        Map<String, Integer> labourMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(labourPath))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] cols = line.split(",", -1);
                if (cols.length < 2) continue;
                labourMap.put(cols[0].trim().toLowerCase(), Integer.parseInt(cols[1].trim()));
            }
        } catch (Exception e) { e.printStackTrace(); }

        // ── 3. Load parts from cartrequest.csv for this customer + idreq ──
        String cartPath = "src\\cartrequest.csv";
        List<String[]> partsData = new ArrayList<>(); // [partName, price]

        try (BufferedReader br = new BufferedReader(new FileReader(cartPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (line.toLowerCase().startsWith("orderid")) continue;
                if (line.startsWith("---") || line.contains("GRAND TOTAL")) continue; // ← already there ✅


                String[] cols = line.split(",", -1);
                if (cols.length >= 8) {
                    String cust = cols[6].trim(); // col 6 = customer
                    String mech = cols[7].trim(); // col 7 = mechanic
                    if (cust.equalsIgnoreCase(customer) &&
                        !requestMechanic.isEmpty() &&
                        mech.equalsIgnoreCase(requestMechanic)) { // match by car slot directly
                        String partName = cols[2].trim();
                        String qty      = cols[3].trim();
                        String total    = cols[5].trim();
                        String category = cols[1].trim();
                        partsData.add(new String[]{partName, qty, total, category});
                    }
                }
            }
        } catch (Exception e) { /* cartrequest may not exist yet */ }

        // ── 4. Build paymentareatext ──
        StringBuilder sb = new StringBuilder();
        int labourTotal = 0;

        sb.append("══════════════════════════════\n");
        sb.append("  REQUEST #").append(idreqTarget).append(" — LABOUR FEES\n");
        sb.append("══════════════════════════════\n");

        for (String issue : issues) {
            int fee = labourMap.getOrDefault(issue.toLowerCase(), 0);
            labourTotal += fee;
            sb.append(String.format("  %-24s ₱%,d%n", issue, fee));
        }

        sb.append("──────────────────────────────\n");
        sb.append(String.format("  %-24s ₱%,d%n", "Labour Subtotal:", labourTotal));

        int partsTotal = 0;
        if (!partsData.isEmpty()) {
            sb.append("\n══════════════════════════════\n");
            sb.append("  PARTS FEES\n");
            sb.append("══════════════════════════════\n");
            for (String[] part : partsData) {
                int rowTotal = 0;
                try { rowTotal = Integer.parseInt(part[2]); } catch (Exception ignored) {}
                partsTotal += rowTotal;
                sb.append(String.format("  %-20s x%-2s  ₱%,d%n", part[0], part[1], rowTotal));
            }
            sb.append("──────────────────────────────\n");
            sb.append(String.format("  %-24s ₱%,d%n", "Parts Subtotal:", partsTotal));
        }

        int grandTotal = labourTotal + partsTotal;
        currentGrandTotal = grandTotal;

        sb.append("══════════════════════════════\n");
        sb.append(String.format("  %-24s ₱%,d%n", "Labour Subtotal:", labourTotal));
        sb.append(String.format("  %-24s ₱%,d%n", "Parts Subtotal:", partsTotal));
        sb.append(String.format("  %-24s ₱%,d%n", "GRAND TOTAL:", grandTotal));
        sb.append("══════════════════════════════\n");
        
        

        // ── Save state for pay button ──
        currentGrandTotal  = grandTotal;
        currentLabourTotal = labourTotal;
        currentPartsTotal  = partsTotal;
        currentIdreq       = idreqTarget;
        currentIssues      = issues;
        currentPartsData   = partsData;
        currentCar         = issues.isEmpty() ? "" : 
            // grab cr from problems.csv - already loaded, store it during loop above
            currentCar; // (see note below)

        // ... rest stays the same
        
        
        paymentareatext.setText(sb.toString());
        paymentareatext.setCaretPosition(0);

        // ── 5. Fill partslist table ──
        String[] colNames = {"Category", "Part", "Qty", "Total (₱)"};
        Object[][] tableData = new Object[partsData.size()][4];
        for (int i = 0; i < partsData.size(); i++) {
            tableData[i][0] = partsData.get(i)[3]; // category
            tableData[i][1] = partsData.get(i)[0]; // part name
            tableData[i][2] = partsData.get(i)[1]; // qty
            tableData[i][3] = "₱" + String.format("%,d", Integer.parseInt(partsData.get(i)[2])); // total
        }
        partslist.setModel(new javax.swing.table.DefaultTableModel(tableData, colNames));

        // ── 6. Update totalpayment label ──
        totalpayment.setText("₱" + String.format("%,d", grandTotal));

        // ── 7. Reset balance ──
        balance.setText("₱0");
        cash.setText("");
    }    
    

    public void bill(int cashAmount, int grandTotal) {
        String customer = customerframe.username;
        int change = 0;

        if (currentPaymentMethod.equals("CASH")) {
            if (cashAmount < grandTotal) {
                JOptionPane.showMessageDialog(null, "NOT ENOUGH CASH. TRY AGAIN.");
                return;
            }
            change = cashAmount - grandTotal;
            balance.setText("₱" + String.format("%,d", change));
        } else {
            // GCASH — full amount, no change
            cashAmount = grandTotal;
            balance.setText("N/A");
        }

        String dateStr = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                             .format(new java.util.Date());
        String sep  = "================================\n";
        String thin = "--------------------------------\n";

        StringBuilder r = new StringBuilder();
        r.append(sep);
        r.append("          FIXO\n");
        r.append("  Cuenco Avenue in Cebu City,\n");
        r.append("   Philippines, 09671842210\n");
        r.append(sep);
        r.append(String.format("  Customer : %s%n", customer));
        r.append(String.format("  Car      : %s%n", currentCar));
        r.append(String.format("  Car Slot : %d%n", currentSameCustomerId));
        r.append(String.format("  Req #    : %d%n", currentIdreq));
        r.append(String.format("  Date     : %s%n", dateStr));
        r.append(String.format("  Payment  : %s%n", currentPaymentMethod));
        r.append(thin);

        r.append("  LABOUR FEES\n");
        r.append(thin);
        for (String issue : currentIssues) {
            int fee = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("src\\labourfee.csv"))) {
                String line; boolean first = true;
                while ((line = br.readLine()) != null) {
                    if (first) { first = false; continue; }
                    String[] c = line.split(",", -1);
                    if (c.length >= 2 && c[0].trim().equalsIgnoreCase(issue))
                        fee = Integer.parseInt(c[1].trim());
                }
            } catch (Exception ignored) {}
            r.append(String.format("  %-22s ₱%,d%n", issue, fee));
        }
        r.append(String.format("  %-22s ₱%,d%n", "Labour Subtotal:", currentLabourTotal));
        r.append(thin);

        if (!currentPartsData.isEmpty()) {
            r.append("  PARTS\n");
            r.append(thin);
            for (String[] part : currentPartsData) {
                int rowTotal = 0;
                try { rowTotal = Integer.parseInt(part[2]); } catch (Exception ignored) {}
                r.append(String.format("  %-18s x%-2s  ₱%,d%n", part[0], part[1], rowTotal));
            }
            r.append(String.format("  %-22s ₱%,d%n", "Parts Subtotal:", currentPartsTotal));
            r.append(thin);
        }

        r.append(sep);
        r.append(String.format("  TOTAL    :        ₱%,d%n", grandTotal));
        if (currentPaymentMethod.equals("CASH")) {
            r.append(String.format("  CASH     :        ₱%,d%n", cashAmount));
            r.append(String.format("  CHANGE   :        ₱%,d%n", change));
        } else {
            r.append(String.format("  GCASH    :        ₱%,d%n", grandTotal));
        }
        r.append(sep);
        r.append("   Thanks For Your Business...!\n");
        r.append("   Software by FIXO\n");
        r.append(sep);

        // Show in recieptarea — append don't overwrite
        recieptarea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        String existing = recieptarea.getText().trim();
        recieptarea.setText(existing.isEmpty() ? r.toString()
                                               : existing + "\n" + r.toString());
        recieptarea.setCaretPosition(0);

        saveRecieptToCSV(customer, currentSameCustomerId, dateStr,
                         currentPaymentMethod, grandTotal, r.toString());

        JOptionPane.showMessageDialog(null,
            "Payment Successful!\n" +
            (currentPaymentMethod.equals("CASH")
                ? "Change: ₱" + String.format("%,d", change)
                : "Paid via GCash"));
    }

    private void saveRecieptToCSV(String customer, int samecustomerid,
                               String date, String payment,
                               int totalcost, String receiptText) {
        String path = "src\\reciept.csv";
        java.io.File file = new java.io.File(path);
        boolean isNew = !file.exists() || file.length() == 0;

        // Get next id first (read before opening for append)
        int nextId = 1;
        if (!isNew) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line, last = null;
                while ((line = br.readLine()) != null)
                    if (!line.trim().isEmpty()) last = line;
                if (last != null && !last.startsWith("id")) {
                    try { nextId = Integer.parseInt(last.split(",")[0].trim()) + 1; }
                    catch (Exception ignored) {}
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        try (java.io.PrintWriter pw = new java.io.PrintWriter(
                new java.io.FileWriter(file, true))) {

            if (isNew)
                pw.println("id,samecustomerid,customer,DATE,payment,totalcost,reciept");

            String escaped = "\"" + receiptText.replace("\"", "\"\"") + "\"";

            pw.printf("%d,%d,%s,%s,%s,%d,%s%n",
                nextId, samecustomerid, customer,
                date, payment, totalcost, escaped);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    
        class ButtonRenderer extends JButton implements TableCellRenderer {
            public ButtonRenderer() { setOpaque(true); }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                if (value instanceof JButton) {
                    setText(((JButton) value).getText());
                } else {
                    setText(value == null ? "" : value.toString());
                }
                return this;
            }
        }

        class ButtonEditor extends DefaultCellEditor {
            private JButton button;
            private String label;
            private boolean clicked;
            private JTable table;
            private int currentRow;
            private customerframe frame;

            public ButtonEditor(JCheckBox checkBox, customerframe frame) {
                super(checkBox);
                this.frame = frame;
                button = new JButton();
                button.setOpaque(true);
                button.addActionListener(e -> fireEditingStopped());
            }

            @Override
            public Component getTableCellEditorComponent(JTable tbl, Object value,
                    boolean isSelected, int row, int col) {
                table = tbl;
                currentRow = row;
                label = (value instanceof JButton)
                    ? ((JButton) value).getText()
                    : (value == null ? "" : value.toString());
                button.setText(label);
                clicked = true;
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                if (clicked) {
                    String repairman = table.getValueAt(currentRow, 0).toString();
                    String customer  = frame.username; // same as Step 3
                    frame.openConversation(customer, repairman);
                }
                clicked = false;
                return label;
            }

            @Override
            public boolean stopCellEditing() {
                clicked = false;
                return super.stopCellEditing();

            }
            }
            

        // ── Renderer ──
    class IssueButtonRenderer extends JButton implements TableCellRenderer {
        private Map<JButton, Boolean> state;
        public IssueButtonRenderer(Map<JButton, Boolean> state) {
            this.state = state;
            setOpaque(true);
            setBorderPainted(false);
            setFont(new java.awt.Font("Bahnschrift", 0, 13));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            if (value instanceof JButton) {
                JButton btn = (JButton) value;
                Boolean selected = state.getOrDefault(btn, false);
                setText(btn.getText());
                setBackground(selected ? new Color(0, 153, 153) : new Color(200, 200, 200));
                setForeground(selected ? Color.WHITE : Color.DARK_GRAY);
            }
            return this;
        }
    }

    // ── Editor ──
    class IssueButtonEditor extends DefaultCellEditor {
        private JButton editorBtn;
        private JButton currentBtn;
        private Map<JButton, Boolean> state;

        public IssueButtonEditor(Map<JButton, Boolean> state) {
            super(new JCheckBox());
            this.state = state;
            editorBtn = new JButton();
            editorBtn.setOpaque(true);
            editorBtn.setBorderPainted(false);
            editorBtn.setFont(new java.awt.Font("Bahnschrift", 0, 13));
            editorBtn.addActionListener(e -> {
                if (currentBtn != null) {
                    boolean wasSelected = state.getOrDefault(currentBtn, false);
                    boolean nowSelected = !wasSelected;
                    state.put(currentBtn, nowSelected);
                    String problem = currentBtn.getText();
                    if (nowSelected) {
                        selectedProblems.add(problem);
                    } else {
                        selectedProblems.remove(problem);
                    }
                    txtareaissues.setText("");
                    int i = 1;
                    for (String p : selectedProblems) {
                        txtareaissues.append(i + ". " + p + "\n");
                        i++;
                    }
                    issuess.repaint();
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int col) {
            if (value instanceof JButton) {
                currentBtn = (JButton) value;
                Boolean sel = state.getOrDefault(currentBtn, false);
                editorBtn.setText(currentBtn.getText());
                editorBtn.setBackground(sel ? new Color(0, 153, 153) : new Color(200, 200, 200));
                editorBtn.setForeground(sel ? Color.WHITE : Color.DARK_GRAY);
            }
            return editorBtn;
        }

        @Override
        public Object getCellEditorValue() {
            return currentBtn;
        }
    }
        

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(customerframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(customerframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(customerframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(customerframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            new customerframe(username).setVisible(true);            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ADDCAR;
    private javax.swing.JRadioButton CASH;
    private javax.swing.JButton CreateR;
    private javax.swing.JPanel DASHBOARD;
    private javax.swing.JButton Dashboard;
    private javax.swing.JButton History;
    private javax.swing.JButton Messages;
    private javax.swing.JButton Payment;
    private javax.swing.JPanel Photos;
    private javax.swing.JButton SENDBUTTONMESSAGE;
    private javax.swing.JPanel SERVPANEL1;
    private javax.swing.JPanel SERVPANEL2;
    private javax.swing.JPanel SUMMARY;
    private javax.swing.JButton ServiceStatus;
    private javax.swing.JTabbedPane USER;
    private javax.swing.JLabel USERNAME;
    private javax.swing.JButton addcar1;
    private javax.swing.JButton addcar2;
    private javax.swing.JButton addcar3;
    private javax.swing.JButton back;
    private javax.swing.JButton back2;
    private javax.swing.JLabel balance;
    private javax.swing.JToggleButton button1;
    private javax.swing.JToggleButton button2;
    private javax.swing.JToggleButton button3;
    private javax.swing.JToggleButton button4;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel carmodelandplatenum;
    private javax.swing.JLabel carmodelandplatenum1;
    private javax.swing.JLabel carmodelandplatenum2;
    private javax.swing.JButton carpic1;
    private javax.swing.JButton carpic2;
    private javax.swing.JButton carpic3;
    private javax.swing.JTextField cash;
    private javax.swing.JLayeredPane createreqpanel;
    private javax.swing.JPanel createrq;
    private com.toedter.calendar.JDateChooser datee;
    private javax.swing.JTextArea display2;
    private javax.swing.JButton edit1;
    private javax.swing.JButton edit2;
    private javax.swing.JButton edit3;
    private javax.swing.JButton edit4;
    private javax.swing.JButton edit5;
    private javax.swing.JButton edits3;
    private javax.swing.JPanel history;
    private javax.swing.JTable historytable;
    private javax.swing.JButton idontknow;
    private javax.swing.JButton image;
    private javax.swing.JPanel issue;
    private javax.swing.JTable issuess;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JTextArea materialarea;
    private javax.swing.JPanel message;
    private javax.swing.JButton next;
    private javax.swing.JTextArea notifarea;
    private javax.swing.JLabel numofcarinprogress;
    private javax.swing.JLabel numofcarinreadytopickup;
    private javax.swing.JButton others;
    private javax.swing.JTable partslist;
    private javax.swing.JButton paybutton;
    private javax.swing.JPanel payment;
    private javax.swing.JTextArea paymentareatext;
    private javax.swing.JButton paymentcar1;
    private javax.swing.JButton paymentcar2;
    private javax.swing.JButton paymentcar3;
    private javax.swing.JPanel paymentlayer1;
    private javax.swing.JPanel paymentlayer2;
    private javax.swing.JLayeredPane paymentlayerpanel;
    private javax.swing.JLabel progress;
    private javax.swing.JLabel progress2;
    private javax.swing.JLabel progress3;
    private javax.swing.JTextArea recieptarea;
    private javax.swing.JPanel schedule;
    private javax.swing.JButton servicecar1;
    private javax.swing.JButton servicecar2;
    private javax.swing.JButton servicecar3;
    private javax.swing.JLayeredPane servicestatpanel;
    private javax.swing.JPanel servicestatus;
    private javax.swing.JButton serviceupdate;
    private javax.swing.JLabel servpanel;
    private javax.swing.JPanel servpanel1;
    private javax.swing.JPanel servpanel2;
    private javax.swing.JTextArea summarypanel;
    private javax.swing.JLabel sumpic;
    private javax.swing.JTextField text2message;
    private javax.swing.JPanel timeslot;
    private javax.swing.JLayeredPane toppanel;
    private javax.swing.JLabel totalpayment;
    private javax.swing.JTextArea txtareaissues;
    private javax.swing.JTextArea updatestatus;
    private javax.swing.JPanel vehicles;
    private javax.swing.JPanel videopanel;
    // End of variables declaration//GEN-END:variables
}

