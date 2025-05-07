package kz.saya.finals.common.DTOs;

public class RegisterRequestDTO {
    String login;
    String password; // Пока что два поля, потом добавим поля чтобы юзер мог заполнить имя и фамилию и тд

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
