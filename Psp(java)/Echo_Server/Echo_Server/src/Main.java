import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        try(
            ServerSocket server= new ServerSocket(6000);
            Socket service =server.accept();
            DataInputStream socketIn =
                    new DataInputStream(service.getInputStream());
            DataOutputStream socketOut=
                    new DataOutputStream(service.getOutputStream());

        ){
            String message=socketIn.readUTF();
            System.out.println("Message received: "+message);
            socketOut.writeUTF(message.toUpperCase());
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}