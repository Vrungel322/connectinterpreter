package com.getbewarned.connectinterpreter.managers;

public class QrCodeManager {

    private static final String GROUP_SESSION_DOMAIN = "https://interpreter.getbw.me/sessions/";

    public String getGroupSessionId(String link) {
        if (link.startsWith(GROUP_SESSION_DOMAIN)) {
            String sessionId = link.replace(GROUP_SESSION_DOMAIN, "").trim();
            if (!sessionId.isEmpty()) {
                return sessionId;
            }
        }
        return null;
    }
}
