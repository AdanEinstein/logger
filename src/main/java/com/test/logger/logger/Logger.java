package com.test.logger.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.springframework.stereotype.Component;

@Component
public class Logger {

    public String getFileAppenderFileName() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        RollingFileAppender appender = context.getConfiguration().getAppender("RollingFileAppender");
        return appender.getFileName();
    }
}
