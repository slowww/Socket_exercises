package NewChat;

public interface MsgListener {
	
	public void onMsg(String daLogin, String contenutoMsg);

	
	//grazie a questa interfaccia, quando un utente invia un messaggio ad un altro cliente (Dario), esso potra sapere
	//da chi l'ha ricevuto ed ovviamente il contenuto del messaggio
}
