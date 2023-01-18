import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.LinkedList;

public class Main {
    public static final String BD_URL = "jdbc:mysql://127.0.0.1:3306/mydb";
    public static String LOGIN = "root";
    public static String PASSWORD = "101002Сщеуя";

    public static void main(String[] args) {RequestSQLХ.createConnect();}

    static class RequestSQLХ {

        static void createConnect() {
            String LOGIN = "root";
            String PASSWORD = "101002Сщеуя";
            try{
                Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                try(Connection conn = DriverManager.getConnection(BD_URL, LOGIN, PASSWORD)){
                    System.out.println("Подключение произошло успешно");
                    Table.createTable();
                }
                catch (Exception e){System.out.println("Неверный логин или пароль " + e);}
            } catch(Exception e){System.out.println("Ошибка подключения: " + e);}
        }

        static class Table extends JFrame {

            JButton completeButton = new JButton("Клиентское");
            JButton ordersButton = new JButton("Запчасти");


            CardLayout cardLayout;

            Table() {
                super("Часики тикают");

                JPanel ordersPanel = new OrdersPanel();
                JPanel completePanel = new CompletePanel();
                JPanel mainPanel = new JPanel();

                setSize(850,800);
                mainPanel.setBounds(0,50,800,800);
                setContentPane(mainPanel);
                setVisible(true);

                mainPanel.add(ordersPanel, "Orders");
                mainPanel.add(completePanel, "Components");

                completeButton.setBounds(100,0,100,25);
                ordersButton.setBounds(225,0,100,25);

                cardLayout = (CardLayout)(mainPanel.getLayout());

                completeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        cardLayout.last(mainPanel);
                    }
                });

                ordersButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        cardLayout.first(mainPanel);
                    }
                });
            }
            static void createTable(){
                new Table();
            }

            static class OrdersPanel extends JPanel{
                String requset = "SELECT * FROM complete";
                String typeTable = "complete";
                OrdersPanel(){
                    super();
                    String[] columName = {"Id", "Компонент", "Цена"};
                    LinkedList<Orders> date = createElement(requset, typeTable);
                    String[][] completeDate = new String[date.size()][4];

                    for(int i = 0; i < date.size(); i++){
                        completeDate[i][0] = String.valueOf(date.get(i).idOrders);
                        completeDate[i][1] = date.get(i).Model;
                        completeDate[i][3] = String.valueOf(date.get(i).Cost);
                    }

                    for(int i = 0; i < columName.length; i++){
                        add(new JTextArea(columName[i]))
                                .setBounds(100+100*i,50,100,25);;
                    }

                    for(int i = 0; i < date.size(); i++){
                        for(int j = 0; j < columName.length; j++){
                            add(new JTextArea(completeDate[i][j]))
                                    .setBounds(100+100*j,75+25*i,100,25);
                        }
                    }
                    setLayout(null);
                }
            }
        }

        static class CompletePanel extends  JPanel{
            String requset = "SELECT * FROM complete";
            String typeTable = "complete";
            CompletePanel(){
                super();
                String[] columName = {"Id", "Дата Выплнения", "Цена", "Модель Часов"};
                LinkedList<Complete> date = createElement(requset, typeTable);
                String[][] completeDate = new String[date.size()][4];

                for(int i = 0; i < date.size(); i++){
                    completeDate[i][0] = String.valueOf(date.get(i).idComplete);
                    completeDate[i][1] = date.get(i).ModelWatch;
                    completeDate[i][3] = date.get(i).DateCompleted;
                    completeDate[i][2] = String.valueOf(date.get(i).Cost);
                }

                for(int i = 0; i < columName.length; i++){
                    add(new JTextArea(columName[i]))
                            .setBounds(100+100*i,50,100,25);;
                }

                for(int i = 0; i < date.size(); i++){
                    for(int j = 0; j < columName.length; j++){
                        add(new JTextArea(completeDate[i][j]))
                                .setBounds(100+100*j,75+25*i,100,25);
                    }
                }
                setLayout(null);
            }
        }

        public static <Object> LinkedList<Object> createElement(String request, String typeTable) {
            LinkedList<Object> date = new LinkedList<>();
            try (Connection conn = DriverManager.getConnection(BD_URL, LOGIN, PASSWORD)) {
                Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(request);
                while (resultSet.next()) {
                    switch(typeTable){
                        case "orders":
                            date.add((Object) new Orders(resultSet.getInt("idOrder"),
                                    resultSet.getString("Model"), resultSet.getInt("cost")));
                            return date;
                        case "complete":
                            date.add((Object) new Complete(resultSet.getInt("idComplete"),
                                    resultSet.getString("DateCompleted"), resultSet.getInt("Cost"),
                                    resultSet.getString("ModelWatch")));
                            return date;
                    }
                }
            } catch (Exception e){System.out.println("Ошибка подключения: " + e);}
            return null;
        }
    }

    }

class Orders {
    int idOrders;
    String Model;
    int Cost;
    Orders(int idOrders, String Model, int Cost){
        this.Cost = Cost;
        this.Model = Model;
        this.idOrders = idOrders;
    }
}

class Complete{
    int idComplete;
    String DateCompleted;
    int Cost;
    String ModelWatch;
    Complete(int idComplete, String DateCompleted, int Cost, String ModelWatch){
        this.idComplete = idComplete;
        this.Cost = Cost;
        this.ModelWatch = ModelWatch;
        this.DateCompleted = DateCompleted;
    }
}