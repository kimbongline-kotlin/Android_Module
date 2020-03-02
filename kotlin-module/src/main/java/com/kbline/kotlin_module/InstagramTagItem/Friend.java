package com.kbline.kotlin_module.InstagramTagItem;

import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class Friend {
    private Collection<Friend> friends;
    private String email;

    public Friend(String email) {
        this.email = email;
        this.friends = new ArrayList<Friend>();
    }

    public String getEmail() {
        return email;
    }

    public Collection<Friend> getFriends() {
        return friends;
    }

    public void addFriendship(Friend friend) {
        friends.add(friend);
        friend.getFriends().add(this);
    }

    public boolean canBeConnected(Friend friend) {

        System.out.println(getFriends().toArray().length);
        ArrayList<Friend> friends = new ArrayList<>(friend.getFriends());
        for(int i = 0 ; i< friends.size(); i ++) {
            System.out.println(friends.get(i).getFriends().contains(friend));
        }
        if(getFriends().contains(friend)) {
            return true;
        }
        return  false;
        //throw new UnsupportedOperationException("Waiting to be implemented.");
    }

    public static void main(String[] args) {
        Friend a = new Friend("A");
        Friend b = new Friend("B");
        Friend c = new Friend("C");
        Friend d = new Friend("d");

        a.addFriendship(b);
        b.addFriendship(c);
        c.addFriendship(d);

        System.out.println(a.canBeConnected(d));
    }
}