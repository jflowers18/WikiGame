import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.*;


public class WikiGame implements ActionListener {
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JPanel inputPanel;
    private JTextArea taLinks;
    private JTextArea taSearchItems;
    private JScrollPane scrollLinks;
    private JScrollPane scrollSearch;
    private int WIDTH = 700;
    private int HEIGHT = 600;


    //my controls and labels
    private JLabel URLLabel;
    private JLabel searchLabel;
    private JTextField starttext;
    private JTextField findtext;

    //VARIABLES FROM WIKIGAME FILE
    private int maxDepth = 1;
    private String searchperson;
    //private String tracksteps;
    private boolean finished = false;

    public WikiGame() {
        prepareGUI();
    }

    public static void main(String[] args) {
        WikiGame Wiki = new WikiGame();
        Wiki.showEventDemo();
    }


    private void prepareGUI() {
        mainFrame = new JFrame("Jack Flowers Wiki Game");
        mainFrame.setSize(WIDTH, HEIGHT);
        //mainFrame.setLayout(new GridLayout(2,2));
        //see what the flow layout does
        //mainFrame.setLayout(new FlowLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(8, 8));


        //TEXT ENTRY BOXES FOR THE URL AND TEXT TO SEARCH FOR
        //Make a text box to get the Search Word
        //Make the label that says "Search for"
        searchLabel = new JLabel("Start:", JLabel.CENTER);
        //Make a text box to let the user type the search word into it
        starttext = new JTextField("Brad_Pitt");
        //set the size of the search box to be 100 wide and 30 high
        starttext.setPreferredSize(new Dimension(200, 25));
        inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(searchLabel);
        inputPanel.add(starttext);

        //make a text box to get the URL
        //Make the label that says "URL"
        URLLabel = new JLabel("Find:", JLabel.CENTER);
        //Make a text box to let the user type the search word into it
        findtext = new JTextField("Nancy_Reagan");
        //set the size of the search box to be 100 wide and 30 high
        findtext.setPreferredSize(new Dimension(200, 25));
        //add both of them to the screen, hopefully the position is right
        inputPanel.add(URLLabel);
        inputPanel.add(findtext);

        JButton okButton = new JButton("Search");
        okButton.setActionCommand("search");
        okButton.addActionListener(new ButtonClickListener());
        inputPanel.add(okButton);

        //Make two Textareas one for all the links and one for the links that have
        // the search word in them
        taLinks = new JTextArea();
        taLinks.setBounds(50, 5, WIDTH - 100, HEIGHT - 500);
        taLinks.setPreferredSize(new Dimension(400, 450));
        taLinks.setLineWrap(true);
        scrollLinks = new JScrollPane(taLinks);
        scrollLinks.setPreferredSize(new Dimension(400, 450));
        scrollLinks.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

 /*taSearchItems = new JTextArea();
 taSearchItems.setBounds(50, 5, WIDTH - 100, HEIGHT - 500);
 taSearchItems.setPreferredSize(new Dimension(300, 450));
 taSearchItems.setLineWrap(true);
 scrollSearch = new JScrollPane(taSearchItems);
 scrollSearch.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);*/
        controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(scrollLinks);
        // controlPanel.add(scrollSearch);

        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(350, 26);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }


    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            //if they click readmilton them it calls the htmlread class
            if (command.equals("search")) {

                String start = starttext.getText();
                String startLink = "https://en.wikipedia.org/wiki/" + start; // beginning link, where the program will start
                searchperson = "/wiki/" + findtext.getText(); // ending link, where the program is trying to get to
                maxDepth = 2; // start this at 1 or 2, and if you get it going fast, increase
                int depth = 0;
                finished = false;
                System.out.println(startLink);
                System.out.println(searchperson);
                taLinks.setText("");

                statusLabel.setText("Searching");
                //clear the path

                HtmlRead(startLink, depth, "/wiki/" + start);
            }
        }
    }
    private void showEventDemo() {
        mainFrame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
 /*if (e.getSource() == cut)
 ta.cut();
 if (e.getSource() == paste)
 ta.paste();
 if (e.getSource() == copy)
 ta.copy();
 if (e.getSource() == selectAll)
 ta.selectAll();*/
    }

    public boolean recurseLink(String startLink, int depth, String tracksteps) {

        System.out.println("Depth: " + depth + ", link is: " + startLink);

        //NEED TO ADD RULES AND RECURSION
        if (startLink.contains(searchperson)) {
            finished = true;
            System.out.println("I Found: " + searchperson);
            taLinks.append(tracksteps);
            return true;
        } else if (depth > maxDepth) {
            return false;
        } else {
            HtmlRead("https://en.wikipedia.org" + startLink, depth + 1, tracksteps);
            return false;
        }
    }

    private void HtmlRead(String startlink, int depth, String tracksteps) {

        try {
            System.out.print("In HTMLRead\n");
            //check how deep I am
            if (depth > maxDepth)
                return;

            //get the string from the JTextfield URLtext
            URL url = new URL(startlink);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String line;

            //variables to hold info for each link
            int startoflink = 0;
            int endoflink = 0;
            String link = "";

            //loop over all the lines
            while ((line = reader.readLine()) != null && !finished) {
                //System.out.println(line);

                while (line.contains("/wiki/")) {

                    //Chop the link out of the whole line.
                    startoflink = line.indexOf("/wiki/");

                    //find the end of the link. the end of each link string by looking for "
                    //I have to use the \ to search for a "
                    endoflink = line.indexOf("\"", startoflink);
                    //sometimes runs into links that don't end in "
                    if (endoflink > -1) {

                        //pull out the link.
                        link = line.substring(startoflink, endoflink);

                        //cut off the part of the line I already read so I can search the rest
                        line = line.substring(endoflink);

                        //Stick the link on the list after filtering out some of them
                        if (link.contains("%") == false && link.contains(":") == false && link.contains("(") == false
                                && startlink.contains(link) == false && link.contains("Help") == false) {
                            recurseLink(link, depth, tracksteps + "\nDepth:[" + depth + "] " + link);
                            if (finished)
                                return;
                        }
                    } else {
                        //move past the bad link
                        line = line.substring(startoflink + 6);
                    }
                }
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}


/*
Basic stuff you need
HTML reading
String parsing
Recursion
Program to successfully find and output a path between actors
Extra things to boost your grade from a B+ to something higher
UI (user interface) (similar to the one from the link puller assignment)
Shortest path (program not only gives a path, but it gives the best path) (you may want to read about depth search vs breadth search, though there are also other ways to do it)
Efficiency (generallly, the faster the program runs the better)
Expand beyond actors (any page to any page) (do not attempt until you have achieved high efficiency)
 */