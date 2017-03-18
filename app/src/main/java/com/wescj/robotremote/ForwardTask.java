package com.wescj.robotremote;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class ForwardTask extends AsyncTask<Integer, Void, Void> {

    private ChannelExec channelssh;

    public ForwardTask() {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("pi", "192.168.1.102", 22);
            session.setPassword("sicario345");

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);

            session.connect();

            // SSH Channel
            channelssh = (ChannelExec) session.openChannel("exec");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channelssh.setOutputStream(baos);

            // Execute command
        } catch (Exception e) {
            Log.e("ForwardTask", "Making Forward Task Failed", e);
        }
    }

    @Override
    protected Void doInBackground(Integer... params) {
        channelssh.setCommand("sudo java -cp .:/opt/pi4j/lib/* moveForApp");
        try {
            channelssh.connect();
        } catch (JSchException e) {
            Log.e("Forward Task", "Error connecting", e);
        }
        channelssh.disconnect();
        return null;
    }
}
