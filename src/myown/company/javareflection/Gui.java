package myown.company.javareflection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;

public class Gui extends JFrame{

    private JFrame jFrame;
    private JLabel jLabelTranslateContent;
    private JTextField jTextMessageToTranslate;
    private JLabel jLabelLanguage;
    private JTextField jTextLanguage;
    private JButton jButtonTranslate;
    private JTextArea jTextAreaTranslatedSentence;
    private Socket socket;
    private static final int PORT_TO_COMMUNICATE_WITH_GUI=5555;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String translatedMessage;

    public Gui() {
    }

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.initComponents();
    }

    public void initComponents(){
       
        //Tworzymy nasze gui w tym wymiary widoczność siatkę ułożenia oraz opisy
        jFrame = new JFrame("Translator");
        jFrame.setBounds(300, 300, 800, 200);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(3);
        jFrame.setLayout(new GridLayout(3,2));
        jLabelTranslateContent = new JLabel("Podaj zdanie do przetłumaczenia");
        jTextMessageToTranslate = new JTextField();
        jLabelLanguage = new JLabel("Podaj skrót języka na który chcesz przetłumaczyć (np\"EN\")");
        jTextLanguage = new JTextField();
        jButtonTranslate = new JButton("TŁUMACZ");
        jTextAreaTranslatedSentence = new JTextArea();
        // Dodajemy wszystkie elementy do naszej ramki
        jFrame.add(jLabelTranslateContent);
        jFrame.add(jTextMessageToTranslate);
        jFrame.add(jLabelLanguage);
        jFrame.add(jTextLanguage);
        jFrame.add(jButtonTranslate);
        jFrame.add(jTextAreaTranslatedSentence);
        SwingUtilities.updateComponentTreeUI(jFrame); //Odświeżamy
        //Dodajemy reakcję na przycisk
        jButtonTranslate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSocketConnection();
            }

            private void runSocketConnection() {
                try {
                    socket = new Socket("localhost",PORT_TO_COMMUNICATE_WITH_GUI);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(jTextMessageToTranslate.getText().toLowerCase(Locale.ROOT) + ":" +
                            jTextLanguage.getText().toLowerCase(Locale.ROOT));
                    translatedMessage = dataInputStream.readUTF();
                    jTextAreaTranslatedSentence.setText(translatedMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                        dataOutputStream.close();
                        dataInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
