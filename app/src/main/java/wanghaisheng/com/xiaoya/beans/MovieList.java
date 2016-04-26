package wanghaisheng.com.xiaoya.beans;

import java.util.List;

import wanghaisheng.com.xiaoya.db.Movie;

/**
 * Created by sheng on 2016/4/17.
 */
public class MovieList{


    /**
     * status : 0
     * data : {"hasNext":true,"movies":[{"showInfo":"今天154家影院放映2540场","scm":"男孩混狼群，从小住森林","dir":"乔恩·费儒","star":"尼尔·塞西,郭子睿,比尔·默瑞","cat":"剧情,奇幻,冒险","wish":123410,"3d":true,"nm":"奇幻森林","imax":true,"snum":80075,"sc":9,"ver":"3D/IMAX 3D/中国巨幕","rt":"2016-04-15上映","img":"http://p1.meituan.net/165.220/movie/0c9ee4c5b44928292bc00914686374a1160686.jpg","dur":105,"id":246970}]}
     */

    private int status;
    /**
     * hasNext : true
     * movies : [{"showInfo":"今天154家影院放映2540场","scm":"男孩混狼群，从小住森林","dir":"乔恩·费儒","star":"尼尔·塞西,郭子睿,比尔·默瑞","cat":"剧情,奇幻,冒险","wish":123410,"3d":true,"nm":"奇幻森林","imax":true,"snum":80075,"sc":9,"ver":"3D/IMAX 3D/中国巨幕","rt":"2016-04-15上映","img":"http://p1.meituan.net/165.220/movie/0c9ee4c5b44928292bc00914686374a1160686.jpg","dur":105,"id":246970}]
     */

    private Data data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private boolean hasNext;
        /**
         * showInfo : 今天154家影院放映2540场
         * scm : 男孩混狼群，从小住森林
         * dir : 乔恩·费儒
         * star : 尼尔·塞西,郭子睿,比尔·默瑞
         * cat : 剧情,奇幻,冒险
         * wish : 123410
         * 3d : true
         * nm : 奇幻森林
         * imax : true
         * snum : 80075
         * sc : 9
         * ver : 3D/IMAX 3D/中国巨幕
         * rt : 2016-04-15上映
         * img : http://p1.meituan.net/165.220/movie/0c9ee4c5b44928292bc00914686374a1160686.jpg
         * dur : 105
         * id : 246970
         */

        private List<Movie> movies;

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public List<Movie> getMovies() {
            return movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies = movies;
        }
    }
}
