package fm.douban.app.control;

import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SingerControl {

    @Autowired
    private SingerService singerService;

    @GetMapping("/user-guide")
    public  String myMhz(Model model) {
        List<Singer> singers = randomSingers();
        model.addAttribute("singers",singers);
        return "userguide.html";
    }

    //局部刷新
    @RequestMapping("/singer-info")
    public  String info(Model model) {
        List<Singer> singers = randomSingers();
        model.addAttribute("singers",singers);
        return "userguide::singer-info";
    }

    /*
     * 随机获取10位歌手
     */
    @GetMapping("/singer/random")
    @ResponseBody
    public List<Singer> randomSingers(){
        List<Singer> singerList = singerService.getAll();
        List<Singer> singers = new ArrayList<>();
        int[] arr = new int[10];
        int i=0;
        while (i<10){
            int random = (int) Math.floor(Math.random()*singerList.size()+1);
            boolean flag = true;
            for(int j =0;j<arr.length;j++){
                if (arr[j]==random){
                    flag=false;
                    break;
                }
            }
            if (flag){
                arr[i] = random;
                singers.add(singerList.get(random));
                i++;
            }
        }
        return singers;
    }
}
