package NewChat;

public interface UserStatusListener {//notifica lo stato dell'user (se online o offline)
	
	public void online (String login);
	public void offline (String login);

}
