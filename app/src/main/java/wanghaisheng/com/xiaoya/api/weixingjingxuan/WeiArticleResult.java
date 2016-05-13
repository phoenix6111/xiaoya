package wanghaisheng.com.xiaoya.api.weixingjingxuan;

import java.util.List;

public class WeiArticleResult {
    private int totalPage;
    private int ps;
    private int pno;

    private List<WeiArticle> list;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPno() {
        return pno;
    }

    public void setPno(int pno) {
        this.pno = pno;
    }

    public List<WeiArticle> getList() {
        return list;
    }

    public void setList(List<WeiArticle> list) {
        this.list = list;
    }


}