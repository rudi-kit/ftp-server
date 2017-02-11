package commands;

import core.FtpSession;
import exceptions.FTPError550Exception;
import exceptions.FtpErrorReplyException;
import exceptions.NoSuchMessageException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;

import static utils.MessageFactory.getMessage;

/**
 * Created by rodya on 4.2.17.
 * This class is an implementation of {@link Command}interface.
 * Allows to load files from server machine to client machine.
 */
public class RETRCommand implements Command {
    private String[] args;
    private FtpSession ftpSession;

    public RETRCommand(FtpSession session, String[] args) {

        ftpSession = session;
        this.args = args;
    }

    @Override
    public void execute() throws IOException, FtpErrorReplyException, NoSuchMessageException {
        try {
            InputStream inputStream = ftpSession.getFileSystem().getFileInputStream(args[0]);
            BufferedInputStream inputStream1 = new BufferedInputStream(inputStream);
            ftpSession.getControlConnection().write(getMessage("150"));
            ftpSession.getDataConnection().writeFrom(inputStream1);
            ftpSession.getDataConnection().close();
            inputStream.close();
            ftpSession.getControlConnection().write("226 Succesfully transferred.\r\n");

        } catch (NoSuchFileException e) {
            throw new FTPError550Exception(String.format("File is not exists [%s]", args[0]));
        }
    }
}
