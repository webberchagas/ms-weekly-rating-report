package br.com.fiap.report.rating.util;

public class EnvUtil {

    public static String getEnvOrElseThrow(String env) {
        String envValue = System.getenv(env);

        if (envValue == null || envValue.isBlank()) {
            throw new IllegalStateException("Missing env var: MONGODB_URI");
        }

        return envValue;
    }

}
