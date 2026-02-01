@echo off
echo ====================================
echo QnA AI 애플리케이션 시작
echo ====================================

echo.
echo [1/2] ChromaDB 컨테이너 시작 중...
docker-compose up -d

echo.
echo [2/2] Spring Boot 애플리케이션 시작...
echo (MariaDB는 로컬에서 이미 실행 중이라고 가정합니다)
echo.
gradlew.bat bootRun
