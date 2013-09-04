package com.alberovalley.novedadesumbria.comm;

import android.content.Context;

import com.alberovalley.novedadesumbria.R;
import com.alberovalley.utils.AlberoLog;

/**
 * Collection of methods that transform the data received into information
 * 
 * @author frank
 * 
 */
public class UmbriaMessenger {
    // ////////////////////////////////////////////////////////////
    // Methods
    // ////////////////////////////////////////////////////////////
    /**
     * receives UmbriaData and decides if there's news or not
     * 
     * @param data
     * @return boolean
     * @throws UmbriaConnectionException
     */
    public static boolean isThereAnythingNew(UmbriaData data) throws UmbriaConnectionException {
        boolean somethingNew = false;
        AlberoLog.d("UmbriaMessenger.isThereAnythingNew: ");
        if (!data.isThereError()) {
            int player = 0, storyteller = 0, vip = 0, pMessages = 0;
            // only take into account news the user wants to be notified about
            if (data.notifyPlayerMessages)
                player = data.getPlayerMessages();
            if (data.notifyStorytellerMessages)
                storyteller = data.getStorytellerMessages();
            if (data.notifyVipMessages)
                vip = data.getVipMessages();
            if (data.notifyPrivateMessages)
                pMessages = data.getPrivateMessages();
            somethingNew = (player + storyteller + vip + pMessages) != 0;
        } else {
            // there was some error, throw UmbriaConnectionException
            throw new UmbriaConnectionException(data.getErrorMessage());
        }

        return somethingNew;
    }

    public static String makeNotificationText(UmbriaData data, Context ctx) {
        String message = "";
        AlberoLog.d("UmbriaMessenger.makeNotificationText: ");
        if (!data.isThereError()) {
            message = "";
            if (data.isNotifyPlayerMessages() && data.getPlayerMessages() > 0)
                message += ctx.getResources().getString(R.string.widget_text_player);
            if (data.notifyStorytellerMessages && data.getStorytellerMessages() > 0)
                message += ctx.getResources().getString(R.string.widget_text_storyteller);
            if (data.notifyVipMessages && data.getVipMessages() > 0)
                message += ctx.getResources().getString(R.string.widget_text_vip);
            if (data.notifyPrivateMessages && data.getPrivateMessages() > 0)
                message += ctx.getResources().getString(R.string.widget_text_private);

            if (message.equalsIgnoreCase(""))
                message = ctx.getResources().getString(R.string.widget_text_empty);
        } else {
            message = ctx.getResources().getString(R.string.widget_text_error);
            AlberoLog.e("UmbriaMessenger.makeNotificationText Error en comunicación: " + data.getErrorMessage());
        }

        return message;
    }
}
