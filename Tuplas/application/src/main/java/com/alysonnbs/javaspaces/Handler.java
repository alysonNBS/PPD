package com.alysonnbs.javaspaces;

import java.util.ArrayList;

import com.alysonnbs.javaspaces.models.Device;
import com.alysonnbs.javaspaces.models.Environment;
import com.alysonnbs.javaspaces.models.Message;
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
            System.out
                    .println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }
        System.out.println("O servico JavaSpace foi encontrado.");
    }

    public void createEnvironment() {
        try {
            Environment environment = new Environment();
            int index = 1;
            Entry msg;
            do {
                environment.name = "amb" + index;
                msg = (Environment) space.read(environment, null, JavaSpace.NO_WAIT);
                ++index;
            } while (msg != null);
            space.write(environment, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public void createUser() {
        try {
            Environment environment = new Environment();
            environment = (Environment) space.read(environment, null, JavaSpace.NO_WAIT);
            if (environment == null) return;

            User user = new User();
            user.environment = environment.name;
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

    public void createDevice() {
        try {
            Environment environment = new Environment();
            environment = (Environment) space.read(environment, null, JavaSpace.NO_WAIT);
            if (environment == null) return;

            Device device = new Device();
            device.environment = environment.name;
            int index = 1;
            Entry msg;
            do {
                device.name = "disp" + index;
                msg = (Device) space.read(device, null, JavaSpace.NO_WAIT);
                ++index;
            } while (msg != null);
            space.write(device, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public void deleteEnvironment(String environmentName) {
        try {
            if (environmentName == null) return;
            User user = new User();
            user.environment = environmentName;
            Device device = new Device();
            device.environment = environmentName;
            Entry msg = (User) space.read(user, null, JavaSpace.NO_WAIT);
            if (msg != null) return;
            msg = (Device) space.read(device, null, JavaSpace.NO_WAIT);
            if (msg != null) return;

            Environment environment = new Environment();
            environment.name = environmentName;
            space.take(environment, null, JavaSpace.NO_WAIT);
        } catch (Exception e) {}
    }

    public void deleteUser(String userName) {
        try {
            if (userName == null) return;
            User user = new User();
            user.name = userName;
            space.take(user, null, JavaSpace.NO_WAIT);
        } catch (Exception e) {}
    }

    public void deleteDevice(String deviceName) {
        try {
            if (deviceName == null) return;
            Device device = new Device();
            device.name = deviceName;
            space.take(device, null, JavaSpace.NO_WAIT);
        } catch (Exception e) {}
    }

    public void changeUserEnvironment(String userName, String environmentName) {
        try {
            if (userName == null || environmentName == null) return;
            Environment environment = new Environment();
            environment.name = environmentName;
            environment = (Environment) space.read(environment, null, JavaSpace.NO_WAIT);
            if (environment == null) return;

            User user = new User();
            user.name = userName;
            space.take(user, null, JavaSpace.NO_WAIT);
            user.environment = environmentName;
            space.write(user, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public void changeDeviceEnvironment(String deviceName, String environmentName) {
        try {
            if (deviceName == null || environmentName == null) return;
            Environment environment = new Environment();
            environment.name = environmentName;
            environment = (Environment) space.read(environment, null, JavaSpace.NO_WAIT);
            if (environment == null) return;

            Device device = new Device();
            device.name = deviceName;
            space.take(device, null, JavaSpace.NO_WAIT);
            device.environment = environmentName;
            space.write(device, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public ArrayList<Environment> getEnvironments() {
        ArrayList<Environment> environmentList = new ArrayList<Environment>();
        try {
            Environment environment = new Environment();
            Entry msg;
            
            do {
                msg = space.take(environment, null, JavaSpace.NO_WAIT);
                if (msg != null) environmentList.add((Environment) msg);
            } while (msg != null);

            for (int i = 0; i < environmentList.size(); ++i)
                space.write(environmentList.get(i), null, Lease.FOREVER);
        } catch (Exception e) {}
        return environmentList;
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

    public ArrayList<Device> getDevices() {
        ArrayList<Device> deviceList = new ArrayList<Device>();
        try {
            Device device = new Device();
            Entry msg;
            do {
                msg = space.take(device, null, JavaSpace.NO_WAIT);
                if (msg != null) deviceList.add((Device) msg);
            } while (msg != null);

            for (int i = 0; i < deviceList.size(); ++i)
                space.write(deviceList.get(i), null, Lease.FOREVER);
        } catch (Exception e) {}
        return deviceList;
    }

    public ArrayList<User> getUsers(String environmentName) {
        ArrayList<User> userList = new ArrayList<User>();
        try {
            User user = new User();
            user.environment = environmentName;
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

    public ArrayList<Device> getDevices(String environmentName) {
        ArrayList<Device> deviceList = new ArrayList<Device>();
        try {
            Device device = new Device();
            device.environment = environmentName;
            Entry msg;
            do {
                msg = space.take(device, null, JavaSpace.NO_WAIT);
                if (msg != null) deviceList.add((Device) msg);
            } while (msg != null);

            for (int i = 0; i < deviceList.size(); ++i)
                space.write(deviceList.get(i), null, Lease.FOREVER);
        } catch (Exception e) {}
        return deviceList;
    }

    public String getEnvironmentOfUser(String userName) {
        String environment = "";
        try {
            User user = new User();
            user.name = userName;
            user = (User) space.read(user, null, JavaSpace.NO_WAIT);
            environment = user.environment;
        } catch (Exception e) {}
        return environment;
    }

    public String getEnvironmentOfDevice(String deviceName) {
        String environment = "";
        try {
            Device device = new Device();
            device.name = deviceName;
            device = (Device) space.read(device, null, JavaSpace.NO_WAIT);
            environment = device.environment;
        } catch (Exception e) {}
        return environment;
    }

    public void sendMessage(String from, String to, String content) {
        try {
            String environmentFrom = getEnvironmentOfUser(from);
            Message message = new Message();
            message.environment = environmentFrom;
            message.content = content;
            message.from = from;
            message.to = to;
            space.write(message, null, Lease.FOREVER);
        } catch (Exception e) {}
    }

    public Message getMessage(String to, String from) {
        Message ret = new Message();
        try {
            String environmentFrom = getEnvironmentOfUser(from);
            Message message = new Message();
            message.environment = environmentFrom;
            message.from = to;
            message.to = from;
            ret = (Message) space.take(message, null, Lease.FOREVER);
        } catch (Exception e) {}
        return ret;
    }
}
