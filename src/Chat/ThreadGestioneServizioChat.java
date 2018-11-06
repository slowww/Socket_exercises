package Chat;
import java.io.*;
import java.net.*;
import java.util.List;

import javax.swing.JOptionPane;
/////////////////SERVER/////////////////////////////////////
public class ThreadGestioneServizioChat implements Runnable
{
	private int nrMaxConnessioni;//PRIVATE: accessibile e modificabile solo dall'interno di una classe
	private List lista;
	private ThreadChatConnessioni[] listaConnessioni;
	Thread me;
	private ServerSocket serverChat;
	
	public ThreadGestioneServizioChat (int nrMaxConnessioni, List lista)//costruttore
	{
		this.nrMaxConnessioni = nrMaxConnessioni-1;
		this.lista = lista;
		this.listaConnessioni = new ThreadChatConnessioni[this.nrMaxConnessioni];
		me = new Thread(this);
		me.start();//invoca il metodo run
	}
	
	
	
	
	
	
	
	
	
	
	public void run()
	{
		boolean continua = true;
		
		try {
			serverChat = new ServerSocket(6789);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Impossibile istanziare il server");
			continua = false;
		}
		
		if(continua)//se continua=true si crea un thread per ogni connessione avviata
		{	
			try {
				for(int xx=0; xx<nrMaxConnessioni; xx++)
				{
					Socket tempo = null;
					tempo = serverChat.accept();//il server si mette in ascolto sul socket istanziato
					listaConnessioni[xx] = new ThreadChatConnessioni(this,tempo);//l'elenco delle connessioni viene memorizzato nell'array
				}
				serverChat.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Impossibile istanziare server chat!");
			}
			
		}
	}//fine metodo run
	
	
	
	
	
	
	
	
	
	
	//metodo che invia un messaggio a tutti i client
	public void spedisciMessaggio(String mex)
	{
		//scrivo il messaggio nella mia lista
		lista.add(mex);
		lista.select(lista.getItemCount()-1);
		
		//mando il messaggio agli altri
		for(int xx=0; xx<this.nrMaxConnessioni; xx++)
		{
			if(listaConnessioni[xx] != null)
			{
				listaConnessioni[xx].spedisciMessaggioChat(mex);
			}
		}
	}
}// fine ThreadGestioneServizioChat
