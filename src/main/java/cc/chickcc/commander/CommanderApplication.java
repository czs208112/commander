package cc.chickcc.commander;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SpringBootApplication
public class CommanderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommanderApplication.class, args);
    }

    @GetMapping("/dl/{vcode}")
    public String aaa(@PathVariable String vcode) {
        BufferedReader stdout = null;
        BufferedReader stderr = null;
        String title = getMD5(vcode) + ".mp4";
        try {
            Process process = Runtime.getRuntime().exec("youtube-dl -f bestvideo[ext=mp4]+bestaudio[ext=m4a]/bestvideo+bestaudio"
                    + " --merge-output-format mp4 https://www.youtube.com/watch?v=" + vcode
//                    + " -o /usr/share/nginx/html/%(title)s");
                    + " -o /usr/share/nginx/html/" + title);
            stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            String line1;
            String result = "";
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
            result = "http://chickcc.cc/dl/" + title;
            while ((line1 = stderr.readLine()) != null) {
                System.out.println("ERROR:" + line1);
                result = "下载失败!";
            }
            process.waitFor(); // 等待程序运行结束
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "下载失败";
        } finally {
            try {
                stdout.close();
                stderr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMD5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(string.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int sign = b & 0xff;
                String str = Integer.toHexString(sign);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
