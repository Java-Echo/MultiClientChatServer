import java.io.*;
import java.net.*;
import java.util.LinkedList;


class Server extends Thread {
    private PrintWriter out;
    public static int count = 0;
    static final int PORT = 9000;
    public static LinkedList<PrintWriter> outlist = new LinkedList<>();
    public void go() throws IOException{
        Server.count = 0;
        ServerSocket serverSocket = new ServerSocket(PORT);
        try{
            while(true){
                Socket socket = serverSocket.accept();
                Server.count += 1;
                System.out.println("Client " + count + " connected");
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println("Client " + count + " connected");
                out.flush();
                outlist.add(out);
                //implements  multi-thread
                MessageTransfer mt = this.new MessageTransfer(socket);
                Thread t = new Thread(mt);
                t.start();
            }
        } finally{
            serverSocket.close();
        }
    }
    public static void main(String[] args) throws IOException {
        
        System.out.println("Server started");
        Server server = new Server();  
        server.go();
    }
    
    public class MessageTransfer implements Runnable {
        String message;
        BufferedReader in;
        Socket s;
        public MessageTransfer(Socket s) throws IOException{
            this.s = s;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        public void run(){
            try{
                while((message = in.readLine()) != null){
                    for(PrintWriter pw : outlist){
                        pw.println(message);
                        pw.flush();
                    }
                    System.out.println("Message received: " + message);
                }
            }catch(IOException e){
                System.out.println("Error handling client");
            }
        }
    }
        
}