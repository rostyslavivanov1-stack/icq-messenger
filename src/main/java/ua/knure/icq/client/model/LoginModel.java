package ua.knure.icq.client.model;

public class LoginModel {
    private String serverIp;
    private String username;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isValid() {
        return serverIp != null && !serverIp.isBlank()
                && username != null && !username.isBlank();
    }
}