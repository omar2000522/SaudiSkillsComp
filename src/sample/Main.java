package sample;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment.*;
import org.w3c.dom.ls.LSException;

import java.sql.*;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class Main extends Application {
    Connection conn = null;
    boolean LoggedIn = false;
    Double windowWidth = 800.0;
    Double windowHight = 600.0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Brazil Marathon");
        textParse();
        screen1(primaryStage);
    }

    public void screen1(Stage window){
        BorderPane rootBorderPane = new BorderPane();
        Button loginButton = new Button("Login");
        Button runnerButton = new Button("I want to be a runner");
        Button sponsorButton = new Button("I want to sponsor a runner");
        Button infoButton = new Button("I want to find out more");
        Label marathonNameLabel = new Label("Marathon Skills 2015");
        Label countdownLabel = new Label();
        VBox buttonsBox = new VBox(runnerButton,sponsorButton,infoButton);
        HBox bottomBox = new HBox(countdownLabel,loginButton);
        HBox topBox = new HBox(marathonNameLabel);
        Long interval = 1000L;

        Calendar marathonStart = Calendar.getInstance();
        marathonStart.set(2019,8,5,6,0);
        Long countDownInMillis = marathonStart.getTimeInMillis() - System.currentTimeMillis();
        System.out.print("Days: ");
        System.out.println(countDownInMillis/86400000);
        System.out.print("Hours: ");
        System.out.println((countDownInMillis%86400000)/3600000);
        System.out.print("Minutes: ");
        System.out.println(((countDownInMillis%86400000)%3600000)/60000);
        //--------Proprieties--------
        marathonNameLabel.setFont(Font.font("Open Sans Semibold",36));
        topBox.setStyle("-fx-background-color: #336699;");
        bottomBox.setStyle("-fx-background-color: #336699;");
        topBox.setAlignment(Pos.CENTER);
        bottomBox.setAlignment(Pos.CENTER);
        buttonsBox.setAlignment(Pos.CENTER);
        loginButton.setAlignment(Pos.CENTER);
        runnerButton.setMinWidth(300);
        sponsorButton.setMinWidth(300);
        infoButton.setMinWidth(300);
        runnerButton.setMaxWidth(300);
        sponsorButton.setMaxWidth(300);
        infoButton.setMaxWidth(300);
        runnerButton.setMinHeight(50);
        sponsorButton.setMinHeight(50);
        infoButton.setMinHeight(50);
        topBox.setPadding(new Insets(30));
        bottomBox.setPadding(new Insets(30));
        buttonsBox.setPadding(new Insets(30,50,50,50));
        topBox.setSpacing(50);
        buttonsBox.setSpacing(50);
        bottomBox.setSpacing(30);

        rootBorderPane.setCenter(buttonsBox);
        rootBorderPane.setTop(topBox);
        rootBorderPane.setBottom(bottomBox);
        //rootGridPane.add(loginButton, 60, 4);
        window.setScene(new Scene(rootBorderPane, windowWidth, windowHight));
        window.show();

        Runnable countdown = new Runnable() {
            @Override
            public void run() {
                while(true){
                    long countDownInMillis = marathonStart.getTimeInMillis() - System.currentTimeMillis();
                    long days = countDownInMillis/86400000;
                    long hours = (countDownInMillis%86400000)/3600000;
                    long mins = ((countDownInMillis%86400000)%3600000)/60000;
                    long secs = (((countDownInMillis%86400000)%3600000)%60000)/1000;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            countdownLabel.setText(days+" days "+hours+" hours "+mins+" minutes "+secs+" seconds until marathon start.");
                        }
                    });

                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thrd = new Thread(countdown);
        thrd.start();

        loginButton.setOnAction(value -> {
            loginScreen3(window);
        });
        sponsorButton.setOnAction(value -> {
            try {
                screen6(window);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public void screen2(Stage window){

    }

    public void screen6(Stage window) throws SQLException {
        BorderPane rootBorderPane = new BorderPane();
        ComboBox runners = new ComboBox();
        TextField nameTextField = new TextField();
        TextField nameOnCardTextField = new TextField();
        TextField cardNumTextField = new TextField();
        TextField expiryMonthTextField = new TextField();
        TextField expiryYearTextField = new TextField();
        TextField cvcTextField = new TextField();
        Label nameLabel = new Label("Your Name:");
        Label runnerLabel = new Label("Runner:");
        Label nameOnCardLabel = new Label("Name on Card:");
        Label cardNumLabel = new Label("Credit Card #:");
        Label expiryLabel = new Label("Expiry Date:");
        Label cvcLabel = new Label("CVC:");
        HBox expiryBox = new HBox(expiryMonthTextField,expiryYearTextField);
        VBox fieldBox = new VBox(nameTextField,runners,nameOnCardTextField,cardNumTextField,expiryBox,cvcTextField);
        VBox labelBox = new VBox(nameLabel,runnerLabel,nameOnCardLabel,cardNumLabel,expiryLabel,cvcLabel);
        HBox leftSide = new HBox(labelBox,fieldBox);
        Label charityLabel = new Label();
        Button charityInfoButton = new Button("INFO");
        Label amountLabel = new Label("0$");
        Button minButton = new Button("-");
        Button plusButton = new Button("+");
        TextField amountTextField = new TextField("0");
        Button payButton = new Button("Pay now");
        Button cancelButton = new Button("Cancel");
        HBox charityBox = new HBox(charityLabel,charityInfoButton);
        HBox amountBox = new HBox(minButton,amountTextField,plusButton);
        HBox payCancelBox = new HBox(payButton, cancelButton);
        VBox rightSide = new VBox(charityBox,amountLabel,amountBox,payCancelBox);
        int amount = 0;
        int numberOfRunners = 0;
        int x = 0;
        final String[] runnerId = new String[1];


        //----------Properties----------
        rootBorderPane.setLeft(leftSide);
        rootBorderPane.setRight(rightSide);
        amountLabel.setFont(Font.font("Open Sans Semibold",100));
        //leftBox.setAlignment(Pos.CENTER);
        leftSide.setSpacing(10);
        leftSide.setPadding(new Insets(30));
        rightSide.setSpacing(10);
        rightSide.setPadding(new Insets(30));
        labelBox.setSpacing(15);
        labelBox.setPadding(new Insets(5));
        fieldBox.setSpacing(7.5);
        cvcTextField.maxWidth(60);
        expiryBox.setSpacing(10);
        expiryMonthTextField.maxWidth(30);
        expiryYearTextField.maxWidth(60);
        amountTextField.maxWidth(70);
        minButton.setMinWidth(30);
        plusButton.setMinWidth(30);
        amountBox.setSpacing(10);
        payCancelBox.setSpacing(20);
        cancelButton.minWidth(100);
        payButton.minWidth(100);
        cancelButton.maxWidth(100);
        payButton.maxWidth(100);
        payCancelBox.setAlignment(Pos.CENTER);
        runners.setMinWidth(nameTextField.getWidth());
        //rootBorderPane.setRight();

        ResultSet runnersSet = sqlExe("SELECT user.Firstname, user.Lastname, runner.CountryCode, RegistrationEvent.BibNumber, Charity.CharityName, Charity.CharityDescription, Charity.CharityLogo, Registration.RunnerId FROM ((((user INNER JOIN runner ON runner.Email = user.Email) INNER JOIN Registration ON runner.RunnerId = Registration.RunnerId) INNER JOIN RegistrationEvent ON Registration.RegistrationId = RegistrationEvent.RegistrationId) INNER JOIN Charity ON Registration.CharityId = Charity.CharityId);");
        while (runnersSet.next()) numberOfRunners = runnersSet.getRow();
        runnersSet.beforeFirst();
        String[][] charityTable = new String[numberOfRunners][3];

        while (runnersSet.next()){
            runners.getItems().addAll(
                    runnersSet.getString("LastName")+", "
                            +runnersSet.getString("FirstName")+" - "
                            +runnersSet.getString("BibNumber") +" ("
                            +runnersSet.getString("CountryCode")+")");
            charityTable[x][0] = runnersSet.getString("CharityName");
            charityTable[x][1] = runnersSet.getString("CharityDescription");
            charityTable[x][2] = runnersSet.getString("CharityLogo");
            charityTable[x][3] = runnersSet.getString("RunnerId");
            x++;
        }

        //---------Lambda Funcs--------------
        minButton.setOnAction(value -> {
            Integer currentAmount = Integer.parseInt(amountTextField.getText()) - 10;
            if (currentAmount>0 && currentAmount<=999) amountTextField.setText(currentAmount.toString());
            else if (currentAmount<10) amountTextField.setText("0");
            amountLabel.setText(amountTextField.getText() + "$");
        });
        plusButton.setOnAction(value -> {
            Integer currentAmount = Integer.parseInt(amountTextField.getText()) + 10;
            if (currentAmount>0 && currentAmount<=999) amountTextField.setText(currentAmount.toString());
            amountLabel.setText(amountTextField.getText() + "$");
        });
        payButton.setOnAction(value -> {
            try{
                Calendar currentDate = Calendar.getInstance();
                currentDate.setTimeInMillis(System.currentTimeMillis());
                Calendar expiryDate = Calendar.getInstance();
                expiryDate.set(Integer.parseInt(expiryYearTextField.getText()), Integer.parseInt(expiryMonthTextField.getText()),1);

                if(!(nameTextField.getText().isEmpty()) &&
                !(nameOnCardTextField.getText().isEmpty()) &&
                cardNumTextField.getText().length() == 16 &&
                currentDate.before(expiryDate) &&
                cvcTextField.getText().length() == 3){
                    System.out.println("zucc");
                    screen7(window,runnerId[0],amountLabel.getText());
                }}
            finally {}
        });
        runners.setOnAction(value -> {
            charityLabel.setText(charityTable[runners.getSelectionModel().getSelectedIndex()][0]);
            runnerId[0] = charityTable[runners.getSelectionModel().getSelectedIndex()][3];
        });
        charityInfoButton.setOnAction(value -> {

            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(window);
            Text charityInfo = new Text(charityTable[runners.getSelectionModel().getSelectedIndex()][1]);
            VBox dialogVbox = new VBox(charityInfo);
            dialogVbox.setSpacing(20);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();

        });
        cancelButton.setOnAction(value -> {
            screen1(window);
        });
        window.setScene(new Scene(rootBorderPane,windowWidth,windowHight));
        window.show();
    }

    public void screen7(Stage window, String runnerId, String amount){
        BorderPane rootBorderPane = new BorderPane();
        Label thxLabel = new Label("Thank you for your sponsorship!");
        Label smolThxLabel = new Label("Thank you for sponsoring a runner in Marathon Skills 2019!\nYour donation will help out their chosen charity.");
        Label runnerInfoLabel = new Label();
        Label charityNameLabel = new Label();
        Label amountLabel = new Label();
        Button backButton = new Button("Back");
        VBox centerBox = new VBox(thxLabel,smolThxLabel,runnerInfoLabel,charityNameLabel,amountLabel,backButton);

        //----------Proprieties----------
        rootBorderPane.setCenter(centerBox);

        sqlExe("SELECT user.FirstName, user.LastName, runner.RunnerId, runner.CountryCode, charity.CharityName FROM ((((runner INNER JOIN user ON runner.Email = user.Email) INNER JOIN registration ON runner.RunnerId = registration.RunnerId) ");

    }

    public void loginScreen3(Stage window){
        //---------DEFINITIONS--------
        BorderPane borderPane = new BorderPane();
        Button b6on = new Button("Login");
        Button backButton = new Button("Back");
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        Label usernameLabel = new Label("Username");
        Label passwordLabel = new Label("Password");
        HBox b6onBox = new HBox(b6on, backButton);
        VBox textBox = new VBox(usernameLabel, username, passwordLabel, password);

        //---------Proprieties--------
        borderPane.setBottom(b6onBox);
        borderPane.setCenter(textBox);
        textBox.setSpacing(20);
        textBox.setPadding(new Insets(30));
        b6onBox.setPadding(new Insets(30));
        b6onBox.setSpacing(50);
        b6on.setAlignment(Pos.CENTER);
        b6onBox.setAlignment(Pos.CENTER);
        username.setAlignment(Pos.CENTER);
        password.setAlignment(Pos.CENTER);
        usernameLabel.setAlignment(Pos.CENTER);
        passwordLabel.setAlignment(Pos.CENTER);
        usernameLabel.setTextAlignment(TextAlignment.CENTER);
        passwordLabel.setTextAlignment(TextAlignment.CENTER);

        b6on.setOnAction(value -> {
            sql_login(username.getText(), password.getText());
//            primaryStage.setScene(new Scene(new Button("lol"), 69, 69));
//            primaryStage.show();
        });
        backButton.setOnAction(value -> {
            screen1(window);
        });

        window.setScene(new Scene(borderPane, windowWidth, windowHight));
        window.show();
    }

    public void  sql_login(String username, String password){
        try {

            String URL = "jdbc:mysql://127.0.0.1:3306/nibba?useSSL=False";
            String USER = "root";
            String PASS = "omar";
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("connected");
            String query = "select * "+ "from " + "nibba" +"."+ "users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("username");
                String pass = rs.getString("password");

                if (name.equals(username) && pass.equals(password)){
                    System.out.println("logged in");
                    LoggedIn = true;
                }

            }
            if (!LoggedIn){
                System.out.println("Invalid Username or Password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet sqlExe(String query){
        try {

            String URL = "jdbc:mysql://127.0.0.1:3306/cpt02?useSSL=False";
            String USER = "root";
            String PASS = "omar";
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("connected");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            conn.close();
            return rs;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void textParse(){
        String text = "1\tLAKISHA # TOMBLIN\t8/6/1961\tF\t2\tAdministrative Clerk\tProvides administrative support\tH\t $55.00 \tl_tomblin@nnl.com\n" +
                "2\tKRIS|SWEELY\t12/24/1980\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tk_sweely@yahoo.com\n" +
                "3\tELSIE|ESTELL\t7/7/1974\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \telsie.estell@rrg.net\n" +
                "4\tTYRELL|ROSENBERG\t8/2/1965\tMale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \ttyrell_rosenberg@hotmail.com\n" +
                "5\tMADELYN # HOOVER\t12/4/1962\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tH\t $68.00 \tmadelyn_hoover@@hotmail.com\n" +
                "6\tKENETH *PARHAM\t3/30/1967\tMale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tk_parham@live.com\n" +
                "7\tLYLE *MALLORY\t7/6/1989\tMales\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tl_mallory@live.com\n" +
                "8\tCLIFTON *FIGUEROA\t3/23/1956\tM\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tc_figueroa@live.com\n" +
                "9\tMOHAMMAD|RICHEY\t7/1/1995\tMale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tmohammad_richey@jcl.net\n" +
                "10\tWINIFRED # DUNCAN\t10/20/1968\tFemales\t11\tSponsorship Coordinator\tIdentifies, develops and maintains effective relationships with all sponsors\tYearly\t $70,000.00 \tw_duncan@hotmail.com\n" +
                "11\tVERNITA # SEABORN\t10/13/1993\tFemale\t19\tAdministrative Manager\tOversees the administrative functions of the organisation\tYearly\t $65,000.00 \tv_seaborn@gmail.com\n" +
                "12\tLENORA|HIGGINS\t6/29/1997\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tl_higgins@msv.org\n" +
                "13\tDON *RUSS\t10/13/1971\tMale\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tdon_russ@jcl.net\n" +
                "14\tTHADDEUS *STALEY\t2/13/1992\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tt_staley@nnl.com\n" +
                "15\tWILFORD *RIVERA\t9/21/1978\tMale\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \twilford.rivera@gmail.com\n" +
                "16\tLANCE|BEYER\t9/30/1990\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tl_beyer@rnl.com\n" +
                "17\tLASHANDA|SNOWDEN\t6/28/1983\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tlashanda.snowden@rrg.net\n" +
                "18\tGLORY *FELDER\t11/18/1963\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tglory.felder@@hotmail.com\n" +
                "19\tESTELLA *BLALOCK\t10/17/1951\tF\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \te_blalock@@ccf.org\n" +
                "20\tTRAVIS # RAMOS\t4/21/1983\tMales\t1\tHelpdesk Advisor\tWorks on the helpdesk\tH\t $68.00 \ttravis_ramos@hotmail.com\n" +
                "21\tILA *THOMPSON\t5/17/1979\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \ti_thompson@yahoo.com\n" +
                "22\tLORRAINE|MCKEE\t10/11/1973\tFemale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tl_mckee@hotmail.com\n" +
                "23\tSHELLIE *MONTANO\t9/19/1974\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tshellie_montano@hotmail.com\n" +
                "24\tERVIN # LAWRENCE\t12/20/1984\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tervin_lawrence@hotmail.com\n" +
                "25\tGOLDA # GRANGER\t4/28/1982\tFemale\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tg_granger@msv.org\n" +
                "26\tKING|MERCADO\t11/26/1952\tM\t13\tSocial Media Coordinator\tManages content across social media platforms\tYearly\t $78,000.00 \tk_mercado@@hotmail.com\n" +
                "27\tGERARDO # BOWSER\t8/19/1953\tM\t2\tAdministrative Clerk\tProvides administrative support\tH\t $55.00 \tgerardo_bowser@hotmail.com\n" +
                "28\tKARIN # MCCARTY\t10/17/1985\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tk_mccarty@gmail.com\n" +
                "29\tMAYRA # CALLAHAN\t8/7/1979\tFemales\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tH\t $45.00 \tm_callahan@live.com\n" +
                "30\tCALLAN|MCCONNELL\t12/13/1958\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tcallan.mcconnell@@gmail.com\n" +
                "31\tCLAYTON # MARKS\t3/8/1988\tMale\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tH\t $56.00 \tc_marks@nnl.com\n" +
                "32\tTRACY # CUMMINGS\t1/7/1991\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tH\t $62.00 \ttracy.cummings@gmail.com\n" +
                "33\tNELSON *CASEY\t6/18/1986\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tn_casey@hr.org\n" +
                "34\tSTAN # STOVALL\t10/7/1960\tMale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \ts_stovall@gmail.com\n" +
                "35\tEARLENE|HOGAN\t2/27/1964\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \te_hogan@gmail.com\n" +
                "36\tWAYNE|ROSARIO\t6/20/1953\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tw_rosario@gmail.com\n" +
                "37\tAUTUMN # MCCALL\t3/11/1965\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tautumn.mccall@rrg.net\n" +
                "38\tPRINCESS *CATT\t6/21/1970\tFemales\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tp_catt@hotmail.com\n" +
                "39\tSIBYL *TAGALA\t5/21/1998\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tsibyl.tagala@hotmail.com\n" +
                "40\tMILAGROS *BUSCH\t12/20/1965\tFemale\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tmilagros_busch@jcl.net\n" +
                "41\tDONNIE *JENNINGS\t10/15/1969\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \td_jennings@@msv.org\n" +
                "42\tOTIS # ENRIQUEZ\t1/11/1986\tMales\t11\tSponsorship Coordinator\tIdentifies, develops and maintains effective relationships with all sponsors\tYearly\t $70,000.00 \totis_enriquez@hotmail.com\n" +
                "43\tPETE *REDDING\t12/17/1988\tM\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tp_redding@ccf.org\n" +
                "44\tVAN|JACK\t2/18/1967\tMales\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tv_jack@hr.org\n" +
                "45\tCYNTHIA *SOSA\t11/8/1989\tFemale\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tc_sosa@gmail.com\n" +
                "46\tLUCIA *SPICER\t9/26/1952\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tl_spicer@@yahoo.com\n" +
                "47\tTONJA|LEPPALA\t5/20/1958\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tt_leppala@yahoo.com\n" +
                "48\tRENATO # CREECH\t11/8/1967\tMales\t3\tHR Clerk\tProvides support to the HR Manager\tH\t $35.00 \trenato.creech@yahoo.com\n" +
                "49\tDESMOND *JEFFERS\t5/31/1952\tMale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tdesmond_jeffers@@hotmail.com\n" +
                "50\tCANDELARIA *WISE\t8/1/1967\tFemale\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tc_wise@hotmail.com\n" +
                "51\tRORY|RODEFER\t9/19/1995\tMale\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \trory_rodefer@ffrs.gov\n" +
                "52\tSHERRI|CRAWFORD\t1/24/1953\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \ts_crawford@@yahoo.com\n" +
                "53\tMANUELA *PRUETT\t10/3/1964\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tmanuela_pruett@yahoo.com\n" +
                "54\tTASHA *INGRAHAM\t10/6/1965\tFemales\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tt_ingraham@@gmail.com\n" +
                "55\tTAMIKA *ESCOBEDO\t3/29/1951\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tt_escobedo@@msv.org\n" +
                "56\tMARTYN *GLEASON\t6/8/1979\tMale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tm_gleason@nnl.com\n" +
                "57\tCHRISTOPHER|LANGEVIN\t11/12/1962\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tc_langevin@@hotmail.com\n" +
                "58\tNICHOLAS|FORRESTER\t8/29/1996\tMales\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tnicholas.forrester@rrg.net\n" +
                "59\tMARITA *ELAM\t2/21/1984\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tm_elam@@yahoo.com\n" +
                "60\tRYAN *HAHN\t4/20/1968\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tryan_hahn@hotmail.com\n" +
                "61\tCHI *MIMS\t8/5/1963\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tc_mims@@msv.org\n" +
                "62\tSUSAN *VANCE\t9/7/1973\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \ts_vance@gmail.com\n" +
                "63\tGARETH *ROBERSON\t4/30/1978\tMale\t11\tSponsorship Coordinator\tIdentifies, develops and maintains effective relationships with all sponsors\tYearly\t $70,000.00 \tg_roberson@nnl.com\n" +
                "64\tGREIG # WALTERS\t11/3/1976\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tH\t $56.00 \tg_walters@hotmail.com\n" +
                "65\tARMANDO *MOSLEY\t1/1/1956\tMale\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \ta_mosley@hotmail.com\n" +
                "66\tJAYSON|MCCLELLAND\t3/15/1967\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tj_mcclelland@yahoo.com\n" +
                "67\tGRANT # DOWDY\t12/15/1955\tMale\t21\tFinance Manager\tResponsible for the financial health of Marathon Skills\tYearly\t $115,000.00 \tgrant.dowdy@hotmail.com\n" +
                "68\tALVA *OWEN\t8/20/1971\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \ta_owen@hotmail.com\n" +
                "69\tELDON|CRAIG\t7/15/1983\tM\t10\tVolunteer Coordinator\tCoordinates  recruitment and role allocation of volunteers\tY\t $90,000.00 \te_craig@gmail.com\n" +
                "70\tBOYCE # WILLIAM\t12/6/1983\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tH\t $62.00 \tb_william@hotmail.com\n" +
                "71\tVITO # KANIECKI\t3/31/1965\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tH\t $62.00 \tv_kaniecki@hr.org\n" +
                "72\tNAPOLEON # PEDROZO\t4/7/1964\tMale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tn_pedrozo@hotmail.com\n" +
                "73\tDARYL # MASON\t8/14/1977\tMale\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tH\t $56.00 \td_mason@fin.gov\n" +
                "74\tLESLIE *BOSTON\t7/24/1995\tMale\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tleslie.boston@yahoo.com\n" +
                "75\tMITCHEL *KIM\t12/29/1994\tMale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tm_kim@hotmail.com\n" +
                "76\tBERTA|HERMAN\t5/24/1993\tFemales\t13\tSocial Media Coordinator\tManages content across social media platforms\tYearly\t $78,000.00 \tb_herman@gmail.com\n" +
                "77\tWILDA *LAMBERT\t9/3/1991\tFemale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tw_lambert@yahoo.com\n" +
                "78\tNEVA # CLAY\t12/26/1978\tF\t7\tAssistant\tWorks  in any number of areas\tH\t $40.00 \tn_clay@gmail.com\n" +
                "79\tCHARLES *MACK\t8/11/1975\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tc_mack@hotmail.com\n" +
                "80\tHETTIE *CARMONA\t6/8/1996\tFemale\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \th_carmona@ccf.org\n" +
                "81\tLUCIEN|MURILLO\t6/24/1965\tMale\t10\tVolunteer Coordinator\tCoordinates  recruitment and role allocation of volunteers\tYearly\t $90,000.00 \tl_murillo@fin.gov\n" +
                "82\tDEVON|GOBERN\t9/2/1958\tMale\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \td_gobern@ccf.org\n" +
                "83\tKRISTA *EARLY\t8/21/1985\tFemale\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tk_early@hr.org\n" +
                "84\tGREIG *CONN\t10/30/1993\tMale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tgreig_conn@hotmail.com\n" +
                "85\tIRMA|MONTANEZ\t11/27/1954\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \ti_montanez@gmail.com\n" +
                "86\tJERRELL|OHARA\t5/16/1967\tMale\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tj_ohara@msv.org\n" +
                "87\tBRANDON|YANG\t10/10/1975\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tbrandon.yang@gmail.com\n" +
                "88\tMARLENE # SCHUMACHER\t11/5/1987\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tmarlene.schumacher@rrg.net\n" +
                "89\tCLEMENTINE # JACKSON\t6/13/1972\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tclementine.jackson@gmail.com\n" +
                "90\tSHAWANDA *CANTU\t5/2/1977\tF\t13\tSocial Media Coordinator\tManages content across social media platforms\tYearly\t $78,000.00 \ts_cantu@hr.org\n" +
                "91\tTAMIKA *COE\t9/2/1953\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tt_coe@fin.gov\n" +
                "92\tABRAHAM *GATLIN\t6/19/1987\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tabraham.gatlin@hotmail.com\n" +
                "93\tJANE|HEDRICK\t3/5/1975\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tj_hedrick@gmail.com\n" +
                "94\tALONSO|HITCHCOCK\t1/29/1968\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \talonso.hitchcock@gmail.com\n" +
                "95\tLEO # VEGA\t11/17/1957\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tl_vega@live.com\n" +
                "96\tTANA *HILLMAN\t1/20/1957\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \ttana_hillman@hotmail.com\n" +
                "97\tNELLIE|LINDSEY\t6/5/1985\tFemales\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tn_lindsey@ccf.org\n" +
                "98\tKELLEY|DANIEL\t6/19/1966\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tkelley.daniel@gmail.com\n" +
                "99\tMARGARETTE|STEM\t9/12/1966\tF\t16\tIT Manager\tOversees the planning and coordinating of technological solutions\tY\t $130,000.00 \tm_stem@ccf.org\n" +
                "100\tKELVIN|RULAPAUGH\t8/23/1983\tM\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tk_rulapaugh@gmail.com\n" +
                "101\tMAYRA # SMITH\t1/17/1982\tFemales\t7\tAssistant\tWorks  in any number of areas\tH\t $40.00 \tm_smith@yahoo.com\n" +
                "102\tJANNA|NICKA\t8/7/1980\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tj_nicka@msv.org\n" +
                "103\tPAULINE # NEWCOMB\t12/7/1957\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tH\t $68.00 \tp_newcomb@@gmail.com\n" +
                "104\tELVA # PATTEN\t4/7/1983\tF\t7\tAssistant\tWorks  in any number of areas\tH\t $40.00 \te_patten@msv.org\n" +
                "105\tSHANE *COLAIZZO\t5/30/1970\tM\t16\tIT Manager\tOversees the planning and coordinating of technological solutions\tYearly\t $130,000.00 \ts_colaizzo@fin.gov\n" +
                "106\tTHERSA|ARREDONDO\t2/2/1964\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tt_arredondo@@gmail.com\n" +
                "107\tAMPARO # SYKES\t2/29/1960\tFemale\t16\tIT Manager\tOversees the planning and coordinating of technological solutions\tYearly\t $130,000.00 \ta_sykes@ccf.org\n" +
                "108\tJORGE *BLACKWELL\t5/8/1955\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tj_blackwell@hotmail.com\n" +
                "109\tTYLER *BOOTH\t1/26/1972\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tt_booth@gmail.com\n" +
                "110\tJORGE *WAYMIRE\t2/17/1988\tMale\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tjorge_waymire@@ffrs.gov\n" +
                "111\tKENNETH|MEIER\t3/17/1984\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tkenneth_meier@ffrs.gov\n" +
                "112\tAUBREY *ERWIN\t11/1/1960\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \ta_erwin@yahoo.com\n" +
                "113\tCHARLES *STILES\t8/25/1981\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tc_stiles@gmail.com\n" +
                "114\tLOWELL *BAER\t8/1/1951\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tl_baer@@fin.gov\n" +
                "115\tTRACEY *ONOFRIO\t1/7/1986\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tt_onofrio@@ccf.org\n" +
                "116\tSTELLA *HIEBER\t7/23/1972\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tstella_hieber@hotmail.com\n" +
                "117\tROSEANNE *VILLANUEVA\t6/14/1955\tFemale\t9\tCEO\tChief Executive Officer\tYearly\t $75,000.00 \troseanne_villanueva@yahoo.com\n" +
                "118\tVALENTIN # PARRISH\t11/22/1978\tMales\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tvalentin.parrish@rrg.net\n" +
                "119\tTERESITA *RUSH\t7/5/1991\tF\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tt_rush@yahoo.com\n" +
                "120\tCAREY *HALEY\t2/4/1979\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tc_haley@yahoo.com\n" +
                "121\tBURT # PLATT\t2/27/1962\tM\t23\tWorkforce Manager\tResponsible for coordinating the workforce at the venue\tYearly\t $130.00 \tb_platt@hotmail.com\n" +
                "122\tZORA *TATE\t6/23/1967\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tzora_tate@jcl.net\n" +
                "123\tPENELOPE|ROOT\t7/16/1963\tFemale\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tpenelope.root@@rrg.net\n" +
                "124\tDENNY *MORALES\t3/21/1995\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tdenny_morales@hotmail.com\n" +
                "125\tDALE|BRANCH\t4/6/1982\tM\t14\tOperations Manager\tOversees all logistical aspects of bringing the marathon together\tY\t $110,000.00 \td_branch@live.com\n" +
                "126\tLAURENCE *METCALF\t6/13/1956\tMales\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tlaurence.metcalf@yahoo.com\n" +
                "127\tMERYL *BENNETT\t4/25/1963\tFemales\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tm_bennett@@nnl.com\n" +
                "128\tBERNARDO|RICHEY\t3/7/1951\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tb_richey@@live.com\n" +
                "129\tDAN|KINNEY\t2/12/1974\tMales\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \td_kinney@hotmail.com\n" +
                "130\tARCHIE # ATKINSON\t3/6/1970\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \ta_atkinson@ccf.org\n" +
                "131\tHUGH # VARRIANO\t2/10/1958\tM\t10\tVolunteer Coordinator\tCoordinates  recruitment and role allocation of volunteers\tYearly\t $90,000.00 \thugh.varriano@gmail.com\n" +
                "132\tSONYA *HOLBROOK\t12/19/1969\tFemale\t16\tIT Manager\tOversees the planning and coordinating of technological solutions\tYearly\t $130,000.00 \ts_holbrook@rnl.com\n" +
                "133\tPATRICA *OLSEN\t9/12/1982\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tp_olsen@gmail.com\n" +
                "134\tDELIA *SHOMO\t12/19/1986\tFemale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \td_shomo@yahoo.com\n" +
                "135\tCARLA|VINSON\t1/21/1958\tF\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tc_vinson@ccf.org\n" +
                "136\tFRANCIS # NGUYEN\t11/10/1986\tFemales\t13\tSocial Media Coordinator\tManages content across social media platforms\tYearly\t $78,000.00 \tf_nguyen@live.com\n" +
                "137\tWILLARD # PUTNAM\t8/28/1958\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tw_putnam@gmail.com\n" +
                "138\tTARSHA *WILKINSON\t4/2/1963\tFemale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tt_wilkinson@@hr.org\n" +
                "139\tDEBBY # DUNLAP\t1/4/1974\tFemale\t23\tWorkforce Manager\tResponsible for coordinating the workforce at the venue\tYearly\t $130.00 \tdebby_dunlap@jcl.net\n" +
                "140\tGILDA *PERRY\t7/20/1993\tF\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tg_perry@yahoo.com\n" +
                "141\tGARRY # SANTANA\t7/2/1993\tMale\t4\tFinance Clerk\tProvides support to the Finance Manager\tH\t $62.00 \tg_santana@live.com\n" +
                "142\tNICKOLAS *NUNEZ\t3/3/1994\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tnickolas.nunez@gmail.com\n" +
                "143\tPALMA *GRIER\t8/24/1962\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tp_grier@yahoo.com\n" +
                "144\tRUSSELL # WHITTINGTON\t9/1/1957\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tH\t $65.00 \trussell_whittington@hotmail.com\n" +
                "145\tOLIVE|SALMON\t5/7/1954\tF\t23\tWorkforce Manager\tResponsible for coordinating the workforce at the venue\tY\t $130.00 \to_salmon@ccf.org\n" +
                "146\tGARRETT # BOLLINGER\t12/19/1970\tM\t1\tHelpdesk Advisor\tWorks on the helpdesk\tH\t $68.00 \tg_bollinger@@gmail.com\n" +
                "147\tIGNACIO *COKLEY\t1/23/1955\tM\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tignacio_cokley@jcl.net\n" +
                "148\tNOBLE *MEINERDING\t8/21/1950\tM\t20\tHR Manager\tManages the recruitment, retention and development of staff\tYearly\t $70,000.00 \tn_meinerding@@ccf.org\n" +
                "149\tTHELMA *SKURSKY\t4/28/1959\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tt_skursky@@rnl.com\n" +
                "150\tAL *BOWLES\t2/1/1969\tM\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tal.bowles@@hotmail.com\n" +
                "151\tCRAIG # COUSEY\t2/16/1970\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \tc_cousey@gmail.com\n" +
                "152\tTRACI # BROOKS\t2/15/1969\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tH\t $68.00 \ttraci_brooks@@hotmail.com\n" +
                "153\tISABELLA *WATERMAN\t1/1/1979\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tisabella.waterman@rrg.net\n" +
                "154\tJEROME *JACKSON\t1/23/1956\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tjerome.jackson@rrg.net\n" +
                "155\tSILAS *DIAS\t2/8/1997\tMale\t13\tSocial Media Coordinator\tManages content across social media platforms\tYearly\t $78,000.00 \ts_dias@hotmail.com\n" +
                "156\tTOD|CLARKE\t4/4/1964\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tt_clarke@ccf.org\n" +
                "157\tDONNELL *MYLES\t1/2/1991\tM\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \td_myles@gmail.com\n" +
                "158\tKEITH # MEADOWS\t8/8/1959\tMale\t2\tAdministrative Clerk\tProvides administrative support\tH\t $55.00 \tk_meadows@live.com\n" +
                "159\tJOHNNIE # PRESTON\t9/19/1957\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tjohnnie.preston@gmail.com\n" +
                "160\tNANETTE *TALBOT\t4/29/1994\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tn_talbot@hotmail.com\n" +
                "161\tRENATA *ENGLISH\t1/3/1956\tFemales\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tr_english@gmail.com\n" +
                "162\tSAMMIE|DICKENS\t7/5/1972\tF\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \ts_dickens@msv.org\n" +
                "163\tANDRA # CAREY\t11/27/1987\tFemales\t3\tHR Clerk\tProvides support to the HR Manager\tH\t $35.00 \ta_carey@gmail.com\n" +
                "164\tGREGOR *WOLNY\t4/30/1980\tMale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tg_wolny@yahoo.com\n" +
                "165\tLUTHER # DOYLE\t4/29/1998\tMale\t10\tVolunteer Coordinator\tCoordinates  recruitment and role allocation of volunteers\tYearly\t $90,000.00 \tl_doyle@gmail.com\n" +
                "166\tENOLA *AUSTIN\t11/23/1991\tFemale\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \te_austin@nnl.com\n" +
                "167\tDARIUS *NOBLE\t12/6/1959\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \td_noble@gmail.com\n" +
                "168\tWILTON # SEXTON\t12/3/1982\tM\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tw_sexton@msv.org\n" +
                "169\tKATHY|KRAMER\t2/20/1989\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tkathy_kramer@yahoo.com\n" +
                "170\tLUPE *MAILLARD\t4/23/1981\tFemale\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tlupe_maillard@hotmail.com\n" +
                "171\tLEANNE *NIELSEN\t6/22/1982\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tl_nielsen@gmail.com\n" +
                "172\tSEYMOUR *GROVES\t5/2/1978\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tseymour.groves@rrg.net\n" +
                "173\tGLENNA # FELTON\t6/21/1964\tF\t2\tAdministrative Clerk\tProvides administrative support\tH\t $55.00 \tg_felton@hotmail.com\n" +
                "174\tLIN *PALAIA\t12/3/1961\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tlin.palaia@yahoo.com\n" +
                "175\tSHANELL *ORTIZ\t8/29/1962\tF\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tshanell_ortiz@jcl.net\n" +
                "176\tNICOLETTE # DAVISON\t5/17/1996\tFemale\t3\tHR Clerk\tProvides support to the HR Manager\tH\t $35.00 \tn_davison@ccf.org\n" +
                "177\tGREGORIA|RYDER\t9/21/1955\tFemales\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tgregoria.ryder@gmail.com\n" +
                "178\tKARRIE|TRUJILLO\t11/8/1990\tFemales\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tk_trujillo@hr.org\n" +
                "179\tSTACEY *SWITZER\t7/23/1955\tFemale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tstacey.switzer@hotmail.com\n" +
                "180\tKATHERYN # SPEER\t2/11/1976\tFemale\t7\tAssistant\tWorks  in any number of areas\tH\t $40.00 \tk_speer@gmail.com\n" +
                "181\tPRUDENCE *HACKETT\t9/30/1950\tFemale\t10\tVolunteer Coordinator\tCoordinates  recruitment and role allocation of volunteers\tYearly\t $90,000.00 \tp_hackett@@hotmail.com\n" +
                "182\tROSANNE *CORCORAN\t1/19/1977\tFemales\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \trosanne_corcoran@jcl.net\n" +
                "183\tROSALIE|STEM\t7/15/1985\tFemale\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \trosalie.stem@hotmail.com\n" +
                "184\tGEORGE # LYONS\t3/12/1984\tMale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tgeorge_lyons@ffrs.gov\n" +
                "185\tJAYNE *HARMAN\t1/11/1982\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tj_harman@yahoo.com\n" +
                "186\tMALLORY # WORRELL\t1/25/1992\tFemale\t23\tWorkforce Manager\tResponsible for coordinating the workforce at the venue\tYearly\t $130.00 \tm_worrell@hotmail.com\n" +
                "187\tCAMERON # NORMAN\t12/16/1974\tM\t7\tAssistant\tWorks  in any number of areas\tH\t $40.00 \tcameron.norman@yahoo.com\n" +
                "188\tBERNARD *CROSBY\t2/6/1977\tM\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tb_crosby@yahoo.com\n" +
                "189\tSHANELL *VILLALOBOS\t3/19/1960\tF\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \ts_villalobos@yahoo.com\n" +
                "190\tEVELYN *DEMESA\t6/8/1961\tF\t10\tVolunteer Coordinator\tCoordinates  recruitment and role allocation of volunteers\tY\t $90,000.00 \te_demesa@live.com\n" +
                "191\tTY *LABRECHE\t6/25/1966\tMale\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tt_labreche@hotmail.com\n" +
                "192\tBRANDEE # MEANS\t6/3/1962\tFemales\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tH\t $45.00 \tb_means@nnl.com\n" +
                "193\tLARISSA # COTTRELL\t8/10/1957\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tl_cottrell@gmail.com\n" +
                "194\tFRIEDA *BRAND\t4/28/1965\tF\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tfrieda_brand@hotmail.com\n" +
                "195\tDANI *GRIFFIN\t7/24/1994\tF\t14\tOperations Manager\tOversees all logistical aspects of bringing the marathon together\tYearly\t $110,000.00 \td_griffin@gmail.com\n" +
                "196\tLEE *DOW\t12/28/1960\tMale\t11\tSponsorship Coordinator\tIdentifies, develops and maintains effective relationships with all sponsors\tYearly\t $70,000.00 \tl_dow@yahoo.com\n" +
                "197\tMEAGAN|BRUCE\t3/20/1982\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tm_bruce@live.com\n" +
                "198\tJEANETTE *DOYLE\t10/14/1989\tFemale\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tjeanette_doyle@jcl.net\n" +
                "199\tANTHONY *SHAW\t6/27/1977\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tanthony.shaw@rrg.net\n" +
                "200\tROBYN # ROSA\t5/13/1984\tFemale\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tr_rosa@msv.org\n" +
                "201\tRANDI # CHAFFINS\t8/27/1979\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tH\t $62.00 \tr_chaffins@gmail.com\n" +
                "202\tMARJORIE *BLEDSOE\t7/13/1969\tF\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tm_bledsoe@gmail.com\n" +
                "203\tDALE *PEARSON\t11/1/1975\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \td_pearson@live.com\n" +
                "204\tPAULA|BROWER\t4/9/1960\tF\t9\tCEO\tChief Executive Officer\tYearly\t $75,000.00 \tpaula_brower@hotmail.com\n" +
                "205\tDEVIN *MEYERS\t7/1/1973\tMale\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \td_meyers@nnl.com\n" +
                "206\tCHANTEL *LINDSEY\t4/14/1972\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tchantel_lindsey@ffrs.gov\n" +
                "207\tTOMAS|ABERNATHY\t11/18/1958\tM\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tt_abernathy@hr.org\n" +
                "208\tKRISTI # CROOK\t10/16/1972\tF\t2\tAdministrative Clerk\tProvides administrative support\tH\t $55.00 \tk_crook@hr.org\n" +
                "209\tRAMIRO # HIATT\t5/25/1952\tM\t7\tAssistant\tWorks  in any number of areas\tH\t $40.00 \tramiro.hiatt@@yahoo.com\n" +
                "210\tMARANDA *MEIER\t8/18/1952\tFemale\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tm_meier@@live.com\n" +
                "211\tHESTER *CARDENAS\t1/5/1975\tF\t8\tWorkforce Support\tProvides support to the Workforce Manager\tHourly\t $65.00 \th_cardenas@gmail.com\n" +
                "212\tJOESPH # LANKFORD\t3/6/1993\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tH\t $56.00 \tjoesph_lankford@hotmail.com\n" +
                "213\tBRANDY # MUELLER\t2/15/1972\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tH\t $56.00 \tb_mueller@hotmail.com\n" +
                "214\tMAUD *MORGAN\t10/19/1971\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tm_morgan@gmail.com\n" +
                "215\tIDA|GUERRERO\t11/30/1989\tFemales\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \ti_guerrero@hr.org\n" +
                "216\tTOMASA # HANDY\t12/14/1956\tF\t1\tHelpdesk Advisor\tWorks on the helpdesk\tH\t $68.00 \tt_handy@hotmail.com\n" +
                "217\tDANNY *WILLARD\t11/26/1953\tM\t23\tWorkforce Manager\tResponsible for coordinating the workforce at the venue\tYearly\t $130.00 \td_willard@hotmail.com\n" +
                "218\tDEMETRA|DURHAM\t5/1/1998\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \td_durham@rnl.com\n" +
                "219\tSAUL # PYLE\t4/26/1998\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tH\t $62.00 \ts_pyle@fin.gov\n" +
                "220\tCECELIA *MCDONALD\t6/22/1968\tF\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tc_mcdonald@nnl.com\n" +
                "221\tJULES *BLACKMAN\t7/1/1979\tMale\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \tjules_blackman@hotmail.com\n" +
                "222\tDEBBY *NUGENT\t11/3/1958\tF\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \td_nugent@rnl.com\n" +
                "223\tMARTIN|JIMENEZ\t5/5/1994\tMale\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tm_jimenez@hotmail.com\n" +
                "224\tWARREN # PINTO\t10/16/1973\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tH\t $65.00 \twarren.pinto@rrg.net\n" +
                "225\tJUDI *NEWMAN\t3/15/1961\tF\t2\tAdministrative Clerk\tProvides administrative support\tHourly\t $55.00 \tj_newman@ccf.org\n" +
                "226\tBORIS *LEBLANC\t8/28/1977\tM\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tboris_leblanc@jcl.net\n" +
                "227\tGERTRUDE # ALFARO\t1/7/1972\tF\t19\tAdministrative Manager\tOversees the administrative functions of the organisation\tYearly\t $65,000.00 \tg_alfaro@rnl.com\n" +
                "228\tJONATHON|DELASANCHA\t6/17/1952\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tjonathon.delasancha@@rrg.net\n" +
                "229\tLIZABETH|HUMPHREY\t2/8/1998\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tl_humphrey@ccf.org\n" +
                "230\tKATHI *FULTON\t6/10/1977\tFemale\t1\tHelpdesk Advisor\tWorks on the helpdesk\tHourly\t $68.00 \tk_fulton@yahoo.com\n" +
                "231\tDENIS|BUCK\t6/5/1966\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \td_buck@hotmail.com\n" +
                "232\tBRET # ACUFF\t1/26/1951\tM\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tH\t $56.00 \tb_acuff@@hotmail.com\n" +
                "233\tBEULAH # OSTOLAZA\t10/13/1955\tF\t7\tAssistant\tWorks  in any number of areas\tH\t $40.00 \tb_ostolaza@msv.org\n" +
                "234\tRODOLFO # MERCADO\t2/11/1954\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tr_mercado@yahoo.com\n" +
                "235\tLEONARDO # CONNER\t5/16/1998\tM\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tl_conner@live.com\n" +
                "236\tARTHUR *YUM\t3/26/1957\tM\t4\tFinance Clerk\tProvides support to the Finance Manager\tHourly\t $62.00 \ta_yum@yahoo.com\n" +
                "237\tMARSHALL # CHANG\t8/1/1984\tM\t8\tWorkforce Support\tProvides support to the Workforce Manager\tH\t $65.00 \tm_chang@gmail.com\n" +
                "238\tCHUN|RYAN\t3/2/1974\tFemale\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tc_ryan@yahoo.com\n" +
                "239\tJACK|GRUBB\t1/6/1973\tMale\t3\tHR Clerk\tProvides support to the HR Manager\tHourly\t $35.00 \tj_grubb@hr.org\n" +
                "240\tBLANCH *HARDING\t7/8/1987\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tb_harding@fin.gov\n" +
                "241\tANGEL # JERNIGAN\t1/20/1962\tFemale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \ta_jernigan@ccf.org\n" +
                "242\tSTEPHAINE *FARRELL\t12/10/1978\tFemale\t23\tWorkforce Manager\tResponsible for coordinating the workforce at the venue\tY\t $130.00 \ts_farrell@yahoo.com\n" +
                "243\tELIJAH *SNOW\t1/7/1990\tM\t23\tWorkforce Manager\tResponsible for coordinating the workforce at the venue\tYearly\t $130.00 \te_snow@yahoo.com\n" +
                "244\tRORY *HENGEL\t5/22/1996\tM\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tr_hengel@hotmail.com\n" +
                "245\tJOSEFINA *CHRISTIAN\t1/16/1997\tF\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tjosefina.christian@yahoo.com\n" +
                "246\tJANNA *HILLS\t10/14/1995\tFemale\t5\tMarketing Assistant\tProvides support to the Marketing Manager\tHourly\t $56.00 \tjanna_hills@hotmail.com\n" +
                "247\tQUINCY # LAMBERT\t9/21/1977\tMale\t2\tAdministrative Clerk\tProvides administrative support\tH\t $55.00 \tquincy_lambert@hotmail.com\n" +
                "248\tJERRY # DARLING\t2/25/1980\tF\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \tj_darling@ccf.org\n" +
                "249\tSUSIE *CARDONA\t3/11/1986\tFemale\t7\tAssistant\tWorks  in any number of areas\tHourly\t $40.00 \tsusie_cardona@yahoo.com\n" +
                "250\tALEXANDRIA *SMALLS\t5/17/1980\tFemale\t6\tSocial Media Assistant\tProvides support to the Social Media Coordinator\tHourly\t $45.00 \talexandria.smalls@hotmail.com\n" +
                "251\tDESMOND *STONE\t3/28/1991\tM\t20\tHR Manager\tManages the recruitment, retention and development of staff\tY\t $70,000.00 \td_stone@gmail.com\n";
            int x = 0;
            String[][] zuccJuice = new String[251][];
        while(x<251){
                String[] arre = text.split("\n");
                System.out.println(arre[x]);
                zuccJuice[x] = arre[x].split("\t");
                System.out.println(zuccJuice[x]);
                for(int i = 0; i<10; i++){
                    System.out.println(zuccJuice[x][i]);
                }
                x++;
            }



    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
