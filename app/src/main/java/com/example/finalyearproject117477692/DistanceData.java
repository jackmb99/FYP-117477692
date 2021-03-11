package com.example.finalyearproject117477692;

// leaderbaord functionality from https://www.youtube.com/watch?v=j1_tEaYchyk
// class for leaderboard
public class DistanceData {
    String name;
    long distance;
    String userUid;

    public DistanceData() {
    }

    public DistanceData(String name, long distance, String userUid) {
        this.name = name;
        this.distance = distance;
        this.userUid = userUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
