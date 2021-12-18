import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client extends Thread{
    private Socket socket;
    private PrintWriter out;
    public static int flag = 0;
    private BufferedReader in;
    static final int PORT = 9000;
    private int id;

    public Client() throws IOException{
        try{
            socket = new Socket("localhost", PORT);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(socket);
        } catch (IOException e){
            if(socket != null)
                socket.close();
        }
    }

    public void go() throws IOException {
        try {
            while(true){
                // block
                String message;
                Scanner in = new Scanner(System.in);
                message = in.nextLine();
                if(message != null){
                   try{
                        out.println("Client " + id + ":" + message);
                        out.flush();
                   }catch(Exception e){
                   }
                }
            }

        }catch (Exception e) {
            if (socket != null)
                socket.close();
        }
    }
    public static void main(String[] args)throws IOException{
        Client client = new Client();
        client.go();
        MessagePrint mp = client.new MessagePrint();
        Thread t = new Thread(mp);
        t.start();

    }
    public class MessagePrint implements Runnable{
        String message;
        public void run(){
            try {
                while((message = in.readLine()) != null){
                    if (flag == 0){
                        String[] strs = message.split(" ");
                        id = Integer.parseInt(strs[1]);
                        flag++;
                    }
                    System.out.println(message+'\n');
                }
            } catch ( Exception e){
            }
        }
    }
}
