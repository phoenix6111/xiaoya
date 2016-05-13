package wanghaisheng.com.xiaoya.beans;

import java.io.Serializable;
import java.util.List;

public class GaoxiaoPic implements Serializable{
        private int id;
        private String thumbUrl;
        private int thumb_width;
        private int thumb_height;
        private String sthumbUrl;
        private int sthumb_width;
        private int sthumb_height;
        private String bthumbUrl;
        private int bthumb_width;
        private int bthumb_height;
        private String pic_url;
        private int width;
        private int height;
        private int size;
        private String ori_pic_url;
        private Object page_title;
        private String page_url;
        private String title;
        private Object category;
        private int weight;
        private List<String> tags;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public int getThumb_width() {
            return thumb_width;
        }

        public void setThumb_width(int thumb_width) {
            this.thumb_width = thumb_width;
        }

        public int getThumb_height() {
            return thumb_height;
        }

        public void setThumb_height(int thumb_height) {
            this.thumb_height = thumb_height;
        }

        public String getSthumbUrl() {
            return sthumbUrl;
        }

        public void setSthumbUrl(String sthumbUrl) {
            this.sthumbUrl = sthumbUrl;
        }

        public int getSthumb_width() {
            return sthumb_width;
        }

        public void setSthumb_width(int sthumb_width) {
            this.sthumb_width = sthumb_width;
        }

        public int getSthumb_height() {
            return sthumb_height;
        }

        public void setSthumb_height(int sthumb_height) {
            this.sthumb_height = sthumb_height;
        }

        public String getBthumbUrl() {
            return bthumbUrl;
        }

        public void setBthumbUrl(String bthumbUrl) {
            this.bthumbUrl = bthumbUrl;
        }

        public int getBthumb_width() {
            return bthumb_width;
        }

        public void setBthumb_width(int bthumb_width) {
            this.bthumb_width = bthumb_width;
        }

        public int getBthumb_height() {
            return bthumb_height;
        }

        public void setBthumb_height(int bthumb_height) {
            this.bthumb_height = bthumb_height;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getOri_pic_url() {
            return ori_pic_url;
        }

        public void setOri_pic_url(String ori_pic_url) {
            this.ori_pic_url = ori_pic_url;
        }

        public Object getPage_title() {
            return page_title;
        }

        public void setPage_title(Object page_title) {
            this.page_title = page_title;
        }

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getCategory() {
            return category;
        }

        public void setCategory(Object category) {
            this.category = category;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }