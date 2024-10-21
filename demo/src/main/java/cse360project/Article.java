package cse360project;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cse360project.utils.Level;

public class Article implements Serializable{
	public long ID;
	public Level level;
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
     * @param title the title of the article cannot be null
     * @param description the description of the article cannot be null
     * @param keywords the keywords about the article cannot be null
     * @param body the body of the article cannot be null
     * @param links the link to the articles cannot be null 
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
	public void setID(long id) {
		this.ID = id;
	}
	public long getID() {
		return this.ID;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return this.title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return this.description;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getKeywords() {
		return this.keywords;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBody() {
		return this.body;
	}
	public boolean hasGroup(String group) {
		if (this.groups.contains(group)) {
			return true;
		}
		return false;
	}
	
	public static Article fromResultSet(ResultSet rs) throws SQLException {
		String allGroups = rs.getString("groups");
		ArrayList<String> group = new ArrayList<>();
		String groupArray[];
		groupArray = (allGroups.split("\n"));
		for (int i = 0; i < groupArray.length; i++) {
			group.add(groupArray[i]);
		}
		String allLinks = rs.getString("links");
		ArrayList<String> link = new ArrayList<>();
		String linkArray[];
		linkArray = (allLinks.split("\n"));
		for (int j = 0; j < linkArray.length; j++) {
			link.add(linkArray[j]);
		}
		Level level = Level.stringToLevel(rs.getString("level"));
	    return new Article(rs.getLong("article_id"), level, group, rs.getString("title"), rs.getString("description"), rs.getString("keywords"), rs.getString("body"), link);
	    }
	
	public boolean isLevel(Level level) {
		if (this.level == level) {
			return true;
		}
		return false;
	}
	
	public void addGroup(String group) {
		group.stripLeading();
		group.stripTrailing();
		String groupArray[] = group.split(" ");
		String newGroup = "";
		int index[] = new int[10];
		int indexc = 0;
		for (int i = 0; i < groupArray.length; i++) {
			if(!groupArray[i].contains(" ")) {
				groupArray[i] = groupArray[i].toLowerCase();
				groupArray[i] = groupArray[i].substring(0, 1).toUpperCase() + groupArray[i].substring(1);
				index[indexc] = i;
				indexc++;
			}
		}
		for (int j = 0; j < index.length; j++) {
			newGroup += groupArray[j];
		}
		newGroup.stripTrailing();
		if(!this.groups.contains(newGroup)) {
			this.groups.add(newGroup);
		}
	}
	
	public void removeGroup(String group) {
		group.stripLeading();
		group.stripTrailing();
		String groupArray[] = group.split(" ");
		String newGroup = "";
		int index[] = new int[10];
		int indexc = 0;
		for (int i = 0; i < groupArray.length; i++) {
			if(!groupArray[i].contains(" ")) {
				groupArray[i] = groupArray[i].toLowerCase();
				groupArray[i] = groupArray[i].substring(0, 1).toUpperCase() + groupArray[i].substring(1);
				index[indexc] = i;
				indexc++;
			}
		}
		for (int j = 0; j < index.length; j++) {
			newGroup += groupArray[j];
		}
		newGroup.stripTrailing();
		if(this.groups.contains(newGroup)) {
			this.groups.remove(newGroup);
		}
	}
	
	
}

