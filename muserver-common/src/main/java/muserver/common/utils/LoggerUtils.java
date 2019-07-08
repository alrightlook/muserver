package muserver.common.utils;

import muserver.common.logging.LoggingLevel;
import muserver.common.types.AppenderType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

public class LoggerUtils {
    public static void updateLoggerConfiguration(String appenderName, AppenderType appenderType, String appenderFormat, LoggingLevel loggingLevel) {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        Level level = Level.getLevel(loggingLevel.loggingLevel());
        builder.setStatusLevel(level);
        AppenderComponentBuilder stdoutAppenderComponentBuilder = builder.newAppender(appenderName, appenderType.getName()).add(builder.newLayout("PatternLayout").addAttribute("pattern", appenderFormat));
        builder.add(stdoutAppenderComponentBuilder);
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(level);
        rootLogger.add(builder.newAppenderRef(appenderName));
        builder.add(rootLogger);
        BuiltConfiguration configuration = builder.build();
        LoggerContext ctx = Configurator.initialize(configuration);
        ctx.updateLoggers(configuration);
        ctx.start(configuration);
    }
}
