package cse360project.utils;

import cse360project.Article;
import cse360project.utils.BackupRestoreUtils;
import cse360project.utils.DatabaseHelper;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class BackupRestoreTest {

    private static final String TEST_BACKUP_PATH = "test_backup.db";
    private static final String TEST_RESTORE_PATH = "test_restore.db";

    @Before
    public void setUp() {
        // Ensure that the database is clean before each test
        DatabaseHelper.setDatabasePath("~/testdb");
        DatabaseHelper.connectToDatabase();
        DatabaseHelper.deleteAllArticles();
    }

    @Test
    public void testBackupDatabase() {
        // Create some mock articles and groups
        ArrayList<String> groups = new ArrayList<>();
        groups.add("group1");
        groups.add("group2");

        // Call backup method
        boolean success = BackupRestoreUtils.backupDatabase(TEST_BACKUP_PATH, groups);

        // Verify that the backup was successful
        assertTrue("Backup should be successful", success);

        // Check if the backup file exists
        File backupFile = new File(TEST_BACKUP_PATH);
        assertTrue("Backup file should exist after backup", backupFile.exists());

        // Cleanup the backup file after the test
        backupFile.delete();
    }

    @Test
    public void testHardRestoreDatabase() {
        // Create mock articles and groups for backup
        ArrayList<String> groups = new ArrayList<>();
        groups.add("group1");
        groups.add("group2");

        // Backup the database first
        BackupRestoreUtils.backupDatabase(TEST_BACKUP_PATH, groups);

        // Now, delete all articles to simulate the restore
        DatabaseHelper.deleteAllArticles();

        // Restore the database from the backup
        boolean success = BackupRestoreUtils.hardRestoreDatabase(TEST_BACKUP_PATH);
        assertTrue("Restore should be successful", success);

        // Verify that articles are restored
        // Add assertions depending on how you verify articles are in the database
        // For example, checking the number of articles in the database or verifying specific ones

        // Cleanup the backup file after the test
        File backupFile = new File(TEST_BACKUP_PATH);
        backupFile.delete();
    }

    @Test
    public void testSoftRestoreDatabase() {
        // Create mock articles and groups for backup
        ArrayList<String> groups = new ArrayList<>();
        groups.add("group1");
        groups.add("group2");

        // Backup the database first
        BackupRestoreUtils.backupDatabase(TEST_BACKUP_PATH, groups);

        // Now, delete all articles to simulate the restore
        DatabaseHelper.deleteAllArticles();

        // Soft restore the database (with merging and overwriting)
        boolean success = BackupRestoreUtils.softRestoreDatabase(TEST_BACKUP_PATH, true);
        assertTrue("Soft restore should be successful", success);

        // Verify that articles are restored
        // Add assertions depending on how you verify articles are in the database

        // Cleanup the backup file after the test
        File backupFile = new File(TEST_BACKUP_PATH);
        backupFile.delete();
    }

    @Test
    public void testBackupRestoreWithNoExistingBackup() {
        // Test backup when there is no pre-existing backup file
        File backupFile = new File(TEST_RESTORE_PATH);
        if (backupFile.exists()) {
            backupFile.delete();
        }

        // Backup the database
        ArrayList<String> groups = new ArrayList<>();
        groups.add("group1");
        boolean success = BackupRestoreUtils.backupDatabase(TEST_RESTORE_PATH, groups);

        // Ensure the backup is created
        assertTrue("Backup should be successful", success);

        // Verify the file was created
        assertTrue("Backup file should exist after backup", backupFile.exists());

        // Cleanup
        backupFile.delete();
    }

    @Test
    public void testRestoreFromNonExistentFile() {
        // Test restore from a non-existent backup file
        boolean success = BackupRestoreUtils.hardRestoreDatabase("non_existent_file.db");
        assertFalse("Restoring from a non-existent file should fail", success);
    }

    @Test
    public void testBackupWithInvalidPath() {
        // Test backup with an invalid file path
        boolean success = BackupRestoreUtils.backupDatabase("/invalid/path/to/backup.db", new ArrayList<>());
        assertFalse("Backup to an invalid path should fail", success);
    }
}