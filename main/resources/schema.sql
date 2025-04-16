
CREATE DATABASE IF NOT EXISTS sns_project CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sns_project;

-- 1. Users 테이블
CREATE TABLE IF NOT EXISTS Users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARBINARY(255) NOT NULL,
    points INT DEFAULT 0,
    membership_level ENUM('basic', 'premium') DEFAULT 'basic',
    phone_number VARCHAR(15) UNIQUE,
    birthdate DATE,
    profile_image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Admin 테이블
CREATE TABLE IF NOT EXISTS Admin (
    Admin_id INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARBINARY(100) NOT NULL,
    Role ENUM('superadmin', 'moderator') DEFAULT 'moderator',
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Phone_number VARCHAR(15)
);

-- 3. Board 테이블
CREATE TABLE IF NOT EXISTS Board (
    Board_id INT PRIMARY KEY AUTO_INCREMENT,
    Board_name VARCHAR(50) NOT NULL
);

-- 4. Post 테이블
CREATE TABLE IF NOT EXISTS Post (
    Post_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    Board_id INT NOT NULL,
    Title VARCHAR(255) NOT NULL,
    Content TEXT NOT NULL,
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Likes INT DEFAULT 0,
    Tag VARCHAR(100),
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE,
    FOREIGN KEY (Board_id) REFERENCES Board(Board_id) ON DELETE CASCADE
);

-- 5. Comment 테이블
CREATE TABLE IF NOT EXISTS Comment (
    Comment_id INT PRIMARY KEY AUTO_INCREMENT,
    Post_id INT NOT NULL,
    User_id BIGINT NOT NULL,
    Parent_Comment_id INT NULL,
    Content TEXT NOT NULL,
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Likes INT DEFAULT 0,
    FOREIGN KEY (Post_id) REFERENCES Post(Post_id) ON DELETE CASCADE,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE,
    FOREIGN KEY (Parent_Comment_id) REFERENCES Comment(Comment_id) ON DELETE CASCADE
);

-- 6. Report 테이블
CREATE TABLE IF NOT EXISTS Report (
    Report_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    User_id_reported BIGINT NOT NULL,
    Post_id INT NULL,
    Comment_id INT NULL,
    Status ENUM('pending', 'resolved') DEFAULT 'pending',
    Admin_comment TEXT,
    Processed_by INT NULL,
    Resolved_at TIMESTAMP NULL,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE,
    FOREIGN KEY (User_id_reported) REFERENCES Users(User_id) ON DELETE CASCADE,
    FOREIGN KEY (Post_id) REFERENCES Post(Post_id) ON DELETE CASCADE,
    FOREIGN KEY (Comment_id) REFERENCES Comment(Comment_id) ON DELETE CASCADE,
    FOREIGN KEY (Processed_by) REFERENCES Admin(Admin_id) ON DELETE SET NULL
);

-- 7. LikeTable 테이블
CREATE TABLE IF NOT EXISTS LikeTable (
    Like_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    Post_id INT NULL,
    Comment_id INT NULL,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE,
    FOREIGN KEY (Post_id) REFERENCES Post(Post_id) ON DELETE CASCADE,
    FOREIGN KEY (Comment_id) REFERENCES Comment(Comment_id) ON DELETE CASCADE
);

-- 8. Friend 테이블
CREATE TABLE IF NOT EXISTS Friend (
    User_id BIGINT NOT NULL,
    Friend_id BIGINT NOT NULL,
    Status ENUM('pending', 'accepted', 'blocked') DEFAULT 'pending',
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (User_id, Friend_id),
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE,
    FOREIGN KEY (Friend_id) REFERENCES Users(User_id) ON DELETE CASCADE
);

-- 9. Message 테이블
CREATE TABLE IF NOT EXISTS Message (
    Message_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    Sender_id BIGINT NOT NULL,
    Content TEXT NOT NULL,
    Send_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE,
    FOREIGN KEY (Sender_id) REFERENCES Users(User_id) ON DELETE CASCADE
);

-- 10. EmotionLog 테이블
CREATE TABLE IF NOT EXISTS EmotionLog (
    Log_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    Text TEXT,
    Emotion ENUM('joy', 'sadness', 'anger', 'calm', 'anxiety'),
    Created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE
);

-- 11. EmotionCalendar 테이블
CREATE TABLE IF NOT EXISTS EmotionCalendar (
    Calendar_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    Date DATE NOT NULL,
    Emotion ENUM('joy', 'sadness', 'anger', 'calm', 'anxiety'),
    Note TEXT,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE
);

-- 12. PointSystem 테이블
CREATE TABLE IF NOT EXISTS PointSystem (
    Point_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    Action TEXT NOT NULL,
    Points INT NOT NULL,
    Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE
);

-- 13. Entertainment 테이블
CREATE TABLE IF NOT EXISTS Entertainment (
    Recommendation_id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    Category VARCHAR(50) NOT NULL,
    Item_name VARCHAR(100) NOT NULL,
    Timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Review_text TEXT,
    FOREIGN KEY (User_id) REFERENCES Users(User_id) ON DELETE CASCADE
);

-- 14. Story 테이블
CREATE TABLE IF NOT EXISTS Story (
    story_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    text TEXT,
    picture VARCHAR(500),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- 15. EmotionGroup 테이블
CREATE TABLE IF NOT EXISTS EmotionGroup (
    group_id INT PRIMARY KEY AUTO_INCREMENT,
    creator_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    emotion ENUM('joy', 'sadness', 'anger', 'calm', 'anxiety') NOT NULL,
    tags TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
