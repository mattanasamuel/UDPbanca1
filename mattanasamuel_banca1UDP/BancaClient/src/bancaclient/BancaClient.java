/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package bancaclient;

/**
 *
 * @author samue
 */
public class BancaClient {

    public static void main(String[] args)throws Exception {
        System.out.println("Hello World!");
        Client client = new Client();
        client.run(50000);
    }
}
