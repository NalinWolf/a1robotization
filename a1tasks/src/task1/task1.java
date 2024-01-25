package task1;

public class task1 {

    public static long ipToLong(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        //int result = 0;
        long result = 0;

        for (int i = 0; i < 4; i++) {
            //int octetValue = Integer.parseInt((octets[i]));
            long octetValue = Long.parseLong(octets[i]);
            result = (result << 8) | octetValue;
        }

        return result;
    }

    public static String longToIp(long ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xFF,
                (ip >> 16) & 0xFF,
                (ip >> 8) & 0xFF,
                ip & 0xFF);
    }

    public static void main(String[] args) {
        // Примеры использования
        long ipLong = ipToLong("128.32.10.0");
        System.out.println(ipLong);  // Выведет: 2149583360

        String ipString = longToIp(255);
        System.out.println(ipString);  // Выведет: "0.0.0.255"
    }
}
