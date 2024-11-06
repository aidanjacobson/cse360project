package cse360project;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import cse360project.utils.GroupUtils;
import cse360project.utils.Level;
import cse360project.utils.EncryptionUtils;

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
     * Constructor for Article
     * @param id the article id in the db, or -1 if not in db
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

          // format the groups before adding them
          ArrayList<String> newGroups = new ArrayList<>();
          for (String g : group) {
               newGroups.add(GroupUtils.formatGroupName(g));
          }

          this.groups = newGroups;
          this.title = title;
          this.description = description;
          this.keywords = keywords;
          this.body = body;
          this.links = link;
    }
	
    /**
     * Set an id for the article
     * @param id the id to set
     */
	public void setID(long id) {
		this.ID = id;
	}
	
    /**
     * Get the id the article has
     * @return a long of the id
     */
	public long getID() {
		return this.ID;
	}
	
    /**
     * Set the level for the article
     * @param level the level to set
     */
	public void setLevel(Level level) {
		this.level = level;
	}
	
    /**
     * Set the title for the article
     * @param title the title to set
     */
	public void setTitle(String title) {
		this.title = title;
	}
	
    /**
     * Get the title the article has
     * @return a String of the title
     */
	public String getTitle() {
		return this.title;
	}
	
    /**
     * Set the description for the article
     * @param description the description to set
     */
	public void setDescription(String description) {
		this.description = description;
	}
	
    /**
     * Get the description the article has
     * @return a String of the description
     */
	public String getDescription() {
		return this.description;
	}
	
    /**
     * Set the keywords for the article
     * @param keywords the keywords to set
     */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
    /**
     * Get the keywords the article has
     * @return a String of the keywords
     */
	public String getKeywords() {
		return this.keywords;
	}
	
    /**
     * Set the body for the article
     * @param body the body to set
     */
	public void setBody(String body) {
		this.body = body;
	}
	
    /**
     * Get the body the article has
     * @return a String of the body
     */
	public String getBody() {
		return this.body;
	}
	
    /**
     * Check if the article has a given group
     * @param group the group to check
     * @return true if the article has the group, false otherwise
     */
	public boolean hasGroup(String group) {
		if (this.groups.contains(group)) {
			return true;
		}
		return false;
	}
	
    /**
     * Create an article from a result set from the database
     * @param rs the result set
     * @return the article
     * @throws SQLException
     */
	public static Article fromResultSet(ResultSet rs) throws SQLException {
          // split the groups into an arraylist
          String allGroups = rs.getString("groups");
          ArrayList<String> groups = new ArrayList<>(Arrays.asList(allGroups.split("\n")));
          groups.removeIf(String::isEmpty);

          // split the links into an arraylist
          String allLinks = rs.getString("links");
          ArrayList<String> links = new ArrayList<>(Arrays.asList(allLinks.split("\n")));
          links.removeIf(String::isEmpty);

          // get the level
          Level level = Level.stringToLevel(rs.getString("level"));

          // get the rest of the data
          long id = rs.getLong("article_id");
          String title = rs.getString("title");
          String description = rs.getString("description");
          String keywords = rs.getString("keywords");
          String encryptedBody = rs.getString("body");
          String body = EncryptionUtils.decryptString(encryptedBody);

          // return the article
          return new Article(id, level, groups, title, description, keywords, body, links);
     }
	
    /**
     * Check if the article is the level of a specified level
     * @param level the level to check
     * @return true if the article is the level, false otherwise
     */
	public boolean isLevel(Level level) {
		if (this.level == level) {
			return true;
		}
		return false;
	}
	
    /**
     * Add the group to the ArrayList of groups
     * @param group the group to add
     */
	public void addGroup(String group) {
		String newGroup = GroupUtils.formatGroupName(group);
		if(!this.groups.contains(newGroup)) {
			this.groups.add(newGroup);
		}
	}
	
    /**
     * Remove the group from the ArrayList of groups
     * @param group the group to remove
     */
	public void removeGroup(String group) {
		String newGroup = GroupUtils.formatGroupName(group);
		if(this.groups.contains(newGroup)) {
			this.groups.remove(newGroup);
		}
	}

     @Override
     public boolean equals(Object obj) {
          if (this == obj)
               return true;
          if (obj == null)
               return false;
          if (getClass() != obj.getClass())
               return false;
          Article other = (Article) obj;
          if (ID != other.ID)
               return false;
          if (level != other.level)
               return false;
          if (groups == null) {
               if (other.groups != null)
                    return false;
          } else if (!groups.equals(other.groups))
               return false;
          if (title == null) {
               if (other.title != null)
                    return false;
          } else if (!title.equals(other.title))
               return false;
          if (description == null) {
               if (other.description != null)
                    return false;
          } else if (!description.equals(other.description))
               return false;
          if (keywords == null) {
               if (other.keywords != null)
                    return false;
          } else if (!keywords.equals(other.keywords))
               return false;
          if (body == null) {
               if (other.body != null)
                    return false;
          } else if (!body.equals(other.body))
               return false;
          if (links == null) {
               if (other.links != null)
                    return false;
          } else if (!links.equals(other.links))
               return false;
          return true;
     }
}

