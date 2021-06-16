
package book_database;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Book_Database extends JFrame implements ActionListener
{
    // Menu Buttons
    private final JButton isbnButton = new JButton("ISBN");
    private final JButton titleButton = new JButton("BOOK TITLES");
    private final JButton authorButton = new JButton("AUTHOR");
    private final JButton releaseDateButton = new JButton("RELEASE DATE");
    private final JButton publisherButton = new JButton("PUBLISHER");
    private final JButton submitButton = new JButton("SUBMIT ORDER");
    private final JButton shoppingCartButton = new JButton("PROCEED TO CHECKOUT");
    private final JButton removeButton = new JButton("REMOVE AN ORDER");
    private final JButton cancelButton = new JButton("CANCEL ORDER");
    
    // Main Screen with imported database and buttons to add the displayed items
    public Book_Database()
    {
        // Initializes main menu with executables
        JPanel menu = new JPanel(new BorderLayout());
        JPanel tableLayout = new JPanel(new BorderLayout());
        menu.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        menu.add(isbnButton);
        menu.add(titleButton);
        menu.add(authorButton);
        menu.add(releaseDateButton);
        menu.add(publisherButton);
        menu.add(shoppingCartButton);
        
        add(menu, BorderLayout.SOUTH);
        add(tableLayout, BorderLayout.NORTH);
        
        this.isbnButton.addActionListener(this);
        this.titleButton.addActionListener(this);
        this.authorButton.addActionListener(this);
        this.releaseDateButton.addActionListener(this);
        this.publisherButton.addActionListener(this);
        this.shoppingCartButton.addActionListener(this);
        
        try
        {   
            // Creates main table of books for sale
            final String user = "root";
            final String password = "password";
            String url = "jdbc:mysql://localhost:3306/bookdb";
            Connection connection = DriverManager.getConnection(url, user, password);

            String querySum = "SELECT * FROM book;";
            Statement bookSum = connection.createStatement();
            ResultSet resultSetTotal = bookSum.executeQuery(querySum);
            // Iterates thrugh rows to determine how many rows the table will hold
            int totalRows = 0;
            while (resultSetTotal.next())
            {
                if (resultSetTotal != null)
                {
                    resultSetTotal.last();
                    totalRows = resultSetTotal.getRow();
                }
            }
            int totalColumns = resultSetTotal.getMetaData().getColumnCount();
            Object[][] input = new Object[totalRows][totalColumns];
            // Iterates through each row from the database for transfer to main menu table
            resultSetTotal.beforeFirst();
            int i = 0;
            while (resultSetTotal.next()) 
            {
                    int x = 0;
                    input[i][x++] = resultSetTotal.getString("ISBN");
                    input[i][x++] = resultSetTotal.getString("BookTitle");
                    input[i][x++] = resultSetTotal.getString("BookPrice");
                    input[i][x++] = resultSetTotal.getString("BookReleaseDate");
                    input[i][x++] = resultSetTotal.getString("PUBLISHER_PublisherID");
                    input[i][x++] = resultSetTotal.getString("AUTHOR_AuthorID");
                    i++;
            }
            if(i < 1)
            {
                JOptionPane.showMessageDialog(null, "No Orders Have Been Filed!", "Error",
                JOptionPane.ERROR_MESSAGE);
            }
            // Defines the table layout and design, including column titles
            String[] columns = {"ISBN", "TITLE", "PRICE", "RELEASE DATE", "PUBLISHER ID", "AUTHOR ID"};
            JTable table = new JTable(input, columns);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tableLayout.add(table.getTableHeader(), BorderLayout.NORTH);
            tableLayout.add(table, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Exception catched!");
            System.err.println(e.getMessage());
        }
    }
    // Each if statement is executed depending on which button is pressed
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == isbnButton)
        {
            isbnButton.setFont(isbnButton.getFont().deriveFont(Font.ITALIC));
            titleButton.setFont(titleButton.getFont().deriveFont(Font.BOLD));
            authorButton.setFont(authorButton.getFont().deriveFont(Font.BOLD));
            releaseDateButton.setFont(releaseDateButton.getFont().deriveFont(Font.BOLD));
            publisherButton.setFont(publisherButton.getFont().deriveFont(Font.BOLD));
            shoppingCartButton.setFont(shoppingCartButton.getFont().deriveFont(Font.BOLD));
            // A message asks the user for ISBN to buy from the database
            String searchISBN = JOptionPane.showInputDialog(null, "Input ISBN to Order!\nType the ISBN accurately!", "Yummy Bookstore!",
                                    JOptionPane.QUESTION_MESSAGE);
            if (searchISBN.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Error: No Input!", "Error",
                JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try
                {
                    // Searches through the book table for the ISBN given by the user
                    final String user = "root";
                    final String password = "password";
                    String url = "jdbc:mysql://localhost:3306/bookdb";
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String queryTitle = "SELECT * FROM book WHERE ISBN = " + "\"" + searchISBN + "\";";
                    Statement statementTitle = connection.createStatement();
                    ResultSet result = statementTitle.executeQuery(queryTitle);
                    // Once ISBN is located, it's information is displayed to the user for confirmation
                    while (result.next())
                    {
                        int order = JOptionPane.showConfirmDialog(null, "This item was retrieved!\n ISBN: " + result.getString("ISBN") + "\n Book Title: " + result.getString("BookTitle")
                                + "\n Price: " + result.getString("BookPrice") + "\n Release Date: " + result.getString("BookReleaseDate") + "\n Publisher ID: "
                                + result.getString("PUBLISHER_PublisherID")+ "\n Author ID: " + result.getString("AUTHOR_AuthorID") + "\n\nAccept Order?");
                        // These variables hold the ISBN and BookTitle in order to be called for insert statement upon confirmation
                        String resultISBN = result.getString("ISBN");
                        String resultTitleCopies = result.getString("BookTitle");
                        // 0 = Yes on Confirmation Panel; 1 = No on Confirmation Panel
                        if (order == 0)
                        {
                            try
                            {
                                // Inputs ISBN of book(s) confirmed by the user into user's shopping cart
                                PreparedStatement addCartBook = connection.prepareStatement("INSERT INTO shoppingcart_book (BOOK_ISBN, SHOPPINGCART_ShoppingCartID, BookCount) "
                                        + "VALUES (" + "\"" + resultISBN + "\"" + ", 1, 1);");
                                addCartBook.executeUpdate();
                                // A message asks the user how many copies of this book they want to order
                                String bookCopies = JOptionPane.showInputDialog(null, "How Many Copies Would You Like to Order?\nInput an Integer!");
                                if (!bookCopies.isEmpty())
                                {
                                    try
                                    {
                                        // The number of book copies given by the user are re-inserted into the same row as above via an update
                                        PreparedStatement updateCartBook = connection.prepareStatement("UPDATE shoppingcart_book SET BookCount = " + bookCopies + 
                                                " WHERE BOOK_ISBN = " + "\"" + resultISBN + "\"" + ";");
                                        updateCartBook.executeUpdate();
                                        JOptionPane.showMessageDialog(null, bookCopies + " Copies of " + "\"" + resultTitleCopies + "\"" + " Are Now in Your Shopping Cart!");
                                    } catch (Exception es) {
                                        System.err.println("Exception catched!");
                                        System.err.println(es.getMessage());
                                        JOptionPane.showMessageDialog(null, "Error: Book Copies could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Error! Only 1 Copy Will Be In Your Shopping Cart!");
                                }
                            } catch (Exception es) {
                                System.err.println("Inner exception catched!");
                                System.err.println(es.getMessage());
                                JOptionPane.showMessageDialog(null, "Error: This item could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Order was not placed!");
                        }
                    }
                } catch (Exception es) {
                    System.err.println("Exception catched!");
                    System.err.println(es.getMessage());
                    JOptionPane.showMessageDialog(null, "Order was not placed!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
            isbnButton.setFont(isbnButton.getFont().deriveFont(Font.BOLD));
        }
        if (e.getSource() == titleButton)
        {
            isbnButton.setFont(isbnButton.getFont().deriveFont(Font.BOLD));
            titleButton.setFont(titleButton.getFont().deriveFont(Font.ITALIC));
            authorButton.setFont(authorButton.getFont().deriveFont(Font.BOLD));
            releaseDateButton.setFont(releaseDateButton.getFont().deriveFont(Font.BOLD));
            publisherButton.setFont(publisherButton.getFont().deriveFont(Font.BOLD));
            shoppingCartButton.setFont(shoppingCartButton.getFont().deriveFont(Font.BOLD));
            
            String searchTitle = JOptionPane.showInputDialog(null, "Input Book Title to Order!", "Yummy Bookstore!",
                                    JOptionPane.QUESTION_MESSAGE);
            if (searchTitle.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Error: No Input!", "Error",
                JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try
                {
                    // Searches through the book table for the Book Title given by the user
                    final String user = "root";
                    final String password = "password";
                    String url = "jdbc:mysql://localhost:3306/bookdb";
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String queryTitle = "SELECT * FROM book WHERE BookTitle = " + "\"" + searchTitle + "\";";
                    Statement statementTitle = connection.createStatement();
                    ResultSet result = statementTitle.executeQuery(queryTitle);
                    // Once Book Title is located, it's information is displayed to the user for confirmation
                    while (result.next())
                    {
                        int order = JOptionPane.showConfirmDialog(null, "This item was retrieved!\n ISBN: " + result.getString("ISBN") + "\n Book Title: " + result.getString("BookTitle")
                                + "\n Price: " + result.getString("BookPrice") + "\n Release Date: " + result.getString("BookReleaseDate") + "\n Publisher ID: "
                                + result.getString("PUBLISHER_PublisherID")+ "\n Author ID: " + result.getString("AUTHOR_AuthorID") + "\n\nAccept Order?");
                        String resultISBN = result.getString("ISBN");
                        String resultTitleCopies = result.getString("BookTitle");
                        // 0 = Yes on Confirmation Panel; 1 = No on Confirmation Panel
                        if (order == 0)
                        {
                            try
                            {
                                // Inputs ISBN of book(s) confirmed by the user into the user's shopping cart
                                PreparedStatement addCartBook = connection.prepareStatement("INSERT INTO shoppingcart_book (BOOK_ISBN, SHOPPINGCART_ShoppingCartID, BookCount) "
                                        + "VALUES (" + "\"" + resultISBN + "\"" + ", 1, 1);");
                                addCartBook.executeUpdate();
                                // A message asks the user how many copies of this book they want to order
                                String bookCopies = JOptionPane.showInputDialog(null, "How Many Copies Would You Like to Order?");
                                if (!bookCopies.isEmpty())
                                {
                                    try
                                    {
                                        // The number of book copies given by the user are re-inserted into the same row as above via an update
                                        PreparedStatement updateCartBook = connection.prepareStatement("UPDATE shoppingcart_book SET BookCount = " + bookCopies + 
                                                " WHERE BOOK_ISBN = " + "\"" + resultISBN + "\"" + ";");
                                        updateCartBook.executeUpdate();
                                        JOptionPane.showMessageDialog(null, bookCopies + " Copies of " + "\"" + resultTitleCopies + "\"" + " Are Now in Your Shopping Cart!");
                                    } catch (Exception es) {
                                        System.err.println("Exception catched!");
                                        System.err.println(es.getMessage());
                                        JOptionPane.showMessageDialog(null, "Error: Book Copies could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Error! Only 1 Copy Will Be In Your Shopping Cart!");
                                }
                            } catch (Exception es) {
                            System.err.println("Exception catched!");
                            System.err.println(es.getMessage());
                            JOptionPane.showMessageDialog(null, "Error: This item could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Order was not placed!");
                        }
                    }
                } catch (Exception es) {
                    System.err.println("Exception catched!");
                    System.err.println(es.getMessage());
                    JOptionPane.showMessageDialog(null, "Order was not placed!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
            titleButton.setFont(titleButton.getFont().deriveFont(Font.BOLD));
        }
        if (e.getSource() == authorButton)
        {
            isbnButton.setFont(isbnButton.getFont().deriveFont(Font.BOLD));
            titleButton.setFont(titleButton.getFont().deriveFont(Font.BOLD));
            authorButton.setFont(authorButton.getFont().deriveFont(Font.ITALIC));
            releaseDateButton.setFont(releaseDateButton.getFont().deriveFont(Font.BOLD));
            publisherButton.setFont(publisherButton.getFont().deriveFont(Font.BOLD));
            shoppingCartButton.setFont(shoppingCartButton.getFont().deriveFont(Font.BOLD));
            
            String searchAuthor = JOptionPane.showInputDialog(null, "Input Author ID to Order His/Her Book!", "Yummy Bookstore!",
                                    JOptionPane.QUESTION_MESSAGE);
            if (searchAuthor.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Error: No Input!", "Error",
                JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try
                {
                    // Searches through the book table for the Author given by the user
                    final String user = "root";
                    final String password = "password";
                    String url = "jdbc:mysql://localhost:3306/bookdb";
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String queryTitle = "SELECT * FROM book WHERE AUTHOR_AuthorID = " + "\"" + searchAuthor + "\";";
                    Statement statementTitle = connection.createStatement();
                    ResultSet result = statementTitle.executeQuery(queryTitle);
                    
                     // Once the Author is located, their books' information is displayed to the user for confirmation
                    while (result.next())
                    {
                        int order = JOptionPane.showConfirmDialog(null, "This item was retrieved!\n ISBN: " + result.getString("ISBN") + "\n Book Title: " + result.getString("BookTitle")
                        + "\n Price: " + result.getString("BookPrice") + "\n Release Date: " + result.getString("BookReleaseDate") + "\n Publisher ID: "
                        + result.getString("PUBLISHER_PublisherID")+ "\n Author ID: " + result.getString("AUTHOR_AuthorID") + "\n\nAccept Order?");
                        String resultISBN = result.getString("ISBN");
                        String resultTitleCopies = result.getString("BookTitle");
                        // 0 = Yes on Confirmation Panel; 1 = No on Confirmation Panel
                        if (order == 0)
                        {
                            try
                            {
                                // Inputs ISBN of book(s) confirmed by the user into the user's shopping cart
                                PreparedStatement addCartBook = connection.prepareStatement("INSERT INTO shoppingcart_book (BOOK_ISBN, SHOPPINGCART_ShoppingCartID, BookCount) VALUES (" + "\"" + resultISBN + "\"" + ", 1, 1);");
                                addCartBook.executeUpdate();
                                // A message asks the user how many copies of this book they want to order
                                String bookCopies = JOptionPane.showInputDialog(null, "How Many Copies Would You Like to Order?");
                                if (!bookCopies.isEmpty())
                                {
                                    try
                                    {
                                        // The number of book copies given by the user are re-inserted into the same row as above via an update
                                        PreparedStatement updateCartBook = connection.prepareStatement("UPDATE shoppingcart_book SET BookCount = " + bookCopies + 
                                                " WHERE BOOK_ISBN = " + "\"" + resultISBN + "\"" + ";");
                                        updateCartBook.executeUpdate();
                                        JOptionPane.showMessageDialog(null, bookCopies + " Copies of " + "\"" + resultTitleCopies + "\"" + " Are Now in Your Shopping Cart!");
                                    } catch (Exception es) {
                                        System.err.println("Exception catched!");
                                        System.err.println(es.getMessage());
                                        JOptionPane.showMessageDialog(null, "Error: Book Copies could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Error! Only 1 Copy Will Be In Your Shopping Cart!");
                                }
                            } catch (Exception es) {
                                System.err.println("Exception catched!");
                                System.err.println(es.getMessage());
                                JOptionPane.showMessageDialog(null, "Error: This item could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Order was not placed!");
                        }
                    }
                } catch (Exception es) {
                    System.err.println("Exception catched!");
                    System.err.println(es.getMessage());
                    JOptionPane.showMessageDialog(null, "This item is not available!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
            authorButton.setFont(authorButton.getFont().deriveFont(Font.BOLD));
        }
        if (e.getSource() == releaseDateButton)
        {
            isbnButton.setFont(isbnButton.getFont().deriveFont(Font.BOLD));
            titleButton.setFont(titleButton.getFont().deriveFont(Font.BOLD));
            authorButton.setFont(authorButton.getFont().deriveFont(Font.BOLD));
            releaseDateButton.setFont(releaseDateButton.getFont().deriveFont(Font.ITALIC));
            publisherButton.setFont(publisherButton.getFont().deriveFont(Font.BOLD));
            shoppingCartButton.setFont(shoppingCartButton.getFont().deriveFont(Font.BOLD));
            
            String searchDate = JOptionPane.showInputDialog(null, "Input Release Date of Book to Order!\nType the date as displayed on Main Table!", "Yummy Bookstore!",
                                    JOptionPane.QUESTION_MESSAGE);
            if (searchDate.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Error: No Input!", "Error",
                JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try
                {
                    // Searches through the book table for the Release Date given by the user
                    final String user = "root";
                    final String password = "password";
                    String url = "jdbc:mysql://localhost:3306/bookdb";
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String queryTitle = "SELECT * FROM book WHERE BookReleaseDate = " + "\"" + searchDate + "\";";
                    Statement statementTitle = connection.createStatement();
                    ResultSet result = statementTitle.executeQuery(queryTitle);
                    
                    // Once the Release Date is located, the information of its corresponding books are displayed to the user for confirmation
                    while (result.next())
                    {
                        int order = JOptionPane.showConfirmDialog(null, "This item was retrieved!\n ISBN: " + result.getString("ISBN") + "\n Book Title: " + result.getString("BookTitle")
                        + "\n Price: " + result.getString("BookPrice") + "\n Release Date: " + result.getString("BookReleaseDate") + "\n Publisher ID: "
                        + result.getString("PUBLISHER_PublisherID")+ "\n Author ID: " + result.getString("AUTHOR_AuthorID") + "\n\nAccept Order?");
                        String resultISBN = result.getString("ISBN");
                        String resultTitleCopies = result.getString("BookTitle");
                        // 0 = Yes on Confirmation Panel; 1 = No on Confirmation Panel
                        if (order == 0)
                        {
                            try
                            {
                                // Inputs ISBN of book(s) confirmed by the user into the user's shopping cart
                                PreparedStatement addCartBook = connection.prepareStatement("INSERT INTO shoppingcart_book (BOOK_ISBN, SHOPPINGCART_ShoppingCartID, BookCount) VALUES (" + "\"" + resultISBN + "\"" + ", 1, 1);");
                                addCartBook.executeUpdate();
                                // A message asks the user how many copies of this book they want to order
                                String bookCopies = JOptionPane.showInputDialog(null, "How Many Copies Would You Like to Order?");
                                if (!bookCopies.isEmpty())
                                {
                                    try
                                    {
                                        // The number of book copies given by the user are re-inserted into the same row as above via an update
                                        PreparedStatement updateCartBook = connection.prepareStatement("UPDATE shoppingcart_book SET BookCount = " + bookCopies + 
                                                " WHERE BOOK_ISBN = " + "\"" + resultISBN + "\"" + ";");
                                        updateCartBook.executeUpdate();
                                        JOptionPane.showMessageDialog(null, bookCopies + " Copies of " + "\"" + resultTitleCopies + "\"" + " Are Now in Your Shopping Cart!");
                                    } catch (Exception es) {
                                        System.err.println("Exception catched!");
                                        System.err.println(es.getMessage());
                                        JOptionPane.showMessageDialog(null, "Error: Book Copies could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Error! Only 1 Copy Will Be In Your Shopping Cart!");
                                }
                            } catch (Exception es) {
                                System.err.println("Exception catched!");
                                System.err.println(es.getMessage());
                                JOptionPane.showMessageDialog(null, "Error: This item could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Order was not placed!");
                        }
                    }
                } catch (Exception es) {
                    System.err.println("Exception catched!");
                    System.err.println(es.getMessage());
                    JOptionPane.showMessageDialog(null, "This item is not available!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
            releaseDateButton.setFont(releaseDateButton.getFont().deriveFont(Font.BOLD));
        }
        if (e.getSource() == publisherButton)
        {
            isbnButton.setFont(isbnButton.getFont().deriveFont(Font.BOLD));
            titleButton.setFont(titleButton.getFont().deriveFont(Font.BOLD));
            authorButton.setFont(authorButton.getFont().deriveFont(Font.BOLD));
            releaseDateButton.setFont(releaseDateButton.getFont().deriveFont(Font.BOLD));
            publisherButton.setFont(publisherButton.getFont().deriveFont(Font.ITALIC));
            shoppingCartButton.setFont(shoppingCartButton.getFont().deriveFont(Font.BOLD));
            
            String searchPublisher = JOptionPane.showInputDialog(null, "Input Publisher ID to Order Their Books!\nType the ID as displayed on the Main Table!", "Yummy Bookstore!",
                                    JOptionPane.QUESTION_MESSAGE);
            if (searchPublisher.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Error: No Input!", "Error",
                JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try
                {
                    // Searches through the book table for the Publisher given by user
                    final String user = "root";
                    final String password = "password";
                    String url = "jdbc:mysql://localhost:3306/bookdb";
                    Connection connection = DriverManager.getConnection(url, user, password);
                    String queryTitle = "SELECT * FROM book WHERE PUBLISHER_PublisherID = " + "\"" + searchPublisher + "\";";
                    Statement statementTitle = connection.createStatement();
                    ResultSet result = statementTitle.executeQuery(queryTitle);
                    
                    // Once the Publisher is located, the information of its corresponding books are displayed to the user for confirmation
                    while (result.next())
                    {
                        int order = JOptionPane.showConfirmDialog(null, "This item was retrieved!\n ISBN: " + result.getString("ISBN") + "\n Book Title: " + result.getString("BookTitle")
                                + "\n Price: " + result.getString("BookPrice") + "\n Release Date: " + result.getString("BookReleaseDate") + "\n Publisher ID: "
                                + result.getString("PUBLISHER_PublisherID")+ "\n Author ID: " + result.getString("AUTHOR_AuthorID") + "\n\nAccept Order?");
                        String resultISBN = result.getString("ISBN");
                        String resultTitleCopies = result.getString("BookTitle");
                        // 0 = Yes on Confirmation Panel; 1 = No on Confirmation Panel
                        if (order == 0)
                        {
                            try
                            {
                                // Inputs ISBN of book(s) confirmed by the user into user's shopping cart
                                PreparedStatement addCartBook = connection.prepareStatement("INSERT INTO shoppingcart_book (BOOK_ISBN, SHOPPINGCART_ShoppingCartID, BookCount) "
                                        + "VALUES (" + "\"" + resultISBN + "\"" + ", 1, 1);");
                                addCartBook.executeUpdate();
                                // A message asks the user how many copies of this book they want to order
                                String bookCopies = JOptionPane.showInputDialog(null, "How Many Copies Would You Like to Order?");
                                if (!bookCopies.isEmpty())
                                {
                                    try
                                    {
                                        // The number of book copies given by user are re-inserted into the same row as above via an update
                                        PreparedStatement updateCartBook = connection.prepareStatement("UPDATE shoppingcart_book SET BookCount = " + bookCopies + 
                                                " WHERE BOOK_ISBN = " + "\"" + resultISBN + "\"" + ";");
                                        updateCartBook.executeUpdate();
                                        JOptionPane.showMessageDialog(null, bookCopies + " Copies of " + "\"" + resultTitleCopies + "\"" + " Are Now in Your Shopping Cart!");
                                    } catch (Exception es) {
                                        System.err.println("Exception catched!");
                                        System.err.println(es.getMessage());
                                        JOptionPane.showMessageDialog(null, "Error: Book Copies could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Error! Only 1 Copy Will Be In Your Shopping Cart!");
                                }
                            } catch (Exception es) {
                            System.err.println("Exception catched!");
                            System.err.println(es.getMessage());
                            JOptionPane.showMessageDialog(null, "Error: This item could not be placed!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Order was not placed!");
                        }
                    }
                } catch (Exception es) {
                    System.err.println("Exception catched!");
                    System.err.println(es.getMessage());
                    JOptionPane.showMessageDialog(null, "Order was not placed!", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
            publisherButton.setFont(publisherButton.getFont().deriveFont(Font.BOLD));
        }
        if(e.getSource() == shoppingCartButton)
        {
            isbnButton.setFont(isbnButton.getFont().deriveFont(Font.BOLD));
            titleButton.setFont(titleButton.getFont().deriveFont(Font.BOLD));
            authorButton.setFont(authorButton.getFont().deriveFont(Font.BOLD));
            releaseDateButton.setFont(releaseDateButton.getFont().deriveFont(Font.BOLD));
            publisherButton.setFont(publisherButton.getFont().deriveFont(Font.BOLD));
            shoppingCartButton.setFont(shoppingCartButton.getFont().deriveFont(Font.ITALIC));
            
            try
            {
                // Reveals the table, shoppingcart_book, to the user - this table is full of book orders inputted by the user
                final String user = "root";
                final String password = "password";
                String url = "jdbc:mysql://localhost:3306/bookdb";
                Connection connection = DriverManager.getConnection(url, user, password);
                String queryPrice = "SELECT shoppingcart_book.BOOK_ISBN, shoppingcart_book.BookCount, book.ISBN, book.BookPrice FROM shoppingcart_book "
                        + "INNER JOIN book ON shoppingcart_book.BOOK_ISBN = book.ISBN;";
                Statement bookPrice = connection.createStatement();
                ResultSet resultSetPrice = bookPrice.executeQuery(queryPrice);

                // Calculates the amount of rows to deploy on the table representing the database table selected
                int totalRowsPrice = 0;
                while (resultSetPrice.next())
                {
                    if (resultSetPrice != null)
                    {
                        resultSetPrice.last();
                        totalRowsPrice = resultSetPrice.getRow();
                    }
                }
                // A separate textfield, priceLabel, is created to calculate the total cost of book orders by the user
                int p = 0;
                int totalPrice = 0;
                int totalCount = 0;
                int totalOrderCost = 0;
                resultSetPrice.beforeFirst();
                while (resultSetPrice.next()) 
                {
                        totalPrice = resultSetPrice.getInt("book.BookPrice");
                        totalCount = resultSetPrice.getInt("BookCount");
                        totalOrderCost += totalPrice * totalCount;
                        p++;
                }
                JLabel priceLabel = new JLabel("TOTAL COST: $" + totalOrderCost);
                
                try
                {
                    String querySum = "SELECT * FROM shoppingcart_book;";
                    Statement bookSum = connection.createStatement();
                    ResultSet resultSetTotal = bookSum.executeQuery(querySum);

                    int totalRows = 0;
                    while (resultSetTotal.next())
                    {
                        if (resultSetTotal != null)
                        {
                            resultSetTotal.last();
                            totalRows = resultSetTotal.getRow();
                        }
                    }
                    int totalColumns = resultSetTotal.getMetaData().getColumnCount();
                    Object[][] input = new Object[totalRows][totalColumns];
                    
                    // Inputs the data from database to JTable for the user
                    int n = 0;
                    resultSetTotal.beforeFirst();
                    while (resultSetTotal.next()) 
                    {
                            int x = 0;
                            input[n][x++] = resultSetTotal.getString("BOOK_ISBN");
                            input[n][x++] = resultSetTotal.getString("SHOPPINGCART_ShoppingCartID");
                            input[n][x++] = resultSetTotal.getString("BookCount");
                            n++;
                    }
                    if(n < 1)
                    {
                        JOptionPane.showMessageDialog(null, "No Orders Have Been Filed!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        // Creates a new frame for the user to review their orders before submitting, canceling, or removing their orders
                        JFrame frame1 = new JFrame("Shopping Cart");
                        JPanel cartMenu = new JPanel(new BorderLayout());
                        JPanel tableCart = new JPanel(new BorderLayout());
                        cartMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
                        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        // Adds the buttons to the bottom menu
                        cartMenu.add(submitButton);
                        cartMenu.add(removeButton);
                        cartMenu.add(cancelButton);
                        cartMenu.add(priceLabel);

                        // Defines the layout between the table holding the others and the bottom menu of buttons
                        frame1.add(tableCart, BorderLayout.NORTH);
                        frame1.add(cartMenu, BorderLayout.SOUTH);
                        cartMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
                        
                        // Creates the table holding the user's orders
                        String[] columns = {"ISBN", "Your Cart ID", "Book Count"};
                        JTable table = new JTable(input, columns);
                        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                        table.setFillsViewportHeight(true);
                        JScrollPane scroll = new JScrollPane(table);
                        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                        tableCart.add(table.getTableHeader(), BorderLayout.NORTH);
                        tableCart.add(table, BorderLayout.CENTER);

                        // Defines the frame's visibility and size upon creation
                        frame1.add(scroll);
                        frame1.setVisible(true);
                        frame1.setSize(600, 300);
                        
                        submitButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                removeButton.setFont(submitButton.getFont().deriveFont(Font.BOLD));
                                cancelButton.setFont(cancelButton.getFont().deriveFont(Font.BOLD));
                                submitButton.setFont(submitButton.getFont().deriveFont(Font.ITALIC));
                                int confirm = JOptionPane.showConfirmDialog(null, "Ready to Submit Order?");
                                // 0 = Yes on Confirmation Panel; 1 = No on Confirmation Panel
                                if (confirm == 0)
                                {
                                    try
                                    {
                                        // Drops all data from the shopping cart then closes the shopping cart frame
                                        final String user = "root";
                                        final String password = "password";
                                        String url = "jdbc:mysql://localhost:3306/bookdb";
                                        Connection connection = DriverManager.getConnection(url, user, password);
                                        PreparedStatement dropOrders = connection.prepareStatement("TRUNCATE TABLE shoppingcart_book;");
                                        dropOrders.executeUpdate();
                                        frame1.setVisible(false);
                                        frame1.dispose();
                                        
                                        JOptionPane.showMessageDialog(null, "Your order has been approved!\nThank you for shopping with us! :D");
                                        
                                    } catch (Exception exp) {
                                        System.err.println("Exception catched!");
                                        System.err.println(exp.getMessage());
                                    }
                                }
                                submitButton.setFont(submitButton.getFont().deriveFont(Font.BOLD));
                            }
                        });
                        removeButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                removeButton.setFont(removeButton.getFont().deriveFont(Font.ITALIC));
                                submitButton.setFont(submitButton.getFont().deriveFont(Font.BOLD));
                                cancelButton.setFont(cancelButton.getFont().deriveFont(Font.BOLD));
                                // Asks the user for the ISBN of book they want to remove from their shopping cart
                                String removeOrder = JOptionPane.showInputDialog(null, "Input ISBN to Remove Your Order!");
                                
                                try
                                {
                                    // Deletes the specific row where the inputted ISBN is located
                                    final String user = "root";
                                    final String password = "password";
                                    String url = "jdbc:mysql://localhost:3306/bookdb";
                                    Connection connection = DriverManager.getConnection(url, user, password);
                                    PreparedStatement dropOrders = connection.prepareStatement("DELETE FROM shoppingcart_book WHERE BOOK_ISBN = " + "\"" + removeOrder + "\"" + ";");
                                    dropOrders.executeUpdate();
                                    
                                    // Drops the shopping cart frame and confirms removal
                                    frame1.setVisible(false);
                                    frame1.dispose();
                                    JOptionPane.showMessageDialog(null, "The ISBN: " + removeOrder + " Has Been Removed From Your Shopping Cart!"
                                    + "\nProceed Again to Checkout or Continue Shopping!");
                                    
                                } catch (Exception exp) {
                                    System.err.println("Weird Exception Catched!");
                                    System.err.println(exp.getMessage());
                                }
                                removeButton.setFont(removeButton.getFont().deriveFont(Font.BOLD));
                            }
                        });
                        cancelButton.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent e)
                            {
                                submitButton.setFont(submitButton.getFont().deriveFont(Font.BOLD));
                                removeButton.setFont(cancelButton.getFont().deriveFont(Font.BOLD));
                                cancelButton.setFont(cancelButton.getFont().deriveFont(Font.ITALIC));
                                int cancel = JOptionPane.showConfirmDialog(null, "Your orders will be deleted!\nPlease confirm your deletion!");
                                // 0 = Yes on Confirmation Panel; 1 = No on Confirmation Panel
                                if (cancel == 0)
                                {
                                    try
                                    {
                                        // Drops all data from the shopping cart then closes the shopping cart frame
                                        final String user = "root";
                                        final String password = "password";
                                        String url = "jdbc:mysql://localhost:3306/bookdb";
                                        Connection connection = DriverManager.getConnection(url, user, password);
                                        PreparedStatement dropOrders = connection.prepareStatement("TRUNCATE TABLE shoppingcart_book;");
                                        dropOrders.executeUpdate();
                                        frame1.setVisible(false);
                                        frame1.dispose();
                                        
                                        JOptionPane.showMessageDialog(null, "Your order has been canceled! :(");
                                        
                                    } catch (Exception exp) {
                                        System.err.println("Exception catched!");
                                        System.err.println(exp.getMessage());
                                    }
                                }
                                cancelButton.setFont(cancelButton.getFont().deriveFont(Font.BOLD));
                            }
                        });
                    } 
                } catch (Exception edit) {
                    System.err.println("Exception catched!");
                    System.err.println(edit.getMessage());
                    cancelButton.setFont(cancelButton.getFont().deriveFont(Font.BOLD));
                }
            } catch (Exception cost) {
                System.err.println("Exception catched!");
                System.err.println(cost.getMessage());
                cancelButton.setFont(cancelButton.getFont().deriveFont(Font.BOLD));
            }
        }
    }
    public static void main(String[] args)
    {
        // Creates GUI of main menu
        JFrame frame = new Book_Database();
        frame.setTitle("Yummy Bookstore!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Defines the size and appearance of the main menu
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        frame.setSize(width/2, height/2);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setVisible(true);
    }
}
