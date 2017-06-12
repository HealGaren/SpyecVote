package kr.spyec.spyecvote;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 최예찬 on 2016-08-22.
 */
public class DataManager {

    private SharedPreferences votePref, resultPref, configPref;

    private Gson gson;
    private Type fromJsonType;
    private List<VoteItem> items;


    private static final String VOTE_PREF_KEY = "vote";
    private static final String RESULT_PREF_KEY = "result";
    private static final String CONFIG_PREF_KEY = "config";

    private DataManager(Context context) {
        if (votePref == null)
            votePref = context.getSharedPreferences(VOTE_PREF_KEY, Context.MODE_PRIVATE);
        if (resultPref == null)
            resultPref = context.getSharedPreferences(RESULT_PREF_KEY, Context.MODE_PRIVATE);
        if (configPref == null)
            configPref = context.getSharedPreferences(CONFIG_PREF_KEY, Context.MODE_PRIVATE);

        gson = new Gson();
        fromJsonType = new TypeToken<List<VoteItem>>() {
        }.getType();
        loadItems();
        loadReceiverActiveStatus();
    }

    private static DataManager instance;

    public static void init(Context context) {
        instance = new DataManager(context);
    }


    public static DataManager getInstance() {
        if (instance == null) throw new Error("DataManager need to be called init method!");
        return instance;
    }


    public void resetVote() {
        votePref.edit().clear().apply();
        resultPref.edit().clear().apply();
    }

    public void resetAll() {
        resetVote();
        configPref.edit().clear().apply();
        loadItems();
    }


    private final static String ITEMS_KEY = "items";

    public void saveItems() {
        configPref.edit().putString(ITEMS_KEY, gson.toJson(items)).apply();
    }

    public void loadItems() {
        items = gson.fromJson(configPref.getString(ITEMS_KEY, ""), fromJsonType);
        if (items == null) items = new ArrayList<>();
    }

    public List<VoteItem> getItems() {
        return items;
    }

    public void addItem(VoteItem item) {
        items.add(item);
        saveItems();
    }

    public void removeItem(int position) {
        items.remove(position);
        saveItems();
    }

    private final static String ALREADY_VOTE_KEY = "alreadyVoteMessage";

    public void setAlreadyVoteMessage(String str) {
        configPref.edit().putString(ALREADY_VOTE_KEY, str).apply();
    }

    public String getAlreadyVoteMessage() {
        return configPref.getString(ALREADY_VOTE_KEY, "[선린투표] 투표를 이미 하셨습니다. 현재 투표는 반영되지 않았습니다.");
    }


    private final static String UNKNOWN_TARGET_VOTE_KEY = "unknownTargetVoteMessage";

    public void setUnknownTargetVoteMessage(String str) {
        configPref.edit().putString(UNKNOWN_TARGET_VOTE_KEY, str).apply();
    }

    public String getUnknownTargetVoteMessage() {
        return configPref.getString(ALREADY_VOTE_KEY, "[선린투표] 투표자를 잘못 입력하셨습니다. 현재 투표는 반영되지 않았습니다.");
    }


    public void vote(String phone, String target) throws AlreadyVoteException, UnknownTargetVoteException {

        Set<String> voted = votePref.getStringSet(phone, new HashSet<String>());

        String searchedTarget = null;
        for (VoteItem item : items) {
            if (target.equals(item.getMainName()) || item.getEtc().contains(target)) {
                searchedTarget = item.getMainName();
                break;
            }
        }

        if (searchedTarget == null) throw new UnknownTargetVoteException();
        if (voted.contains(searchedTarget)) throw new AlreadyVoteException();

        voted.add(searchedTarget);
        votePref.edit().putStringSet(phone, voted).apply();

        int currentVoteCount = getVoteCount(searchedTarget);
        resultPref.edit().putInt(searchedTarget, currentVoteCount + 1).apply();
    }

    public int getVoteCount(String target) {
        return resultPref.getInt(target, 0);
    }


    public static class VoteException extends Exception {

    }

    public static class AlreadyVoteException extends VoteException {

    }

    public static class UnknownTargetVoteException extends VoteException {

    }

    private final static String IS_RECEIVER_ACTIVE_KEY = "isReceiverActive";

    private boolean isReceiverActive = false;

    public void toggleVoteReceiver() {
        isReceiverActive = !isReceiverActive;
        configPref.edit().putBoolean(IS_RECEIVER_ACTIVE_KEY, isReceiverActive).apply();
    }

    public void loadReceiverActiveStatus() {
        isReceiverActive = configPref.getBoolean(IS_RECEIVER_ACTIVE_KEY, false);
    }

    public boolean isReceiverActive() {
        return isReceiverActive;
    }
}
