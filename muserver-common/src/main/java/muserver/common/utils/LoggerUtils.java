package muserver.common.utils;

import muserver.common.configs.AppenderConfigs;
import muserver.common.configs.CommonConfigs;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.ArrayList;
import java.util.List;

public class LoggerUtils {
    public static void updateLoggerConfiguration(CommonConfigs commonConfigs) throws Exception {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

        Level level = Level.getLevel(commonConfigs.logging().level().loggingLevel());

        builder.setStatusLevel(level);
        builder.setConfigurationName("muserver.cs.ConnectServer");

        List<AppenderRefComponentBuilder> appenderRefComponentBuilderList = new ArrayList<>();

        for (AppenderConfigs appender : commonConfigs.logging().appenders()) {
            switch (appender.type()) {
                case FILE: {
                    AppenderComponentBuilder appenderComponentBuilder = builder.newAppender(appender.name(), appender.type().getName());

                    appenderComponentBuilder.addAttribute("fileName", appender.fileName());

                    appenderComponentBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", appender.format()));

                    builder.add(appenderComponentBuilder);

                    appenderRefComponentBuilderList.add(builder.newAppenderRef(appender.name()));
                }
                break;

                case CONSOLE: {
                    AppenderComponentBuilder appenderComponentBuilder = builder.newAppender(appender.name(), appender.type().getName());

                    appenderComponentBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", appender.format()));

                    builder.add(appenderComponentBuilder);

                    appenderRefComponentBuilderList.add(builder.newAppenderRef(appender.name()));
                }
                break;

                case ROLLING_FILE: {
                    AppenderComponentBuilder appenderComponentBuilder = builder.newAppender(appender.name(), appender.type().getName());

                    appenderComponentBuilder.addAttribute("fileName", appender.fileName());
                    appenderComponentBuilder.addAttribute("filePattern", appender.filePattern());

                    appenderComponentBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", appender.format()));

                    ComponentBuilder triggeringPolicy = null;

                    if (appender.sizeBasedTriggeringPolicy() != null) {
                        triggeringPolicy = builder.newComponent("Policies").addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", appender.sizeBasedTriggeringPolicy().size()));
                        appenderComponentBuilder.addComponent(triggeringPolicy);
                    }

                    if (triggeringPolicy == null) {
                        throw new Exception("Triggering policy is null or not valid");
                    }

                    builder.add(appenderComponentBuilder);

                    appenderRefComponentBuilderList.add(builder.newAppenderRef(appender.name()));
                }
                break;

                default:
                    throw new UnsupportedOperationException(String.format("Unsupported appender type: %s", appender.type().getName()));
            }
        }

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(level);

        for (AppenderRefComponentBuilder appenderRefComponentBuilder : appenderRefComponentBuilderList) {
            rootLogger.add(appenderRefComponentBuilder);
        }

        builder.add(rootLogger);

        BuiltConfiguration configuration = builder.build();

        LoggerContext ctx = Configurator.initialize(configuration);

        ctx.updateLoggers(configuration);

        ctx.start(configuration);
    }
}
