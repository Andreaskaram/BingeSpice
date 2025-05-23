package com.example.bingespice_app;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BingespiceDBManager {

    // DB Connection
    static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://bingespicedb.cva6i4ugasuu.eu-north-1.rds.amazonaws.com:3306/bingespicedb",
                "admin",
                "bingedbpw"
        );
    }

    public static void testdb() {
        System.out.println("testdb");
        try{
            Connection conn = getConnection();
            System.out.println("✅ Connected to the database successfully.");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User");

            while (rs.next()) {
                System.out.println("HellowWorld");
                System.out.println(rs.getString(1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String signup(String username, String firstName, String lastName, String email, String password, String gender, String country, byte[] profileImage){
        String sql = "INSERT INTO User (Username, FirstName, LastName, Email, Password, Gender, Country,ProfilePicture) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, username);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, gender);
            stmt.setString(7, country);
            // Profile picture
            if(profileImage != null) {
                if(profileImage.length > 1_048_576) { // 1MB limit
                    return "Image too large (max 1MB)";
                }
                stmt.setBytes(8, profileImage);
            } else {
                stmt.setNull(8, Types.BLOB);
            }

            stmt.executeUpdate();
            return null;
        } catch (SQLIntegrityConstraintViolationException dupEx) {
            System.out.println("⚠️ Username already exists. Please choose another one.");
            return "Username already exists";
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            return "An unexpected SQL error occurred";
        } catch (Exception e) {
            e.printStackTrace();
            return "An unexpected error occurred";
        }
    }

    public static int login(String username, String password) {
        String sql = "SELECT * FROM User WHERE BINARY Username = ? AND BINARY Password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("UserID");
                logLogin(username);  // ✅ Only logs on successful login
                return userId;
            }

        } catch (SQLException e) {
            System.out.println("❌ SQL Error during login: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void logLogin(String username) {
        String sql = "INSERT INTO LOG (Username, Time, Action) VALUES (?, NOW(), 'Login')";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
            System.out.println("📝 Login recorded in LOG table.");
        } catch (SQLException e) {
            System.out.println("⚠️ Could not log login: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUserData(String username) {
        String sql = "SELECT FirstName, LastName, Email, Gender, Country, ProfilePicture FROM User WHERE Username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Gender"),
                        rs.getString("Country"),
                        rs.getBytes("ProfilePicture")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error fetching user details: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static boolean updateProfile(String oldUsername, String newUsername, String firstName, String lastName,
                                        String email, String gender, String country) {
        String sql = "UPDATE User SET Username=?, FirstName=?, LastName=?, Email=?, Gender=?, Country=? WHERE Username=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newUsername);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email);
            stmt.setString(5, gender);
            stmt.setString(6, country);
            stmt.setString(7, oldUsername);
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateWatchedCategory(int UserId, int ContentId, String Type) {
        String sql = "INSERT INTO WatchedMoviesSeries (UserID, ContentID, Type) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, UserId);
            stmt.setInt(2, ContentId);
            if (Type.equals("tv")) { Type = "Series"; }
            stmt.setString(3, Type);
            stmt.executeUpdate();
            System.out.println("📺 Watched category recorded successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Could not update watched category: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkIfWatched(int UserId, int ContentId) {
        String sql = "SELECT * FROM WatchedMoviesSeries WHERE UserID = ? AND ContentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, UserId);
            stmt.setInt(2, ContentId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error during login: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeFromWatched(int UserId, int ContentId) {
        String sql = "DELETE FROM WatchedMoviesSeries WHERE UserID = ? AND ContentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, UserId);
            stmt.setInt(2, ContentId);
            stmt.executeUpdate();
            System.out.println("📺 Content Removed successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Could not update watched category: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean markEpisodeAsWatched(int UserId, int ContentId, int SeasonNumber, int EpisodeNumber) {
        String sql = "INSERT INTO WatchedSeriesEpisodes (UserID, ContentID, SeasonNumber, SeasonEpisode) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, UserId);
            stmt.setInt(2, ContentId);
            stmt.setInt(3, SeasonNumber);
            stmt.setInt(4, EpisodeNumber);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Could not update watched category: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeEpisodeFromWatched(int UserId, int ContentId, int SeasonNumber, int EpisodeNumber) {
        String sql = "DELETE FROM WatchedSeriesEpisodes WHERE UserID = ? AND ContentID = ? AND SeasonNumber = ? AND SeasonEpisode = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, UserId);
            stmt.setInt(2, ContentId);
            stmt.setInt(3, SeasonNumber);
            stmt.setInt(4, EpisodeNumber);
            stmt.executeUpdate();
            System.out.println("📺 Content Removed successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Could not update watched category: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<int[]> checkEpisodeIfWatched(int UserId, int ContentId) {
        String sql = "SELECT * FROM WatchedSeriesEpisodes WHERE UserID = ? AND ContentID = ?";
        List<int[]> watchedEpisodes = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, UserId);
            stmt.setInt(2, ContentId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int season = rs.getInt("SeasonNumber");
                int episode = rs.getInt("SeasonEpisode");
                watchedEpisodes.add(new int[]{season, episode});
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error during login: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return watchedEpisodes;
    }

    public static Map<Integer, List<Integer>> getAllWatchedEpisodes(int userId, int contentId) {
        String sql = "SELECT SeasonNumber, SeasonEpisode FROM WatchedSeriesEpisodes WHERE UserId = ? AND ContentId = ?";
        Map<Integer, List<Integer>> watchedEpisodes = new HashMap<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, contentId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int season = rs.getInt("SeasonNumber");
                int episode = rs.getInt("SeasonEpisode");

                if (!watchedEpisodes.containsKey(season)) {
                    watchedEpisodes.put(season, new ArrayList<>());
                }
                watchedEpisodes.get(season).add(episode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
        System.out.println("❌ Unexpected error fetching watched episodes: " + e.getMessage());
        e.printStackTrace();
    }
        return watchedEpisodes;
    }

    public static List<Notification> getNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT Content, Date, Status FROM Notifications WHERE UserID = ? ORDER BY Date DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String content = rs.getString("Content");
                Timestamp date = rs.getTimestamp("Date");
                String status = rs.getString("Status");
                notifications.add(new Notification(content, date, status));
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching notifications: " + e.getMessage());
        }
        return notifications;
    }

    public static boolean deleteNotifications(int userId) {
        String sql = "DELETE FROM Notifications WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error deleting notifications: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNotificationStatus(int userId, String content, Timestamp date) {
        String sql = "UPDATE Notifications SET Status = 'Read' WHERE UserID = ? AND Content = ? AND Date = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, content);
            stmt.setTimestamp(3, date);

            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error updating notification status: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Refactored: createNewWatchlist to return boolean, store newWatchlistId, and link user to watchlist
    public static boolean createNewWatchlist(String name, String type, int UserId) {
        String sql = "INSERT INTO Watchlist (Name, Type) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, type);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating watchlist failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newWatchlistId = generatedKeys.getInt(1);
                    // Placeholder: use dummy userId (0) for now
                    // TODO: Replace 0 with actual userId
                    return linkUserToWatchlist(UserId, newWatchlistId);
                } else {
                    throw new SQLException("Creating watchlist failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Could not create Watchlist: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean linkUserToWatchlist(int userId, int watchlistId) {
        String sql = "INSERT INTO UserWatchlist (UserID, WatchlistID) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, watchlistId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Could not link user to watchlist: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}