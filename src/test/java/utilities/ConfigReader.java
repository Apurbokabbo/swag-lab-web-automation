package utilities;
import io.github.cdimascio.dotenv.Dotenv;

public class ConfigReader {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()      // Won't fail if .env file is absent
            .ignoreIfMalformed()    // Won't fail on malformed lines
            .load();

    /**
     * Priority: System Property (-DKEY=...) > OS Env Var > .env file > default
     */
    public static String get(String key, String defaultValue) {
        // 1. Command line system property (-DMODE=headless)
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isEmpty()) {
            return sysProp;
        }

        // 2. OS environment variable
        String envVar = System.getenv(key);
        if (envVar != null && !envVar.isEmpty()) {
            return envVar;
        }

        // 3. .env file
        String dotenvValue = dotenv.get(key);
        if (dotenvValue != null && !dotenvValue.isEmpty()) {
            return dotenvValue;
        }

        // 4. Default fallback
        return defaultValue;
    }

}
