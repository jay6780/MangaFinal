package com.m.manga.classes;

import java.util.List;

public class ApiBean {
    private List<Data> data;
    private List<Manga> manga;
    private List<Chapters> chapters;
    private List<String> genre;
    private List<String> genres;

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<Chapters> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapters> chapters) {
        this.chapters = chapters;
    }

    public List<Manga> getManga() {
        return manga;
    }


    public void setManga(List<Manga> manga) {
        this.manga = manga;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Chapters{
        String chapterId;
        String views;
        String uploaded;
        String timestamp;

        public String getChapterId() {
            return chapterId;
        }

        public String getViews() {
            return views;
        }

        public String getUploaded() {
            return uploaded;
        }

        public String getTimestamp() {
            return timestamp;
        }
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

    public static class Data{
        String id;
        String title;
        String imgUrl;
        String latestChapter;
        String description;

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
