package com.leapsoftware.twitterusername.model;

import java.util.List;

public class TwitterResponse {
    public boolean valid;
    public String reason;
    public String msg;
    public String desc;
    public List<Suggestion> suggestions;
}
