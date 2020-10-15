package fm.douban.app.control;

import fm.douban.model.User;
import fm.douban.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserControl {

    @Autowired
    private UserService userService;

    @GetMapping("/sign")
    public String  signPage(Model model) {
        return "sign";
    }

    @PostMapping("/register")
    @ResponseBody
    public Map registerAction(@RequestParam String name, @RequestParam String password, @RequestParam String mobile, HttpServletRequest request, HttpServletResponse response) {
        Map returnMap = new HashMap();
        User user = userService.get(name);
        if (user != null) {
            returnMap.put("message","用户名已存在");
            return  returnMap;
        }
        User user1 = new User();
        user1.setName(name);
        user1.setPassword(password);
        user1.setMobile(mobile);
        userService.add(user1);
        returnMap.put("message","注册成功");
        return  returnMap;
    }

    //登录页面
    @GetMapping("/login")
    public  String loginPage(Model model) {
        return "login";
    }

    //登录操作
    @PostMapping("/authenticate")
    @ResponseBody
    public Map login(@RequestParam String name,@RequestParam String password,HttpServletRequest request,HttpServletResponse response) {
        Map returnData = new HashMap();
        User user = userService.get(name);
        if (user == null) {
            returnData.put("message","用户名不存在");
            return returnData;
        }
        if (user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("userLoginInfo",name);
            returnData.put("message","登录成功");
        }else {
            returnData.put("message","用户名或密码错误");
        }
        return returnData;
    }
}

