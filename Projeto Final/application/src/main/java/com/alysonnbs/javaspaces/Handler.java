package com.alysonnbs.javaspaces;

import java.util.ArrayList;

import com.alysonnbs.javaspaces.models.Environment;
import com.alysonnbs.javaspaces.models.MyMessage;
import com.alysonnbs.javaspaces.models.SpyWord;
import com.alysonnbs.javaspaces.models.User;

import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;

public class Handler {
    private final JavaSpace space;

    public Handler() {
        System.out.println("Procurando pelo servico JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        space = (JavaSpace) finder.getService();

        if (space == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }
        System.out.println("O servico JavaSpace foi encontrado.");

        Environment environment = new Environment();
        Entry msg;
        try {
            environment.name = "default";
            msg = space.read(environment, null, JavaSpace.NO_WAIT);
            if (msg == null) {
                space.write(environment, null, Lease.FOREVER);
            }
            environment.name = "words";
            msg = space.read(environment, null, JavaSpace.NO_WAIT);
            if (msg == null) {
                space.write(environment, null, Lease.FOREVER);
            }
            // spy.name = "spy";
            // spy.environment = environment.name;
            // spy = (Spy) space.read(spy, null, JavaSpace.NO_WAIT);
            // if (spy == null) {
            //     spy = new Spy();
            //     spy.name = "spy";
            //     spy.environment = environment.name;
            //     space.write(spy, null, Lease.FOREVER);
            // }
        } catch (Exception e) {}
    }

    public void createUser() {
        try {
            User user = new User();
            user.environment = "default";
            int index = 1;
            Entry msg;
            do {
                user.name = "user" + index;
                msg = (User) space.read(user, null, JavaSpace.NO_WAIT);
                ++index;
            } while (msg != null);
            space.write(user, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public ArrayList<User> getUsers() {
        ArrayList<User> userList = new ArrayList<User>();
        try {
            User user = new User();
            Entry msg;
            do {
                msg = space.take(user, null, JavaSpace.NO_WAIT);
                if (msg != null) userList.add((User) msg);
            } while (msg != null);

            for (int i = 0; i < userList.size(); ++i)
                space.write(userList.get(i), null, Lease.FOREVER);
        } catch (Exception e) {}
        return userList;
    }

    public void sendMessage(String from, String to, String content) {
        try {
            MyMessage message = new MyMessage();
            message.environment = "default";
            message.content = content;
            message.from = from;
            message.to = to;
            message.wasSpiedOn = "0";
            space.write(message, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public void spySendMessage(MyMessage message) {
        try {
            message.wasSpiedOn = "1";
            space.write(message, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public MyMessage spyWatchingMessages() {
        MyMessage message = new MyMessage();
        try {
            message.environment = "default";
            message.wasSpiedOn = "0";
            message = (MyMessage) space.take(message, null, Lease.FOREVER);
        } catch (Exception e) {
            System.out.println(e);
        }
        return message;
    }

    public MyMessage getMessage(String to, String from) {
        MyMessage ret = new MyMessage();
        try {
            MyMessage message = new MyMessage();
            message.environment = "default";
            message.from = to;
            message.to = from;
            message.wasSpiedOn = "1";
            ret = (MyMessage) space.take(message, null, Lease.FOREVER);
        } catch (Exception e) {}
        return ret;
    }

    public void deleteWord(String word) {
        try {
            if (word == null) return;
            SpyWord entry = new SpyWord();
            entry.environment = "words";
            entry.word = word;
            space.take(entry, null, JavaSpace.NO_WAIT);
        } catch (Exception e) {}
    }
    
    public void addWord(String word) {
        try {
            SpyWord entry = new SpyWord();
            entry.environment = "words";
            entry.word = word;
            space.write(entry, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public ArrayList<SpyWord> getWords() {
        ArrayList<SpyWord> wordList = new ArrayList<SpyWord>();
        try {
            SpyWord word = new SpyWord();
            Entry msg;
            do {
                msg = space.take(word, null, JavaSpace.NO_WAIT);
                if (msg != null) wordList.add((SpyWord) msg);
            } while (msg != null);

            for (int i = 0; i < wordList.size(); ++i)
                space.write(wordList.get(i), null, Lease.FOREVER);
        } catch (Exception e) {}
        return wordList;
    }
}
