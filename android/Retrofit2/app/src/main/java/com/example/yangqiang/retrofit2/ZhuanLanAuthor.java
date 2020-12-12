package com.example.yangqiang.retrofit2;

import java.util.List;

/**
 * Created by yangqiang on 02/01/2018.
 */

public class ZhuanLanAuthor {
    public long followersCount;
    public Creator creator;
    public List<Topic> topics;
    public String activateState;
    public String href;
    public boolean acceptSubmission;
    public boolean firstTime;
    public List<PostTopic> postTopics;
    public String pendingName;
    public Avatar avatar;
    public boolean canManage;
    public String description;
    public List<PendingTopic> pendingTopics;
    public int nameCanEditUntil;
    public String reason;
    public int banUntil;
    public String slug;
    public String name;
    public String url;
    public String intro;
    public int topicsCanEditUntil;
    public String activateAuthorRequested;
    public String commentPermission;
    public boolean following;
    public int postsCount;
    public boolean canPost;

    public static class Creator {
        public String bio;
        public boolean isFollowing;
        public String hash;
        public long uid;
        public boolean isOrg;
        public String slug;
        public boolean isFollowed;
        public String description;
        public String name;
        public String profileUrl;
        public Avatar avatar;
        public boolean isOrgWhiteList;
        public boolean isBanned;
    }

    public static class Avatar {
        public String id;
        public String profileUrl;
    }

    public static class Topic {
        public String url;
        public String id;
        public String name;
    }

    public static class PostTopic {
        public int postsCount;
        public int id;
        public String name;
    }

    public static class PendingTopic {

    }
}
