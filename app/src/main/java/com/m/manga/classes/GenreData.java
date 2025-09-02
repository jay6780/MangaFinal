package com.m.manga.classes;

import java.util.List;

public class GenreData {

    private List<Manga> manga;

    public List<Manga> getManga() {
        return manga;
    }

    public void setManga(List<Manga> manga) {
        this.manga = manga;
    }

    public static class Manga{
        String id;
        String title;
        String imgUrl;
        String latestChapter;
        String description;
        String image;

        public String getImage() {
            return image;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getLatestChapter() {
            return latestChapter;
        }
    }

}
