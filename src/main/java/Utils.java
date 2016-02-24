import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String[] WIN_RUNTIME = {"cmd.exe", "/C"};
    private static final String[] OS_LINUX_RUNTIME = {"/bin/bash", "-l", "-c"};

    Utils() {
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static List<String> runProcess(boolean isWin, String... command) {
        String[] allCommand;
        try {
            if (isWin) {
                allCommand = concat(WIN_RUNTIME, command);
            } else {
                allCommand = concat(OS_LINUX_RUNTIME, command);
            }
            ProcessBuilder pb = new ProcessBuilder(allCommand);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String _temp;
            List<String> line = new ArrayList<>();
            while ((_temp = in.readLine()) != null) {
                line.add(_temp);
            }
            return line;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] getDeviceSize() {
        List<String> dumpsysWindow = runProcess(isWin(), "adb shell dumpsys window | grep \"mUnrestrictedScreen\" ");
        if (dumpsysWindow == null) {
            throw new AssertionError();
        }
        String screenSize = dumpsysWindow.toString().replace("[", "").replace("]", "").replaceAll(".* ", "");
        Pattern getScreenResolution = Pattern.compile("(\\d+)x(\\d+)");
        Matcher matcher = getScreenResolution.matcher(screenSize);
        // Voodoo, will override matches without it. 
        matcher.matches();

        int width = Integer.parseInt(matcher.group(1));
        int height = Integer.parseInt(matcher.group(2));

        return new int[]{width, height};
    }

    public static String getDeviceName() {
        List<String> deviceOutput = runProcess(isWin(), "adb shell getprop ro.product.model");
        if (deviceOutput == null) {
            throw new AssertionError();
        }

        return deviceOutput.toString().replace("[", "").replace("]", "");
    }

    public static boolean isWin() {
        return SystemUtils.IS_OS_WINDOWS;
    }


    public static int[] suggestPlusButtonCords(int[] screenRes) {
        // Delta of the plus button position based on screenSize
        double x = screenRes[0] * 0.07;
        double y = screenRes[1] * 0.9;

        int intX = (int) x;
        int intY = (int) y;

        return new int[]{intX, intY};
    }

    public static int[] suggestWatchButtonCords(int[] screenRes) {
        // Delta of the watch button position based on screenSize
        double x = screenRes[0] * 0.75;
        double y = screenRes[1] * 0.85;

        int intX = (int) x;
        int intY = (int) y;

        return new int[]{intX, intY};
    }

}
