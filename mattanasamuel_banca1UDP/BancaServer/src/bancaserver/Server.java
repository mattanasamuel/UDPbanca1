/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancaserver;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.util.Arrays;

/**
 *
 * @author samue
 */
public class Server {

    private int port;
    private InetAddress ip;
    private DatagramSocket socket; //parametro del socket.send, contiene un array di byte
    private InetAddress senderIp;
    private int senderPort;
    
    public Server(InetAddress ip, int port)throws Exception{
        this.ip = ip;
        this.socket = new DatagramSocket(port, ip);
        this.port = this.socket.getPort();
    }
     
    public Server() throws Exception{
        this(InetAddress.getLocalHost(), 50000);
    }
    
    public void run() throws Exception {
        byte ricevi [] = new byte [1024];
        DatagramPacket datagrammaRicevuto = new DatagramPacket(ricevi, ricevi.length);
        socket.receive(datagrammaRicevuto);
        this.senderPort = datagrammaRicevuto.getPort(); //ottengo le info del mittente per poter rispondere
        this.senderIp = datagrammaRicevuto.getAddress();
        
        //estraggo le informazioni dall'array di bytes ricevuto e le processo
        int pos = 0;
        int LC = ricevi[pos];
        System.out.println("lunghezza cognome: " + LC);
        pos +=1;
        String cognome = new String(Arrays.copyOfRange(ricevi, pos, pos+=LC)); //chiede array di partenza, indice di partenza e indice subito immediato a quello di arrivo
        System.out.println("cognome: " + cognome);
        //pos+=1;
        int LN = ricevi[pos];
        System.out.println("lunghezza nome: " + LN);
        pos+=1;
        String nome = new String(Arrays.copyOfRange(ricevi, pos, pos+=LN));
        System.out.println("nome: " + nome);
        byte num [] = Arrays.copyOfRange(ricevi, pos, pos+4);
        int numeroConto = ((num[0] & 0xFF) << 24) | ((num[1] & 0xFF) << 16) | ((num[2] & 0xFF) << 8) | ((num[3] & 0xFF) << 0); //and e or bitwise per convertire da byte a intero
        
        System.out.println("numero conto: " + numeroConto);
        
        
        
        
        
        
        //formulo una risposta:
        byte invia [] = "messaggio ricevuto".getBytes();
        DatagramPacket datagramInviato = new DatagramPacket(invia, invia.length, this.getSenderIp(), this.getSenderPort());
        socket.send(datagramInviato);
        System.out.println("datagramma in risposta inviato, chiusura della connessione");
        socket.close();
    }

    public int getPort() {
        return port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public InetAddress getSenderIp() {
        return senderIp;
    }

    public int getSenderPort() {
        return senderPort;
    }
    
    
   
}
