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
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class problembuttons {
    static String file = "src\\problems.csv";
    
    public static void savetocsvcustomer(int repairmanissuenumber, int issuenumber, String username, String car, String problem, String date, int idreq) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            String row = repairmanissuenumber + "," + issuenumber + "," + username + "," + car + "," + problem + "," + date + "," + idreq;

            writer.write(row);
            writer.newLine();
            writer.close();
            System.out.println("Problems saved to CSV!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
