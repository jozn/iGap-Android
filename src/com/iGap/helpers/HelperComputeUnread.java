// Copyright (c) 2016, iGap - www.iGap.im
// iGap is a Hybrid instant messaging service .
// RooyeKhat Media Co . - www.RooyeKhat.co
// All rights reserved.

package com.iGap.helpers;

import com.iGap.adapter.G;
import com.iGap.badge.ShortcutBadger;


/**
 * 
 * mohasebeye tedade payamhaye nakhandeye karbar
 *
 */

public class HelperComputeUnread {

    /**
     * 
     * tedad payamhaye nakhandeye karbar ra mohasebe mikond va in tedad ra dar icon barname namayesh midahad (badge count)
     * 
     */

    public static void unreadMessageCount() {
        int singleChatUnread = G.cmd.selectUnreadCountSingleChat();
        int groupChatUnread = G.cmd.selectUnreadCountGroupChat();
        int ChannelUnread = G.cmd.selectUnreadCountChannel();

        int unreadCount = (singleChatUnread + groupChatUnread + ChannelUnread);

        if (unreadCount == 0) {
            ShortcutBadger.removeCount(G.context);
        } else {
            ShortcutBadger.applyCount(G.context, unreadCount);
        }
    }
}
