import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class HomePage extends Frame {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Hackathon Contest Base");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create the "File" menu
        JMenu fileMenu = new JMenu("File");

        // Create the "Open" menu item
        JMenuItem openMenuItem = new JMenuItem("Contest");
        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Contest();
            }
        });
        fileMenu.add(openMenuItem);

        // Create the "Save" menu item
        JMenuItem saveMenuItem = new JMenuItem("Technology");
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TechnologyUI();
            }
        });
        fileMenu.add(saveMenuItem);

        // Create a separator
        //fileMenu.addSeparator();

        // Create the "Exit" menu item
        JMenuItem exitMenuItem = new JMenuItem("Organization");
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               new Org();
            }
        });
        fileMenu.add(exitMenuItem);

        // Add the "File" menu to the menu bar
        menuBar.add(fileMenu);

        // Create the "Users" menu
        JMenu usersMenu = new JMenu("Users");

        // Create the "Show Users" menu item
        /*JMenuItem showUsersMenuItem = new JMenuItem("Show Users");
        showUsersMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new  button2();
            }
        });
        usersMenu.add(showUsersMenuItem);

        // Add the "Users" menu to the menu bar
        menuBar.add(usersMenu);

        // Create the "Locations" menu
        JMenu locationsMenu = new JMenu("Locations");

        // Create the "Show Locations" menu item
        JMenuItem showLocationsMenuItem = new JMenuItem("Show Locations");
        showLocationsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 new button1();
            }
        });
        locationsMenu.add(showLocationsMenuItem);

        // Add the "Locations" menu to the menu bar
        menuBar.add(locationsMenu);

        // Create the "Devices" menu
        JMenu devicesMenu = new JMenu("Devices");

        // Create the "Show Devices" menu item
        JMenuItem showDevicesMenuItem = new JMenuItem("Show Devices");
        showDevicesMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new button3();
            }
        });
        devicesMenu.add(showDevicesMenuItem);

        // Add the "Devices" menu to the menu bar
        menuBar.add(devicesMenu);*/

        // Set the menu bar on the frame
        frame.setJMenuBar(menuBar);

        // Set the size and visibility of the frame
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

public HomePage()
{
	 addWindowListener(new WindowAdapter() {
         @SuppressWarnings("deprecation")
         @Override
             public void windowClosing(WindowEvent we) {
                 dispose();
             }
         });
}

}