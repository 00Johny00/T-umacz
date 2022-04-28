package myown.company.javareflection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Translate {

    private String language;
    private String messageToTranslate;
    private String translatedWord;
    private String[] wordsToTranslate;
    private StringBuilder translatedMessage;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private HashMap dictionaryEN;
    private String line;
    private String fileName;
    private BufferedReader br;
    private Map mapLanguageToNumbers;

    public Translate(){
    }

    public static void main(String[] args) {
        Translate translate = new Translate();
        translate.startClient();
    }

    public void startClient() {
        //Tutaj dajemy możliwość wprowadzania nowych serwerów tłumaczacych
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj dokładną nazwę pliku do tłumaczenia : (PL_EN.csv)");
        fileName = sc.nextLine(); //PL_EN.csv
        //Do nowych serwerów tłumaczących tworzymy plik(hasło,tłumaczenie) jeden do jeden i program wczyta te mapy
        System.out.println("Podaj język który został wczytany (np:en): ");
        language = sc.nextLine();
        //a następnie przyporządkuje skrót do numeru portu pod którym będzie ten serwer np it = 3333
        dictionaryEN = new HashMap<String, String>();
        fillHashMapWithWordsTranslated();
        fillMapWithLanguageCode();
        System.out.println("MAP: ----------------");
        System.out.println(dictionaryEN);
        while (true) {
            try {
                //Do składania wiadomości StringBuildera
                translatedMessage = new StringBuilder();
                //Tworzymy połączenie na porcie z danym językiem
                serverSocket = new ServerSocket((Integer) mapLanguageToNumbers.get(language.toLowerCase(Locale.ROOT)));
                socket = serverSocket.accept();
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                messageToTranslate = dataInputStream.readUTF();
                System.out.println(messageToTranslate);
                wordsToTranslate = messageToTranslate.split(" ");
                for (String wordToTranslate : wordsToTranslate) {
                    translatedWord = (String) dictionaryEN.get(wordToTranslate);
                    System.out.println("TRANSLATED WORD IS :  " + translatedWord);
                    translatedMessage.append(translatedWord + " ");
                    System.out.println("TRANSLATED SENTENCE IS : " + translatedMessage);
                }
                dataOutputStream.writeUTF(String.valueOf(translatedMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                stopConnection();
            }
        }
    }
        private void stopConnection() {
            try {
                socket.close();
                serverSocket.close();
                dataInputStream.close();
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void fillHashMapWithWordsTranslated() {
            try {
                br = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            try {
                while ((line=br.readLine())!=null) {
                    String str[] = line.split(",");
                    dictionaryEN.put(str[0],str[1]);
                }
            } catch (IOException ex) {
                System.out.println(dictionaryEN);
                ex.printStackTrace();
            }
        }

    private void fillMapWithLanguageCode() {
        mapLanguageToNumbers = new HashMap();
        mapLanguageToNumbers.put("en",2222);
        mapLanguageToNumbers.put("it",3333);
        mapLanguageToNumbers.put("es",4444);

    }
    }

