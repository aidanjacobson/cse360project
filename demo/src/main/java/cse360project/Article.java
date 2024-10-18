package cse360project;

import java.util.ArrayList;

import cse360project.utils.Level;

public class Article {
	public long ID;
	Level level;
	public ArrayList<String> groups = new ArrayList<>();
	public String title;
	public String description;
	public String keywords;
	public String body;
	public ArrayList<String>links = new ArrayList<>();
	
	
	/**
     * Constructor for User
     * @param id the users id in the db, or -1 if not in db
     * @param level the difficulty of the article cannot be null
     * @param groups list of the groupings of articles
     * @param title
     * @param description
     * @param keywords
     * @param body
     * @param links
     */
	public Article(long id, Level level , ArrayList<String> group, String title, String description, String keywords, String body, ArrayList<String> link) {
        this.ID = id;
        this.level = level;
        this.groups = group;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.body = body;
        this.links = link;
    }
}
