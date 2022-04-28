package myown.company.javareflection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server{

    private String messageToTranslate;
    private String[] tempTable;
    private String messageToSend;
    private String language;
    private Socket socketSend;
    private Socket socketReceive;
    private ServerSocket serverSocketReceive;
    private static final int PORT_TO_COMMUNICATE_WITH_GUI=5555;
    private DataInputStream dataInputStreamGui;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStreamGui;
    private DataOutputStream dataOutputStream;
    private String messageTranslated;
    private Map mapLanguageToNumbers;

    public Server() {
    }
    public static void main(String[] args) {

        Server server = new Server();
        server.startServer();
    }

   public void startServer(){
      while(true){
              try {

                  if (serverSocketReceive == null) serverSocketReceive = new ServerSocket(PORT_TO_COMMUNICATE_WITH_GUI);
                  language ="en";
                  //Tworzymy mapę połączenia nazwy języka z numerem portu np en = 2222
                  fillMapWithLanguageCode();
               socketReceive = serverSocketReceive.accept();
               dataInputStreamGui = new DataInputStream(socketReceive.getInputStream());
               dataOutputStreamGui = new DataOutputStream(socketReceive.getOutputStream());
               //Tworzymy socket z na danym porcie pobranego z mapy oraz na IP localhost
               socketSend = new Socket("localhost", (Integer) mapLanguageToNumbers.get(language));
               dataOutputStream = new DataOutputStream(socketSend.getOutputStream());
               dataInputStream = new DataInputStream(socketSend.getInputStream());
               messageToTranslate = dataInputStreamGui.readUTF();
               tempTable = messageToTranslate.split(":");
               messageToSend = tempTable[0];
               language = tempTable[1];
               dataOutputStream.writeUTF(messageToSend); // Przesyłamy wiadomość do serwera
               messageTranslated = dataInputStream.readUTF();
               System.out.println("Wiadomość przetłumaczona: " + messageTranslated);
               dataOutputStreamGui.writeUTF(messageTranslated);
           } catch (IOException e) {
               e.printStackTrace();
           } finally {
               stopConnection();
           }
        }
    }
    //Tworzymy mapę połączenia nazwy języka z numerem portu np en = 2222
    private void fillMapWithLanguageCode() {
        mapLanguageToNumbers = new HashMap();
        mapLanguageToNumbers.put("en",2222);
        mapLanguageToNumbers.put("it",3333);
        mapLanguageToNumbers.put("es",4444);
    }
//Zamykamy połączenia
    private void stopConnection() {
        try {
            socketSend.close();
            socketReceive.close();
            dataInputStreamGui.close();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
