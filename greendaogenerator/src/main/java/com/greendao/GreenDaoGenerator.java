package com.greendao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static final int VERSION = 51;
    public static final String GREEN_DAO_CODE_PATH = "../xiaoya/app/src/main/java";

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(VERSION, "wanghaisheng.com.xiaoya.db");

        //daily表，装最新的数据
        Entity story = schema.addEntity("DBStory");

        story.addIdProperty();
        story.addStringProperty("title");
        story.addIntProperty("theme_id");
        story.addStringProperty("name");
        story.addStringProperty("image");
        story.addStringProperty("images");
        story.addStringProperty("body");
        story.addStringProperty("css");
        story.addStringProperty("share_url");
        story.addBooleanProperty("is_collected");

        //daily_collection表，装收藏的数据
        Entity story_collection = schema.addEntity("StoryCollection");

        story_collection.addIdProperty();
        story_collection.addStringProperty("title");
        story_collection.addStringProperty("name");
        story_collection.addStringProperty("image");
        story_collection.addStringProperty("images");
        story_collection.addStringProperty("body");
        story_collection.addStringProperty("css");
        story_collection.addIntProperty("theme_id");
        story_collection.addStringProperty("share_url");

        Entity article = schema.addEntity("DBArticle");
        article.addIdProperty();
        article.addStringProperty("title");
        article.addStringProperty("date_published");
        article.addStringProperty("info");
        article.addStringProperty("image");
        article.addStringProperty("description");
        article.addIntProperty("comment_count");
        article.addStringProperty("url");
        article.addStringProperty("channel_keys");
        article.addBooleanProperty("is_collected");

        Entity ArticleCollection = schema.addEntity("ArticleCollection");
        ArticleCollection.addIdProperty();
        ArticleCollection.addStringProperty("title");
        ArticleCollection.addStringProperty("date_published");
        ArticleCollection.addStringProperty("info");
        ArticleCollection.addStringProperty("image");
        ArticleCollection.addStringProperty("description");
        ArticleCollection.addIntProperty("comment_count");
        ArticleCollection.addStringProperty("url");
        ArticleCollection.addStringProperty("channel_keys");

        Entity movie = schema.addEntity("Movie");
        movie.addIdProperty();
        movie.addStringProperty("showInfo");
        movie.addStringProperty("scm");
        movie.addStringProperty("dir");
        movie.addStringProperty("star");
        movie.addStringProperty("cat");
        movie.addIntProperty("wish");
        movie.addBooleanProperty("value3d");
        movie.addStringProperty("nm");
        movie.addBooleanProperty("imax");
        movie.addIntProperty("snum");
        movie.addStringProperty("sc");
        movie.addStringProperty("ver");
        movie.addStringProperty("rt");
        movie.addStringProperty("img");
        movie.addStringProperty("dur");
        movie.addBooleanProperty("is_collected");


        Entity collectedMovie = schema.addEntity("MovieCollection");
        collectedMovie.addIdProperty();
        collectedMovie.addStringProperty("showInfo");
        collectedMovie.addStringProperty("scm");
        collectedMovie.addStringProperty("dir");
        collectedMovie.addStringProperty("star");
        collectedMovie.addStringProperty("cat");
        collectedMovie.addIntProperty("wish");
        collectedMovie.addBooleanProperty("value3d");
        collectedMovie.addStringProperty("nm");
        collectedMovie.addBooleanProperty("imax");
        collectedMovie.addIntProperty("snum");
        collectedMovie.addStringProperty("sc");
        collectedMovie.addStringProperty("ver");
        collectedMovie.addStringProperty("rt");
        collectedMovie.addStringProperty("img");
        collectedMovie.addStringProperty("dur");
        collectedMovie.addBooleanProperty("is_collected");


        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }
}
