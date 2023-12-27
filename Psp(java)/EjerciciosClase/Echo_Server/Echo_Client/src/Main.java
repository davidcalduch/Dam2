import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try(
                Socket mySocket= new Socket("localhost",6000);
                DataInputStream socketIn=
                        new DataInputStream(mySocket.getInputStream());
                DataOutputStream socketOut=
                        new DataOutputStream(mySocket.getOutputStream())
                ){
            socketOut.writeUTF("hola");
            String response=socketIn.readUTF();
            System.out.println("Response: "+response);
    }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
}