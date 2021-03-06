package javabot.irclib;

import java.util.Date;

import javabot.dao.LogsDao;
import javabot.model.Logs.Type;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class LoggerListener implements IRCEventListener {
    private static final Logger log = LoggerFactory.getLogger(LoggerListener.class);

    @Autowired
    private LogsDao logsDao;

    public void onAction(final IRCUser sender, final String action) {
        logsDao.logMessage(Type.ACTION, null, null, action);
    }

    @Override
    public void onRegistered() {
        logsDao.logMessage(Type.REGISTERED, null, null, "Connected at " + new Date());
    }

    @Override
    public void onDisconnected() {
        logsDao.logMessage(Type.DISCONNECTED, null, null, "Disconnected at " + new Date());
    }

    @Override
    public void onError(final String msg) {
        logsDao.logMessage(Type.ERROR, null, null, "Disconnected at " + new Date());
    }

    @Override
    public void onError(final int num, final String msg) {
    }

    @Override
    public void onInvite(final String chan, final IRCUser user, final String passiveNick) {
        logsDao.logMessage(Type.INVITE, null, chan, String.format("Invited to %s by %s", chan, user.getNick()));
    }

    @Override
    public void onJoin(final String chan, final IRCUser user) {
        logsDao.logMessage(Type.INVITE, user.getNick(), chan, String.format("%s joined %s", user.getNick(), chan));
    }

    @Override
    public void onKick(final String chan, final IRCUser user, final String passiveNick, final String msg) {
        logsDao.logMessage(Type.KICK, user.getNick(), chan, String.format("%s was kicked from %s", user.getNick(), chan));
    }

    @Override
    public void onMode(final String chan, final IRCUser user, final IRCModeParser modeParser) {
    }

    @Override
    public void onMode(final IRCUser user, final String passiveNick, final String mode) {
    }

    @Override
    public void onNick(final IRCUser user, final String newNick) {
        logsDao.logMessage(Type.NICK, user.getNick(), null, String.format("%s changed nicks to %s", user.getNick(), newNick));
    }

    @Override
    public void onNotice(final String target, final IRCUser user, final String msg) {
    }

    @Override
    public void onPart(final String chan, final IRCUser user, final String msg) {
        logsDao.logMessage(Type.PART, user.getNick(), chan, String.format("%s parted %s with [%s]", user.getNick(),
            chan, msg));
    }

    @Override
    public void onPing(final String ping) {
        System.out.println("ping = " + ping);
    }

    @Override
    public void onPrivmsg(final String target, final IRCUser user, final String msg) {
        logsDao.logMessage(Type.MESSAGE, user.getNick(), target, msg);
    }

    @Override
    public void onQuit(final IRCUser user, final String msg) {
        logsDao.logMessage(Type.QUIT, user.getNick(), null, String.format("%s quit with [%s]", user.getNick(), msg));
    }

    @Override
    public void onReply(final int num, final String value, final String msg) {
    }

    @Override
    public void onTopic(final String chan, final IRCUser user, final String topic) {
        logsDao.logMessage(Type.TOPIC, user.getNick(), chan, String.format("%s changed the topic to [%s]",
            user.getNick(), topic));
    }

    @Override
    public void unknown(final String prefix, final String command, final String middle, final String trailing) {
    }
}
