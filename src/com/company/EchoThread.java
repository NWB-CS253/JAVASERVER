package com.company;

import java.io.*;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EchoThread extends Thread {
    protected Socket socket;
    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder out = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                out.append(line + "\n");
            }

            String clientString = out.toString();
            bufferedReader.close();

            String path = System.getProperty("user.home") + File.separator + "Invoices";

            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }

            if (clientString.length() > 0) {
                ZonedDateTime zdt = ZonedDateTime.now();
                String time = zdt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                time = time.replaceAll(":", "-");

                String saveAs = null;

                if (clientString.startsWith("{")) {
                    saveAs = ".json";
                }
                else if (clientString.startsWith("<"))  {
                    saveAs = ".xml";
                }
                else {
                    saveAs = ".txt";
                }

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + File.separator + time + saveAs, true));

                char[] charArray = clientString.toCharArray();
                // for each char in array, append char to file
                for (char c: charArray) {
                    bufferedWriter.append(c);
                    bufferedWriter.flush();
                }

                bufferedWriter.close();

                System.out.println("File saved as: " + time + saveAs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
