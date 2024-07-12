package com.vk.auth.form.user;

public class UserLoginBody {
    private String email;
    private String password;

    private String waitCode;
    private Integer codeOrPas;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWaitCode() {
        return waitCode;
    }

    public void setWaitCode(String waitCode) {
        this.waitCode = waitCode;
    }

    public Integer getCodeOrPas() {
        return codeOrPas;
    }

    public void setCodeOrPas(Integer codeOrPas) {
        this.codeOrPas = codeOrPas;
    }
}
