package fm.pattern.jwt.sdk;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class JwtClientProperties {

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(JwtClientProperties.class.getClassLoader().getResourceAsStream("jwt-client.properties"));
        }
        catch (Exception e) {

        }
    }

    private JwtClientProperties() {

    }

    public static boolean hasEndpoint() {
        return StringUtils.isNotBlank(getEndpoint());
    }

    public static void setEndpoint(String endpoint) {
        properties.setProperty("endpoint", endpoint);
    }

    public static String getEndpoint() {
        return properties.getProperty("endpoint");
    }

}
