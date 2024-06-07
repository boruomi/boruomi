package time;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

public class ChangeSysTime {

    @Test
    public void test3() throws ParseException {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
        Date data = sdfTime.parse("2022-05-22");
        String sysShortTimeFormat = getSysShortTimeFormat();
        SimpleDateFormat sdfDay = new SimpleDateFormat(sysShortTimeFormat);
        String yearMonthDay = sdfDay.format(data);
        System.out.println(yearMonthDay);
    }

    public  String getSysShortTimeFormat(){
        try {
            // 运行 reg query 命令，查询当前用户的短日期格式设置
            Process process = Runtime.getRuntime().exec("reg query \"HKCU\\Control Panel\\International\" /v sShortDate");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String dateFormat = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("sShortDate")) {
                    dateFormat = line.split("\\s+")[line.split("\\s+").length - 1];
                    break;
                }
            }
            reader.close();

            // 输出从注册表中读取到的短日期格式
            if (dateFormat != null) {
                return dateFormat;
            } else {
                System.out.println("无法获取系统默认的短日期格式。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
