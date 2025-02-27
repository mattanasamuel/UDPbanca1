/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancaclient;
import java.net.InetAddress;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.Scanner;
import java.nio.ByteBuffer;

/**
 *
 * @author samue
 */
public class Client {
    private int port;
    private InetAddress ip;
    private DatagramSocket socket; //parametro del socket.send, contiene un array di byte
    
    public Client(InetAddress ip, int port)throws Exception{
        this.ip = ip;
        this.port = port;
        this.socket = new DatagramSocket(port); 
    }
    
    public Client(InetAddress ip)throws Exception{
        this.ip = ip;
        this.socket = new DatagramSocket(); //al client non occorre una porta specifica e ben definita al contrario del server
        this.port = socket.getLocalPort(); //al client non occorre una porta specifica e ben definita al contrario del server
    }    
    
    public Client() throws Exception{
        this(InetAddress.getLocalHost());
    }
    
    public void run(int destinationPort)throws Exception{
        Scanner input = new Scanner(System.in);
        System.out.println("richiesti in tale ordine nome, cognome e numero conto\nfornire i dati separati da virgola -> ,");
        String stringaInvio = input.nextLine();
        String [] split = new String [3];
        split = stringaInvio.split(",");
        
        // stiliamo ora in opportune variabili i dati che andremo ad inviare:
        byte [] nome = split[0].getBytes();
        byte LN = (byte) nome.length;
        
        byte [] cognome = split[1].getBytes();
        byte LC = (byte) cognome.length;
        
        int numeroConto = Integer.parseInt(split[2]);
        
        System.out.println("numero conto intero: " + numeroConto);
        
        //conversione del numero conto in un array di 4 byte
        byte [] num = new byte [4];
        int jump = 0;
        for(int i=0; i<4; i++){
            switch(i){
                case 0:
                    jump = 24;
                    break;
                case 1:
                    jump = 16;
                    break;
                case 2:
                    jump = 8;
                    break;    
                case 3:
                    jump = 0;
                    break;
            }
            num[i] = (byte) ((numeroConto >> jump) & 0xFF); // per riconvertirlo in intero si fa l'operazione opposta
        };
        
        /*
        System.out.println("lunghezza cognome: " + LC + ", lunghezza nome: " + LN +"conversione in byte ottenuta:\nprimo byte: " + num[0] + "\nsecondo byte: " + num[1] + "\nterzo byte: " + num[2] + "\n quarto byte: " + num[3]);
        String nomeTmp = new String(nome);
        String cognomeTmp = new String(cognome);
        
        System.out.println("nome: " + nomeTmp + " cognome: " + cognomeTmp); */
        
        ByteBuffer buffer = ByteBuffer.allocate(2+nome.length+cognome.length+4);
        buffer.put(LC);
        buffer.put(cognome);
        buffer.put(LN);
        buffer.put(nome);
        buffer.put(num);
        byte invia [] = buffer.array();
        
        //ora inviamo i dati ottenuti ed opportunamente processati
        DatagramPacket datagrammaInvio = new DatagramPacket(invia, invia.length, InetAddress.getLocalHost(), destinationPort);
        socket.send(datagrammaInvio);
        //fase di ricezione
        
        
        
        
        
        
        
        
        
        
        byte ricevi [] = new byte [1024];
        DatagramPacket datagrammaRicevuto = new DatagramPacket(ricevi, ricevi.length);
        socket.receive(datagrammaRicevuto);
        String stringaRicevuta = new String(datagrammaRicevuto.getData());
        System.out.println("risposta dal server: " + stringaRicevuta);
    }
    
}
