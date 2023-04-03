import java.net.*;
import java.io.*;
import java.util.*;

public class TCPClient {public static void main(String[] args) {
        Socket s = null;
        BufferedReader in = null;
        DataOutputStream dout = null;
        String reply = " " ;
        Boolean flag = true;
        String largestType = null;
        int largestCore = 0;
        int serverCount = 0;
        int sendingTo = 0;
        int numOfServerTypes = 0;      

        try {
            // Connecting to server
            s = new Socket("127.0.0.1", 50000);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            dout = new DataOutputStream(s.getOutputStream());
   
dout.write(("HELO\n").getBytes());
dout.flush();
System.out.println("SENT: HELO");

String str = (String)in.readLine();
System.out.println("RCVD: "+str);

dout.write(("AUTH 123\n").getBytes());
dout.flush();
System.out.println("SENT: AUTH");
reply = in.readLine();
                System.out.println("RCVD: "+ reply);

dout.write("REDY\n".getBytes() );
                System.out.println("SENT; REDY");
                dout.flush();
                reply = in.readLine();
                System.out.println("RCVD: "+ reply);
                                       
            // Loop through jobs
            while (!reply.equals("NONE")) {
                if (reply.equals("JCPL")) {
                    dout.write("REDY\n".getBytes() );
                    System.out.println("SENT; REDY");
                 
                    dout.flush();
                    reply = in.readLine();
                    System.out.println("RCVD: "+ reply);
                    continue;
                   
                } else { // JOBN message
                    if (flag) {
                        dout.write("GETS ALL\n".getBytes());
                        dout.flush();
                        System.out.println("SENT; GETS ALL");
                        reply = in.readLine();
                   
    System.out.println("RCVD: "+ reply);
                        // Parsing DATA message to get server information
                        String[] data = reply.split(" ");
                        int numOfServers = Integer.parseInt(data[1]);
                        numOfServerTypes = Integer.parseInt(data[1]);  
    dout.flush();
                            System.out.println("RCVD: "+ reply);
                   
                        // Loop through servers to find largest type and core count //incomplete
                        for (int i = 0; i < numOfServerTypes; i++) {
                           
                        }
                        dout.write("OK\n".getBytes());
                        System.out.println("SENT; OK");

                        dout.flush();
                        reply = in.readLine();
                        System.out.println(reply);
                    }
                    flag = false;
   
                    // Schedule job using largest type
                    String[] jobInfo = reply.split(" ");
                    int jobID = Integer.parseInt(jobInfo[2]);
                    String schd = "SCHD " + jobID + " " + largestType + " " + sendingTo+ "\n";
                    dout.write(schd.getBytes());
                    dout.flush();
                    System.out.println(schd);
                    reply = in.readLine();
                    System.out.println(reply);
                    sendingTo++;
                    sendingTo = sendingTo %numOfServerTypes; //Round Robin
                    if (largestType.equals(jobInfo[0])) {
                        serverCount++;
                    }
                }
                dout.write("REDY\n".getBytes() );
                System.out.println("SENT; REDY");
                dout.flush();
                reply = in.readLine();
            }
   
            // Send quit message
            dout.write("QUIT".getBytes());
            reply = in.readLine();
            dout.flush();
            System.out.println(reply);

   
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                dout.close();
                s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}}
