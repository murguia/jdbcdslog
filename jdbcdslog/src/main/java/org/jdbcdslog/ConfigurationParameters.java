package org.jdbcdslog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationParameters {

    static Logger logger = LoggerFactory.getLogger(ConfigurationParameters.class);

    static long slowQueryThreshold = Long.MAX_VALUE;
    static boolean logText = false;

    static Boolean showTime = false;
    static boolean printStackTrace = false;
    static RdbmsSpecifics rdbmsSpecifics;

    static {

        ClassLoader loader = ConfigurationParameters.class.getClassLoader();
        InputStream in = null;
        try {
            in = loader.getResourceAsStream("jdbcdslog.properties");
            Properties props = new Properties(System.getProperties());
            if (in != null)
                props.load(in);

            String sSlowQueryThreshold = props.getProperty("jdbcdslog.slowQueryThreshold");
            if (sSlowQueryThreshold != null && isLong(sSlowQueryThreshold))
                slowQueryThreshold = Long.parseLong(sSlowQueryThreshold);
            if (slowQueryThreshold == -1)
                slowQueryThreshold = Long.MAX_VALUE;

            String sLogText = props.getProperty("jdbcdslog.logText", "false");
            if ("true".equalsIgnoreCase(sLogText))
                logText = true;

            String sprintStackTrace = props.getProperty("jdbcdslog.printStackTrace", "false");
            if ("true".equalsIgnoreCase(sprintStackTrace))
                printStackTrace = true;

            String isShowTime = props.getProperty("jdbcdslog.showTime", "false");
            if ("true".equalsIgnoreCase(isShowTime)) {
                showTime = true;
            }

            String driverName = props.getProperty("jdbcdslog.driverName");
            if ("oracle".equalsIgnoreCase(driverName)) {
                rdbmsSpecifics = new OracleRdbmsSpecifics();
            } else if ("mysql".equalsIgnoreCase(driverName)) {
                rdbmsSpecifics = new MySqlRdbmsSpecifics();
            } else if ("sqlserver".equalsIgnoreCase(driverName)) {
                rdbmsSpecifics = new SqlServerRdbmsSpecifics();
            } else {
                rdbmsSpecifics = new RdbmsSpecifics();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }
    }

    public static void setLogText(boolean alogText) {
        logText = alogText;
    }

    private static boolean isLong(String sSlowQueryThreshold) {
        try {
            Long.parseLong(sSlowQueryThreshold);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
