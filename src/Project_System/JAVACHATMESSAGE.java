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
public class JAVACHATMESSAGE {
    static String file = "src\\MESSAGES.csv";

    public static void appendToCSV(String message, String sender) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(sender + "," + message);
            writer.newLine();
            writer.close();
            System.out.println("Message saved to CSV!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public static String readAll() {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",", 2); // limit 2 to keep message intact
                if (row.length == 2) {
                    sb.append(row[0]).append(": ").append(row[1]).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                Logger.getLogger(JAVACHATMESSAGE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        BufferedReader reader = null;
        String line = "";
        
        try{
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine())!= null){
                String[]row = line.split(",");  
                for (int i = 0; i < row.length; i++){
                    System.out.printf("%-10s", row[i]);
                }
                System.out.println();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(JAVACHATMESSAGE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

