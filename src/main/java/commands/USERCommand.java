package commands;

import core.FtpSession;

import java.io.IOException;

public class USERCommand implements Command {
    private final FtpSession session;
    private final String[] args;

    public USERCommand(FtpSession session, String[] args) {

        this.session = session;
        this.args = args;
    }

    @Override
    public void execute() throws IOException {
        session.getControlConnection().write("331 \n");
    }

}