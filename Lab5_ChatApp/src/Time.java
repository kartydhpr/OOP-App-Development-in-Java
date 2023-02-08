import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class Time {
    public String getTime() throws ParseException {
        String time = null;
        String formattedTime = null;
        System.out.println("Getting time...");
        try {
            Socket s = new Socket("time-A.timefreq.bldrdoc.gov", 13);
            try {
                InputStream inStream = s.getInputStream();
                Scanner in = new Scanner(inStream);
                while (in.hasNextLine()) {
                    time = in.nextLine();
                }
            } finally {
                s.close();
            }
        } catch (IOException ioexc) {
            ioexc.printStackTrace();
        }

        try{
            String dateFromServer = time.substring(6,23);
            SimpleDateFormat preFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            preFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = preFormat.parse(dateFromServer);
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mmaa");
            formatTime.setTimeZone(TimeZone.getTimeZone("EST"));
            formattedTime = formatTime.format(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }
}
