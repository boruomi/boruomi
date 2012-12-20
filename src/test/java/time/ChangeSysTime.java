package time;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

public class ChangeSysTime {

    @Test
    public void test1() throws ParseException {
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
        Date data = sdfTime.parse("2012-12-21");
        setDateTimeBat(data);
    }

    /**
     * 获取win短时间格式
     * @return
     */
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

    /**
     * 调用bat修改本地时间
     * @param date
     */
    public void setDateTimeBat(Date date) {
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
//        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy/MM/dd");
        String sysShortTimeFormat = getSysShortTimeFormat();
        if (sysShortTimeFormat == null) {
            return;
        }
        SimpleDateFormat sdfDay = new SimpleDateFormat(sysShortTimeFormat);
        String time = sdfTime.format(date);
        String yearMonthDay = sdfDay.format(date);
        try {

            File temDir = new File("temp");
            String filePath = "setDateTime.bat";
            File batFile = new File(temDir.getPath() + "/" + filePath);

            if (!temDir.exists()) {
                temDir.mkdir();
                batFile.createNewFile();
            }

            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@echo off\n");
            bw.write("%1 mshta vbscript:CreateObject(\"Shell.Application\").ShellExecute(\"cmd.exe\",\"/c %~s0 ::\",\"\",\"runas\",1)(window.close)&&exit\n");
            bw.write("time "+time);
            bw.newLine();
            bw.write("date "+yearMonthDay);
            //bw.write("date 2023/10/1");
            bw.close();
            fw.close();
            Process process = Runtime.getRuntime().exec(filePath);
            process.waitFor();
            //等上面的执行完毕后再删除文件
            batFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
