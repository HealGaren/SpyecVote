package kr.spyec.spyecvote;

import java.util.List;

//최예찬, 투표 리사이클러뷰 데이터클래스, 6월20일
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