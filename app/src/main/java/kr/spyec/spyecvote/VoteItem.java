package kr.spyec.spyecvote;

import java.util.List;

/**
 * Created by 최예찬 on 2016-08-22.
 */

public class VoteItem {
    private String mainName;
    private List<String> etc;

    public VoteItem(String word, List<String> etc) {
        this.mainName = word;
        this.etc = etc;
    }

    public String getMainName() {
        return mainName;
    }

    public List<String> getEtc() {
        return etc;
    }
}