package commands;

import core.FtpSession;
import exeptions.NotImplementedFunctionException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;

public class CommandFactory {
    public Collection<String> getCommands() {
        return commands.keySet();
    }

    private Map<String, BiFunction<FtpSession, String[], Command>> commands = new HashMap<>();

    public CommandFactory() {
        commands.put("LIST", (session, args) -> new ListCommandTask(session, args));
        commands.put("FEAT", (session, args) -> new FEATCommand(session, args, this));
        commands.put("PASS", (session, args) -> new PASSCommand(session, args));
        commands.put("PORT", (session, args) -> new PORTCommand(session, args));
        commands.put("PWD", (session, args) -> new PWDCommand(session, args));
        commands.put("SYST", (session, args) -> new SYSTCommand(session, args));
        commands.put("USER", (session, args) -> new USERCommand(session, args));
        commands.put("TYPE", (session, args) -> new TYPECommand(session, args));
//        commands.put("PASV", (session, args) -> new PASVCommand(session, args));
    }

    public Command get(String command, String[] args, FtpSession session) throws NotImplementedFunctionException {
        return ofNullable(commands.get(command))
                .orElseThrow(() -> new NotImplementedFunctionException(command))
                .apply(session, args);
    }

    public enum Singleton {
        instance(new CommandFactory());

        private CommandFactory factory;

        Singleton(CommandFactory factory) {
            this.factory = factory;
        }

        public CommandFactory getFactory() {
            return factory;
        }
    }
}