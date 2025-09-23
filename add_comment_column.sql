-- emotioncalendar 테이블에 comment 컬럼 추가
USE sns_project;

-- comment 컬럼이 이미 존재하는지 확인하고 추가
ALTER TABLE emotioncalendar 
ADD COLUMN IF NOT EXISTS comment VARCHAR(255) NULL 
AFTER emoji;

-- 테이블 구조 확인
DESCRIBE emotioncalendar;
