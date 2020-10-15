package fm.douban.model;

import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotEmpty;

public class User {

    @Id
    @NotEmpty(message = "用户名不能为空")
    private String name;
    @NotEmpty(message = "用户密码不能为空")
    private String password;
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
